(ns google.maps.location)

(defrecord Location [latitude longitude altitude])

(defn make-location [latitude longitude & [altitude]]
  (Location. latitude longitude altitude))
