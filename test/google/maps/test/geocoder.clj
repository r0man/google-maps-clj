(ns google.maps.test.geocoder
  (:use clojure.test google.maps.geocoder))

(def *berlin*
     {:name "Berlin", :Status {:code 200, :request "geocode"}, :Placemark [{:id "p1", :address "Berlin, Germany", :AddressDetails {:Accuracy 4, :Country {:AdministrativeArea {:AdministrativeAreaName "Berlin", :SubAdministrativeArea {:Locality {:LocalityName "Berlin"}, :SubAdministrativeAreaName "Berlin"}}, :CountryName "Deutschland", :CountryNameCode "DE"}}, :ExtendedData {:LatLonBox {:north 52.7401142, :south 52.3056216, :east 13.9236375, :west 12.8991623}}, :Point {:coordinates [13.4113999 52.5234051 0]}}]})

(deftest test-geocode-url
  (are [query options expected]
    (is (= (apply geocode-url query options) expected))
    "Berlin" {} "http://maps.google.com/maps/geo?q=Berlin&key=ABQIAAAA7Wppa7cXmDsEPzdGLSrk_xTJQa0g3IQ9GZqIMmInSLzwtGDKaBRFHdz-TBNlgTndXeES0ZvJHx5Pbw&output=json&sensor=false"
    "13.41,52.52" {} "http://maps.google.com/maps/geo?q=13.41%2C52.52&key=ABQIAAAA7Wppa7cXmDsEPzdGLSrk_xTJQa0g3IQ9GZqIMmInSLzwtGDKaBRFHdz-TBNlgTndXeES0ZvJHx5Pbw&output=json&sensor=false"))

(deftest test-geocode
  (are [query options expected]
    (is (= (apply geocode query options) expected))
    "Berlin" {} *berlin*))

(deftest test-with-api-key
  (with-api-key "my-key"
    (is (= *api-key*) "my-key")))

(deftest test-address
  (is (= (address *berlin*) "Berlin, Germany")))

(deftest test-addresses
  (is (= (addresses *berlin*) ["Berlin, Germany"])))

(deftest test-coordinates
  (is (= (coordinates *berlin*) [13.4113999 52.5234051 0])))

(deftest test-altitude
  (is (= (altitude *berlin*) 0.0)))

(deftest test-latitude
  (is (= (latitude *berlin*) 52.5234051)))

(deftest test-longitude
  (is (= (longitude *berlin*) 13.4113999)))

(deftest test-location
  (is (= (location *berlin*)
         {:altitude 0, :longitude 13.4113999, :latitude 52.5234051})))
