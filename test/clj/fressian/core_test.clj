(ns fressian.core-test
  (:require [clojure.data.fressian :as fress]
    [clojure.java.io :as io]))

(with-open [os (io/output-stream "a.fress")]
  (let [writer (fress/create-writer os)]
    (fress/write-object writer {:abc "ABC"})))

