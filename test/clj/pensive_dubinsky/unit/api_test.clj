(ns pensive-dubinsky.unit.api-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [pensive-dubinsky.api.spec :as spec]
            [pensive-dubinsky.gen :as gen*]
            [pensive-dubinsky.test-util :as tu]
            [pensive-dubinsky.api.server :as server]
            [pensive-dubinsky.parse :as parse]
            [clojure.test :refer :all]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as prop]
            [clojure.test]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]
            [cheshire.core :as json]
            [pensive-dubinsky.api.db :as db]
            [clojure.set :as cljset]
            [pensive-dubinsky.samples :as samples]))

(def service
  (:io.pedestal.http/service-fn
    (io.pedestal.http/create-servlet server/service-map)))

(defspec test-post-records tu/num-tests

  ;; Given
  (prop/for-all [data-lines (gen/vector (s/gen ::spec/data-line))
                 existing-records (gen/fmap vec (s/gen ::spec/records))]
    (with-redefs [db/db (atom existing-records)]
      (let [expected-records (map parse/line->map data-lines)]

        ;; When
        (let [{json-body :body
               status    :status} (response-for
                                    service
                                    :post "/records"
                                    :headers {"Content-Type" "application/json"}
                                    :body (json/generate-string data-lines))
              body (json/parse-string json-body true)]

          ;; Then
          (testing "Status = 201"
            (is (= 201 status)))
          (testing "Added records are returned"
            (is (= expected-records body)))
          (testing "Added are in db"
            (is (cljset/subset?
                  (set expected-records)
                  (set @db/db)))))))))

(defspec test-get-sorted-emails tu/num-tests

  ;; Given
  (prop/for-all [records (gen/fmap vec (s/gen ::spec/records))]
    (with-redefs
      [db/db (atom records)]

      ;; When
      (let [{json-body :body
             status    :status} (response-for
                                  service
                                  :get "/records/email")
            body (json/parse-string json-body true)]

        ;; Then
        (testing "Status = 200"
          (is (= 200 status)))
        (testing "Set of records = set of records in response body"
          (is (= (set records) (set body))))
        (testing "Sorted by ascending email"
          (is (= (sort-by
                   :email
                   records)
                 body)))))))

(defspec test-get-sorted-birth-dates tu/num-tests

  ;; Given
  (prop/for-all [records (gen/fmap
                           vec
                           (gen*/records-with-dates
                             samples/sorted-dates))]
    (with-redefs
      [db/db (atom records)]

      ;; When
      (let [{json-body :body
             status    :status} (response-for
                                  service
                                  :get "/records/birthdate")
            body (json/parse-string json-body true)]

        ;; Then
        (testing "Status = 200"
          (is (= 200 status)))
        (testing "Set of records = set of records in response body"
          (is (= (set records) (set body))))
        (testing "Sorted by ascending dates"
          (is (= samples/sorted-dates
                 (mapv :date-of-birth body))))))))

(defspec test-get-sorted-full-name tu/num-tests

  ;; Given
  (prop/for-all [records (gen/fmap vec (s/gen ::spec/records))]
    (with-redefs
      [db/db (atom records)]

      ;; When
      (let [{json-body :body
             status    :status} (response-for
                                  service
                                  :get "/records/name")
            body (json/parse-string json-body true)]

        ;; Then
        (testing "Status = 200"
          (is (= 200 status)))
        (testing "Set of records = set of records in response body"
          (is (= (set records) (set body))))
        (testing "Sorted by ascending full name"
          (is (= (sort-by
                   (juxt :first-name :last-name)
                   records)
                 body)))))))
