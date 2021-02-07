(ns pensive-dubinsky.api.server
  (:require
    [io.pedestal.http :as server]
    [reitit.pedestal :as pedestal]
    [pensive-dubinsky.api.interceptors :refer [reloadable-router router]]))

;; TODO: Create prod config
(def server*
  (-> {::server/type            :jetty
       ::server/port            3000
       ::server/join?           false
       ::server/routes          []
       ::server/allowed-origins {:creds true :allowed-origins (constantly true)}
       ::server/secure-headers  {:content-security-policy-settings {:object-src "'none'"}}}
      (server/default-interceptors)
      (pedestal/replace-last-interceptor reloadable-router)
      (server/dev-interceptors)
      (server/create-server)))

(defn start! []
  (server/start server*))

(defn stop! []
  (server/stop server*))

(defn -main []
  (start!))