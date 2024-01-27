(ns grid.docs.calendar
  (:require
    [grid.api :as doc]
    [grid.presets :as presets]
    [grid.color :as color]
    [grid.svg :as svg]))

(def width (doc/in->px (* 2 8.5)))
(def height (doc/in->px 11))

(defn create-date
  [& [year month]]
  (js/Date. year month))

(defn get-first-day-of-week
  [date]
  (doto (js/Date. date)
    (.setDate 1))
  (.getDay date))

(defn get-days-in-month
  [date]
  (-> (doto (js/Date. date)
        (.setMonth (inc (.getMonth date)) 1)
        (.setDate  (dec (.getDate date))))
      (.getDate date)))

(def date (create-date))
(def first-weekday (get-first-day-of-week date))
(def days-in-month (get-days-in-month date))

(def month-name (.toLocaleDateString date "en-US" #js {:month "long"}))

(def weeks (-> (+ first-weekday days-in-month)
               (/ 7)
               (js/Math.ceil)))

(def id (doc/create
         {:id :calendar
          :title (str month-name " Calendar")
          :out-dir "journal"
          :width width
          :height height}))

(doc/add-pattern id (presets/basegrid-pattern))
(doc/add-pattern id (presets/subgrid-pattern))
(doc/add-pattern id (presets/dots-pattern))

(defn quantize
  [increment value]
  (* (js/Math.round (/ value increment))
     increment))

(def x-gutter 0)
(def y-gutter 0)
(def x-start  2)
(def y-start  5)

(defn calc-grid-size
 [{:keys [total items start gutter] :or {start 1}}]
 (let [gutter-px (* gutter 16)]
   (quantize
     16
     (-> (- total (* start 16))
         (- gutter-px)
         (/ items)
         (- gutter-px)))))

(def rect-width (calc-grid-size
                  {:total     width
                   :items     7
                   :gutter    x-gutter}))

(def rect-height (calc-grid-size
                   {:total    height
                    :items    weeks
                    :gutter   y-gutter
                    :start    y-start}))

(def border
  (-> (color/get :outline)
      (color/saturate -10)
      (color/brightness 10)))

(def month-bg
  (-> (color/get :dark-grape)
      (color/saturate -10)
      (color/brightness -3)))

(def circle-bg
  (color/get :pink))

(def weekend-bg
  (-> month-bg
      (color/saturate -15)
      (color/brightness 2)))



(doseq [col (range 0 7)]
  (let [x (+ (* x-start 16) (* col (+ rect-width (* x-gutter 16))))
        y (* 16 y-start)]
    (doc/add-child
      id
      [svg/rounded-rect
       (merge
         {:x x
          :y y
          :width rect-width
          :height (+ (* y-start 16)
                     (* weeks rect-height))
          :fill weekend-bg}
         (cond (zero? col) {:r [10 0 0 0]}
               (= col 6)   {:r [0 10 0 0]}
               :else       {:r 0
                            :fill month-bg}))])))

(doc/add-child id (presets/subgrid-layer id))
(doc/add-child id (presets/basegrid-layer id))
(doc/add-child id (presets/dots-layer id))

(def box-fill (-> (color/get :dark-grape)
                  (color/brightness 5)))


(def weekdays ["Sunday"
               "Monday"
               "Tuesday"
               "Wednesday"
               "Thursday"
               "Friday"
               "Saturday"])


(doseq [col (range 1 7)]
 (let [x (+ (* x-start 16) (* col (+ rect-width (* x-gutter 16))))]
   (doc/add-child
     id
     [:line
       {:stroke border
        :x1 x
        :y1 (+ (* 16 y-start)
               (if (= col 0) 10 0))
        :x2 x
        :y2 (+ (* y-start 16) (* weeks rect-height))}])))


(doseq [col (range 0 7)]
  (doc/add-child
    id
    [:text
     {:x (+ (* x-start 16)
            (* col (+ rect-width (* x-gutter 16)))
            #_(/ rect-width 2)
            8)
      :y (+ (* 16 y-start)
            (* 1.5 16))
      :font-family "OperatorMono Nerd Font"
      :font-size "18px"
      :font-style "italic"
      :fill (color/get :pink)
      :text-anchor "start"}
     (nth weekdays col)]))

(doseq [row (range 1 weeks)]
 (let [y (+ (* y-start 16) (* row (+ rect-height (* y-gutter 16))))]
   (doc/add-child
     id
     [:line
      {:stroke border
       :x1 (* 16 x-start)
       :y1 y
       :x2 (+ (* x-start 16) (* 7 rect-width))
       :y2 y}])))

(doc/add-child
  id
  [:rect
   {:x (* x-start 16)
    :y (* y-start 16)
    :width (* 7 rect-width)
    :height (* weeks rect-height)
    :rx "10"
    :ry "10"
    :strokeWidth "2px"
    :stroke border
    :fill "none"}])

(doc/add-pattern
  id
  [:<>
   [:g
    {:id "date-circle"}
    [:circle
     {:strokeWidth "1px"
      :fill (color/get :teal)
      :r (* 1 16)
      :cx 0
      :cy 0}]]
   [:g
    {:id "date-box"}
    [svg/rounded-rect
     {:r [0 0 10 0]
      :x 0
      :y 0
      :fill box-fill
      :stroke border
      :width (* 3 16)
      :height (* 2 16)}]]
   [:g
    {:id "date-box-tr"}
    [svg/rounded-rect
     {:r [0 10 10 0]
      :x 0
      :y 0
      :fill box-fill
      :stroke border
      :width (* 3 16)
      :height (* 2 16)}]]])

(doseq [row (range 0 weeks)
        col (range 0 7)]
   (let [idx (+ (* row 7) col)
         day (- (inc idx) first-weekday)
         x-offset (- (rand 10) 5)
         y-offset (- (rand 10) 5)
         x (+ (* x-start 16)
              (* col (+ rect-width (* x-gutter 16)))
              (- rect-width 16)
              x-offset)
         y (+ (* y-start 16)
              (* row (+ rect-height (* y-gutter 16)))
              (* 1 16)
              y-offset)
         tx (+ x 16)
         ty (+ y 16)]

     (when (and (< 0 day)
                (<= day days-in-month))
       (doc/add-child
         id
         [:g
           {:key idx}
           [:use
            {:href (if (= idx 6) "#date-box-tr" "#date-box")
             :x (+ (* x-start 16)
                   (* col (+ rect-width (* x-gutter 16)))
                   (- rect-width (* 3 16)))
             :y (+ (* y-start 16)
                   (* row (+ rect-height (* y-gutter 16))))}]
           [:g
            {:transform (str "rotate(" (- (rand 40) 20) " " tx " " ty  ") "
                             "translate(" tx " " ty ") "
                             "scale(" (+ 1 (rand 0.25)) ") "
                             "translate(-" tx " -" ty ")")}
            [:use
              {:href "#date-circle"
               :x (doc/px x)
               :y (doc/px y)}]

            [:text
              {:x (doc/px x)
               :y (doc/px (+ 4 y))
               :font-family "OperatorMono Nerd Font"
               :font-size   "14px"
               :font-style  "italic"
               :fill        (color/get :dark-grape)
               :text-anchor "middle"}
              (str day)]]]))))

(comment
  (for [row (range 0 3)
        col (range 0 4)]
    (+ (* row 4)
       col)))

(doc/add-child id (presets/outline-layer id))

(doc/add-child
  id
  [:text
   {:x (/ width 2)
    :y 48
    :font-family "OperatorMono Nerd Font"
    :font-size "30px"
    :fill (color/get :pink)
    :text-anchor "middle"}
   (str month-name " 2024")])

(doc/add-child
  id
  [:line
   {:x1 10
    :y1 (+ 48 16)
    :x2 (- width 10)
    :y2 (+ 48 16)
    :stroke (color/get :bone)
    :strokeWidth "1px"}])

(comment
  [width height]
  [(- rect-width (* 2 16)) rect-height])
