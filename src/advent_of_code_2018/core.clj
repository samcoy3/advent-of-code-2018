(ns advent-of-code-2018.core
  (:use [clojure.pprint])
  (:require [advent-of-code-2018.day01 :as day01])
  (:gen-class))

(defn -main
  "Prints all of the solutions to the problems solved so far."
  []
  (print-table
   [
    {:day 1 :part "A" :answer (day01/part-a)}
    {:day 1 :part "B" :answer (day01/part-b)}
    ]))
