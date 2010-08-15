(ns google.maps.test.geocoder
  (:use clojure.test google.maps.geocoder google.maps.test))

(refer-private 'google.maps.geocoder)

(def *berlin*
     {:types ["locality" "political"]
      :formatted_address "Berlin, Germany"
      :address_components [{:long_name "Berlin", :short_name "Berlin", :types ["locality" "political"]}
                           {:long_name "Berlin", :short_name "Berlin", :types ["administrative_area_level_2" "political"]}
                           {:long_name "Berlin", :short_name "Berlin", :types ["administrative_area_level_1" "political"]}
                           {:long_name "Germany", :short_name "DE", :types ["country" "political"]}]
      :geometry {:location {:lat 52.5234051, :lng 13.4113999}, :location_type "APPROXIMATE"
                 :viewport {:southwest {:lat 52.3056216, :lng 12.8991623}, :northeast {:lat 52.7401142, :lng 13.9236375}},
                 :bounds {:southwest {:lat 52.338079, :lng 13.088304}, :northeast {:lat 52.675323, :lng 13.760909}}}})

(deftest test-address-components
  (is (= (address-components *berlin*)
         [{:long_name "Berlin", :short_name "Berlin", :types ["locality" "political"]}
          {:long_name "Berlin", :short_name "Berlin", :types ["administrative_area_level_2" "political"]}
          {:long_name "Berlin", :short_name "Berlin", :types ["administrative_area_level_1" "political"]}
          {:long_name "Germany", :short_name "DE", :types ["country" "political"]}])))

(deftest test-countries
  (is (= (countries *berlin*) [{:long_name "Germany", :short_name "DE", :types ["country" "political"]}])))

(deftest test-formatted-address
  (is (= (formatted-address *berlin*) "Berlin, Germany")))

(deftest test-geometry
  (is (= (geometry *berlin*)
         {:location {:lat 52.5234051, :lng 13.4113999}
          :location_type "APPROXIMATE"
          :viewport {:southwest {:lat 52.3056216, :lng 12.8991623}, :northeast {:lat 52.7401142, :lng 13.9236375}}
          :bounds {:southwest {:lat 52.338079, :lng 13.088304}, :northeast {:lat 52.675323, :lng 13.760909}}})))

(deftest test-partial-match?
  (is (not (partial-match? *berlin*))))

(deftest test-types
  (is (= (types *berlin*) ["locality" "political"])))

(deftest test-geocode-url
  (are [query options expected]
    (is (= (apply geocode-url query options) expected))
    "Berlin" {} "http://maps.google.com/maps/api/geocode/json?address=Berlin&sensor=false"
    "52.52,13.41" {} "http://maps.google.com/maps/api/geocode/json?address=52.52%2C13.41&sensor=false"
    {:latitude 52.52 :longitude 13.41} {} "http://maps.google.com/maps/api/geocode/json?latlng=52.52%2C13.41&sensor=false"))

(deftest test-geocode-with-language
  (is (= (formatted-address (first (geocode "Berlin" :language "en")))
         "Berlin, Germany"))
  (is (= (formatted-address (first (geocode "Berlin" :language "de")))
         "Berlin, Deutschland"))
  (is (= (formatted-address (first (geocode "Berlin" :language "fr")))
         "Berlin, Allemagne")))

(deftest test-geocode-with-region
  (is (= (formatted-address (first (geocode "Santiago" :region "es")))
         "Santiago del Teide, Spain"))
  (is (= (formatted-address (first (geocode "Santiago" :region "us")))
         "Quilicura, Santiago, Chile")))

(deftest test-geocode-unknown
  (is (empty? (geocode "A secret unknown location"))))

(deftest test-request-options
  (is (= (request-options "Berlin")
         (assoc *default-options* :address "Berlin")))
  (is (= (request-options {:latitude 52.52, :longitude 13.41} :language "de" :region "de")
         (assoc *default-options* :language "de" :region "de" :latlng "52.52,13.41")))
  (is (= (request-options {:latitude 52.52, :longitude 13.41} :language "de" :region "de")
         (assoc *default-options* :language "de" :region "de" :latlng "52.52,13.41"))))
