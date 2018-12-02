(ns advent-of-code-2018.day02)

(defn read-input []
  (clojure.string/split-lines (slurp "resources/day02.txt")))

(defn letter-appears?
  "Returns true if any letter appears <n> times in the string <s>."
  [s n]
  (contains? (apply hash-set (vals (frequencies (seq s)))) n))

(defn part-a
  "Count them separately then just multiply."
  []
  (let [input (read-input)]
    (*
     (count (filter #(letter-appears? % 2) input))
     (count (filter #(letter-appears? % 3) input)))))

(defn strings-one-apart
  "Returns true if exactly one character differs between <s> and <t>."
  [s t]
  (=
   1
   (count (filter
           #(not (= (first %) (second %)))
           (map vector (seq s) (seq t))))))

(defn anti-diff-strings
  "Returns the string comprised of all characters where <a> and <b> 'agree'."
  [a b]
  (apply str
         (map first
              (filter
               #(= (first %) (second %))
               (map vector (seq a) (seq b))))))

(defn part-b
  "We loop through the input, comparing the head to the later strings.
When we find a pair of one-apart strings, we anti-diff them and return it."
  []
  (apply anti-diff-strings
         (loop [rem (read-input)]
           (let [matches (filter (partial strings-one-apart (first rem)) (rest rem))]
             (if (not (empty? matches))
               (vector (first matches) (first rem))
               (recur (rest rem)))))))
