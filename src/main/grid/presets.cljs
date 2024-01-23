(ns grid.presets
  (:require
    [grid.api :refer [get-doc grid-layer grid-pattern grid-layer]]
    [grid.color :as color]))

(defn dots-pattern
  []
  [:pattern#dots
   {:patternUnits "userSpaceOnUse"
    :width "16px"
    :height "16px"}
   [:circle
    {:cx "8px"
     :cy "8px"
     :r "1.5px"
     #_#_:fill "#5C414F"
     :fill (color/get :dots)}]])

(defn basegrid-pattern
  []
  (grid-pattern
    {:id "basegrid"
     :size "16px"
     :color (color/get :basegrid)}))

(defn subgrid-pattern
  []
  (grid-pattern
    {:id "subgrid"
     :size "8px"
     :color (color/get :subgrid)}))

(defn basegrid-layer
  [id]
  (grid-layer
    {:fill-id "basegrid"
     :id      id
     :padding 10}))

(defn subgrid-layer
  [id]
  (grid-layer
    {:fill-id "subgrid"
     :id      id
     :padding 10}))

(defn dots-layer
  [id]
  (let [doc (get-doc id)]
    [:rect
     {:x         "0px"
      :y         "0px"
      :width     (- (:width doc) (* 2 8.25))
      :height    (- (:height doc) (* 2 8))
      :transform "translate(8.25, 8)"
      :fill      "url(#dots)"}]))

(defn outline-layer
  [id]
  (let [doc (get-doc id)
        padding 10]
    [:rect
     {:x (str padding "px")
      :y (str padding "px")
      :width (- (:width doc) (* 2 padding))
      :height (- (:height doc) (* 2 padding))
      :stroke (color/get :outline)
      :strokeWidth "1px"
      :fill "none"}]))

