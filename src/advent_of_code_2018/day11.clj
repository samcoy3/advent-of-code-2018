(ns advent-of-code-2018.day11)

(defn power-function
  "Power function as specified in the problem."
  [grid-serial x y]
  (->> (+ x 10)
       (* y)
       (+ grid-serial)
       (* (+ x 10))
       ; The next three lines are all to get the hundreds digit.
       (#(/ % 100))
       Math/floor
       (#(mod % 10))
       int
       (#(- % 5))))

(defn max-of-size
  "Given a size and a memoizing power function, returns the maximum-power square of that size in the grid."
  [size *power]
  (first (sort #(compare (first %2) (first %1))
               (for [x (range 1 (- 302 size))
                     y (range 1 (- 302 size))]
                 (list (reduce +
                               (for [dx (range 0 size)
                                     dy (range 0 size)]
                                 (*power (+ x dx) (+ y dy))))
                       (list x y size))))))

(defn part-a []
  (second (max-of-size 3
                       (memoize (partial power-function 6303)))))

; Part b takes quite some time to run here, more efficient solutions are probably possible.
; Unsure whether memoize is caching when passed to max-of-size, I assume so though.
; Fortunately, the solution is at size=12.
(defn part-b []
  (let [grid-serial 6303
        *power (memoize (partial power-function grid-serial))]
    (second
     (sort #(compare (first %2) (first %1))
           (for [size (range 1 301)]
             (max-of-size size *power))))))
