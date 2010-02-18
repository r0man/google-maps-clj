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


