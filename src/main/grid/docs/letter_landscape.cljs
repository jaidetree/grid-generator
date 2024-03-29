(ns grid.docs.letter-landscape
  (:require
    [grid.svg :as svg]
    [grid.presets :as presets]))

(defn doc
  []
  (let [props {:width  (svg/in->px 11)
               :height (svg/in->px 8.5)}]
    {:file "Letter Landscape 2024"
     :props props
     :defs []
     :children [[presets/subgrid-layer  props]
                [presets/basegrid-layer props]
                [presets/dots-layer     props]
                [presets/outline-layer  props]]}))

(defn -main
  [& _args]
  (list (doc)))
