(ns pensive-dubinsky.api.handlers
  (:require
    [ring.util.http-response :as resp]
    [pensive-dubinsky.api.db :as db]
    [pensive-dubinsky.parse :as parse]
    [pensive-dubinsky.sort :as sort*]))

(defn sort-by-email [_]
  (resp/ok
    (sort*/sort-by-ascending-email @db/db)))

(defn sort-by-full-name [_]
  (resp/ok
    (sort*/sort-by-ascending-full-name @db/db)))

(defn sort-by-birth-date [_]
  (resp/ok
    (sort*/sort-by-ascending-birth-date @db/db)))

(defn add-record [{{{data-line :data-line} :body} :parameters}]
  (resp/created
    ;; FIXME: Add correct location of resource
    "/"
    (db/add-record (parse/line->map data-line))))

