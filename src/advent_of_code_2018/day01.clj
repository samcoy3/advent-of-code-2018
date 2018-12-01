(ns advent-of-code-2018.day01)

(defn read-input []
  (map
   #(Integer/parseInt %)
   (clojure.string/split-lines (slurp "resources/day01.txt"))))

(defn part-a []
  (reduce + (read-input)))

(defn part-b []
  (loop [prev-freqs (hash-set)
         input-cycle (cycle (read-input))
         current-total-freq 0]
    (let [next-freq (+ current-total-freq (first input-cycle))]
      (if (contains? prev-freqs next-freq)
        next-freq
        (recur
         (conj prev-freqs next-freq)
         (drop 1 input-cycle)
         next-freq)))))
