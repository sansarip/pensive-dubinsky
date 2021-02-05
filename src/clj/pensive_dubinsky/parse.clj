(ns pensive-dubinsky.parse
  (:require [clojure.string :as string]
            [pensive-dubinsky.util :as util]))

(def headers [:last-name
              :first-name
              :email
              :favorite-color
              :date-of-birth])

(def value-delimiter-regex
  #"(, )|( \| )|( )")

(defn coerce-record [record]
  (update record :date-of-birth util/conform-date-string))

(defn line->record [line]
  (zipmap
    headers
    (string/split line value-delimiter-regex)))

(defn ->lines [text]
  (string/split text #"\R"))

(defn parse-text [text]
  (map (comp coerce-record line->record) (->lines text)))
