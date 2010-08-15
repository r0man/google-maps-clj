(ns google.maps.geocoder
  (:import java.net.URL)
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.contrib.def :only (defvar)]
        [clojure.contrib.seq :only (includes?)]
        [clojure.contrib.duck-streams :only (read-lines)]
        [google.maps.location :only (format-location location?)]
        google.maps.util))

(defvar *api-url*
  "http://maps.google.com/maps/api/geocode/json"
  "The endpoint for Google's geocoding service.")

(defvar *default-options* {:sensor false}
  "The default options used by geocoding requests.")

(defn- request-options [query & options]
  (merge
   *default-options* (apply hash-map options)
   (if (location? query)
     {:latlng (format-location query)}
     {:address query})))

(defn geocode-url
  "Returns the url for geocoding the query."
  [query & options]
  (let [options (apply request-options query (flatten options))]
    (str *api-url* "?" (url-encode options))))

(defn geocode
  "Returns the answer from the geocoder for the query."
  [query & options]
  (let [response (read-json (apply str (read-lines (.openStream (URL. (apply geocode-url query options))))))]
    (condp = (:status response)
        "OK" (:results response)
        "ZERO_RESULTS" []
        :else (throw (Exception. (str response))))))

(defn address-components
  "Returns an array containing the separate address components."
  [result] (:address_components result))

(defn countries
  "Returns the country from the address components."
  [result] (filter #(includes? (:types %) "country") (address-components result)))

(defn formatted-address
  "Returns the formatted address of the result."
  [result] (:formatted_address result))

(defn geometry
  "Returns the geometry of the result."
  [result] (:geometry result))

(defn partial-match?
  "Returns true if the result is a partial match, otherwise false.

If true geocoder did not return an exact match for the original
request, though it did match part of the requested address. You may
wish to examine the original request for misspellings and/or an
incomplete address. Partial matches most often occur for street
addresses that do not exist within the locality you pass in the
request."
  [result] (not (nil? (:partial_match result))))

(defn types
  "Returns a set of one or more tags identifying the type of feature
returned in the result. For example, a geocode of \"Chicago\" returns
\"locality\" which indicates that \"Chicago\" is a city, and also
returns \"political\" which indicates it is a political entity."
  [result] (:types result))
