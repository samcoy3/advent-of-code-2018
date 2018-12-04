(ns advent-of-code-2018.day04
  (:require [clj-time.core :as t]
            [clj-time.format :as f]))

(defn which-midnight?
  "Given a line of input, return the midnight around which it is centered."
  [input-line]
  (let [parts (clojure.string/split input-line #"[\[\] :]")
        date-string (get parts 1)
        date (f/parse (f/formatter "YYYY-MM-dd") date-string)]
    (if (= (get parts 2) "23")
      (t/plus date (t/days 1))
      date)))

(defn which-guard?
  "Given a list of strings for one night, find which guard is responsible."
  [night-report]
  (let [guard-announcement (first night-report)]
    (-> guard-announcement
        (clojure.string/split #"[ #]")
        (get 4) ; Turns out, this is where the guard number is.
        Integer/parseInt)))

(defn get-night-vector
  "Given a list of strings for one night, return a vector containing a 1 during times the guard was asleep."
  [night-report]
  (->> night-report
      rest
      (map #(clojure.string/split % #"[: \]]"))
      (map #(get % 2))
      (map #(Integer/parseInt %))
      (#(concat % (list 60))) ; This is so partition doesn't discard information
      (partition 2)
      (#(flatten (for [x %] (apply range x)))) ; We get a list of times where the guard is asleep
      (reduce (fn [v u]
                 (update v u inc))
               (vec (repeat 60 0)))))

(defn get-guard-summary
  "Given a list of reports for a guard, return a map of the guard number to an array of number of times they were asleep at each minute over all reports."
  [guard-report]
  (vec (apply map + (map get-night-vector
                         guard-report))))

(defn read-input
  "We do quite a bit here - we return a map from guard ID to a vector containing the number of instances of them being asleep each minute."
  []
  (->> (clojure.string/split-lines (slurp "resources/day04.txt"))
       (sort)
       (partition-by which-midnight?)
       (group-by which-guard?)
       (#(into {} (for [[g v] %] [g (get-guard-summary v)])))))

(defn part-a []
  (let [input (read-input)
        ; Returns a map from guard -> total minutes asleep
        minutes-asleep (into {} (for [[g v] input] [g (reduce + v)]))
        sleepiest-guard (-> (into
                             (sorted-map-by >)
                             (clojure.set/map-invert minutes-asleep))
                            first
                            second)
        sleepiest-vector (get input sleepiest-guard)
        sleepiest-minute (-> (into
                              (sorted-map-by >)
                              (zipmap sleepiest-vector (iterate inc 0)))
                             first
                             second)]
    (*
     sleepiest-minute
     sleepiest-guard)))

(defn part-b []
  (let [input (read-input)
        ; sleepiest-minute-per-guard is a map from (guard, minute) -> occurence
        sleepiest-minute-per-guard (into {} (for [[g v] input]
                                              [(list g (-> (into
                                                            (sorted-map-by >)
                                                            (zipmap v (iterate inc 0)))
                                                           first
                                                           second))
                                               (apply max v)]))]
    (->> (into
          (sorted-map-by >)
          (clojure.set/map-invert sleepiest-minute-per-guard))
         first
         second
         (reduce *))))
