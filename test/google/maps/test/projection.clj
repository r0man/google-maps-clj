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
       3 2048))

(deftest test-radius
  (are [zoom expected]
       (is (= (radius zoom) expected))
       0 40.74366543152521
       1 81.48733086305042
       2 162.97466172610083
       3 325.94932345220167))

(deftest test-false-easting
  (are [zoom expected]
       (is (= (false-easting zoom) expected))
       0 128
       1 256
       2 512
       3 1024))
 
(deftest test-false-northing
  (are [zoom expected]
       (is (= (false-northing zoom) expected))
       0 -128
       1 -256
       2 -512
       3 -1024))

(deftest test-latitude->y-coord
  (are [latitude zoom expected]
       (is (= (latitude->y-coord latitude zoom) expected))
       0 0 -127.5
       0 1 -255.5
       0 2 -511.5
       89 0 -320.67992758647995
       89 1 -641.8598551729599
       89 2 -1284.2197103459198
       -89 0 65.67992758647992
       -89 1 130.85985517295984
       -89 2 261.2197103459197))

(deftest test-y-coord->latitude
    (are [y-coord zoom expected]
         (is (= (y-coord->latitude y-coord zoom) expected))
         0 0 -85.05112877980659
         0 1 -85.05112877980659
         0 2 -85.05112877980659))

(deftest test-longitude->x-coord
    (are [longitude zoom expected]
       (is (= (longitude->x-coord longitude zoom) expected))
       0 0 -86.75633456847478
       0 1 -174.01266913694957
       0 2 -348.52533827389914
       180 0 41.243665431525216
       180 1 81.98733086305043
       180 2 163.47466172610086
       -180 0 -214.75633456847478
       -180 1 -430.01266913694957
       -180 2 -860.5253382738991))

(deftest test-x-coord->longitude
    (are [x-coord zoom expected]
         (is (= (x-coord->longitude x-coord zoom) expected))
         0 0 -180
         0 1 -180
         0 2 -180))
