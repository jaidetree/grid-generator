(ns grid.docs.day
  (:require
    [grid.constants :refer [weekdays]]
    [grid.svg :as svg]
    [grid.color :as color]
    [grid.presets :as presets]))

(def width (svg/in->px 8.5))
(def height (svg/in->px 11))

(def x-start (* 2 16))
(def y-start (* 5 16))
(def x-gutter (* 1 16))
(def y-gutter (* 2 16))

(def work-tasks 8)
(def personal-tasks 6)

(def tasks-gap (* (- 16 work-tasks personal-tasks)
                  16))

(def border
  (-> (color/get :outline)
      (color/saturate -10)
      (color/brightness 10)))

(def habit-color-interval (/ 240 5))

(def habits
  (->> [{:title "Meditate AM"
         :icon  "sunrise"}
        {:title "Meditate PM"
         :icon  "moon"}
        {:title "Exercise"
         :icon  "dumbbell"}
        {:title "Read"
         :icon  "book-open-cover"}
        {:title "Game"
         :icon  "gamepad-modern"}]
       (map-indexed vector)
       (mapv
         (fn [[idx m]]
           (assoc m :color (-> [(* idx habit-color-interval) 50 50]
                               (color/hsl->rgb)
                               (color/rgb->hex)))))))

(defn date->month-name
  [date]
  (.toLocaleDateString date "en-US" #js {:month "long"}))

(defn date->iso-string
  [date]
  (let [year (.getFullYear date)
        month (inc (.getMonth date))
        day  (.getDate date)]
    (str year
         "-" (.padStart (str month) 2 "0")
         "-" (.padStart (str day) 2 "0"))))

(defn date->title
  [date]
  (.toLocaleDateString date "en-US" #js {:year "numeric"
                                         :month "long"
                                         :day   "numeric"}))

(defn date->weekday
  [date]
  (.toLocaleDateString date "en-US" #js {:weekday "long"}))

(comment
  (date->title (js/Date.)))

(defn tasks-fill
  []
  (let [color (-> (color/get :dark-grape)
                  (color/saturate 5)
                  (color/brightness 5))]
   [:g
    [svg/rounded-rect
     {:r [10 10 0 0]
      :x x-start
      :y y-start
      :width (* 30 16)
      :height (* 2 16)
      :fill color}]
    [svg/rounded-rect
     {:r [10 10 0 0]
      :x x-start
      :y (+ y-start
            (* work-tasks 2 16)
            (* 3 16)
            tasks-gap)
      :width (* 30 16)
      :height (* 2 16)
      :fill color}]]))

(defn mood-fill
  []
  [svg/rounded-rect
   {:r [(* 2 16)]
    :x (+ x-start
          (* 30 16)
          x-gutter)
    :y y-start
    :width (* 16 16)
    :height (* 4 16)
    :fill "none" #_(-> (color/get :pink)
                      (color/saturate -75)
                      (color/brightness -55))}])

(defn schedule-fill
  []
  [:g
   [svg/rounded-rect
    {:r [0 0 10 10]
     :x (+ x-start
           (* 30 16)
           x-gutter)
     :y (+ y-start
           (* 19 16)
           y-gutter)
     :width  (* 16 16)
     :height (* 15 16)
     :fill   (-> (color/get :dark-grape)
                 (color/saturate -15)
                 (color/brightness 5))}]])

(defn mood-sticker
  [{:keys [x y fill d transform]}]
  (let [r 23
        x (+ x (* -1 (/ d 2)))
        y (+ y (/ d 2) 2)]
    [:g
     {:class "date-sticker"
      :transform transform}
     [:path
      {:d (svg/path (svg/move x y)
                    (svg/arc-relative r r 0 1 1 d 0)
                    "Z")
       :fill      fill
       #_#_:fill (color/get :teal)}]
     [:path
      {:d (svg/path (svg/move x y)
                    (svg/arc-relative (* 1.5 r) (* 2 r) 0 0 1 d 0)
                    "Z")
       :fill (-> (color/get :teal)
                 (color/hue -180)
                 (color/saturate -40)
                 (color/brightness 50))}]]))

(defn mood-box
  [{:keys [weekday]}]
  (let [day (nth weekdays weekday)
        x-offset (- (rand 8) 4)
        y-offset (- (rand 8) 4)]
    [:g
     [svg/rounded-rect
       {:r [(* 2 16)]
        :x (+ x-start
              (* 30 16)
              x-gutter)
        :y y-start
        :width (* 16 16)
        :height (* 4 16)
        :stroke (color/get :teal)
        :fill "none"}]
     [:text
      {:x (+ x-start
             (* 35.5 16)
             x-gutter)
       :y (+ y-start
             (* 1 16))
       :fontFamily "OperatorMono Nerd Font"
       :fontSize   "14px"
       :fontStyle  "italic"
       :textAnchor "start"
       :fill (color/get :pink) #_(-> border
                                     (color/brightness 10))}
      (str "How was today?")]
     [:circle
      {:cx (+ x-start
              (* 32 16)
              x-gutter)
       :cy (+ y-start
              (* 2 16))
       :r (* 1.5 16)
       :fill "none"
       :stroke border}]
     [mood-sticker
      {:x (+ x-start
             (* 32 16)
             x-gutter
             x-offset)
       :y (+ y-start
             (* 2 16)
             y-offset)
       :d 30
       :fill (:color day)
       :transform (svg/transform
                    (svg/rotate (* -1 (rand 90))
                                (+ x-start
                                  (* 32 16)
                                  x-gutter
                                  x-offset)
                                (+ y-start
                                   (* 2 16)
                                   y-offset)))}]]))

(defn daily-habits
  []
  [:g
   [svg/rounded-rect
    {:r [10]
     :x (+ x-start
           (* 30 16)
           x-gutter)
     :y (+ y-start
           (* 4 16)
           y-gutter)
     :width  (* 16 16)
     :height (* 10 16)
     #_#_
     :stroke border
     :fill "none"
     #_#_
     :fill   (-> (color/get :dark-grape)
                 (color/hue 90)
                 (color/saturate -5)
                 (color/brightness 5))}]
   (for [i (range 0 (count habits))]
     (let [habit (nth habits i)]
       [:g
        {:key i}
        [:path
         {:stroke border
          :fill "none"
          :d (svg/path
               (svg/move
                 (+ x-start
                    (* 31.5 16)
                    (* i 3 16)
                    x-gutter)
                 (+ y-start
                    (* 9 16)
                    y-gutter))
               (svg/v-line-relative
                 -8)
               (svg/line-relative
                 (* 1.8 16)
                 (* -5 16)))}]
        (let [x (+ x-start
                   (* 31.5 16)
                   (* i 3 16)
                   x-gutter)
              y (+ y-start
                   (* 8.25 16)
                   y-gutter)]
          [:text
           {:x x
            :y y
            :font-family "OperatorMono Nerd Font"
            :font-size "10px"
            :fill (:color habit)
            :text-anchor "start"
            :transform (svg/transform
                         (svg/rotate -70 x y))}


           (:title habit)])
        [svg/rounded-rect
         {:x (+ x-start
                (* 31 16)
                (* i 3 16)
                x-gutter
                -8)
          :y (+ y-start
                (* 9 16)
                y-gutter)
          :width (+ (* 1 16)
                    16)
          :height (* 5 16)
          :r [16]
          :stroke border
          :fill (color/get :dark-grape)}]
        [:text
         {:x (+ x-start
                (* 31.5 16)
                (* i 3 16)
                x-gutter)
          :y (+ y-start
                (* 11 16)
                y-gutter)
          :font-family "FontAwesome 6 Pro"
          :font-weight "bold"
          :font-size "20px"
          :text-anchor "middle"
          :fill (:color habit)}
         (:icon habit)]
        [svg/rounded-rect
         {:x (+ x-start
                (* 31 16)
                (* i 3 16)
                x-gutter)
          :y (+ y-start
                (* 12 16)
                y-gutter)
          :r [3]
          :width (* 1 16)
          :height (* 1 16)
          :stroke (color/get :teal)
          :fill "none"}]]))])

(defn schedule
  []
  [:g
   [svg/rounded-rect
    {:r [10 10 0 0]
     :x (+ x-start
           (* 30 16)
           x-gutter)
     :y (+ y-start
           (* 16 16)
           y-gutter)
     :width  (* 16 16)
     :height (* 3 16)
     :stroke "none"
     :fill  (-> [15 40 30]
                (color/hsl->rgb)
                (color/rgb->hex)
                #_(color/saturate -70)
                #_(color/brightness -30))}]
   [svg/rounded-rect
    {:r [10 10 10 10]
     :x (+ x-start
           (* 30 16)
           x-gutter)
     :y (+ y-start
           (* 16 16)
           y-gutter)
     :width  (* 16 16)
     :height (* 18 16)
     :stroke border
     :fill  "none"}]
   [:text
    {:x (+ x-start
           (* 37.5 16)
           x-gutter)
     :y (+ y-start
           (* 17.75 16)
           y-gutter)
     :fontFamily "OperatorMono Nerd Font"
     :fontStyle  "italic"
     :fontSize   "18px"
     :text-anchor "middle"
     :fill (color/get :bone)}
    "Schedule"]
   [:g
    (for [row (range 0 7)]
      [:text
       {:key row
        :x (+ x-start
              (* 32 16)
              x-gutter)
        :y (+ y-start
              (* 20 16)
              12
              (* row 2 16)
              y-gutter)
        :fontFamily "OperatorMono Nerd Font"
        :fontSize   "18px"
        :text-anchor "middle"
        :fill (-> border
                  (color/saturate 10)
                  (color/brightness 20))}
       ":"])]
   #_
   [svg/rounded-rect
    {:r [0 0 10 10]
       :x (+ x-start
             (* 30 16)
             x-gutter)
       :y (+ y-start
             (* 19 16)
             y-gutter)
       :width  (* 16 16)
       :height (* 15 16)
       :stroke border
      :fill "none"}]])

(defn tasks
  []
  [:g
   [svg/rounded-rect
    {:r [10]
     :x x-start
     :y y-start
     :width (* 30 16)
     :height (+ (* work-tasks 2 16)
                (* 3 16))
     :stroke border
     :fill "none"}]
   [svg/rounded-rect
    {:r [10]
     :x x-start
     :y (+ y-start
           (* work-tasks 2 16)
           (* 3 16)
           tasks-gap)
     :width (* 30 16)
     :height (+ (* personal-tasks 2 16)
                (* 3 16))
     :stroke border
     :fill "none"}]
   [:text
    {:x (+ x-start
            (* 1 16))
     :y (+ y-start
           (* 1.25 16))
     :font-family "OperatorMono Nerd Font"
     :font-size "16"
     :font-style "italic"
     :text-anchor "start"
     :fill (color/get :pink)}
    (str "Work Tasks")]
   [:line
    {:x1 (+ x-start
             (* 0 16))
     :y1 (+ y-start
            (* 2 16))
     :x2 (* 32 16)
     :y2 (+ y-start
            (* 2 16))
     :stroke border}]
   (for [row (range 0 work-tasks)]
     [:rect
      {:key row
       :x  (+ x-start
              1
              (* 1 16))
       :y  (+ y-start
              (* 3 16)
              1
              (* row 2 16))
       :rx 3
       :ry 3
       :width 14
       :height 14
       :stroke (color/get :teal)
       :fill "none"}])
   [:text
    {:x (+ x-start
           (* 1 16))
     :y (+ y-start
           (* work-tasks 2 16)
           tasks-gap
           (* 4.25 16))
     :font-family "OperatorMono Nerd Font"
     :font-size "16"
     :font-style "italic"
     :text-anchor "start"
     :fill (color/get :pink)}
    (str "Personal Tasks & Goals")]
   [:line
    {:x1 (+ x-start
             (* 0 16))
     :y1 (+ y-start
            (* work-tasks 2 16)
            (* 5 16)
            tasks-gap)
     :x2 (* 32 16)
     :y2 (+ y-start
            (* work-tasks 2 16)
            (* 5 16)
            tasks-gap)
     :stroke border}]
   (for [row (range (- 16 personal-tasks) 16)]
     [:rect
      {:key row
       :x  (+ x-start
              (* 1 16)
              1)
       :y  (+ y-start
              (* 4 16)
              (* row 2 16)
              1)
       :rx 3
       :ry 3
       :width 14
       :height 14
       :stroke (color/get :teal)
       :fill "none"}])])

(defn notes
  []
  [:g
   (for [row (range 0 12)]
     (let [y (+ (* 43 16)
                (* row 2 16))]
       [:line
        {:key row
         :x1 x-start
         :y1 y
         :x2 (- width x-start)
         :y2 y
         :stroke-width "2"
         :stroke-linecap "round"
         :stroke       (-> (color/get :dark-grape)
                           (color/saturate -20)
                           (color/brightness 20))}]))])

(defn title
  [{:keys [date width]}]
  [:g
   [:line
    {:x1 16
     :y1 (* 2 16)
     :x2 (- width 16)
     :y2 (* 2 16)
     :stroke (color/get :bone)}]
   [:text
    {:x 16
     :y (- (* 2 16)
           1)
     :fontFamily "OperatorMono Nerd Font"
     :fontStyle "italic"
     :fontSize  "18px"
     :text-anchor "start"
     :fill (color/get :pink)}
    (str (date->weekday date))]
   [:text
    {:x (- width 16)
     :y (- (* 2 16)
           1)
     :fontFamily "OperatorMono Nerd Font"
     :fontStyle "italic"
     :fontSize  "18px"
     :text-anchor "end"
     :fill (color/get :teal)}
    (str (date->title date))]])

(defn doc
  [& [date]]
  (let [date (if (isa? date js/Date) date (js/Date.))
        month-name (date->month-name date)
        date-str (date->iso-string date)
        props {:date  date
               :width width
               :height height
               :weekday (.getDay date)}]
    {:file (str "journal/" month-name "/Day " date-str)
     :props props
     :defs     [[presets/subgrid-pattern]
                [presets/basegrid-pattern]
                [presets/dots-pattern]]
     :children [[:g
                 [schedule-fill          props]
                 [mood-fill              props]
                 [tasks-fill             props]]
                [:g
                 [presets/subgrid-layer  props]
                 [presets/basegrid-layer props]
                 [presets/dots-layer     props]]
                [tasks                   props]
                [mood-box                    props]
                [daily-habits            props]
                [schedule                props]
                [title                   props]
                [notes                   props]
                [presets/outline-layer   props]]}))

(defn date->days-in-month
  [date]
  (let [date (js/Date.)]
   (doto date
     (.setMonth (inc (.getMonth date)))
     (.setDate (dec  (.getDate date))))
   (.getDate date)))

(defn cmd-days-in-month
  [[year month]]
  (let [date (js/Date. year month 1)
        days-in-month (date->days-in-month date)]
    (for [day (range 1 (inc days-in-month))]
      (let [date (doto (js/Date. date)
                   (.setDate day))]
        (doc date)))))

(defn cmd-days-in-year
  [year]
  (for [month (range 0 11)
        day   (range 0 (date->days-in-month
                         (js/Date. year month 1)))]
    (let [date (js/Date. year month day)]
      (doc date))))

(defn -main
  [& args]
  (cond (zero? (count args)) [(doc)]
        (= (first args) "month") (cmd-days-in-month (rest args))
        (= (first args) "year") (cmd-days-in-year   (second args))
        (= (count args) 1) [(doc (first args))]))

(comment
  (date->month-name (js/Date.))
  (date->iso-string (js/Date.)))
