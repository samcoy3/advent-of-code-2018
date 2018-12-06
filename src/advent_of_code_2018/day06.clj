(ns advent-of-code-2018.day06)

(defn read-input []
  (->> (clojure.string/split-lines (slurp "resources/day06.txt"))
       (map #(clojure.string/split % #", "))
       flatten
       (map #(Integer/parseInt %))
       (partition 2)
       (map #(apply vector %))))

(defn distance
  "Simple calculator of Manhattan distance."
  [[a b] [c d]]
  (+
   (Math/abs (- a c))
   (Math/abs (- b d))))

(defn get-relevant-points
  "Given a collection of points, returns the points inside the tightest bounding box, and the points just outside it."
  [points-coll]
  (let [low-x (apply min (map first points-coll))
        high-x (apply max (map first points-coll))
        low-y (apply min (map second points-coll))
        high-y (apply max (map second points-coll))]
    {
     :internal-points (for [x (range low-x (inc high-x))
                            y (range low-y (inc high-y))]
                        [x y])

     :boundary-points (concat
                       (for [x (list (dec low-x) (inc high-x))
                             y (range low-y (inc high-y))]
                         [x y])
                       (for [y (list (dec low-y) (inc high-y))
                             x (range low-x (inc high-x))]
                         [x y]))}))

(defn closest-point
  "Given a collection of points and a point, returns the closest point in the collection of points to the point.
  Returns nil if multiple points are closest."
  [points-coll point]
  (let [min-distance (apply min (map #(distance point %) points-coll))
        closest-points (filter
                        #(= (distance % point) min-distance)
                        points-coll)]
    (if (> (count closest-points) 1)
      nil
      (first closest-points))))

(defn part-a
  "We get the internal areas for all points and then remove points that have an illegally infinite area."
  []
  (let [input (read-input)
        {boundary :boundary-points
         internal :internal-points} (get-relevant-points input)
        illegal-points (apply hash-set (map #(closest-point input %) boundary))
        areas-in-boundary (frequencies (map #(closest-point input %) internal))]
    (apply max
           (map second
                (apply dissoc
                       areas-in-boundary
                       illegal-points)))))

(defn total-distance-from-points
  "Given a point and a collection of points returns the total of the distances from the point to each in the collection."
  [point points-coll]
  (reduce + (map #(distance point %) points-coll)))

(defn part-b []
  (let [input (read-input)
        {internal :internal-points} (get-relevant-points input)]
    (count
     (filter
      #(< (total-distance-from-points % input) 10000)
      internal))))
