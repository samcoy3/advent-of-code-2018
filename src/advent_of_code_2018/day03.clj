(ns advent-of-code-2018.day03
  (:use clojure.set))

(defn read-input []
  (clojure.string/split-lines (slurp "resources/day03.txt")))

(defn get-number-bounds
  "Returns a vector containing the bounds, given a line of the input"
  [input-line]
  (apply vector
         (map #(Integer/parseInt
                (get (clojure.string/split input-line #"[,: x]") %)) [2 3 5 6])))

(def elf-plans
  "A convenient way to access a sanitised list of the input"
  (map get-number-bounds (read-input)))

(defn interval-overlap
  "Given two intervals, return their overlap.
  If there isn't one, return []."
  [[x1 x2] [y1 y2]]
  (if (< x2 y1)
    []
    (if (< y2 x1)
      []
      [(max x1 y1) (min x2 y2)])))

(defn overlap
  "Given two sanitised lines of the input, return a list of all points where they overlap.
  Returns '() if the regions are non-overlapping."
  [[lx1 ly1 dx1 dy1] [lx2 ly2 dx2 dy2]]
  (let [xrange (interval-overlap [lx1 (+ lx1 dx1)] [lx2 (+ lx2 dx2)])
        yrange (interval-overlap [ly1 (+ ly1 dy1)] [ly2 (+ ly2 dy2)])]
    (if (or (empty? xrange) (empty? yrange))
      '()
      (for [x (apply range xrange)
            y (apply range yrange)]
        [x y]))))

(defn part-a []
  (count
   (loop [elf-plans--tail elf-plans
          dup-points (hash-set)]
     (if (= (count elf-plans--tail) 1)
       dup-points
       (recur (rest elf-plans--tail)
              ; This jiggery-pokery basically flattens the set.
              (clojure.set/union
               dup-points
               (apply hash-set (partition 2 (flatten
                                             (map #(overlap (first elf-plans--tail) %)
                                                  (rest elf-plans--tail)))))))))))

(defn get-size-of-overlap
  "Gets the number of points in a region of cloth that some other region also covers."
  [plan]
  (count
   (apply hash-set
          (partition 2
                     (flatten (map #(if (= plan %)
                                      (list)
                                      (overlap plan %))
                                   elf-plans))))))

(defn part-b []
  (+ 1
     (.indexOf
        elf-plans
        (first (filter #(= 0 (get-size-of-overlap %)) elf-plans)))))
