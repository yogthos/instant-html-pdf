## About

<img src="https://raw.github.com/yogthos/instant-pdf/master/logo.png"
title="Instant PDF" align="left" padding="5px" width="100" height="50"/>
The service accepts POST requests with HTML body and returns PDF documents,
the document generation is done by the [clj-htmltopdf](https://github.com/gered/clj-htmltopdf) library.

<br clear=all /><br />

## Usage

You will need the following tools to build the service.

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Leiningen](http://leiningen.org/)

To run standalone:

```bash
lein ring uberjar
java -jar target/instant-pdf.jar
```

The port can be specified as an environment variable, e.g:

```
export PORT=3001
java -jar target/instant-pdf.jar
```

To package as a WAR for app server deployment:
```bash
lein ring uberwar
```

Once the service is running you will be able to make a POST request to it and pass in a
JSON or a Markdown document as the request. The JSON documents must be submitted using the form parameter called `json-input`.

### License
***
Copyright © 2015 Dmitri Sotnikov

Distributed under the Eclipse Public License, the same as Clojure.


