(ns grid.docs.letter-portrait
  (:require
    [grid.svg :as svg]
    [grid.presets :as presets]
    [grid.color :as color]))

(def width (svg/in->px 8.5))
(def height (svg/in->px 11))

(def x-margin 10)
(def y-margin 10)

(defn grid
  [{:keys [width height
           x-margin y-margin
           x-start y-start
           spacing color]
    :or {x-start 16
         y-start 16}}]
  (let [rows (-> height
                 (- (* 2 y-margin))
                 (/ spacing)
                 (dec)
                 (+ 0.5)
                 (js/Math.ceil))
        cols (-> width
                 (- (* 2 x-margin))
                 (/ spacing)
                 (dec)
                 (+ 0.5)
                 (js/Math.ceil))]

    ;; @debug
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


(defn subgrid
  []
  [grid
   {:width width
    :height height
    :x-margin y-margin
    :y-margin x-margin
    :spacing  8
    :color    (color/get :subgrid)}])

(defn basegrid
  []
  [grid
   {:width width
    :height height
    :x-margin x-margin
    :y-margin y-margin
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
         x-start 16
         y-start 16
         radius  1.5
         spacing 16}}]
  (let [rows (-> height
                 (- (* 2 y-margin))
                 (/ spacing)
                 (dec)
                 (+ 0.5)
                 (js/Math.ceil))
        cols (-> width
                 (- (* 2 x-margin))
                 (/ spacing)
                 (dec)
                 (+ 0.5)
                 (js/Math.ceil))]
    [:g
     (for [row (range 0 rows)
           col (range 0 cols)]
      [:circle
       {:key row
        :cx (+ x-start
               (* col spacing))
        :cy (+ y-start
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
