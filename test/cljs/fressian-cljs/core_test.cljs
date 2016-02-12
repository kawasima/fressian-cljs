(ns fressian-cljs.core-test
  (:require-macros [cemerick.cljs.test
                     :refer (is deftest with-test run-tests testing)])
  (:require [cemerick.cljs.test :as t]
            [fressian-cljs.core :as fress]))

(defn print-buf [buf]
  (println (clojure.string/join " "
             (for [n (range 0 (. buf -length))]
               (.toString (bit-and (aget buf n) 0xff) 16)))))

; Example binary output from clojure.data.fressian
; to test against.
(def EXAMPLES
  {{:abc "ABC"}
    [0xc0 0xe6 0xca 0xf7
     0xcd 0xdd 0x61 0x62
     0x63 0xdd 0x41 0x42
     0x43]

    [1 2 3]
    [0xe7 0x10 0x20 0x30]

    #{1 2 3}
    [0xc1 0xe7 0x10 0x30
     0x2]

    {:a "foo" :b #"foo" :c 2.0 :d 30}
    [0xc0 0xec 0x8 0xca
     0xf7 0xcd 0xdb 0x61
     0xdd 0x66 0x6f 0x6f
     0xca 0xf7 0xcd 0xdb
     0x62 0xc4 0xdd 0x66
     0x6f 0x6f 0xca 0xf7
     0xcd 0xdb 0x63 0xfa
     0x40 0x00 0x00 0x00
     0x00 0x00 0x00 0x00
     0xca 0xf7 0xcd 0xdb
     0x64 0x1e]
  })

(defn vec->bytes
  [v]
  (let [buf (js/ArrayBuffer. (count v))
        arr (js/Uint8Array. buf)]
    (doall (map-indexed #(aset arr %1 %2) v))
    buf))

(deftest read-test
  (doseq [[v fress] EXAMPLES]
    (let [obj (fress/read (vec->bytes fress))]
      (is (= v obj))
      (prn obj))))

(deftest write-test
  (doseq [[v fress] EXAMPLES]
    (let [arr (fress/write v)
          new-v (fress/read (. arr -buffer))]
      (print-buf arr)
      (is (= v new-v)))))

(deftest list-fress
  (let [ arr (fress/write [:a :b :c])
         obj (fress/read (. arr -buffer))]
    (print-buf arr)
    (prn obj)
    (is (= (seq [:a :b :c]) obj))))

(deftest set-fress
  (let [ arr (fress/write #{3 2 1})
         obj (fress/read (. arr -buffer))]
    (print-buf arr)
    (prn obj)
    (is (= #{3 2 1} obj))))

(deftest complex-fress
  (let [ complex-obj {:map {:a 1 :b "B"} :set #{[1 2 3] "4"} :array (into-array ["Java" "script"])}
         arr (fress/write complex-obj)]
    (print-buf arr)
    (let [obj (fress/read (. arr -buffer))]
      (prn obj)
      (is (= {:map {:a 1 :b "B"} :set #{[1 2 3] "4"} :array '("Java" "script")} obj)))))

(deftest uuid-fress
  (let [ arr (fress/write (UUID. "550e8400-e29b-41d4-a716-446655440000"))
         obj (fress/read (. arr -buffer))]
    (print-buf arr)
    (prn obj)
    (is (= (UUID. "550e8400-e29b-41d4-a716-446655440000") obj))))

(deftest js-object-fress
  (let [ arr (fress/write (clj->js {:name "kawasima" :age 15}))
         obj (fress/read (. arr -buffer))]
    (print-buf arr)
    (prn obj)
    (is (= {"name" "kawasima", "age" 15} obj))))

(deftest js-date-fress
  (let [ d   (js/Date.)
         arr (fress/write d)
         obj (fress/read (. arr -buffer))]
    (print-buf arr)
    (prn obj)
    (is (= d obj))))

(deftest number-fress
  (let [nums [1234567890123 3.1415926 -987654321]
        arr  (fress/write nums)
        obj  (fress/read (. arr -buffer))]
    (print-buf arr)
    (prn obj)
    (is (= (first nums) (first obj)))
    (is (= (last  nums) (last  obj)))
    (is (< (.abs js/Math (- (nth nums 1) (nth obj 1))) 0.01))))

(deftest boolean-fress
  (let [bools [true false nil]
        arr   (fress/write bools)
        obj   (fress/read (. arr -buffer))]
    (print-buf arr)
    (prn obj)
    (is (= bools obj))))

(defrecord Member [fname lname])
(deftest record-fress
  (is (thrown-with-msg? js/Error #"Cannot write" (fress/write (Member. "Yoshitaka" "Kawashima")))))


