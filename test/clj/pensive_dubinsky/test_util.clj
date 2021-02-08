(ns pensive-dubinsky.test-util
  (:require [pensive-dubinsky.api.db :as db]))

(def num-tests 100)

(defn with-cleanup [records f]
  (try
    (f)
    (finally
      (reset!
        db/db
        (filterv
          (fn [record]
            (not-any? (partial = record) records))
          @db/db)))))
