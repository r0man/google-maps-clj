(ns google.maps.projection)

(def *tile-size* 256)

(defn circumference
  "Returns the circumference for the zoom level."
  [#^int zoom] (* *tile-size* (bit-shift-left zoom 1)))

(defn radius
  "Returns the radius for the zoom level."
  [#^int zoom]
  (/ (circumference zoom) (* 2.0 Math/PI)))

(defn false-easting
  "Returns the false easting for the zoom level."
  [#^int zoom] (/ (circumference zoom) 2.0))

(defn false-northing
  "Returns the false northing for the zoom level."
  [#^int zoom] (/ (circumference zoom) -2.0))

