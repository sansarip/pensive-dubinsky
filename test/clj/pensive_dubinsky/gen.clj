(ns pensive-dubinsky.gen
  (:require [pensive-dubinsky.api.spec :as spec]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]
            [clojure.string :as string]
            [miner.strgen :as sg]))

(s/def ::spec/favorite-color
  (s/with-gen
    ::spec/favorite-color
    #(sg/string-generator spec/hex-color-regex)))

(s/def ::spec/email
  (s/with-gen
    ::spec/email
    #(sg/string-generator spec/email-regex)))

(s/def :string/date-of-birth
  (s/with-gen
    :string/date-of-birth
    #(gen/fmap
       (fn [date-tuple] (string/join "/" date-tuple))
       (gen/tuple
         (gen/choose 1 12)
         (gen/choose 1 28)
         (gen/choose 1901 2020)))))

(s/def :date/date-of-birth
  (s/with-gen
    :date/date-of-birth
    #(gen/such-that
       identity
       (gen/fmap
         spec/conform-date-string
         (s/gen :string/date-of-birth)))))


