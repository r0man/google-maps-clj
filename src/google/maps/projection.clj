(ns google.maps.projection)

(def *tile-size* 256)

(defn tiles
  "Returns the number of tiles for the zoom level."
  [zoom] (. Math pow 2 zoom))

(defn circumference
  "Returns the circumference for the zoom level."
  [zoom] (* *tile-size* (tiles zoom)))

(defn radius
  "Returns the radius for the zoom level."
  [zoom]
  (/ (circumference zoom) (* 2.0 Math/PI)))

(defn false-easting
  "Returns the false easting for the zoom level."
  [zoom] (/ (circumference zoom) 2.0))

(defn false-northing
  "Returns the false northing for the zoom level."
  [zoom] (/ (circumference zoom) -2.0))

(defn latitude->y-coord
  "Returns the y coordinate of the latitude for the zoom level."
  [latitude zoom]
  (let [sinus (. Math sin (. Math toRadians latitude))
        pixel (* (/ (radius zoom) 2.0) (. Math log (/ (+ 1.0 sinus) (- 1.0 sinus))))]
    (+ 0.5 (* -1.0 (+ pixel (false-easting zoom))))))

(defn longitude->x-coord
  "Returns the x coordinate of the longitude for the zoom level."
  [longitude zoom]
  (+ 0.5
     (radius zoom)
     (* (radius zoom) (. Math toRadians longitude))
     (false-northing zoom)))

(defn x-coord->longitude [x-coord zoom]
  "Returns the longitude of the x coordinate for the zoom level."
  (let [degree (. Math toDegrees (/ (+ x-coord (false-northing zoom)) (radius zoom)))
        rotation (. Math floor (/ (+ degree 180.0) 360.0))]
    (- degree (* rotation 360.0))))

(defn y-coord->latitude [y-coord zoom]
  "Returns the latitude of the y coordinate for the zoom level."
  (let [value (. Math exp  (* -1.0 (/ (+ y-coord (false-easting zoom)) (radius zoom))))]
    (* -1.0 (. Math toDegrees (- (/ Math/PI 2.0) (* 2.0 (. Math atan value)))))))


