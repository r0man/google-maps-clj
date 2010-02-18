(ns google.maps.test.projection
  (:use clojure.test google.maps.projection))

(def *lat-max* 85.05112877980659)
(def *lat-min* -85.05112877980659)

(def *lon-max* 180)
(def *lon-min* -180)

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
    *lon-min* 0 0
    0 0 128       
    *lon-max* 0 256
    *lon-min* 1 0
    0 1 256      
    *lon-max* 1 512))

(deftest test-location->coords
  (let [location {:latitude 0 :longitude 0}]
    (are [zoom x y]
      (is (= (location->coords location zoom) {:x x :y y}))
      0 128 128
      1 256 256
      2 512 512)))

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
    0 0 *lon-min*
    128 0 0
    256 0 *lon-min*
    0 1 *lon-min*
    128 1 -90
    256 1 0))

(deftest test-coords->location
  (let [coords {:x 0 :y 0}]
    (are [zoom latitude longitude]
      (is (= (coords->location coords zoom) {:latitude latitude :longitude longitude}))
      0 *lat-max* *lon-min*
      1 *lat-max* *lon-min*
      2 *lat-max* *lon-min*))
  (let [coords {:x 128 :y 128}]
    (are [zoom latitude longitude]
      (is (= (coords->location coords zoom) {:latitude latitude :longitude longitude}))
      0 0 0
      1 66.51326044311185 -90
      2 79.17133464081945 -135))  
  (let [coords {:x 256 :y 256}]
    (are [zoom latitude longitude]
      (is (= (coords->location coords zoom) {:latitude latitude :longitude longitude}))
      0 -85.05112877980659 -180
      1 0 0
      2 66.51326044311185 -90)))

(deftest test-latitude-delta
  (are [y1 y2 zoom expected]
    (is (= (latitude-delta y1 y2 zoom) expected))
    0 0 0 0
    0 0 1 0
    0 0 2 0
    0 256 0 -170.10225755961318
    0 256 1 *lat-min*
    0 256 2 -18.537868336694743))

(deftest test-longitude-delta
  (are [x1 x2 zoom expected]
    (is (= (longitude-delta x1 x2 zoom) expected))
    0 0 0 0
    0 0 1 0
    0 0 2 0
    0 256 0 0
    0 256 1 180
    0 256 2 90))

(deftest test-x-coord-delta
  (are [lon1 lon2 zoom expected]
    (is (= (x-coord-delta lon1 lon2 zoom) expected))
    0 0 0 0
    0 0 1 0
    0 0 2 0
    *lon-min* *lon-max* 0 256
    *lon-min* *lon-max* 1 512
    *lon-min* *lon-max* 2 1024))

(deftest test-y-coord-delta
  (are [lat1 lat2 zoom expected]
    (is (= (y-coord-delta lat1 lat2 zoom) expected))
    0 0 0 0
    0 0 1 0
    0 0 2 0
    *lat-max* *lat-min* 0 256
    *lat-max* *lat-min* 1 512
    *lat-max* *lat-min* 2 1024))
