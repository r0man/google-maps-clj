(ns google.maps.test.projection
  (:use clojure.test google.maps.projection))

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
       0 128
       1 256
       2 512
       3 1024
       4 2048))
 
(deftest test-false-northing
  (are [zoom expected]
       (is (= (false-northing zoom) expected))
       0 -128
       1 -256
       2 -512
       3 -1024
       4 -2048))

(deftest test-latitude->y-coord
  (are [latitude zoom expected]
       (is (= (latitude->y-coord latitude zoom) expected))
       0 0 -127.5
       0 1 -255.5
       0 2 -511.5))

(deftest test-y-coord->latitude
  (are [y-coord zoom expected]
       (is (= (y-coord->latitude y-coord zoom) expected))
       0 0 -85.05112877980659
       0 1 -85.05112877980659
       0 2 -85.05112877980659))

(deftest test-longitude->x-coord
    (are [longitude zoom expected]
       (is (= (longitude->x-coord longitude zoom) expected))
       0 0 -127.5
       0 1 -255.5
       0 2 -511.5
       ;; 180 0 0
       ;; -180 0 0
       ))

(deftest test-x-coord->longitude
    (are [x-coord zoom expected]
         (is (= (x-coord->longitude x-coord zoom) expected))
         0 0 -180
         0 1 -180
         0 2 -180))

(deftest test-location->coords
  (let [location {:latitude 0 :longitude 0}]
    (is (= (location->coords location 0) {:x -127.5 :y -127.5}))
    (is (= (location->coords location 1) {:x -255.5 :y -255.5}))
    (is (= (location->coords location 2) {:x -511.5 :y -511.5}))))

