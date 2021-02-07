(ns pensive-dubinsky.api.routes
  (:require
    [pensive-dubinsky.api.handlers :as handlers]
    [pensive-dubinsky.api.spec :as spec]))

(def routes
  ["/records"
   [""
    {:post {:handler    handlers/add-record
            :parameters {:body {:data-line ::spec/data-line}}
            :responses  {201 {:body ::spec/records}}}}]
   ["/email"
    {:get {:handler   handlers/sort-by-email
           :responses {200 {:body ::spec/records}}}}]
   ["/birthdate"
    {:get {:handler   handlers/sort-by-birth-date
           :responses {200 {:body ::spec/records}}}}]
   ["/name"
    {:get {:handler   handlers/sort-by-full-name
           :responses {200 {:body ::spec/records}}}}]])
