(ns advent-of-code-2018.day05)

(defn read-input []
  (slurp "resources/day05.txt"))

(defn remove-polymers
  "Given a word and a lower-case letter, removes all instances of the letter (either case) from the word, and reduce the polymers as described in the spec."
  [word lower-case-letter]
  (let [upper-case-letter (first (clojure.string/upper-case (str lower-case-letter)))]
    (loop [checked-stack ""
           rem-word word]
      (if (= (count rem-word) 0)
        (reverse checked-stack)
        (if (or (= (first rem-word) lower-case-letter)
                (= (first rem-word) upper-case-letter))
          (recur checked-stack (next rem-word))
          (if (or (= (count checked-stack) 0)
                  (not
                   (= 32
                      (Math/abs (- (int (first checked-stack))
                                   (int (first rem-word)))))))
            (recur (concat (str (first rem-word)) checked-stack)
                   (next rem-word))
            (recur (next checked-stack)
                   (next rem-word))))))))

(defn part-a []
  (count (remove-polymers (read-input) \newline)))

(defn part-b []
  (let [input (read-input)]
    (->> (apply list (range 97 123))
         (map char)
         (map #(count (remove-polymers input %)))
         (apply min)
         dec)))

