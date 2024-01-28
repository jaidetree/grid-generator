(ns grid.docs.letter-portrait
  (:require
    [grid.svg :as svg]
    [grid.presets :as presets]))

(defn doc
  []
  (let [props {:width  (svg/in->px 8.5)
               :height (svg/in->px 11)}]
    {:file "Letter Portrait 2024"
     :props props
     :defs [[presets/basegrid-pattern]
            [presets/subgrid-pattern]
            [presets/dots-pattern]]
     :children [[presets/subgrid-layer  props]
                [presets/basegrid-layer props]
                [presets/dots-layer     props]
                [presets/outline-layer  props]]}))

(defn -main
  [& _args]
  (list (doc)))
