(ns pensive-dubinsky.util
  (:import (java.time.format DateTimeFormatter DateTimeParseException)
           (java.time LocalDate)))

(def date-format (DateTimeFormatter/ofPattern "MM/dd/yyyy"))

(defn conform-date-string [date]
  (try
    (LocalDate/parse date date-format)
    (catch DateTimeParseException _)))
