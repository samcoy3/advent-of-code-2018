(ns advent-of-code-2018.day02)

(defn read-input []
  (clojure.string/split-lines (slurp "resources/day02.txt")))

(defn letter-appears [s n]
  (contains? (apply hash-set (vals (frequencies (seq s)))) n))

(defn part-a []
  (let [input (read-input)]
    (*
     (count (filter #(letter-appears % 2) input))
     (count (filter #(letter-appears % 3) input)))))

(defn strings-one-apart [s t]
  (=
   1
   (count (filter
           #(not (= (first %) (second %)))
           (map vector (seq s) (seq t))))))

(defn anti-diff-strings [a b]
  (apply str
         (map first
              (filter
               #(= (first %) (second %))
               (map vector (seq a) (seq b))))))

(defn part-b []
  (apply anti-diff-strings
         (loop [rem (read-input)]
           (let [matches (filter (partial strings-one-apart (first rem)) (rest rem))]
             (if (not (empty? matches))
               (vector (first matches) (first rem))
               (recur (rest rem)))))))
