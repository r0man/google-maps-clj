(ns google.maps.geocoder
  (:require [clj-http.client :as client])
  (:use [clojure.data.json :only (read-json)]
        [google.maps.location :only (format-location location?)]))

(def ^:dynamic *api-url* "http://maps.google.com/maps/api/geocode/json")
(def ^:dynamic *options* {:sensor false :language "en"})

(defn request [options]
  (-> (client/request
       {:method :get
        :url *api-url*
        :query-params (merge *options* options)})
      :body read-json :results))

(defn geocode
  "Geocode the location."
  [address & {:as options}]
  (request (assoc options :address address)))

(defn reverse-geocode
  "Reverse geocode the location."
  [location & {:as options}]
  (request (assoc options :latlng (format-location location))))

(defn address-components
  "Returns an array containing the separate address components."
  [result] (:address_components result))

(defn- find-address-component [result component]
  (first (filter #(contains? (set (:types %)) component) (address-components result))))

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
  [result] (find-address-component result "administrative_area_level_1"))

(defn administrative-area-level-2
  "Returns the administrative area level 2 from address components."
  [result] (find-address-component result "administrative_area_level_2"))

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
