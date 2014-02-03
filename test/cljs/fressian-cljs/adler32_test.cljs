(ns fressian-cljs.adler32-test
  (:require-macros [cemerick.cljs.test
                     :refer (is deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test :as t])
  (:use [fressian-cljs.adler32 :only [make-adler32 update! get-value]]))

(deftest checksum
  (let [adler32 (make-adler32)]
    (update! adler32 1)
    (update! adler32 2)
    (is (= 393220 (get-value adler32)))))