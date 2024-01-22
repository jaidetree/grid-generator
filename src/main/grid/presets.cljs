(ns grid.presets
  (:require
    [grid.api :refer [get-doc grid-layer grid-pattern grid-layer]]))

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
     :fill "#5C414F"}]])

(defn basegrid-pattern
  []
  (grid-pattern
    {:id "basegrid"
     :size "16px"
     :color "#334544"}))

(defn subgrid-pattern
  []
  (grid-pattern
    {:id "subgrid"
     :size "8px"
     :color "#24253a"}))

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
      :stroke "#453941"
      :strokeWidth "1px"
      :fill "none"}]))

