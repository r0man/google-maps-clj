(ns google.maps.static
  (:import javax.swing.ImageIcon java.awt.image.BufferedImage)
  (:use google.maps.util)
  (:require [clojure.contrib.http.agent :as agent]))

(def *api-url* "http://maps.google.com/maps/api/staticmap")
(def *options* {:center {:latitude 0 :longitude 0} :width 300 :height 200 :maptype "roadmap" :sensor false :zoom 1})

(defn- parse-center [options]
  (let [{:keys [latitude longitude]} (:center options)]
    (str latitude "," longitude)))

(defn- parse-size [options]
  (str (or (:width options) (:width *options*)) "x" (or (:height options) (:height *options*))))

(defn- parse-options [options]
  (dissoc
   (assoc (merge *options* options)
     :center (parse-center options)
     :size (parse-size options))
   :width :height))

(defn static-map-url
  "Returns the url of the map centered at the location."
  [location & options]
  (let [options (apply hash-map options)]
    (str *api-url* "?" (url-encode (parse-options (assoc options :center location))))))

(defn static-map-bytes
  "Returns the bytes of the map centered at the location."
  [location & options] (agent/bytes (agent/http-agent (apply static-map-url location options))))

(defn static-map-image
  "Returns the image of the map centered at the location."
  [location & options]
  (let [icon (ImageIcon. (apply static-map-bytes location options))
        options (apply hash-map options)
        image (BufferedImage. (or (:width options) (:width *options*)) (or (:height options) (:height *options*)) BufferedImage/TYPE_3BYTE_BGR)]
    (. (.getGraphics image) drawImage (.getImage icon) 0 0 nil)
    image))
