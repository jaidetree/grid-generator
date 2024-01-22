(ns grid.docs.letter-portrait
  (:require
    [grid.api :as doc]
    [grid.presets :as presets]))

(def id (doc/create
          {:id :letter-portrait
           :title "Letter 2024 Portrait"
           :width (doc/in->px 8.5)
           :height (doc/in->px 11)}))

(doc/add-pattern id (presets/basegrid-pattern))
(doc/add-pattern id (presets/subgrid-pattern))
(doc/add-pattern id (presets/dots-pattern))

(doc/add-child id (presets/subgrid-layer id))
(doc/add-child id (presets/basegrid-layer id))
(doc/add-child id (presets/dots-layer id))
(doc/add-child id (presets/outline-layer id))


