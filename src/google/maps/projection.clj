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

(defn latitude->y-coord
  "Returns the y coordinate of the latitude for the zoom level."
  [latitude zoom]
  (let [sinus (. Math sin (. Math toRadians latitude))
        pixel (* (/ (radius zoom) 2.0) (. Math log (/ (+ 1.0 sinus) (- 1.0 sinus))))]
    (+ 0.5 (* -1.0 (+ pixel (false-easting zoom))))))

(defn longitude->x-coord
  "Returns the x coordinate of the longitude for the zoom level."
  [longitude zoom]
  (+
   0.5
   (radius zoom)
   (* (radius zoom) (. Math toRadians longitude))
   (false-northing zoom)))
