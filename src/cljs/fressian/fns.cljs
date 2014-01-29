(ns fressian.fns
  (:require [fressian.uuid :as uuid]))

(defn expected
  ([expected ch]
   (throw (format "expected %s at %d" expected ch)))
  ([expected ch got]
   (throw (format "expected %s at %d, got %s" expected ch got))))


(defn solo-entry [m]
  (if (and (map? m) (= (count m) 1))
    (first m)))


(defn solo-key [m]
  (first (solo-entry m)))

(defn solo-val [m]
  (last (solo-entry m)))

(defn solo-map [k v]
  {k v})

(defn lookup [the-lookup k]
  (when the-lookup
    (nth the-lookup k)))

(defn read-utf8-chars [source offset length]
  (let [encoded-string (.apply (.-fromCharCode js/String fromCharCode) nil source)]
    (->> encoded-string
         (.atob js/window)
         (.escape js/window)
         (.decodeURIComponent js/window))))


