(ns grid.docs.year
  (:require
    [grid.presets :as presets]
    [grid.color :as color]
    [grid.svg :as svg]))

(def width (svg/in->px (* 2 8.5)))
(def height (svg/in->px 11))

(def x-start (* 2 16))
(def x-gutter (* 2 16))

(def y-start (* 6 16))
(def y-gutter (* 2 16))

(def months
  [{:title "Jan"
    :emoji "🍾"}
   {:title "Feb"
    :emoji "⛄"}
   {:title "Mar"
    :emoji "💐"}
   {:title "Apr"
    :emoji "🌧️"}
   {:title "May"
    :emoji "😸"}
   {:title "Jun"
    :emoji "😎"}
   {:title "Jul"
    :emoji "🌭"}
   {:title "Aug"
    :emoji "💼"}
   {:title "Sep"
    :emoji "🏖️"}
   {:title "Oct"
    :emoji "🎃"}
   {:title "Nov"
    :emoji "🎂"}
   {:title "Dec"
    :emoji "🎊"}])


(def border
  (-> (color/get :outline)
      (color/saturate -10)
      (color/brightness 10)))

(def month-bg
  (-> (color/get :dark-grape)
      (color/saturate -5)
      (color/brightness 5)))

(def circle-bg
  (color/get :pink))

(defn swoosh
  []
  (svg/swoosh
    {:curve [(* 4 16) (* 32 16)
             800 -800 800 800
             (- width (* 8 16)) 0]
     :stroke (color/get :teal)
     :strokeWidth (* 4 16)
     :strokeLinecap "round"
     :fill "none"}))

(defn random-circles
  []
  [:g
   (for [i (range 0 35)]
     (let [x (rand-int width)
           y (+ (rand-int (- height (* 16 6)))
                (* 16 6))
           r (rand-int 40)]
        [:circle
          {:key i
           :cx (svg/px x)
           :cy (svg/px y)
           :r  (svg/px r)
           :fill circle-bg}]))])

(defn month-fill-symbol
  [{:keys [rect-width rect-height]}]
  [:g
   {:id "month-fill"}
   [:rect
     {:fill month-bg
      :rx "5px"
      :ry "5px"
      :width (svg/px rect-width)
      :height (svg/px rect-height)}]])

(defn month-symbol
  [{:keys [rect-width rect-height]}]
  [:g
   {:id "month"}
   [:rect
    {:strokeWidth "2px"
     :stroke border
     :fill "none"
     :rx "5px"
     :ry "5px"
     :x "0px"
     :y "0px"
     :width (svg/px rect-width)
     :height (svg/px rect-height)}]

   [:line
    {:strokeWidth "2px"
     :stroke      border
     :x1 "0px"
     :y1 (svg/px (* 3 16))
     :x2 rect-width
     :y2 (svg/px (* 3 16))}]

   [:circle
    {:cx (svg/px 8)
     :cy (svg/px 24)
     :r "28px"
     :stroke border
     :stroke-width "2px"
     :fill month-bg}]])

(defn month-fill
  [{:keys [rect-width rect-height]}]
  [:g
   (for [row (range 0 3)
         col (range 0 4)]
     [:use
      {:key (str row "-" col)
       :href "#month-fill"
       :x (svg/px (+ x-start (* col (+ rect-width x-gutter))))
       :y (svg/px (+ y-start (* row (+ rect-height y-gutter))))}])])

(defn month-boxes
  [{:keys [rect-width rect-height]}]
  [:g
    (for [row (range 0 3)
          col (range 0 4)]
      (let [idx (+ col (* row 4))
            month-map (nth months idx)]
       [:g
         {:key idx}
         [:use
          {:href "#month"
           :x (svg/px (+ x-start (* col (+ rect-width x-gutter))))
           :y (svg/px (+ y-start (* row (+ rect-height y-gutter))))}]

         [:text
          {:x           (svg/px (+ x-start (* col (+ rect-width x-gutter))
                                   (/ rect-width 2)))
           :y           (svg/px (+ y-start (* row (+ rect-height x-gutter))
                                   (* 2 16)))
           :font-family "OperatorMono Nerd Font"
           :font-size   "24px"
           :font-style  "italic"
           :fill        (color/get :teal)
           :text-anchor "middle"}
          (str (:title month-map))]

         [:text
          {:x           (svg/px (+ (* 2.5 16) (* col (+ rect-width (* 2 16)))))
           :y           (svg/px (+ (* 8.5 16) (* row (+ rect-height (* 2 16)))))
           :font-family "OperatorMono Nerd Font"
           :font-size   "32px"
           :fill        (color/get :teal)
           :text-anchor "middle"}
          (str (:emoji month-map))]]))])

(defn create-props
  []
  (let [rect-width (svg/calc-grid-size
                     {:total width
                      :items 4
                      :start x-start
                      :gutter x-gutter})
        rect-height (svg/calc-grid-size
                      {:total height
                       :items 3
                       :start y-start
                       :gutter y-gutter})]
    {:width width
     :height height
     :rect-width rect-width
     :rect-height rect-height}))

(defn doc
  []
  (let [props (create-props)]
    {:file     "journal/Year"
     :props    props
     :defs     [[presets/basegrid-pattern]
                [presets/subgrid-pattern]
                [presets/dots-pattern]
                [month-symbol            props]
                [month-fill-symbol       props]]

     :children [[:g
                 [random-circles         props]
                 [swoosh                 props]
                 [random-circles         props]
                 [month-fill             props]]
                [:g
                 [presets/subgrid-layer  props]
                 [presets/basegrid-layer props]
                 [presets/dots-layer     props]]
                [:g
                 [month-boxes            props]]
                [:g
                 [presets/outline-layer  props]
                 [presets/title-layer    props "Year 2024"]]]}))

(defn -main
  [& _args]
  (list (doc)))
