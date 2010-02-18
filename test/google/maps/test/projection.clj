(ns google.maps.test.projection
  (:import Projection)
  (:use clojure.test google.maps.projection))

(def *lat-min* -85.05112877980659)
(def *lat-max* (* -1.0 *lat-min*))

(deftest test-tiles
  (are [zoom expected]
       (is (= (tiles zoom) expected))
       0 1
       1 2
       2 4
       3 8
       4 16))

(deftest test-circumference
  (are [zoom expected]
       (is (= (circumference zoom) expected))
       0 256
       1 512
       2 1024
       3 2048
       4 4096))

(deftest test-radius
  (are [zoom expected]
       (is (= (radius zoom) expected))
       0 40.74366543152521
       1 81.48733086305042
       2 162.97466172610083
       3 325.94932345220167
       4 651.8986469044033))

(deftest test-false-easting
  (are [zoom expected]
       (is (= (false-easting zoom) expected))
       0 -128
       1 -256
       2 -512
       3 -1024
       4 -2048))
 
(deftest test-false-northing
  (are [zoom expected]
       (is (= (false-northing zoom) expected))
       0 128
       1 256
       2 512
       3 1024
       4 2048))
 
(deftest test-latitude->y-coord
  (are [latitude zoom expected]
       (is (= (latitude->y-coord latitude zoom) expected))
       *lat-min* 0 256
       0 0 128
       *lat-max* 0 0
       *lat-min* 1 512
       0 1 256
       *lat-max* 1 0))

(deftest test-longitude->x-coord
    (are [longitude zoom expected]
       (is (= (longitude->x-coord longitude zoom) expected))
       -180 0 0
       0 0 128       
       180 0 256
       -180 1 0
       0 1 256      
       180 1 512))

(deftest test-y-coord->latitude
  (are [y-coord zoom expected]
       (is (= (y-coord->latitude y-coord zoom) expected))
       0 0 *lat-max*
       128 0 0
       256 0 *lat-min*
       0 1 *lat-max*
       128 1 66.51326044311185
       256 1 0))

(deftest test-x-coord->longitude
    (are [x-coord zoom expected]
         (is (= (x-coord->longitude x-coord zoom) expected))
         0 0 -180
         128 0 0
         256 0 -180
         0 1 -180
         128 1 -90
         256 1 0))

(deftest test-location->coords
  (let [location {:latitude 0 :longitude 0}]
    (is (= (location->coords location 0) {:x 128 :y 128}))
    (is (= (location->coords location 1) {:x 256 :y 256}))
    (is (= (location->coords location 2) {:x 512 :y 512}))))
