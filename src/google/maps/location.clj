(ns google.maps.location)

(defstruct location :latitude :longitude)

(defn make-location [latitude longitude]
  (struct location latitude longitude))
