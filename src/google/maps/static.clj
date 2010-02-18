(ns google.maps.static
  (:import javax.swing.ImageIcon)
  (:use [clojure.contrib.str-utils2 :only (join)])
  (:require [clojure.contrib.http.agent :as agent]))

(def *base-url* "http://maps.google.com/maps/api/staticmap")
(def *options* {:center {:latitude 0 :longitude 0} :width 300 :height 200 :maptype "roadmap" :sensor false :zoom 1})

(defn options->params [options]
  (join "&" (map #(str (name (first %)) "=" (last %)) options)))

(defn parse-center [options]
  (let [{:keys [latitude longitude]} (:center options)]
    (str latitude "," longitude)))

(defn parse-size [options]
  (str (or (:width options) (:width *options*)) "x" (or (:height options) (:height *options*))))

(defn parse-options [options]
  (dissoc
   (assoc (merge *options* options)
     :center (parse-center options)
     :size (parse-size options))
   :width :height))

(defn static-map-url
  "Returns the url of the map centered at the location."
  [location & options]
  (let [options (apply hash-map options)]
    (str *base-url* "?" (options->params (parse-options (assoc options :center location))))))

(defn static-map-bytes
  "Returns the bytes of the map centered at the location."
  [location & options] (agent/bytes (agent/http-agent (apply static-map-url location options))))

(defn static-map-image
  "Returns the image of the map centered at the location."
  [location & options] (.getImage (ImageIcon. (apply static-map-bytes location options))))

