(ns grid.docs.calendar
  (:require
    [clojure.string :as s]
    [grid.presets :as presets]
    [grid.color :as color]
    [grid.constants :refer [weekdays]]
    [grid.svg :as svg]
    [grid.utils.date :as date]))

(def width (svg/in->px (* 2 8.5)))
(def height (svg/in->px 11))

(def x-gutter (* 0 16))
(def y-gutter (* 0 16))
(def x-start  (* 2 16))
(def y-start  (* 5 16))

(def border
  (-> (color/get :outline)
      (color/saturate -10)
      (color/brightness 10)))

(def month-bg
  (-> (color/get :dark-grape)
      (color/saturate -10)
      (color/brightness -3)))

(def weekend-bg
  (-> month-bg
      (color/saturate -15)
      (color/brightness 4)))

(def date-box-bg
  (-> (color/get :dark-grape)
      (color/brightness 5)))

(defn create-date
  [& [year month]]
  (if (and year month)
    (js/Date. year month)
    (js/Date.)))

(defn date-box-symbol
  []
  [:symbol
   {:id "date-box"}
   [svg/rounded-rect
    {:r [0 0 10 0]
     :x 0
     :y 0
     :fill date-box-bg
     :stroke border
     :width (* 3 16)
     :height (* 2 16)}]])

(defn date-box-tr-symbol
  []
  [:symbol
   {:id "date-box-tr"}
   [svg/rounded-rect
    {:r [0 10 10 0]
     :x 0
     :y 0
     :fill date-box-bg
     :stroke border
     :width (* 3 16)
     :height (* 2 16)}]])

(defn fill-weekday-columns
  [{:keys [weeks rect-width rect-height]}]
  [:g
   (for [col (range 0 7)]
     (let [x (+ x-start (* col (+ rect-width x-gutter)))
           y y-start]
       [svg/rounded-rect
         (merge
           {:key col
            :x x
            :y y
            :width rect-width
            :height (* weeks rect-height)
            :fill weekend-bg}
           (cond (zero? col) {:r [10 0 0 0]}
                 (= col 6)   {:r [0 10 0 0]}
                 :else       {:r 0
                              :fill month-bg}))]))])

(defn column-dividers
  [{:keys [weeks rect-height rect-width]}]
  [:g
   (for [col (range 1 7)]
    (let [x (+ x-start (* col (+ rect-width x-gutter)))]
      [:line
       {:key col
        :stroke border
        :x1 x
        :y1 (+ y-start
               (if (= col 0) 10 0))
        :x2 x
        :y2 (+ y-start (* weeks rect-height))}]))])

(defn row-dividers
  [{:keys [rect-width rect-height weeks]}]
  [:g
   (for [row (range 1 weeks)]
     (let [y (+ y-start (* row (+ rect-height y-gutter)))]
       [:line
        {:key row
         :stroke border
         :x1     x-start
         :y1     y
         :x2     (+ x-start (* 7 rect-width))
         :y2     y}]))])

(defn column-labels
  [{:keys [rect-width]}]
  [:g
    (for [col (range 0 7)]
      (let [weekday (nth weekdays col)]
        [:text
          {:key col
           :x (+ x-start
                 (* col (+ rect-width x-gutter))
                 8)
           :y (+ y-start
                 (* 1.5 16))
           :font-family "OperatorMono Nerd Font"
           :font-size "18px"
           :font-style "italic"
           :fill (:color weekday)
           :text-anchor "start"}
          (:title weekday)]))])

(defn date-weekdays
  [{:keys [rect-width rect-height weeks]}]
  [:g
   (for [row (range 1 weeks)
         col (range 0 7)]
    (let [weekday (nth weekdays col)]
     [:g
      {:key (str row "-" col)}
      [:text
       {:x (+ x-start (* col (+ rect-width x-gutter))
              (* 1 8))
        :y (+ y-start (* row (+ rect-height y-gutter))
              (* 2.5 8))
        :font-family "OperatorMono Nerd Font"
        :font-size   "18px"
        :font-style  "italic"
        :fill        (-> (color/get :dark-grape)
                         (color/saturate -4)
                         (color/brightness 25))
        :text-anchor "start"}
       (str (subs (:title weekday) 0 3) " • W" (inc row))]]))])

(defn date-box
  [{:keys [rect-width rect-height idx col row]}]
  [:use
   {:href (if (= idx 6) "#date-box-tr" "#date-box")
    :x (+ x-start
          (* col (+ rect-width x-gutter))
          (- rect-width (* 3 16)))
    :y (+ y-start
          (* row (+ rect-height y-gutter)))}])

(defn date-sticker
  [{:keys [x y fill d transform]}]
  (let [r 20
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

(defn date-circle
  [{:keys [rect-width rect-height col row day]}]
  (let [weekday (nth weekdays col)
        x-offset (- (rand 24) 12)
        y-offset (- (rand 16) 8)
        x (+ x-start
             (* col (+ rect-width x-gutter))
             (- rect-width 32)
             x-offset)
        y (+ y-start
             (* row (+ rect-height y-gutter))
             (* 1 16)
             y-offset)
        tx (+ x 16)
        ty (+ y 16)]
    [:g
     {:transform (svg/transform
                  (svg/rotate (- (rand 30) 15) tx ty)
                  #_(svg/translate tx ty)
                  #_(svg/scale (+ 1 (rand 0.25)))
                  #_(svg/translate (* -1 tx) (* -1 ty)))}
     [date-sticker
       {:transform (svg/transform
                    (svg/translate -16 -16)
                    (svg/rotate (rand -90) tx ty)
                    (svg/translate 16 16))
        :d (+ 24 (rand 8))
        :x x
        :y y
        :fill (:color weekday)}]
     [:text
       {:x (svg/px x)
        :y (svg/px (+ 4 y))
        :font-family "OperatorMono Nerd Font"
        :font-size   "16px"
        :font-style  "italic"
        :fill        (color/get :dark-grape)
        :text-anchor "middle"}
       (str day)]]))

(defn day-labels
  [{:keys [weeks first-weekday days-in-month] :as props}]
  [:g
   (for [row (range 0 weeks)
         col (range 0 7)]
    (let [idx (+ (* row 7) col)
          day (- (inc idx) first-weekday)
          props (merge props
                       {:idx idx
                        :day day
                        :col col
                        :row row})]

      (when (and (< 0 day)
                 (<= day days-in-month))
        [:g
         {:key idx}
         [date-box props]
         [date-circle props]])))])

(defn calendar-border
  [{:keys [weeks rect-width rect-height]}]
  [:rect
   {:x x-start
    :y y-start
    :width (* 7 rect-width)
    :height (* weeks rect-height)
    :rx "10"
    :ry "10"
    :strokeWidth "2px"
    :stroke border
    :fill "none"}])

(comment
  (date/date->first-weekday-of-month (js/Date.)))

(defn create-props
  [{:keys [year month]}]
  (let [date (create-date year month)
        first-weekday (date/date->first-weekday-of-month date)
        days-in-month (date/date->days-in-month date)
        month-name (date/date->month-name date)
        weeks (-> days-in-month
                  (+ first-weekday)
                  (/ 7)
                  (js/Math.ceil))]
    {:width         width
     :height        height
     :year          (.getFullYear date)
     :padding       10
     :rect-width    (svg/calc-grid-size
                     {:total  width
                      :items  7
                      :gutter x-gutter
                      :start  x-start})
     :rect-height   (svg/calc-grid-size
                      {:total  height
                       :items  weeks
                       :gutter y-gutter
                       :start  y-start})
     :date          date
     :first-weekday first-weekday
     :days-in-month days-in-month
     :month-name    month-name
     :weeks         weeks}))

(defn doc
  [& [year month]]
  (let [{:keys [month-name year] :as props} (create-props {:year year
                                                           :month month})
        title (str month-name " " year)]

    {:file     (str "journal/" month-name "/" month-name " Calendar")
     :props    props
     :defs     [[date-box-symbol]
                [date-box-tr-symbol]]
     :children [[fill-weekday-columns    props]
                [:g
                 [presets/subgrid-layer  props]
                 [presets/basegrid-layer props]
                 [presets/dots-layer     props]]
                [:g
                 [column-dividers        props]
                 [row-dividers           props]
                 [calendar-border        props]
                 [column-labels          props]
                 [date-weekdays          props]
                 [day-labels             props]]
                [presets/outline-layer   props]
                [presets/title-layer     props title]]}))

(defn cmd-current-month
  []
  [(doc)])

(defn cmd-months
  [year-month-pairs]
  (->> year-month-pairs
       (map js/Number)
       (partition 2)
       (map (fn [[year month]]
              (doc year (dec month))))))

(defn cmd-full-year
  [year]
  (for [month (range 0 11)]
     (map #(doc year month))))

(defn -main
  [& args]
  (cond (empty? args)           (cmd-current-month)
        (= (second args) "all") (cmd-full-year (first args))
        :else                   (cmd-months args)))

