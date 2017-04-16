(ns app.routes
  (:require
    [hiccup.core :refer :all]
    [hiccup.form :refer :all]
    [hiccup.page :refer :all]
    [clj-htmltopdf.core :refer [->pdf]]
    [clojure.java.io :as io]
    [compojure.core :refer :all]
    [compojure.handler :as handler]
    [ring.util.response :as response]
    [compojure.route :as route]
    [hiccup.middleware :refer [wrap-base-url]]
    [markdown.core :refer [md-to-html]])
  (:import [java.io
            ByteArrayInputStream
            ByteArrayOutputStream
            File
            StringWriter]
           org.apache.commons.io.IOUtils))

(defn- read-help []
  (with-open [in  (-> "public/README.md" io/resource io/input-stream)
              out (new StringWriter)]
    (md-to-html in out)
    (.toString out)))

(defn index-page [req]
  (html5
    [:head [:title "Instant PDF"]
     (include-css "/css/style.css")]
    [:body
     (form-to [:post "/"]
              [:h1 "Enter HTML"]
              (text-area
                {:rows "30"}
                "html-input" "")
              [:br]
              (submit-button {:class "button"} "Generate PDF"))
     [:br]
     (read-help)]))

(defn generate-pdf [input]
  (try
    (with-open [out (ByteArrayOutputStream.)]
      (->pdf input out)
      (with-open [in (ByteArrayInputStream. (.toByteArray out))]
        (.flush out)
        (-> (response/response in)
            (response/header "Content-Disposition" "filename=document.pdf")
            (response/content-type "application/pdf")
            (response/header "Content-Length" (.size out)))))
    (catch Exception ex
      (do
        (.printStackTrace ex)
        {:status  500
         :headers {"Content-Type" "text/html"}
         :body    (html5 [:body [:h2 "An error has occured while parsing the document"] (.getMessage ex)])}))))

(defroutes main-routes
  (GET "/" req (index-page req))
  (POST "/" [html-input]
    (if html-input
      (generate-pdf html-input)
      {:status 400 :body "invalid request"}))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
      (wrap-base-url)
      (handler/api)))
