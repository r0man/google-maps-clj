(ns google.maps.test.projection
  (:use clojure.test google.maps.projection))

(deftest test-circumference
  (are [zoom expected]
       (is (= (circumference zoom) expected))
       1 512
       2 1024
       3 1536
       4 2048
       5 2560
       6 3072
       7 3584))

