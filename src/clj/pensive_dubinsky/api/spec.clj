(ns pensive-dubinsky.api.spec
  (:require [clojure.spec.alpha :as s]
            [pensive-dubinsky.parse :as parse]
            [pensive-dubinsky.util :as util])
  (:import (java.time LocalDate)))

;; https://emailregex.com/
(def email-regex
  #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")

(def hex-color-regex
  #"^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$")

(defn matches-email-regex? [email]
  (re-matches email-regex email))

(defn matches-hex-color-regex? [color]
  (re-matches hex-color-regex color))

(defn valid-name? [name]
  (not (re-find parse/value-delimiter-regex name)))


(defn valid-date? [date]
  (instance? LocalDate date))

(s/def ::name (s/and string? valid-name?))
(s/def ::last-name ::name)
(s/def ::first-name ::name)
(s/def ::email (s/and string? matches-email-regex?))
(s/def ::favorite-color (s/and string? matches-hex-color-regex?))
(s/def :date/date-of-birth valid-date?)
(s/def :string/date-of-birth (s/and string? util/conform-date-string))

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

