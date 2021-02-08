(ns pensive-dubinsky.unit.api-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [pensive-dubinsky.api.spec :as spec]
            [pensive-dubinsky.gen]
            [pensive-dubinsky.sort :as sort*]
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
            [pensive-dubinsky.api.db :as db]))

(def service
  (:io.pedestal.http/service-fn
    (io.pedestal.http/create-servlet server/service-map)))

(defspec test-post-records tu/num-tests

  ;; Given
  (prop/for-all [data-line (s/gen ::spec/data-line)]
    (let [expected-record (parse/line->map data-line)]
      (tu/with-cleanup
        expected-record

        ;; When
        #(let [{json-body :body
                status :status} (response-for
                                  service
                                  :post "/records"
                                  :headers {"Content-Type" "application/json"}
                                  :body (json/generate-string
                                          {:data-line data-line}))
               body (json/parse-string json-body true)]

           ;; Then
           (testing "Status = 201"
             (is (= 201 status)))
           (testing "Added record is in response body"
             (is (some (partial = expected-record) body)))
           (testing "Response = db"
             (is (= @db/db body))))))))

(defspec test-get-sorted-emails tu/num-tests

  ;; Given
  (prop/for-all [records (gen/fmap vec (s/gen ::spec/records))]

    ;; When
    (with-redefs
      [db/db (atom records)]
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






