(ns google.maps.projection)

(def *tile-size* 256)

(defn circumference [zoom]
  (* *tile-size* (bit-shift-left zoom 1)))

;(circumference 3)
