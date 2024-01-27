(ns grid.presets
  (:require
    [grid.svg :refer [grid-layer grid-pattern grid-layer]]
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
  [{:keys [width height]}]
  (grid-layer
    {:fill-id "basegrid"
     :width   width
     :height  height
     :padding 10}))

(defn subgrid-layer
  [{:keys [width height]}]
  (grid-layer
    {:fill-id "subgrid"
     :width   width
     :height  height
     :padding 10}))

(defn dots-layer
  [{:keys [width height]}]
  [:rect
    {:x         "0px"
     :y         "0px"
     :width     (- width (* 2 8.25))
     :height    (- height (* 2 8))
     :transform "translate(8.25, 8)"
     :fill      "url(#dots)"}])

(defn outline-layer
  [{:keys [width height padding] :or {padding 10}}]
  [:rect
   {:x (str padding "px")
    :y (str padding "px")
    :width (- width (* 2 padding))
    :height (- height (* 2 padding))
    :stroke (color/get :outline)
    :strokeWidth "1px"
    :fill "none"}])

(defn title-layer
  [{:keys [width]} title]
  [:g
   [:text
     {:x (/ width 2)
       :y 48
       :font-family "OperatorMono Nerd Font"
       :font-size "30px"
       :fill (color/get :pink)
       :text-anchor "middle"}
     (str title)]
   [:line
    {:x1 10
     :y1 (+ 48 16)
     :x2 (- width 10)
     :y2 (+ 48 16)
     :stroke (color/get :bone)
     :strokeWidth "1px"}]])


