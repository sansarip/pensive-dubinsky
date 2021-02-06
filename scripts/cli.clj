(use 'pensive-dubinsky.parse)
(use 'pensive-dubinsky.sort)
(require '[clojure.string :as string])

(def records
  (parse-text
    (reduce
      (fn [s f] (str s (slurp f)))
      ""
      *command-line-args*)))

(def time-ms (System/currentTimeMillis))

(defn ->text [r]
  (string/join
    (System/lineSeparator)
    (map #(string/join " | " (vals %)) r)))

(def output-file-paths
  {(str "out/sort-by-descending-email-and-ascending-last-name-"
        time-ms
        ".txt")
   sort-by-descending-email-and-ascending-last-name
   (str "out/sort-by-ascending-birth-date-"
        time-ms
        ".txt")
   sort-by-ascending-birth-date
   (str "out/sort-by-descending-last-name-"
        time-ms
        ".txt")
   sort-by-descending-last-name})

(doseq [[fp sort-fn] output-file-paths]
  (spit fp ((comp ->text sort-fn) records)))







