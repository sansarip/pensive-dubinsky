(ns pensive-dubinsky.unit.sort-test
  (:require
    [pensive-dubinsky.api.spec :as spec]
    [pensive-dubinsky.gen]
    [pensive-dubinsky.sort :as sort*]
    [clojure.test :refer :all]
    [clojure.test.check.clojure-test :refer [defspec]]
    [clojure.test.check.properties :as prop]
    [clojure.test]
    [clojure.spec.alpha :as s]
    [clojure.test.check.generators :as gen]))

(defspec test-set-of-sorted-records-equal-to-set-of-input-records 100
  ;; Given
  (prop/for-all [record-set (gen/fmap
                              set
                              (s/gen ::spec/records))]

    ;; When
    (let [sorted-record-sets
          ((juxt
             (comp set sort*/sort-by-descending-email-and-ascending-last-name)
             (comp set sort*/sort-by-ascending-birth-date)
             (comp set sort*/sort-by-descending-last-name))
           record-set)]

      ;; Then
      (testing "Every sorted record set is equal to the original record set"
        (is (every? (partial = record-set) sorted-record-sets))))))

(defspec test-idempotent-sort-fns 100
  ;; Given
  (prop/for-all [[records num-juxt] (gen/fmap
                                      (fn [[records num-juxt]]
                                        [records
                                         (inc (Math/abs num-juxt))])
                                      (gen/tuple
                                        (s/gen ::spec/records)
                                        gen/small-integer))]

    ;; When
    (let [sorted-records
          (map
            (fn [_]
              ((juxt
                 sort*/sort-by-descending-email-and-ascending-last-name
                 sort*/sort-by-ascending-birth-date
                 sort*/sort-by-descending-last-name)
               records))
            (range num-juxt))]

      ;; Then
      (testing "The same inputs result in the same outputs"
        (is (apply = sorted-records))))))

(defspec test-sort-descending-email-with-constant-last-name 100
  ;; Given
  (prop/for-all [records (gen/fmap
                           (fn [records]
                             (map #(assoc % :last-name "a") records))
                           (s/gen ::spec/records))]

    ;; When
    (let [sorted-records (sort*/sort-by-descending-email-and-ascending-last-name
                           records)]

      ;; Then
      (is (= (sort-by :email #(compare %2 %1) records)
             sorted-records)))))


(defspec test-sort-ascending-last-name-with-constant-email 100
  ;; Given
  (prop/for-all [records (gen/fmap
                           (fn [records]
                             (map #(assoc % :email "an-email@test.com") records))
                           (s/gen ::spec/records))]

    ;; When
    (let [sorted-records (sort*/sort-by-descending-email-and-ascending-last-name
                           records)]

      ;; Then
      (is (= (sort-by :last-name records)
             sorted-records)))))
