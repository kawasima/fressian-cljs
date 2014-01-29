(ns fressian.fns
  (:require [fressian.uuid :as uuid]
            [goog.string :as gstring]
            [goog.string.format]))

(defn expected
  ([expected ch]
   (throw (gstring/format "expected %s at %d" expected ch)))
  ([expected ch got]
   (throw (gstring/format "expected %s at %d, got %s" expected ch got))))


(defn solo-entry [m]
  (if (and (map? m) (= (count m) 1))
    (first m)))


(defn solo-key [m]
  (first (solo-entry m)))

(defn solo-val [m]
  (last (solo-entry m)))

(defn solo-map [k v]
  {k v})


(defn read-utf8-chars [source offset length]
  (let [buf (js/Array.)]
    (loop [pos 0]
      (let [ch (bit-and (aget source pos) 0xff)
            ch>>4 (bit-shift-right ch 4)]
        (when (< pos length)
          (cond
           (<=  0 ch>>4 7) (do (.push buf ch) (recur (inc pos)))
           (<= 12 ch>>4 13) (let [ch1 (aget source (inc pos))]
                           (.push buf (bit-or
                                       (bit-shift-left
                                        (bit-and ch 0x1f) 6)
                                       (bit-and ch1 0x3f)))
                           (recur (+ pos 2)))
           (= ch>>4 14) (let [ch1 (aget source (inc pos))
                           ch2 (aget source (+ pos 2))]
                       (.push buf (bit-or
                                   (bit-shift-left
                                    (bit-and ch 0x0f) 12)
                                   (bit-shift-left
                                    (bit-and ch1 0x03f) 6)
                                   (bit-and ch2 0x3f)))
                       (recur (+ pos 3)))
           :default (throw (gstring/format "Invalid UTF-8: %d" ch))))))
    (.apply (.-fromCharCode js/String) nil buf)))


