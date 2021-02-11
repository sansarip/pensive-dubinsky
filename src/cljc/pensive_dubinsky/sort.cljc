(ns pensive-dubinsky.sort
  (:require [pensive-dubinsky.util :as util]
            [clojure.string :as string]))

(defn with-lower-case [kw]
  (comp string/lower-case kw))

(defn reverse-compare [a b]
  (compare b a))

(defn sort-by-descending-email-and-ascending-last-name
  ([records]
   (sort
     sort-by-descending-email-and-ascending-last-name
     records))
  ([{email-a :email last-name-a :last-name}
    {email-b :email last-name-b :last-name}]
   (let [c (reverse-compare email-a email-b)]
     (if (not= c 0)
       c
       (compare last-name-a last-name-b)))))

(defn sort-by-ascending-email
  [records]
  (sort-by
    :email
    records))

(defn sort-by-ascending-birth-date
  [records]
  (sort-by
    (comp util/date-string->local-date :date-of-birth)
    records))

(defn sort-by-descending-last-name
  [records]
  (sort-by
    :last-name
    reverse-compare
    records))

(defn sort-by-ascending-full-name
  "Sort by full name, first name then last name"
  [records]
  (sort-by
    (juxt
      (with-lower-case :first-name)
      (with-lower-case :last-name))
    records))

(defn sort-record
  [records
   column
   direction]
  (sort-by
    (with-lower-case (keyword column))
    ({"ascending"  compare
      "descending" reverse-compare}
     direction)
    records))