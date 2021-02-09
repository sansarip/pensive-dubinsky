(ns pensive-dubinsky.api.routes
  (:require
    [pensive-dubinsky.api.handlers :as handlers]
    [pensive-dubinsky.api.spec :as spec]
    [reitit.swagger :as swagger]))

(def routes
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title       "Pensive Dubinsky"
                            :description "A dummy project for sorting records"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/records"
    [""
     {:post {:summary    "Add records"
             :handler    handlers/add-records
             :parameters {:body [::spec/data-line]}
             :responses  {201 {:body ::spec/records}}}}]
    ["/email"
     {:get {:summary   "Sort records by ascending email"
            :handler   handlers/sort-by-email
            :responses {200 {:body ::spec/records}}}}]
    ["/birthdate"
     {:get {:summary   "Sort records by ascending birth date"
            :handler   handlers/sort-by-birth-date
            :responses {200 {:body ::spec/records}}}}]
    ["/name"
     {:get {:summary   "Sort records by ascending first name, then last name"
            :handler   handlers/sort-by-full-name
            :responses {200 {:body ::spec/records}}}}]]])
