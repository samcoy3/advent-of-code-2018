(ns advent-of-code-2018.day13)

; This is a data structure which sorts the carts by "reading order."
(def cart-priority-set (sorted-set-by #(if (= (second (:position %1)) (second (:position %2)))
                                         (compare (first (:position %1))
                                                  (first (:position %2)))
                                         (compare (second (:position %1))
                                                  (second (:position %2))))))

; The next three maps are just representing orders for certain things.
; For example, if you head upwards into a /, then you should be going right afterwards.
(def forward-deflector-direction-map {:left :down
                                      :down :left
                                      :up :right
                                      :right :up})

(def backward-deflector-direction-map {:left :up
                                       :up :left
                                       :down :right
                                       :right :down})

(def turn-order-map {:left :straight
                     :straight :right
                     :right :left})

(defn turn
  "Given a current heading (up, down, left, right), and a turning (left, straight right), turn."
  [cur-dir turn-dir]
  (cond
    (= turn-dir :straight) cur-dir
    (= turn-dir :left) (get {:left :down
                             :down :right
                             :right :up
                             :up :left}
                            cur-dir)
    (= turn-dir :right) (get {:left :up
                              :up :right
                              :right :down
                              :down :left}
                             cur-dir)))

(defn read-input []
  (loop [input (slurp "resources/day13.txt")
         x 0
         y 0
         state {:carts cart-priority-set
                :intersections (hash-set)
                :forward-deflectors (hash-set)
                :backward-deflectors (hash-set)}]
    (if (empty? input)
      state
      (if (= (first input) \newline)
        (recur (rest input)
               0
               (inc y)
               state)
        (let [state-update-function (condp = (first input)
                                      \+ (fn [m] (update m :intersections conj [x y]))
                                      \\ (fn [m] (update m :backward-deflectors conj [x y]))
                                      \/ (fn [m] (update m :forward-deflectors conj [x y]))
                                      \> (fn [m] (update m :carts conj {:position [x y]
                                                                       :cur-dir :right
                                                                       :next-turn :left}))
                                      \< (fn [m] (update m :carts conj {:position [x y]
                                                                       :cur-dir :left
                                                                       :next-turn :left}))
                                      \^ (fn [m] (update m :carts conj {:position [x y]
                                                                       :cur-dir :up
                                                                       :next-turn :left}))
                                      \v (fn [m] (update m :carts conj {:position [x y]
                                                                       :cur-dir :down
                                                                       :next-turn :left}))
                                      identity)]
          (recur (rest input)
                 (inc x)
                 y
                 (state-update-function state)))))))

(defn advance-until-crash
  "Advances the configuration of carts until a crash occurs. Returns the stringification of the crash-site."
  [carts forward backward intersections]
  (loop [current-positions carts
         next-positions cart-priority-set]
    (if (empty? current-positions)
      next-positions
      (let [cart (first current-positions)
            new-cart-pos (condp = (:cur-dir cart)
                           :left (apply vector (map + (:position cart) [-1 0]))
                           :up (apply vector (map + (:position cart) [0 -1]))
                           :right (apply vector (map + (:position cart) [1 0]))
                           :down (apply vector (map + (:position cart) [0 1])))]
        (if (or (some #{new-cart-pos} (map :position next-positions))
                (some #{new-cart-pos} (map :position current-positions)))
          (clojure.string/join "," new-cart-pos)
          (let [direction-update-func
                (cond
                  (contains? intersections new-cart-pos)
                  (fn [c] (-> c
                              (assoc :cur-dir (turn (:cur-dir c) (:next-turn c)))
                              (assoc :next-turn (get turn-order-map (:next-turn c)))))

                  (contains? forward new-cart-pos)
                  (fn [c] (assoc c :cur-dir (get forward-deflector-direction-map (:cur-dir c))))

                  (contains? backward new-cart-pos) (fn [c] (assoc c :cur-dir (get backward-deflector-direction-map (:cur-dir c))))

                  :default
                  identity)]
            (recur (rest current-positions)
                   (conj next-positions (-> cart
                                            (assoc :position new-cart-pos)
                                            direction-update-func)))))))))

(defn part-a []
  (let [{:keys [carts intersections forward-deflectors backward-deflectors]} (read-input)]
    (loop [carts carts]
      (let [next-carts (advance-until-crash carts
                                            forward-deflectors
                                            backward-deflectors
                                            intersections)]
        (if (string? next-carts)
          next-carts
          (recur next-carts))))))

(defn advance-one-step
  "Advances the position of the carts by one step. Removes carts that crash."
  [carts forward backward intersections]
  (loop [current-positions carts
         next-positions cart-priority-set]
    (if (empty? current-positions)
      next-positions
      (let [cart (first current-positions)
            new-cart-pos (condp = (:cur-dir cart)
                           :left (apply vector (map + (:position cart) [-1 0]))
                           :up (apply vector (map + (:position cart) [0 -1]))
                           :right (apply vector (map + (:position cart) [1 0]))
                           :down (apply vector (map + (:position cart) [0 1])))]
        (if (or (some #{new-cart-pos} (map :position next-positions))
                (some #{new-cart-pos} (map :position current-positions)))
              (recur (filter #(and (not= new-cart-pos (:position %))
                                   (not= (:position cart) (:position %))) current-positions)
                     (filter #(and (not= new-cart-pos (:position %))
                                   (not= (:position cart) (:position %))) next-positions))
          (let [direction-update-func
                (cond
                  (contains? intersections new-cart-pos)
                  (fn [c] (-> c
                              (assoc :cur-dir (turn (:cur-dir c) (:next-turn c)))
                              (assoc :next-turn (get turn-order-map (:next-turn c)))))

                  (contains? forward new-cart-pos)
                  (fn [c] (assoc c :cur-dir (get forward-deflector-direction-map (:cur-dir c))))

                  (contains? backward new-cart-pos)
                  (fn [c] (assoc c :cur-dir (get backward-deflector-direction-map (:cur-dir c))))

                  :default
                  identity)]
            (recur (rest current-positions)
                   (conj next-positions (-> cart
                                            (assoc :position new-cart-pos)
                                            direction-update-func)))))))))

(defn part-b []
  (let [{:keys [carts intersections forward-deflectors backward-deflectors]} (read-input)]
    (loop [carts carts]
      (let [next-carts (advance-one-step carts
                                               forward-deflectors
                                               backward-deflectors
                                               intersections)]
        (if (= (count next-carts) 1)
          (clojure.string/join "," (:position (first next-carts)))
          (recur next-carts))))))
