(ns pensive-dubinsky.test-util
  (:require [pensive-dubinsky.api.db :as db]))

(def num-tests 100)

(defn with-cleanup [record f]
  (try
    (f)
    (finally
      (reset!
        db/db
        (filterv
          (partial not= record)
          @db/db)))))
