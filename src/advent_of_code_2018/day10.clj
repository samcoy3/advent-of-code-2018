(ns advent-of-code-2018.day10)

(defn read-input []
  (->> (slurp "resources/day10.txt")
       clojure.string/split-lines
       (map #(re-seq #"-?\d+" %))
       (map (fn [[a b c d]]
              {:point [(Integer/parseInt a) (Integer/parseInt b)]
               :velocity [(Integer/parseInt c) (Integer/parseInt d)]}))))

(defn update-step
  "Given a set of points and their velocities, update the points by one step."
  [point-data]
  (map (fn [point] {:point (map +
                      (:point point)
                      (:velocity point))
          :velocity (:velocity point)})
       point-data))

(defn print-configuration
  "Prints a list of points."
  [points]
  (let [min-x (apply min (map first points))
        max-x (apply max (map first points))
        min-y (apply min (map second points))
        max-y (apply max (map second points))]
    (clojure.string/join "\n"
                         (for [y (range min-y (inc max-y))]
                           (let [active-x (apply list (map first
                                                           (filter #(= (second %) y)
                                                                   points)))]
                             (->> (apply list (range min-x (inc max-x)))
                                  (map #(if (some #{%} active-x)
                                          \#
                                          \.))
                                  (clojure.string/join "")))))))

(defn run-until-local
  "Updates the points until their y-range is minimised.
  It turns out (as expected), that this is when the point form a character sequence."
  [point-data]
  (loop [point-data point-data
         iteration 0]
    (let [new-point-data (update-step point-data)]
      (if (> (- (apply max (map #(second (:point %)) new-point-data))
                (apply min (map #(second (:point %)) new-point-data)))
             (- (apply max (map #(second (:point %)) point-data))
                (apply min (map #(second (:point %)) point-data))))
        {:configuration (print-configuration (map :point point-data))
         :iterations     iteration}
        (recur new-point-data (inc iteration))))))

(defn part-a []
  (:configuration (run-until-local (read-input))))

(defn part-b []
  (:iterations (run-until-local (read-input))))
