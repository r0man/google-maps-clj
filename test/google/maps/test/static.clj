(ns google.maps.test.static
  (:use clojure.test google.maps.static))

(def location {:latitude 52.523 :longitude 13.411})

(deftest test-static-map-bytes
  (is (= (first (static-map-bytes location)) -17))
  (is (= (first (static-map-bytes location :zoom 4 :width 400 :height 300)) -17)))

(deftest test-static-map-image
  (isa? (class (static-map-image location)) java.awt.Image)
  (isa? (class (static-map-image location :zoom 3 :width 400 :height 300)) java.awt.Image))
