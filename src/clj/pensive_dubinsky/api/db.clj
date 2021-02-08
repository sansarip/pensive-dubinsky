(ns pensive-dubinsky.api.db)

(defonce db (atom []))

(defn add-records [records]
  (swap! db into records))