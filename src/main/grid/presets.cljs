(ns grid.presets
  (:require
    [grid.svg :refer [calc-dividers]]
    [grid.color :as color]))

(def x-margin 10)
(def y-margin 10)

(defn grid
  [{:keys [width height
           x-margin y-margin
           x-start y-start
           spacing color]
    :or {x-start 16
         y-start 16}}]
  (let [rows (calc-dividers
               {:total height
                :margin y-margin
                :spacing spacing})
        cols (calc-dividers
               {:total width
                :margin x-margin
                :spacing spacing})]

    #_
    (println {:width width
              :height height
              :rows rows
              :cols cols})

    [:g.grid
     (for [row (range 0 rows)]
      [:line
       {:key row
        :x1 x-margin
        :y1 (+ y-start
               (* row spacing))
        :x2 (- width x-margin)
        :y2 (+ y-start
               (* row spacing))
        :fill "none"
        :stroke color}])

     (for [col (range 0 cols)]
       [:line
        {:key col
         :x1 (+ x-start
                (* col spacing))
         :y1 y-margin
         :x2 (+ x-start
                (* col spacing))
         :y2 (- height y-margin)
         :fill "none"
         :stroke color}])]))


(defn subgrid-layer
  [{:keys [width height x-margin y-margin]
    :or {x-margin x-margin
         y-margin y-margin}}]
  [grid
   {:width width
    :height height
    :x-margin y-margin
    :y-margin x-margin
    :spacing  8
    :color    (color/get :subgrid)}])

(defn basegrid-layer
  [{:keys [width height x-margin y-margin]
    :or {x-margin x-margin
         y-margin y-margin}}]
  [grid
   {:width width
    :height height
    :x-margin y-margin
    :y-margin x-margin
    :spacing  16
    :color    (color/get :basegrid)}])

(defn dots-layer
  [{:keys [width height
           x-margin y-margin
           spacing radius
           color]
    :or {x-margin x-margin
         y-margin y-margin
         spacing 16
         radius 3}}]
  (let [rows (calc-dividers
               {:total height
                :margin y-margin
                :spacing spacing})]
    [:g
     (for [row (range 0 rows)]
      [:line
       {:key row
        :x1 spacing
        :y1 (+ spacing
               (* row spacing))
        :x2 (- width x-margin)
        :y2 (+ spacing
               (* row spacing))
        :stroke-width radius
        :stroke-linecap "round"
        :stroke-dasharray "0 16"
        :stroke (or color (color/get :dots))}])]))

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


