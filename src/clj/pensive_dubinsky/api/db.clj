(ns pensive-dubinsky.api.db)

(defonce db (atom []))

(defn add-record [record]
  (swap! db conj record))