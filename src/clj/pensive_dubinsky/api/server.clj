(ns pensive-dubinsky.api.server
  (:require
    [io.pedestal.http :as server]
    [reitit.pedestal :as pedestal]
    [pensive-dubinsky.api.env :as env]
    [pensive-dubinsky.api.interceptors :refer [reloadable-router router]]))

(defonce service-map
         (-> {::server/type            :jetty
              ::server/host            "0.0.0.0"
              ::server/port            3000
              ::server/join?           false
              ::server/routes          []
              ;; FIXME: Restrict origins
              ::server/allowed-origins {:creds true :allowed-origins (constantly true)}
              ::server/secure-headers  {:content-security-policy-settings {:object-src "'none'"}}}
             server/default-interceptors
             (pedestal/replace-last-interceptor
               ({:dev reloadable-router} env/env router))
             (cond-> env/dev? server/dev-interceptors)))

(def server* (server/create-server service-map))

(defn start! []
  (server/start server*))

(defn stop! []
  (server/stop server*))

(defn -main []
  (start!))
