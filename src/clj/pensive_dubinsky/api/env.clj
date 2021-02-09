(ns pensive-dubinsky.api.env)

(def env (keyword (System/getProperty "env")))

(def dev? (= :dev env))
