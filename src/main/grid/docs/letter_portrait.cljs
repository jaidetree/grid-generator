(ns grid.docs.letter-portrait
  (:require
    [grid.svg :as svg]
    [grid.presets :as presets]
    [grid.color :as color]))

(def width (svg/in->px 8.5))
(def height (svg/in->px 11))

(def x-margin 10)
(def y-margin 10)
(def rect-height (-> height
                     (- (* 2 y-margin))))

(comment
  width
  (-> width
      (- (* 2 x-margin))
      (/ 16)
      (js/Math.floor))
  (-> width
      (- (* 2 x-margin))
      (/ 8)
      (js/Math.floor)))


(defn grid
  [{:keys [width height
           x-margin y-margin
           x-start y-start
           spacing color]
    :or {x-start 0
         y-start 0}}]
  (let [rows (-> height
                 (- (* 2 y-margin))
                 (/ spacing)
                 (js/Math.floor))
        cols (-> width
                 (- (* 2 x-margin))
                 (/ spacing)
                 (js/Math.floor))
        y-offset (/ (- height
                       (* spacing rows))
                    2)

        x-offset (/ (- width
                       (* spacing cols))
                    2)]
    [:g
     (for [row (range y-start rows)]
      [:line
       {:key row
        :x1 x-margin
        :y1 (+ 16
               (* row spacing))
        :x2 (- width x-margin)
        :y2 (+ 16
               (* row spacing))
        :fill "none"
        :stroke color}])

     (for [col (range x-start cols)]
      [:line
       {:key col
        :x1 (+ 16
               (* col spacing))
        :y1 y-margin
        :x2 (+ 16
               (* col spacing))
        :y2 (- height y-margin)
        :fill "none"
        :stroke color}])]))


(defn subgrid
  []
  [grid
   {:width width
    :height height
    :x-margin 10
    :y-margin 10
    :spacing  8
    :color    (color/get :subgrid)}])

(defn basegrid
  []
  [grid
   {:width width
    :height height
    :x-margin 10
    :y-margin 10
    :spacing  16
    :color    (color/get :basegrid)}])

(comment
  width
  (-> width
      (- 32)
      (/ 16)))

(defn dotgrid
  [{:keys [width height
           x-margin y-margin
           x-start y-start
           radius spacing
           color]
    :or {x-margin 10
         y-margin 10
         x-start 0
         y-start 0
         radius  1.5}}]
  #_
  (let [rows (-> height
                 (- (* 2 y-margin))
                 (/ spacing)
                 (js/Math.round))
        cols (-> width
                 (- (* 2 x-margin))
                 (/ spacing)
                 (js/Math.round))
        y-offset (- spacing y-margin)
        x-offset (- spacing x-margin)]
    [:g
     (for [row (range y-start rows)
           col (range x-start cols)]
      [:circle
       {:key row
        :cx (+ x-margin
               x-offset
               (* col spacing))
        :cy (+ y-margin
               y-offset
               (* row spacing))
        :r radius
        :fill (or color (color/get :dots))}])]))

(defn doc
  []
  (let [props {:width  width
               :height height}]
    {:file "Letter Portrait 2024"
     :props props
     :defs []
     :children [[subgrid                props]
                [basegrid               props]
                [dotgrid                props]
                [presets/outline-layer  props]]}))

(defn -main
  [& _args]
  (list (doc)))
