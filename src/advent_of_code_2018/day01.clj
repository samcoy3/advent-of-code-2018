(ns advent-of-code-2018.day01)

(defn read-input []
  (map
   #(Integer/parseInt %)
   (clojure.string/split-lines (slurp "resources/day01.txt"))))

(defn part-a []
  (reduce + (read-input)))

(defn part-b []
  (loop [prev-freq (hash-set)
         input-cycle (cycle (read-input))
         cum-freq 0]
    (let [next-freq (+ cum-freq (first input-cycle))]
      (if (contains? prev-freq next-freq)
        next-freq
        (recur
         (conj prev-freq next-freq)
         (drop 1 input-cycle)
         next-freq)))))
