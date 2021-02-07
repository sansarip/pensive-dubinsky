(ns pensive-dubinsky.api.interceptors
  (:require
    [pensive-dubinsky.api.routes :as routes]
    [reitit.coercion.spec :as coerce-spec]
    [reitit.pedestal :as pedestal]
    [reitit.http :as http]
    [muuntaja.core :as m]
    [reitit.http.interceptors.parameters :as parameters]
    [reitit.http.interceptors.muuntaja :as muuntaja]
    [reitit.http.interceptors.exception :as exception]
    [reitit.http.coercion :as coercion]
    [ring.middleware.reload :refer [wrap-reload]]
    [reitit.dev.pretty :as pretty]
    [muuntaja.core :as m]
    [reitit.ring :as ring]))

(def router
  (pedestal/routing-interceptor
    (http/router
      routes/routes
      {:exception pretty/exception
       :data      {:coercion     coerce-spec/coercion
                   :muuntaja     m/instance
                   :interceptors [;; query-params & form-params
                                  (parameters/parameters-interceptor)
                                  ;; content-negotiation
                                  (muuntaja/format-negotiate-interceptor)
                                  ;; encoding response body
                                  (muuntaja/format-response-interceptor)
                                  ;; exception handling
                                  (exception/exception-interceptor)
                                  ;; decoding request body
                                  (muuntaja/format-request-interceptor)
                                  ;; coercing response bodys
                                  (coercion/coerce-response-interceptor)
                                  ;; coercing request parameters
                                  (coercion/coerce-request-interceptor)]}})
    (ring/routes
      (ring/create-default-handler))))

(def on-enter (:enter router))

(def reloadable-router
  (assoc router
    :enter (wrap-reload #'on-enter)))

