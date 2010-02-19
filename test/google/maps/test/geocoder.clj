(ns google.maps.test.geocoder
  (:use clojure.test google.maps.geocoder))

(deftest test-geocode-url
  (are [query options expected]
    (is (= (apply geocode-url query options) expected))
    "Berlin" {} "http://maps.google.com/maps/geo?q=Berlin&key=ABQIAAAA7Wppa7cXmDsEPzdGLSrk_xTJQa0g3IQ9GZqIMmInSLzwtGDKaBRFHdz-TBNlgTndXeES0ZvJHx5Pbw&output=json&sensor=false"
    "13.41,52.52" {} "http://maps.google.com/maps/geo?q=13.41%2C52.52&key=ABQIAAAA7Wppa7cXmDsEPzdGLSrk_xTJQa0g3IQ9GZqIMmInSLzwtGDKaBRFHdz-TBNlgTndXeES0ZvJHx5Pbw&output=json&sensor=false"))

(deftest test-geocode
  (are [query options expected]
    (is (= (apply geocode query options) expected))
    "Berlin" {} {"Placemark"
                 [{"Point" {"coordinates" [13.4113999 52.5234051 0]},
                   "ExtendedData" {"LatLonBox" {"west" 12.8991623, "east" 13.9236375, "south" 52.3056216, "north" 52.7401142}},
                   "AddressDetails" {"Country" {"CountryNameCode" "DE", "CountryName" "Deutschland", "AdministrativeArea" {"SubAdministrativeArea" {"SubAdministrativeAreaName" "Berlin", "Locality" {"LocalityName" "Berlin"}}, "AdministrativeAreaName" "Berlin"}}, "Accuracy" 4},
                   "address" "Berlin, Germany", "id" "p1"}],
                 "Status" {"request" "geocode", "code" 200},
                 "name" "Berlin"}
    "13.41,52.52" {} {"Placemark"
                      [{"Point" {"coordinates" [64.33717 17.7657008 0]}, "ExtendedData" {"LatLonBox" {"west" 47.9455681, "east" 80.7287719, "south" 6.6168775, "north" 28.2620646}},
                        "AddressDetails" {"AddressLine" ["Arabian Sea"], "Accuracy" 0}, "address" "Arabian Sea", "id" "p1"} {"Point" {"coordinates" [34.508523 -8.783195 0]}, "ExtendedData" {"LatLonBox" {"west" -26.5869, "east" 60.5566, "south" -37.5341572, "north" 37.9615}}, "AddressDetails" {"AddressLine" ["Africa"], "Accuracy" 0}, "address" "Africa", "id" "p2"}],
                      "Status" {"request" "geocode", "code" 200},
                      "name" "13.41,52.52"}))

(deftest test-with-api-key
  (with-api-key "my-key"
    (is (= *api-key*) "my-key")))

