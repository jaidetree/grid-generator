(ns grid.docs.month
  (:require
    [grid.svg :as svg]
    [grid.color :as color]
    [grid.presets :as presets]
    [grid.utils.date :as date]
    [grid.constants :as constants]))

(def width (svg/in->px 11))
(def height (svg/in->px 8.5))

(def x-start (* 1 16))
(def y-start (* 6 16))

(def habits-height (* 9 16))
(def time-width (* 25 2 16))

(def border
  (-> (color/get :outline)
      (color/saturate -10)
      (color/brightness 10)))

(defn fills
  [{:keys []}]
  [:g
   [svg/rounded-rect
    {:x (+ x-start)
     :y (+ y-start)
     :r [10 10 0 0]
     :width (+ width
               (* -2 16))
     :height (+ habits-height
                -16)
     :fill (-> (color/get :dark-grape)
              (color/saturate -10)
              (color/brightness 5))}]
   [svg/rounded-rect
    {:x (+ x-start)
     :y (+ y-start
           habits-height)
     :r [0 0 10 10]
     :width (+ width
               (* -2 16))
     :height (- height
                habits-height
                y-start)
     :fill (-> (color/get :dark-grape)
               (color/saturate   5)
               (color/brightness -5))}]])


(defn striping
  [{:keys [days-in-month]}]
  [:g
   (for [row (range 0 days-in-month 2)]
     [:rect
      {:key row
       :x x-start
       :y (+ y-start
             habits-height
             -16
             (* row 16))
       :width (+ width
                 (* -2 16))
       :height 16
       :fill (-> (color/get :dark-grape)
                 (color/saturate 2)
                 (color/brightness 2))}])])

(defn rows
  [{:keys [days-in-month first-weekday]}]
  [:g
   (for [i (range 0 days-in-month)]
     (let [day (mod (+ i first-weekday) 7)
           weekday (nth constants/weekdays day)]
        [:g
         {:key i}
         [:text
          {:x (+ x-start
                 8)
           :y (+ y-start
                 habits-height
                 (* i 16)
                 -4)
           :font-family "OperatorMono Nerd Font"
           :font-size 11
           :font-weight 200
           :text-anchor "middle"
           :fill (:color weekday)}
          (str (inc i))]
         [:text
          {:x (+ x-start
                 (* 1 16)
                 8)
           :y (+ y-start
                 habits-height
                 (* i 16)
                 -4)
           :font-family "OperatorMono Nerd Font"
           :font-size 11
           :font-weight 200
           :text-anchor "middle"
           :fill (:color weekday)}
          (str (first (:title weekday)))]
         [:line
          {:x1 (+ x-start)
           :y1 (+ y-start
                  habits-height
                  (* i 16))
           :x2 (- width x-start)
           :y2 (+ y-start
                  habits-height
                  (* i 16))
           :stroke (if (= day 6)
                     (-> border
                         (color/brightness 5))
                     border)
           :stroke-width (if (= day 6)
                           2
                           1)}]]))])

(defn box-border
  [{:keys [days-in-month]}]
  [:g
   [:rect
    {:x x-start
     :y y-start
     :rx 10
     :ry 10
     :width (- width (* 2 x-start))
     :height (+ (* days-in-month 16)
                habits-height)
     :stroke-width 2
     :stroke border
     :fill "none"}]
   [:line#left-labels-separator
    {:x1 (+ x-start
            (* 2 16))
     :y1 (+ y-start)
     :x2 (+ x-start
            (* 2 16))
     :y2 (+ y-start
            habits-height
            (* days-in-month 16))
     :stroke (-> border
                 (color/brightness 5))
     :stroke-width 2}]
   [:line#hours-separator
    {:x1 (+ x-start
            (* 1 16)
            time-width)
     :y1 (+ y-start)
     :x2 (+ x-start
            (* 1 16)
            time-width)
     :y2 (+ y-start
            habits-height
            (* days-in-month 16))
     :stroke (-> border
                 (color/brightness 5))
     :stroke-width 2}]
   [:line#header-separator
    {:x1 x-start
     :y1 (+ y-start
            habits-height
            (* -1 16))
     :x2 (- width x-start)
     :y2 (+ y-start
            habits-height
            (* -1 16))
     :stroke-width 2
     :stroke border}]
   [:line#footer-separator
    {:x1 x-start
     :y1 (+ y-start
            habits-height
            (* (dec days-in-month) 16))
     :x2 (- width x-start)
     :y2 (+ y-start
            habits-height
            (* (dec days-in-month) 16))
     :stroke-width 2
     :stroke border}]])

(defn hours
  [{:keys [days-in-month]}]
  [:g
   (for [i (range 0 24)]
     [:g
      {:key i}
      [:text
       {:x (+ x-start
              (* 2 16)
              (* i 2 16)
              2)
        :y (+ y-start
              habits-height
              -16
              -2)
        :font-family "OperatorMono Nerd Font"
        :font-size 12
        :font-weight 400
        :text-anchor "start"
        :fill border}
       (str (cond (= i 0)  "12a"
                  (= i 12) "12p"
                  (> i 12) (- i 12)
                  :else    i))]
      [:line
       {:x1 (+ x-start
               (* 4 16)
               (* i 2 16))
        :y1 (+ y-start
               habits-height
               (* -2 16))
        :x2 (+ x-start
               (* 4 16)
               (* i 2 16))
        :y2 (+ y-start
               habits-height
               (* days-in-month 16))
        :stroke (-> border
                    (color/saturate -25)
                    (color/brightness -10))}]])
   [:line
    {:x1 (+ x-start
            (* 2 16))
     :y1 (+ y-start
            habits-height
            (* -2 16))
     :x2 (+ x-start
            (* 3 16)
            (* 24 2 16))
     :y2 (+ y-start
            habits-height
            (* -2 16))
     :stroke (-> border
                 (color/saturate -25)
                 (color/brightness -10))}]

   [:line
    {:x1 (+ x-start
            (* 13 2 16))
     :y1 (+ y-start
            habits-height
            -16)
     :x2 (+ x-start
            (* 13 2 16))
     :y2 (+ y-start
            habits-height
            (* days-in-month 16))
     :stroke border
     :stroke-width 2}]])

(defn habits-section
  [{:keys [days-in-month]}]
  [:g
   (for [i (range 0 (inc (count constants/habits)))]
     [:g
      {:key i}
      [:path
       {:d (svg/path (svg/move
                       (+ x-start
                          (* 5 16)
                          time-width
                          (* i 16))
                       (+ y-start))
                     (svg/line-relative
                       (* -3 16)
                       (+ habits-height
                          (* -1 16)))
                     (svg/v-line-relative
                       (* (inc days-in-month) 16)))
        :stroke border
        :stroke-width (if (or (zero? i)
                              (= i (count constants/habits)))
                          2
                          1)
        :fill "none"}]
      (when (> i 0)
        (let [i (dec i)
              x (+ x-start
                   (* 3 16)
                   time-width
                   (* i 16)
                   3)
              y (+ y-start
                   habits-height
                   (* -2 16))
              habit (nth constants/habits i)]
          [:g
           [:text
            {:x x
             :y y
             :font-family "OperatorMono Nerd Font"
             :font-size   12
             :font-weight 400
             :text-anchor "start"
             :fill        (:color habit)
             :transform   (svg/transform
                            (svg/rotate -69.8 x y))}
            (str (:title (nth constants/habits i)))]
           [:text
            {:x (+ x-start
                   (* 2.5 16)
                   2
                   time-width
                   (* i 16))
             :y (+ y-start
                   habits-height
                   -4
                   (* -1 16))
             :font-family "FontAwesome 6 Pro"
             :font-weight :bold
             :font-size   8
             :text-anchor "middle"
             :fill (:color habit)}
            (str (:icon habit))]]))])])

(defn legend
  []
  [:g
   [svg/rounded-rect
    {:r [24]
     :x (+ x-start
           (* 15 16))
     :y (+ y-start
           (* 2 16))
     :width (* 26 16)
     :height (* 3 16)
     :fill (-> (color/get :dark-grape)
               (color/saturate -10)
               (color/brightness 15))}]
   (let [x (+ x-start
               (/ time-width 2)
               (* -6 16))
         y (+ y-start
               (* 3 16))]
     [:g
      [:rect
       {:x (+ x
              (* -1 16))
        :y y
        :width 16
        :height 16
        :rx 0
        :ry 0
        :stroke (color/get :bone)
        :fill (-> (color/get :teal)
                  (color/hue 10))}]
      [:text
       {:x (+ x
              8)
        :y (+ y
              16
              -2)
        :font-family "OperatorMono Nerd Font"
        :font-size 16
        :font-style "italic"
        :text-anchor "start"
        :fill (color/get :bone)}
       "Sleep"]])
   (let [x (+ x-start
            (/ time-width 2)
            (* 1 16))
         y (+ y-start
               (* 3 16))]
       [:g
        [:rect
         {:x (+ x
                (* -1 16))
          :y y
          :width 16
          :height 16
          :rx 0
          :ry 0
          :stroke (color/get :bone)
          :fill (-> [290 80 50]
                    (color/hsl->rgb)
                    (color/rgb->hex))}]
        [:text
         {:x (+ x 8)
          :y (+ y
                16
                -2)
          :font-family "OperatorMono Nerd Font"
          :font-size 16
          :font-style "italic"
          :text-anchor "start"
          :fill (color/get :bone)}
         "Work"]])
   (let [x (+ x-start
            (/ time-width 2)
            (* 7 16))
         y (+ y-start
               (* 3 16))]
       [:g
        [:rect
         {:x (+ x
                (* -1 16))
          :y y
          :width 16
          :height 16
          :rx 0
          :ry 0
          :stroke (color/get :bone)
          :fill (color/get :pink)}]
        [:text
         {:x (+ x 8)
          :y (+ y
                16
                -2)
          :font-family "OperatorMono Nerd Font"
          :font-size 16
          :font-style "italic"
          :text-anchor "start"
          :fill (color/get :bone)}
         "Side-projects"]])])

(defn decorations
  [{:keys [days-in-month]}]
  [:g
   [svg/circle-cluster
    {:n 42
     :min-radius 1.5
     :max-radius 8
     :x-jitter (- time-width 32)
     :y-jitter (* 6 16)
     :cx (+ x-start
            (* 2 16)
            (/ time-width 2))
     :cy (+ y-start
            (* 3.5 16))
     :fill (color/get :pink)}]
   [svg/circle-cluster
    {:n 32
     :min-radius 1.5
     :max-radius 10
     :x-jitter (- width 32)
     :y-jitter (* 6 16)
     :cx (+ x-start
            (/ width 2))
     :cy (+ y-start
            habits-height
            (* (inc days-in-month) 16)
            (* 4 16))
     :fill (color/get :pink)}]
   [svg/swoosh
    {:curve [(+ x-start
                16)
             (+ y-start
                habits-height
                (* (inc days-in-month) 16)
                (* 3 16))
             500 80
             500 -80
             (+ width
                (* -4 16))
             0]

     :stroke-width 24
     :stroke-linecap :round
     :stroke (color/get :teal)
     :fill "none"}]
   [svg/circle-cluster
    {:n 32
     :min-radius 1.5
     :max-radius 10
     :x-jitter (- width 32)
     :y-jitter (* 6 16)
     :cx (+ x-start
            (/ width 2))
     :cy (+ y-start
            habits-height
            (* (inc days-in-month) 16)
            (* 4 16))
     :fill (color/get :pink)}]])

(defn create-props
  [date]
  {:width width
   :height height
   :first-weekday (date/date->first-weekday-of-month date)
   :month-name (date/date->month-name date)
   :days-in-month (date/date->days-in-month date)})

(defn doc
  [& [date]]
  (let [date (if date date (js/Date.))
        props (create-props date)]
    {:file    (str "journal/" (:month-name props) "/Month " (:month-name props))
     :props    props
     :defs     [[presets/subgrid-pattern]
                [presets/basegrid-pattern]
                [presets/dots-pattern]]
     :children [[fills                      props]
                [striping                   props]
                [presets/subgrid-layer      props]
                [presets/basegrid-layer     props]
                [presets/dots-layer         props]
                [rows                       props]
                [box-border                 props]
                [habits-section             props]
                [hours                      props]
                [decorations                props]
                [legend                     props]
                [presets/outline-layer      props]
                [presets/title-layer        props (:month-name props)]]}))


(defn -main
  [& args]
  (cond (empty? args) [(doc)]))

;; Rough code for the tasks part
#_(for [i (range 0 14)
        [:path
         {:key i
          :d (svg/path
               (svg/move
                 (+ x-start
                    (* 33 16)
                    x-gutter
                    (* i 16))
                 (+ y-start
                        (* 5 16)
                        y-gutter))
               (svg/line-relative
                 (* -2 16)
                 (* 7 16))
               (svg/v-line-relative
                 (* 1 16)))
          :stroke (color/get :bone)}]])
