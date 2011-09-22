(ns google.maps.projection
  (:use google.maps.location))

(def tile-size 256)

(defstruct coords :x :y)

(defn tiles
  "Returns the number of tiles for the zoom level."
  [^Integer zoom]
  (int (. Math pow 2 zoom)))

(defn circumference
  "Returns the circumference for the zoom level."
  [^Integer zoom] (* tile-size (tiles zoom)))

(defn radius
  "Returns the radius for the zoom level."
  [^Integer zoom]
  (/ (circumference zoom) (* 2.0 Math/PI)))

(defn false-easting
  "Returns the false easting for the zoom level."
  [^Integer zoom] (int (/ (circumference zoom) -2.0)))

(defn false-northing
  "Returns the false northing for the zoom level."
  [^Integer zoom] (int (/ (circumference zoom) 2.0)))

(defn latitude->y-coord
  "Returns the y coordinate of the latitude for the zoom level."
  [^Double latitude ^Integer zoom]
  (let [sinus (. Math sin (. Math toRadians latitude))
        pixel (* (/ (radius zoom) 2.0) (. Math log (/ (+ 1.0 sinus) (- 1.0 sinus))))]
    (int (+ 0.5 (* -1.0 (+ pixel (false-easting zoom)))))))

(defn longitude->x-coord
  "Returns the x coordinate of the longitude for the zoom level."
  [^Double longitude ^Integer zoom]
  (int (+ 0.5 (* (radius zoom) (. Math toRadians longitude)) (false-northing zoom))))

(defn location->coords
  "Returns the coordinates of the location for the zoom level."
  [location ^Integer zoom]
  {:x (longitude->x-coord (:longitude location) zoom)
   :y (latitude->y-coord (:latitude location) zoom)})

(defn x-coord->longitude [^Integer x-coord ^Integer zoom]
  "Returns the longitude of the x coordinate for the zoom level."
  (let [degree (. Math toDegrees (/ (+ x-coord (false-northing zoom)) (radius zoom)))
        rotation (. Math floor (/ (+ degree 180.0) 360.0))]
    (- degree (* rotation 360.0))))

(defn y-coord->latitude [^Integer y-coord ^Integer zoom]
  "Returns the latitude of the y coordinate for the zoom level."
  (let [value (. Math exp (* -1.0 (/ (+ y-coord (false-easting zoom)) (radius zoom))))]
    (* -1.0 (. Math toDegrees (- (/ Math/PI 2.0) (* 2.0 (. Math atan value)))))))

(defn coords->location
  "Returns the location of the coordinates for the zoom level."
  [coords ^Integer zoom]
  (make-location
   (y-coord->latitude (:y coords) zoom)
   (x-coord->longitude (:x coords) zoom)))

(defn latitude-delta
  "Returns the latitude delta between the y coordinates."
  [^Integer y1 ^Integer y2 ^Integer zoom]
  (- (y-coord->latitude y2 zoom) (y-coord->latitude y1 zoom)))

(defn longitude-delta
  "Returns the longitude delta between the x coordinates."
  [^Integer x1 ^Integer x2 ^Integer zoom]
  (- (x-coord->longitude x2 zoom) (x-coord->longitude x1 zoom)))

(defn x-coord-delta
  "Returns the x coordinate delta between the longitudes."
  [^Double longitude1 ^Double longitude-2 ^Integer zoom]
  (- (longitude->x-coord longitude-2 zoom) (longitude->x-coord longitude1 zoom)))

(defn y-coord-delta
  "Returns the y coordinate delta between the latitudes."
  [^Double latitude-1 ^Double latitude2 ^Integer zoom]
  (- (latitude->y-coord latitude2 zoom) (latitude->y-coord latitude-1 zoom)))

(defn coord-delta [location-1 location-2 ^Integer zoom]
  (struct
   coords
   (x-coord-delta (:longitude location-1) (:longitude location-2) zoom)
   (y-coord-delta (:latitude location-1) (:latitude location-2) zoom)))
