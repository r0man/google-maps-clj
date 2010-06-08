(ns google.maps.location)

(defrecord Location [latitude longitude])

(defn make-location [latitude longitude]
  (Location. latitude longitude))
