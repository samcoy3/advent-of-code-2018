(ns advent-of-code-2018.day09
  (:import [java.util LinkedList]))

(defn read-input []
  (let [input (clojure.string/split (slurp "resources/day09.txt") #" ")]
    [(Integer/parseInt (get input 0)) (Integer/parseInt (get input 6))]))

(defn play-game
  "We use a Java LinkedList to store the state of the board.
  I wanted to try to get a solution to be somewhat acceptable without mutable data structures, but that seems very difficult."
  [players marbles]
  (let [board (LinkedList. [0])]
  (loop [scores (apply vector (repeat players 0))
         current-marble 1]
    (if (> current-marble marbles)
      scores
      (if (zero? (mod current-marble 23))
        (do (dotimes [n 7]
              (.add board 0 (.removeLast board)))
            (let [last-marble (.removeLast board)
                  score (+ current-marble last-marble)]
              (.add board (.removeFirst board))
              (recur (update scores (mod current-marble players) #(+ % score))
                     (inc current-marble))))
        (do
          (.add board (.removeFirst board))
          (.add board current-marble)
          (recur scores
                 (inc current-marble))))))))

(defn part-a []
  (let [[players marbles] (read-input)]
    (apply max
           (play-game players marbles))))

(defn part-b []
  (let [[players marbles] (read-input)]
    (apply max
           (play-game players (* 100 marbles)))))
