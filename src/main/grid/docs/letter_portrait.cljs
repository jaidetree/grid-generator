(ns grid.docs.letter-portrait
  (:require
    [grid.svg :as svg]
    [grid.presets :as presets]))

(def width (svg/in->px 8.5))
(def height (svg/in->px 11))

(defn doc
  []
  (let [props {:width  width
               :height height}]
    {:file "Letter Portrait 2024"
     :props props
     :defs []
     :children [[presets/subgrid-layer  props]
                [presets/basegrid-layer props]
                [presets/dots-layer  props]
                [presets/outline-layer  props]]}))

(defn -main
  [& _args]
  (list (doc)))
