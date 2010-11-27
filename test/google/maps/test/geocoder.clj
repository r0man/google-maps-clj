(ns google.maps.test.geocoder
  (:use clojure.test google.maps.geocoder google.maps.test))

(refer-private 'google.maps.geocoder)

(def *berlin*
  {:types ["street_address"],
   :formatted_address "Senefelderstraße 24, 10437 Berlin, Germany",
   :address_components [{:long_name "24", :short_name "24", :types ["street_number"]}
                        {:long_name "Senefelderstraße", :short_name "Senefelderstraße", :types ["route"]}
                        {:long_name "Berlin", :short_name "Berlin", :types ["sublocality" "political"]}
                        {:long_name "Berlin", :short_name "Berlin", :types ["locality" "political"]}
                        {:long_name "Berlin", :short_name "Berlin", :types ["administrative_area_level_2" "political"]}
                        {:long_name "Berlin", :short_name "Berlin", :types ["administrative_area_level_1" "political"]}
                        {:long_name "Germany", :short_name "DE", :types ["country" "political"]}
                        {:long_name "10437", :short_name "10437", :types ["postal_code"]}],
   :geometry {:location {:lat 52.54258, :lng 13.42299}, :location_type "ROOFTOP", :viewport {:southwest {:lat 52.5394324, :lng 13.4198424}, :northeast {:lat 52.5457276, :lng 13.4261376}}}})

(deftest test-address-components
  (is (= (:address_components *berlin*) (address-components *berlin*))))

(deftest test-country
  (is (= {:long_name "Germany", :short_name "DE", :types ["country" "political"]} (country *berlin*))))

(deftest test-postal-code
  (is (= "10437" (postal-code *berlin*))))

(deftest test-street-number
  (is (= "24" (street-number *berlin*))))

(deftest test-route
  (is (= "Senefelderstraße" (route *berlin*))))

(deftest test-sub-locality
  (is (= "Berlin" (sub-locality *berlin*))))

(deftest test-locality
  (is (= "Berlin" (locality *berlin*))))

(deftest test-administrative-area-level-1
  (is (= "Berlin" (administrative-area-level-1 *berlin*))))

(deftest test-administrative-area-level-2
  (is (= "Berlin" (administrative-area-level-2 *berlin*))))

(deftest test-formatted-address
  (is (= "Senefelderstraße 24, 10437 Berlin, Germany" (formatted-address *berlin*))))

(deftest test-geometry
  (is (= (:geometry *berlin*) (geometry *berlin*))))

(deftest test-location
  (is (= {:latitude 52.54258, :longitude 13.42299} (location *berlin*))))

(deftest test-partial-match?
  (is (not (partial-match? *berlin*))))

(deftest test-types
  (is (= ["street_address"] (types *berlin*))))

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
