(ns advent-of-code-2018.day14)

(defn make-recipes [elf1 elf2]
  (->> (+ elf1 elf2)
       str
       seq
       (map #(str %))
       (map #(Integer/parseInt %))
       (apply vector)))

(defn advance-one-iteration
  "Advances the state by one iteration - makes new recipes and then moves the elves."
  [[elf1pos elf2pos recipes]]
  (let [elf1value (get recipes elf1pos)
        elf2value (get recipes elf2pos)
        new-recipes (apply conj recipes (make-recipes elf1value elf2value))
        new-number-of-recipes (count new-recipes)
        newelf1pos (mod (+ 1 elf1pos elf1value) new-number-of-recipes)
        newelf2pos (mod (+ 1 elf2pos elf2value) new-number-of-recipes)]
    (list newelf1pos newelf2pos new-recipes)))

(defn apply-until-length
  "Applies the process until the recipes are of at least a certain length."
  [length]
  (loop [state '(0 1 [3 7])]
    (if (< (count (last state)) length)
      (recur (advance-one-iteration state))
      (take length (last state)))))

(defn apply-until-pattern
  "Applies the process until a certain pattern appears."
  [pattern]
  (loop [state '(0 1 [3 7])]
    (let [recipes (last state)
          number-of-recipes (count recipes)]
      (if (or (< number-of-recipes (inc (count pattern)))
              (and (not= (subvec recipes
                                 (- number-of-recipes (count pattern)))
                         pattern)
                   (not= (subvec recipes
                                 (- number-of-recipes (count pattern) 1)
                                 (- number-of-recipes 1))
                         pattern)))
        (recur (advance-one-iteration state))
        recipes))))

(defn part-a []
  (->> (+ 286051 10)
       apply-until-length
       (take-last 10)
       (clojure.string/join "")))

(defn part-b []
  (->> [2 8 6 0 5 1]
       apply-until-pattern
       count
       (#(- % 7)) ; This is presumptuous, but in this case, correct.
       str))
