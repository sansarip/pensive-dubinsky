(use 'pensive-dubinsky.parse)

(parse-text
  (reduce
    (fn [s f] (str s (slurp f)))
    ""
    *command-line-args*))


