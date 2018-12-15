(ns advent-of-code-2018.day12)

(defn read-input []
  (let [input-file (clojure.string/split-lines (slurp "resources/day12.txt"))
        initial-state (first (re-seq #"[\.#]+" (first input-file)))
        rules (rest (rest input-file))]
    {:pots initial-state
     :rules (->> (map #(re-seq #"[\.#]+" %) rules)
                 (map #(apply vector %))
                 (apply vector)
                 (into {}))
     :offset 0
     }))

(defn advance-step
  "Advances the configuration of plants by one timestep, according to the rules."
  [{:keys [pots rules offset]}]
  (let [offset (- offset 2)
        pots (apply str (concat "...." pots "...."))
        next-pots (->> (range 2 (- (count pots) 3))
                       (map #(subs pots (- % 2) (+ % 3)))
                       (map #(get rules %))
                       (apply str))
        next-pots-with-trim (re-find #"#[\.#]*" next-pots)
        new-offset (+ offset
                      (- (count next-pots)
                         (count next-pots-with-trim)))]
    {:pots (re-find #"[\.#]+#" next-pots-with-trim)
     :rules rules
     :offset new-offset}))

(defn part-a []
  (let [initial-state (read-input)
        future-states (iterate advance-step initial-state)
        {:keys [pots offset]} (first (drop 20 future-states))]
    (->> (range 0 (count pots))
         (filter #(= (get pots %)
                     \#))
         (map #(+ offset %))
         (reduce +))))

(defn part-b []
  (let [initial-state (read-input)
        future-states (iterate advance-step initial-state)]
    (loop [iteration 0
           [next-state & more-states] future-states]
      (let [{:keys [pots offset]} next-state]
        (if (= iteration 1000) ; This works out to be stable, it could be automatable in the future.
          (+ ; We add the current iteration's score to the score it gains each iteration multiplied by the number of remaining iterations.
           (->> (range 0 (count pots))
                (filter #(= (get pots %)
                            \#))
                (map #(+ offset %))
                (reduce +))
           (* (- 50000000000 iteration)
              (->> (range 0 (count pots))
                   (filter #(= (get pots %)
                               \#))
                   count)))
          (recur (inc iteration) more-states))
        ))))
