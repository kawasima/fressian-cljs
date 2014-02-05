(ns fressian-cljs.uuid-test
  (:require-macros [cemerick.cljs.test
                     :refer (is deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test :as t]
            [fressian-cljs.uuid :as uuid]))

(deftest v4-test
  (let [ uuid-1 (uuid/v4)
         uuid-2 (uuid/v4)]
    (is (not= uuid-1 uuid-2) "UUID is unique")
    (is (re-find #"[0-9a-f]{8}\-[0-9a-f]{4}\-[0-9a-f]{4}\-[0-9a-f]{4}\-[0-9a-f]{12}" uuid-1))))
