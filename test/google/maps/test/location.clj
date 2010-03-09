(ns google.maps.test.location
  (:use clojure.test google.maps.location))

(deftest test-make-location
  (let [location (make-location 1 2)]
    (is (= (:latitude location) 1))
    (is (= (:longitude location) 2))))
