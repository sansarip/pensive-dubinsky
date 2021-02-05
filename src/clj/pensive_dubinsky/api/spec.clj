(ns pensive-dubinsky.api.spec
  (:require [clojure.spec.alpha :as s])
  (:import (java.time LocalDate)
           (java.time.format DateTimeFormatter DateTimeParseException)))

;; https://emailregex.com/
(def email-regex
  #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")

(def hex-color-regex
  #"^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$")

(defn matches-email-regex? [email]
  (re-matches email-regex email))

(defn matches-hex-color-regex? [color]
  (re-matches hex-color-regex color))

(def date-format (DateTimeFormatter/ofPattern "MM/dd/yyyy"))

(defn conform-date-string [date]
  (try
    (LocalDate/parse date date-format)
    (catch DateTimeParseException _)))

(defn valid-date? [date]
  (instance? LocalDate date))

(s/def ::last-name string?)
(s/def ::first-name string?)
(s/def ::email (s/and string? matches-email-regex?))
(s/def ::favorite-color (s/and string? matches-hex-color-regex?))
(s/def :date/date-of-birth valid-date?)
(s/def :string/date-of-birth (s/and string? conform-date-string))

(s/def ::record (s/keys
                  :req-un [::last-name
                           ::first-name
                           ::email
                           ::favorite-color
                           :date/date-of-birth]))

(s/def ::create-record (s/keys
                         :req-un [::last-name
                                  ::first-name
                                  ::email
                                  ::favorite-color
                                  :string/date-of-birth]))

