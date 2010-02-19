(ns google.maps.test.static
  (:use clojure.test google.maps.static))

(def *location* {:latitude 52.523 :longitude 13.411})
  
(deftest test-static-map-url
  (is (= (static-map-url *location*)
         "http://maps.google.com/maps/api/staticmap?size=300x200&center=52.523%2C13.411&maptype=roadmap&sensor=false&zoom=1")))

(deftest test-static-map-bytes
  (is (= (first (static-map-bytes *location*)) -119)))

(deftest test-static-map-image
  (isa? (class (static-map-image *location*)) java.awt.Image))


