(ns google.maps.location)

(defrecord Location [latitude longitude altitude])

(defn make-location [latitude longitude & [altitude]]
  (Location. latitude longitude altitude))

(defn location?
  "Returns true if arg is a location, otherwise false."
  [arg] (and (:latitude arg) (:longitude arg)))
