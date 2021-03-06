(ns advent-of-code-2018.core
  (:use [clojure.pprint])
  (:require [advent-of-code-2018.day01 :as day01]
            [advent-of-code-2018.day02 :as day02]
            [advent-of-code-2018.day03 :as day03]
            [advent-of-code-2018.day04 :as day04]
            [advent-of-code-2018.day05 :as day05]
            [advent-of-code-2018.day06 :as day06]
            [advent-of-code-2018.day07 :as day07]
            [advent-of-code-2018.day08 :as day08]
            [advent-of-code-2018.day09 :as day09]
            [advent-of-code-2018.day10 :as day10]
            [advent-of-code-2018.day11 :as day11]
            [advent-of-code-2018.day12 :as day12]
            [advent-of-code-2018.day13 :as day13]
            [advent-of-code-2018.day14 :as day14])
  (:gen-class))

(defn -main
  "Prints all of the solutions to the problems solved so far."
  []
  (print-table
   [
    {:day 1 :part "A" :answer (day01/part-a)}
    {:day 1 :part "B" :answer (day01/part-b)}
    {:day 2 :part "A" :answer (day02/part-a)}
    {:day 2 :part "B" :answer (day02/part-b)}
    {:day 3 :part "A" :answer (day03/part-a)}
    {:day 3 :part "B" :answer (day03/part-b)}
    {:day 4 :part "A" :answer (day04/part-a)}
    {:day 4 :part "B" :answer (day04/part-b)}
    {:day 5 :part "A" :answer (day05/part-a)}
    {:day 5 :part "B" :answer (day05/part-b)}
    {:day 6 :part "A" :answer (day06/part-a)}
    {:day 6 :part "B" :answer (day06/part-b)}
    {:day 7 :part "A" :answer (day07/part-a)}
    {:day 7 :part "B" :answer (day07/part-b)}
    {:day 8 :part "A" :answer (day08/part-a)}
    {:day 8 :part "B" :answer (day08/part-b)}
    {:day 9 :part "A" :answer (day09/part-a)}
    {:day 9 :part "B" :answer (day09/part-b)}
    {:day 10 :part "A" :answer (day10/part-a)}
    {:day 10 :part "B" :answer (day10/part-b)}
    {:day 11 :part "A" :answer (day11/part-a)}
    {:day 11 :part "B" :answer (day11/part-b)}
    {:day 12 :part "A" :answer (day12/part-a)}
    {:day 12 :part "B" :answer (day12/part-b)}
    {:day 13 :part "A" :answer (day13/part-a)}
    {:day 13 :part "B" :answer (day13/part-b)}
    {:day 14 :part "A" :answer (day14/part-a)}
    {:day 14 :part "B" :answer (day14/part-b)}
    ]))
