(ns pensive-dubinsky.parse
  (:require [clojure.string :as string]))

(def headers [:last-name
              :first-name
              :email
              :favorite-color
              :date-of-birth])

(def value-delimiter-regex
  #"(, )|( \| )|( )")

(defn line->map [line]
  (->> (string/split line value-delimiter-regex)
       (interleave headers)
       (apply array-map)))

(defn parse-text [text]
  (map line->map (string/split-lines text)))


