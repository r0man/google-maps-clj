(ns google.maps.geocoder
  (:use [clojure.contrib.json.read :only (read-json)]
        google.maps.util)
  (:require [clojure.contrib.http.agent :as agent]))

(def *api-url* "http://maps.google.com/maps/geo")
(def *api-key* "ABQIAAAA7Wppa7cXmDsEPzdGLSrk_xTJQa0g3IQ9GZqIMmInSLzwtGDKaBRFHdz-TBNlgTndXeES0ZvJHx5Pbw")

(def *options* { :output "json" :sensor false })

(defn- parse-options [options]
  (merge *options* options))

(defn geocode-url
  "Returns the url for geocoding the query."
  [query & options]
  (let [options (apply hash-map options)]
    (str *api-url* "?" (url-encode (parse-options (assoc options :q query :key *api-key*))))))

(defn geocode
  "Returns the geocoder result the query."
  [query & options]
  (read-json (agent/string (agent/http-agent (apply geocode-url query options)))))

(defmacro with-api-key
  "Binds the key to *api-key* and evaluates body."
  [key & body]
  `(binding [*api-key* key]
     ~@body))
