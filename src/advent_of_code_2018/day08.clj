(ns advent-of-code-2018.day08)

(defn read-input []
  (map #(Integer/parseInt %)
       (clojure.string/split (clojure.string/trimr (slurp "resources/day08.txt"))
                             #" ")))

(defn parse-node
  "Given a sequence of ints, parse the first node in it and return the node as well as the length it occupied.
  Works recursively."
  [int-seq]
  (let [number-of-children (first int-seq)
        number-of-metadata (second int-seq)]
    (if (> number-of-children 0)
      (loop [int-seq-to-pass (drop 2 int-seq)
             list-of-data '()
             number-remaining number-of-children
             total-length-used 2]
        (if (= number-remaining 0)
          [(concat (take number-of-metadata int-seq-to-pass) list-of-data)
           (+ total-length-used number-of-metadata)]
          (let [[node-data length-used] (parse-node int-seq-to-pass)]
            (recur (drop length-used int-seq-to-pass)
                   (concat list-of-data (list node-data))
                   (dec number-remaining)
                   (+ total-length-used length-used))))
        )
      [(apply list (take number-of-metadata (drop 2 int-seq)))
       (+ 2 number-of-metadata)])))

(defn part-a []
  (->> (read-input)
       parse-node
       first
       flatten
       (reduce +)))

(defn value
  "Calculates the value of a node as specified in the problem statement."
  [node]
  (let [parts (partition-by int? node)
        metadata (first parts)
        child-nodes (apply vector (second parts))]
    (if (= (count child-nodes) 0)
      (reduce + metadata)
      (->> metadata
           (filter #(> (inc (count child-nodes)) %))
           (map #(get child-nodes (dec %)))
          (map value)
           (reduce +)))))

(defn part-b []
  (->> (read-input)
       parse-node
       first
       value))
