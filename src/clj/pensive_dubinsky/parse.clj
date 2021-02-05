(ns pensive-dubinsky.parse
  (:require [clojure.string :as string]
            [pensive-dubinsky.api.spec :as spec]))

(def headers [:last-name
              :first-name
              :email
              :favorite-color
              :date-of-birth])

(defn coerce-record [record]
  (update record :date-of-birth spec/conform-date-string))

(defn line->record [line]
  (zipmap
    headers
    (string/split line #"(, )|( \| )|( )")))

(defn ->lines [text]
  (string/split text #"\R"))

(defn parse-text [text]
  (map (comp coerce-record line->record) (->lines text)))
