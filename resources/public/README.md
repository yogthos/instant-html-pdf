This service accepts POST requests with HTML markup and returns PDF documents:

    curl -i -X POST -d 'html-input=[{}, "<html><body><p>hello</p></body></html>"]' http://localhost:3000/ > doc.pdf


### License

Distributed under the Eclipse Public License.

***
Copyright (C) 2012 Yogthos
