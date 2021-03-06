(ns pensive-dubinsky.gen
  (:require
    [pensive-dubinsky.api.spec :as spec]
    [clojure.spec.alpha :as s]
    [clojure.test.check.generators :as gen]
    [clojure.string :as string]
    [miner.strgen :as sg]
    [pensive-dubinsky.parse :as parse]
    [pensive-dubinsky.util :as util]))

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
         util/date-string->local-date
         (s/gen :string/date-of-birth)))))

(s/def ::spec/data-line
  (s/with-gen
    ::spec/data-line
    #(gen/fmap
       (fn [[delim record]]
         (->> parse/headers
              (map record)
              (interpose delim)
              string/join))
       (gen/tuple
         (sg/string-generator parse/value-delimiter-regex)
         (s/gen ::spec/record)))))

(defn records-with-dates [dates]
  (gen/fmap
    (fn [[shuffled-dates records]]
      (map
        (fn [[date record]]
          (assoc record :date-of-birth date))
        (zipmap shuffled-dates records)))
    (gen/tuple
      (gen/shuffle dates)
      (gen/vector (s/gen ::spec/record)
                  (count dates)))))

(def record-line
  (gen/fmap
    (fn [[delimiter record]]
      (string/join delimiter (map record parse/headers)))
    (gen/tuple
      (sg/string-generator parse/value-delimiter-regex)
      (s/gen ::spec/record))))

(def sample-file-paths ["samples/sample1.txt"
                        "samples/sample2.txt"
                        "samples/sample3.txt"])

(defn generate-sample-files! [lines-per-sample]
  (doseq [fp sample-file-paths]
    (->> lines-per-sample
         (gen/sample record-line)
         (string/join (System/lineSeparator))
         (spit fp))))

(defn -main [& [lines-per-sample]]
  ((fnil generate-sample-files! 10)
   (try (Integer/parseInt lines-per-sample)
        (catch NumberFormatException _))))

