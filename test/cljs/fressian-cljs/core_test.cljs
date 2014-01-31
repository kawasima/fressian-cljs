(ns fressian-cljs.core-test
  (:require-macros [cemerick.cljs.test
                     :refer (is deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test :as t]
            [fressian-cljs.core :as fress]))

(deftest read-test
  (let [ buf (js/ArrayBuffer. 13)
         arr (js/Uint8Array. buf)
         fress-data [ 0xc0 0xe6 0xca 0xf7 0xcd ;; {:ABC "abc"}
                      0xdd 0x61 0x62 0x63 0xdd 0x41 0x42 0x43]]
    (doall (map-indexed #(aset arr %1 %2) fress-data))
    (let [obj (fress/read buf)]
      (is {:abc "ABC"} obj)
      (prn obj))))

(deftest write-test
  (let [arr (fress/write {:abc "ABC"})]
    (dotimes [n (. arr -length)]
      (println (.toString (bit-and (aget arr n) 0xff)  16)))
    (prn (fress/read (. arr -buffer)))))
