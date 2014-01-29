(ns fressian.uuid)

(defn rng []
  (let [r (atom 0)]
    (for [i (range 0 15)]
      (do
        (if (= (bit-and i 0x03) 0)
          (reset! r (* (rand) 0x1000000000)))
        (bit-and (bit-shift-right @r (bit-shift-left (bit-and i 0x03) 3))
                 0xFF)))))

(defn unparse [buf]
  (let [offset (atom 0)]
    (for [n [4 2 2 2 6]]
      (let [token (->> buf
                       (drop @offset)
                       (take n)
                       (map #(-> % (+ 0x100)
                                 (.toString 16)
                                 (.substr 1)))
                       (apply str))]
        (swap! offset #(+ n %))
        token))))

(defn v1 []
  (->> (rng)
       (map-indexed (fn [idx item]
                      (if (= idx 6)
                        (bit-or (bit-and item 0x0f) 0x40)
                        item)))
       (map-indexed (fn [idx item]
                      (if (= idx 8)
                        (bit-or (bit-and item 0x3f) 0x80)
                        item)))
       (unparse)
       (clojure.string/join "-")))
