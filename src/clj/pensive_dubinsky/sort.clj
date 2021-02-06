(ns pensive-dubinsky.sort
  (:require [pensive-dubinsky.util :as util]))

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

(defn sort-by-ascending-birth-date
  [records]
  (sort-by
    (comp util/conform-date-string :date-of-birth)
    records))

(defn sort-by-descending-last-name
  [records]
  (sort-by
    :last-name
    reverse-compare
    records))