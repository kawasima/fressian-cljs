# fressian-cljs [![Build Status](https://travis-ci.org/kawasima/fressian-cljs.png?branch=master)](https://travis-ci.org/kawasima/fressian-cljs)

Read and write fressian data by clojurescript.

## Installation

Add the following to your `project.clj`

```clojure
[net.unit8/fressian-cljs "0.1.0"]
```

## Usage

Fressian-cljs uses ArrayBuffer to read/write fressian objects.

```clojure
(ns myapp
  (:require [fressian-cljs.core :as fress])

(let [connection (js/WebSocket. "ws://websocket-host")]
  (set! connection -onmessage #(println (fress/read %)))
  (.send connection (fress/write {:a 1 :b 2})))
```

## License

Copyright (C) 2014 kawasima

Distributed under the Eclipse Public License, the same as Clojure.

