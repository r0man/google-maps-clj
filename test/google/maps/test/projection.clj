(ns google.maps.test.projection
  (:use clojure.test google.maps.projection))

(deftest test-circumference
  (are [zoom expected]
       (is (= (circumference zoom) expected))
       1 512
       2 1024
       3 1536))

(deftest test-radius
  (are [zoom expected]
       (is (= (radius zoom) expected))
       1 81.48733086305042
       2 162.97466172610083
       3 244.46199258915124))

(deftest test-false-easting
  (are [zoom expected]
       (is (= (false-easting zoom) expected))
       1 256.0
       2 512.0
       3 768.0))
 
(deftest test-false-northing
  (are [zoom expected]
       (is (= (false-northing zoom) expected))
       1 -256.0
       2 -512.0
       3 -768.0))

(deftest test->latitude->x-coord
    (are [latitude zoom expected]
       (is (= (latitude->y-coord latitude zoom) expected))
       0 0 0.0
       0 1 0.0
       0 2 0.0
       89 0 0.0
       89 1 386.35985517295984
       89 2 772.7197103459197
       -89 0 0.0
       -89 1 -386.35985517295984
       -89 2 -772.7197103459197))

(deftest test->longitude->x-coord
    (are [longitude zoom expected]
       (is (= (longitude->x-coord longitude zoom) expected))
       0 0 0.5
       0 1 -174.01266913694957
       0 2 -348.52533827389914
       180 0 0.5
       180 1 81.98733086305043
       180 2 163.47466172610086
       -180 0 0.5
       -180 1 -430.01266913694957
       -180 2 -860.5253382738991))
