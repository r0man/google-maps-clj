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

(defn- find-address-component [result component]
  (first (filter #(includes? (:types %) component) (address-components result))))

(defn country
  "Returns the country from the address components."
  [result] (find-address-component result "country"))

(defn postal-code
  "Returns the postal code from address components."
  [result] (:long_name (find-address-component result "postal_code")))

(defn street-number
  "Returns the street number from address components."
  [result] (:long_name (find-address-component result "street_number")))

(defn route
  "Returns the route from address components."
  [result] (:long_name (find-address-component result "route")))

(defn sub-locality
  "Returns the sub locality from address components."
  [result] (:long_name (find-address-component result "sublocality")))

(defn locality
  "Returns the locality from address components."
  [result] (:long_name (find-address-component result "locality")))

(defn administrative-area-level-1
  "Returns the administrative area level 1 from address components."
  [result] (:long_name (find-address-component result "administrative_area_level_1")))

(defn administrative-area-level-2
  "Returns the administrative area level 2 from address components."
  [result] (:long_name (find-address-component result "administrative_area_level_2")))


(defn formatted-address
  "Returns the formatted address of the result."
  [result] (:formatted_address result))

(defn geometry
  "Returns the geometry of the result."
  [result] (:geometry result))

(defn location
  "Returns the location of the result geometry."
  [result]
  (if-let [location (:location (:geometry result))]
    {:latitude (:lat location) :longitude (:lng location)}))

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
