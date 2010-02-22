(ns google.maps.test.static
  (:use clojure.test google.maps.static))

(def *location* {:latitude 52.523 :longitude 13.411})
  
(deftest test-static-map-url
  (is (= (static-map-url *location*)
         "http://maps.google.com/maps/api/staticmap?size=300x200&center=52.523%2C13.411&maptype=roadmap&sensor=false&zoom=1"))
  (is (= (static-map-url *location* :zoom 2 :width 400 :height 300)
         "http://maps.google.com/maps/api/staticmap?size=400x300&center=52.523%2C13.411&maptype=roadmap&sensor=false&zoom=2")))

(deftest test-static-map-bytes
  (is (= (first (static-map-bytes *location*)) -119))
  (is (= (first (static-map-bytes *location* :zoom 4 :width 400 :height 300)) -119)))

(deftest test-static-map-image
  (isa? (class (static-map-image *location*)) java.awt.Image)
  (isa? (class (static-map-image *location* :zoom 3 :width 400 :height 300)) java.awt.Image))
