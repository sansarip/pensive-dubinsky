(ns pensive-dubinsky.unit.sort-test
  (:require
    [pensive-dubinsky.api.spec :as spec]
    [pensive-dubinsky.gen]
    [pensive-dubinsky.sort :as sort*]
    [pensive-dubinsky.test-util :as tu]
    [clojure.test :refer :all]
    [clojure.test.check.clojure-test :refer [defspec]]
    [clojure.test.check.properties :as prop]
    [clojure.test]
    [clojure.spec.alpha :as s]
    [clojure.test.check.generators :as gen]))

(defspec test-set-of-sorted-records-equal-to-set-of-input-records tu/num-tests
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

(defspec test-idempotent-sort-fns tu/num-tests

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

(defspec test-sort-descending-email-with-constant-last-name tu/num-tests

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


(defspec test-sort-ascending-last-name-with-constant-email tu/num-tests

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

(defspec test-sort-descending-birth-date tu/num-tests

  ;;Given
  (let [sorted-dates ["07/24/1901"
                      "01/21/1903"
                      "05/16/1937"
                      "02/22/1947"
                      "10/05/1968"
                      "02/28/1970"
                      "06/03/1970"
                      "03/04/1977"
                      "06/21/1999"
                      "01/01/2008"]]
    (prop/for-all [records (gen/fmap
                             (fn [[shuffled-dates records]]
                               (map
                                 (fn [[date record]]
                                   (assoc record :date-of-birth date))
                                 (zipmap shuffled-dates records)))
                             (gen/tuple
                               (gen/shuffle sorted-dates)
                               (gen/vector (s/gen ::spec/record)
                                           (count sorted-dates))))]

      ;; When
      (let [sorted-record-dates (mapv
                                  :date-of-birth
                                  (sort*/sort-by-ascending-birth-date
                                    records))]

        ;; Then
        (is (= sorted-dates sorted-record-dates))))))
