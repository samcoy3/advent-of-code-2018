(ns advent-of-code-2018.day07)

(defn read-input
  "We want the input as a list of vectors. Each vector represents an edge."
  []
  (->> (clojure.string/split-lines (slurp "resources/day07.txt"))
       (map #(clojure.string/split % #" "))
       (map #(list (get % 1) (get % 7)))
       flatten
       (map first)
       (partition 2)
       (map #(apply vector %))
       (apply list)))

(defn get-next-available-vertex
  "Given:
  - vertex-list: A list of possible vertices
  - edge-list: A list of edges, the source of which have not been completed
  - vertices-chosen: A list of vertices that are (for any reason) invalid

  This method returns the smallest (alphabetically) vertex that can be chosen (those which are not in vertices-chosen and are not the recipient of any edge in edge-list)"
  [vertex-list edge-list vertices-chosen]
  (let [illegal-vertices (apply hash-set (map second edge-list))
        eligible-vertices (filter #(not (or (contains? illegal-vertices %)
                                            (contains? vertices-chosen %)))
                                  vertex-list)]
    (first (sort eligible-vertices))))

(defn choose-vertices-in-order
  "Given:
  - vertex-list: A list of vertices to visit in some order
  - edge-list: A list of directed edges; sources of these edges must be completed before the recipients

  This function returns the correct order in which to visit the vertices in vertex-list (breaking ties alphabetically)."
  [vertex-list edge-list]
  (loop [vertex-string ""
         relevant-edges edge-list]
    (if (= (count vertex-string) (count vertex-list))
      vertex-string
      (let [chosen-vertex (get-next-available-vertex vertex-list
                                                     relevant-edges
                                                     (apply hash-set (seq vertex-string)))]
        (recur
         (concat vertex-string (str chosen-vertex))
         (filter #(not= (first %) chosen-vertex) relevant-edges))))))

(defn part-a []
  (apply str
         (choose-vertices-in-order (apply list (map char (range 65 91)))
                                   (read-input))))

(defn get-time-for-job
  "As specified, job A takes 61 seconds, job B 62, etc. This function calculates that, given a char (it's just the charcode minus 4)."
  [c]
  (- (int c) 4))

(defn choose-vertices-timed
  "Given:
  - number-of-workers: A number or workers that are available to perform tasks
  - vertex-list: The list of vertices to be visited.
  - edge-list: The list of edge dependencies (as in the function above)

  This function returns the length of time taken for the worker to complete all tasks (as specified in the problem), using the following recursive approach:

  At each stage:
  - If the list of completed jobs is the list of jobs, return the current time, otherwise:
  - If there is a free worker and an available job:
    - The worker takes that job instantly (without updating the time)
  - Else
    - Mark the next job scheduled to be completed as completed
    - Advance the 'current time' to the time at which that job is completed"
  [number-of-workers vertex-list edge-list]
  (loop [done-vertices ""
         worker-jobs (sorted-set-by #(if (= (second %1) (second %2))
                                       (< (int (first %1)) (int (first %2)))
                                       (< (second %1) (second %2))))
         relevant-edges edge-list
         current-time 0]
    (if (= (count done-vertices) (count vertex-list))
      current-time
      (if-let [next-vertex
                (get-next-available-vertex vertex-list
                                           relevant-edges
                                           (clojure.set/union (apply hash-set (seq done-vertices))
                                                              (apply hash-set (map first worker-jobs))))]
        (if (<= (count worker-jobs) number-of-workers)
          (recur done-vertices
                 (into worker-jobs (list [next-vertex (+ current-time (get-time-for-job next-vertex))]))
                 relevant-edges
                 current-time))
        (let [just-completed-job (first worker-jobs)
              just-completed-vertex (first just-completed-job)]
          (recur (concat done-vertices (str just-completed-vertex))
                 (clojure.set/difference worker-jobs #{just-completed-job})
                 (filter #(not= (first %) just-completed-vertex) relevant-edges)
                 (second just-completed-job)))))))

(defn part-b []
  (choose-vertices-timed 5
                         (apply list (map char (range 65 91)))
                         (read-input)))
