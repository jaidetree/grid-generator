(ns grid.docs.week
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
(def x-start  (* 2 16))
(def y-start  (* 6 16))

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

(defn format-month-name
  [date]
  (.toLocaleDateString date "en-US" #js {:month "long"}))

(defn format-week-filename
  [date]
  (s/join "-" [(.getFullYear date)
               (inc (.getMonth date))
               (.getDate date)]))

(defn parse-args
  [[start end]]
  (if (and start end)
    [start end]
    (let [[start end] (date/date->week (js/Date.))]
      [start end])))

(defn format-week-range
 [start end]
 (let [year-start (.getFullYear start)
       year-end (.getFullYear end)
       years-change (not= year-start year-end)]
   (str (date/date->month-day start)
        (when years-change (str ", " year-start))
        " – "
        (date/date->month-day end)
        (when years-change (str ", " year-end)))))

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
  [{:keys [rect-width rect-height]}]
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
            :height rect-height
            :fill weekend-bg}
           (cond (zero? col) {:r [10 0 0 0]}
                 (= col 6)   {:r [0 10 0 0]}
                 :else       {:r 0
                              :fill month-bg}))]))])

(defn column-dividers
  [{:keys [rect-height rect-width]}]
  [:g
   (for [col (range 1 7)]
    (let [x (+ x-start (* col (+ rect-width x-gutter)))]
      [:line
       {:key col
        :stroke border
        :x1 x
        :y1 (+ y-start 0)
        :x2 x
        :y2 (+ y-start rect-height)}]))])

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
           :y (+ y-start (* 2.25 16))
           :font-family "OperatorMono Nerd Font"
           :font-size "18px"
           :font-style "italic"
           :fill (:color weekday)
           :text-anchor "start"}
          (:title weekday)]))])

(defn day-date
  [{:keys [rect-width day col]}]
  (let [label (date/date->month-day day)
        m (.getMonth day)
        d (.getDate day)]
    [:g
     {:key col}
     [:text
      {:x (+ x-start (* col (+ rect-width x-gutter))
             (* 1 8))
       :y (+ y-start (* 1 16))
       :font-family "OperatorMono Nerd Font"
       :font-size   "14px"
       :font-style  "italic"
       :fill        (-> (color/get :dark-grape)
                        (color/saturate -10)
                        (color/brightness 30))
       :text-anchor "start"}
      (str label
           (when (and (= m 0) (= d 1))
             (str ", " (.getFullYear day))))]]))

(defn date-box
  [{:keys [rect-width rect-height idx col row]}]
  [:use
   {:href (if (= idx 6) "#date-box-tr" "#date-box")
    :x (+ x-start
          (* col (+ rect-width x-gutter))
          (- rect-width (* 3 16)))
    :y (+ y-start
          (* row rect-height))}])

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
             (* row rect-height)
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
       (str (.getDate day))]]))

(defn day-labels
  [{:keys [start] :as props}]
  [:g
   (for [col (range 0 7)]
    (let [day (date/add start :days col)
          props (merge props
                       {:idx col
                        :day day
                        :col col
                        :row 0})]
      [:g
        {:key col}
        [date-box props]
        [date-circle props]
        [day-date props]]))])

(defn calendar-border
  [{:keys [rect-width rect-height]}]
  [:g#calendar-border
    [:line
      {:id "header-border"
       :stroke border
       :strokeWidth 2
       :fill "none"
       :x1 x-start
       :y1 (+ y-start (* 3 16))
       :x2 (+ x-start (* 7 rect-width))
       :y2 (+ y-start (* 3 16))}]
    [:rect
      {:x x-start
       :y y-start
       :width (* 7 rect-width)
       :height rect-height
       :rx "10"
       :ry "10"
       :strokeWidth "2px"
       :stroke border
       :fill "none"}]])

(defn create-props
  [{:keys [start end]}]
  (let [start-m (.getMonth start)
        end-m (.getMonth end)
        month-name (if (and (= start-m 11) (= end-m 0))
                     "January"
                     (format-month-name start))]

    {:width         width
     :height        height
     :padding       10
     :rect-width    (svg/calc-grid-size
                     {:total  width
                      :items  7
                      :gutter x-gutter
                      :start  x-start})
     :rect-height   (- height y-start (* 1 16))
     :start         start
     :end           end
     :month-name    month-name}))

(defn doc
  [& [start end]]
  (let [[start end] (parse-args [start end])
        {:keys [month-name] :as props} (create-props
                                        {:start start
                                         :end   end})
        title (format-week-range start end)]

    {:file     (str "journal/" month-name "/Week " (format-week-filename start))
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
                 [calendar-border        props]
                 [column-labels          props]
                 [day-labels             props]]
                [presets/outline-layer   props]
                [presets/title-layer     props title]]}))

(defn cmd-current-week
  []
  [(doc)])

(defn cmd-weeks
  [dates]
  (for [date dates]
    (let [date (js/Date. date)
          [start end] (date/date->week date)]
      #_[year (.toLocaleDateString start) (.toLocaleDateString end)]
      (doc start end))))

(defn cmd-full-month
  [[year month]]
  (let [date (js/Date. year month 1)
        first-weekday (date/date->first-weekday-of-month date)
        date          (date/add date :days (* -1 first-weekday))
        days-in-month (date/date->days-in-month date)
        weeks (-> days-in-month
                  (+ first-weekday)
                  (/ 7)
                  (js/Math.ceil))]
    (for [i (range 0 weeks)]
      (let [start (date/add date :days (* i 7))
            end (date/add start :days 6)]
        #_[(.getFullYear start) (.toLocaleDateString start) (.toLocaleDateString end)]
        (doc start end)))))

(defn cmd-full-year
  [year]
  (let [date (js/Date. year 0 1)]
    (for [i (range 0 52)]
      (let [start (js/Date. (.getFullYear date)
                            (.getMonth date)
                            (* i 7))
            end (date/add start :days 6)]
          #_[(.toLocaleDateString start) (.toLocaleDateString end)]
          (doc start end)))))

(defn full-month?
  [[year month day]]
  (and (js/Number.isInteger (js/Number year))
       (js/Number.isInteger (js/Number month))
       (= day "all")))

(defn full-year?
  [[year month]]
  (and (js/Number.isInteger (js/Number year))
       (= month "all")))

(defn -main
  [& args]
  (cond (empty? args)      (cmd-current-week)
        (full-month? args) (cmd-full-month (take 2 args))
        (full-year? args)  (cmd-full-year  (take 2 args))
        :else              (cmd-weeks args)))

(comment
  (take 2 (cmd-full-year 2024))
  (take 2 (cmd-full-month [2024 0]))
  (cmd-weeks ["2024-01-01"])

  ;; Not as easy as adding 32 per month
  (for [i (range 0 11)]
    (let [date (js/Date. 2024 i)]
      (.setDate date (* i 32))
      (.toLocaleString date)))

  (->> (range 0 52)
       (reduce
         (fn [date i]
           (let [offset (.getDay date)]
             (.setDate date (- (.getDate date) offset)))
           (let [end (js/Date. date)]
             (.setDate end (+ 6 (.getDate date)))
             (println (.toLocaleDateString date) "-" (.toLocaleDateString end)))
           (.setDate date (+ 7 (.getDate date)))
           date)
         (js/Date. 2024 0 1))
       (dorun))

  (let [date (js/Date. 2024 0 1)]
    (for [i (range 0 52)]
      (let [start (js/Date. (.getFullYear date) (.getMonth date) (* i 7))
            end (js/Date. start)]
        (.setDate end (+ 6 (.getDate end)))
        (println (.toLocaleDateString start) (.toLocaleDateString end))
        [start end])))

  (get-in [1 2 3] [2]))
