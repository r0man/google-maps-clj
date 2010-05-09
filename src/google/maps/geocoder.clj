(ns google.maps.geocoder
  (:use [clojure.contrib.json.read :only (read-json)]
        google.maps.util)
  (:require [clojure.contrib.http.agent :as agent]))

(def *api-url* "http://maps.google.com/maps/geo")
(def *api-key* "ABQIAAAA7Wppa7cXmDsEPzdGLSrk_xTJQa0g3IQ9GZqIMmInSLzwtGDKaBRFHdz-TBNlgTndXeES0ZvJHx5Pbw")

(def *options* { :output "json" :sensor false })

(defn- parse-options [options]
  (merge *options* options))

(defn coordinates
  "Extracts the coordinates from the geocoder response."
  [response]
  (get (get (first (get response "Placemark")) "Point") "coordinates"))

(defn latitude
  "Extracts the latitude from the geocoder response."
  [response]
  (nth (coordinates response) 0))

(defn longitude
  "Extracts the longitude from the geocoder response."
  [response]
  (nth (coordinates response) 1))

(defn altitude
  "Extracts the altitude from the geocoder response."
  [response]
  (nth (coordinates response) 2))

(defn location
  "Extracts the location from the geocoder response."
  [response]
  (zipmap [:latitude :longitude :altitude] (coordinates response)))

(defn geocode-url
  "Returns the url for geocoding the query."
  [query & options]
  (let [options (apply hash-map options)]
    (str *api-url* "?" (url-encode (parse-options (assoc options :q query :key *api-key*))))))

(defn geocode
  "Returns the answer from the geocoder for the query."
  [query & options]
  (read-json (agent/string (agent/http-agent (apply geocode-url query options)))))

(defmacro with-api-key
  "Binds the key to *api-key* and evaluates body."
  [key & body]
  `(binding [*api-key* key]
     ~@body))

