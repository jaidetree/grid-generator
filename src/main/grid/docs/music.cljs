(ns grid.docs.music
  (:require
    [grid.svg :as svg]
    [grid.color :as color]
    [grid.presets :as presets]))

(def width (svg/in->px 8.5))
(def height (svg/in->px 11))

(def x-start (* 4 16))
(def y-start (* 6 16))
(def x-gutter (* 1 16))
(def y-gutter (* 2 16))

(def border
  (-> (color/get :bone)
      (color/saturate -90)
      (color/brightness -50)))

(defn staff-lines
  [{:keys [y]}]
  (let [y (or y 0)]
    [:g
      (for [i (range 0 5)]
        [:g
         {:key i}
         [:line
          {:x1 x-start
           :x2 (- width
                  x-start)
           :y1 (+ (* i 8)
                  y-start
                  y)
           :y2 (+ (* i 8)
                  y-start
                  y)
           :stroke-width "1"
           :stroke border}]])]))

(defn staff
  [{:keys [y]}]
  (let [y (or y 0)]
    [:g
     [:line
      {:x1 (- x-start 1)
       :x2 (- x-start 1)
       :y1 (+ y-start
              y)
       :y2 (+ y-start
              y
              (* 14 8))
       :stroke-width "2"
       :stroke (color/get :teal)}]
     [:line
      {:x1 (- width
              1
              x-start)
       :x2 (- width
              1
              x-start)
       :y1 (+ y-start
              y)
       :y2 (+ y-start
              y
              (* 14 8))
       :stroke-width "2"
       :stroke (color/get :pink)}]
     [staff-lines
      {:y y}]
     [staff-lines 
      {:y (+ y
             (* 10 8))}]]))

(defn staves
  []
  [:g
    (for [i (range 0 6)]
      [staff
       {:k i
        :y (* i 16 10)}])])

(defn title
  []
  [:g
    [:line
     {:x1 16
      :x2 (- width 16)
      :y1 (* 3 16)
      :y2 (* 3 16)
      :stroke-width "1"
      :stroke (color/get :bone)}]])

(defn doc
  [& args]
  (let [props {:width width
               :height height}]
    {:file "journal/music"
     :props props
     :defs     []
     :children [[:g
                 [presets/subgrid-layer  props]
                 [presets/basegrid-layer props]
                 [presets/dots-layer     props]]
                [staves                  props]
                [title                   props]
                [presets/outline-layer   props]]}))

(defn -main
  [& args]
  [(doc)])
