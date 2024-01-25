(ns grid.docs.year
  (:require
    [grid.api :as doc]
    [grid.presets :as presets]
    [grid.color :as color]))

(def id (doc/create
          {:id :year
           :title "Year 2024"
           :out-dir "journal"
           :width (doc/in->px (* 2 8.5))
           :height (doc/in->px 11)}))

(doc/add-pattern id (presets/basegrid-pattern))
(doc/add-pattern id (presets/subgrid-pattern))
(doc/add-pattern id (presets/dots-pattern))

(def months
  ["Jan"
   "Feb"
   "Mar"
   "Apr"
   "May"
   "Jun"
   "Jul"
   "Aug"
   "Sep"
   "Oct"
   "Nov"
   "Dec"])

(def emojis
  ["🍾"
   "⛄"
   "💐"
   "🌧️"
   "😸"
   "😎"
   "🌭"
   "💼"
   "🏖️"
   "🎃"
   "🎂"
   "🎊"])

(defn quantize
  [increment value]
  (* (js/Math.round (/ value increment))
     increment))

(defn calc-rect-width
 [doc-width]
 (- (/ (- doc-width
          (* 2 16))
       4)
    (* 2 16)))

(defn calc-rect-height
  [doc-height]
  (quantize
    16
    (- (/ (- doc-height
             (* 2 16))
          3)
       (* 3 16))))

(def doc (doc/get-doc id))

(def width (:width doc))
(def height (:height doc))

(def rect-width (calc-rect-width width))
(def rect-height (calc-rect-height height))

(def border
  (-> (color/get :outline)
      (color/saturate -10)
      (color/brightness 10)))

(def month-bg
  (-> (color/get :dark-grape)
      (color/saturate -5)
      (color/brightness 5)))

(def circle-bg
  #_(-> (color/get :dots)
       (color/saturate -10)
       (color/brightness -10))
  (color/get :pink))

(doseq [i (range 0 35)]
  (let [x #_(+ (/ width 2)
               (- (rand-int 600) 300))
          (rand-int width)
        y #_(+ (/ width 2)
              (- (rand-int 600) 300))
        (+ (rand-int (- height (* 16 6)))
           (* 16 6))
        r (rand-int 40)]
    (doc/add-child
      id
      [:circle
       {:cx (doc/px x)
        :cy (doc/px y)
        :r  (doc/px r)
        :fill circle-bg}])))

(doc/add-child
  id
  [:path
   {:d "M207.81,864.662C207.81,864.662 380.878,401.436 782.728,607.848C1188.92,816.491 1411.69,347.644 1411.69,347.644"
    :fill "none"
    :stroke (color/get :teal)
    :strokeLinecap "round"
    :strokeWidth "64px"}])

(doseq [i (range 0 35)]
  (let [x #_(+ (/ width 2)
               (- (rand-int 600) 300))
          (rand-int width)
        y #_(+ (/ width 2)
              (- (rand-int 600) 300))
        (+ (rand-int (- height (* 16 6)))
           (* 16 6))
        r (rand-int 40)]
    (doc/add-child
      id
      [:circle
       {:cx (doc/px x)
        :cy (doc/px y)
        :r  (doc/px r)
        :fill circle-bg}])))


(doseq [row (range 0 3)
        col (range 0 4)]
   (doc/add-child
     id
     [:rect
      {:fill month-bg
       :rx "5px"
       :ry "5px"
       :x (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16)))))
       :y (doc/px (+ (* 6 16) (* row (+ rect-height (* 2 16)))))
       :width (doc/px rect-width)
       :height (doc/px rect-height)}]))


(doc/add-child id (presets/subgrid-layer id))
(doc/add-child id (presets/basegrid-layer id))
(doc/add-child id (presets/dots-layer id))

(doc/add-pattern
  id
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
     :width (doc/px rect-width)
     :height (doc/px rect-height)}]

   [:line
    {:strokeWidth "2px"
     :stroke      border
     :x1 "0px"
     :y1 (doc/px (* 16 3))
     :x2 rect-width
     :y2 (doc/px (* 16 3))}]

   [:circle
    {:cx (doc/px 8)
     :cy (doc/px 24)
     :r "28px"
     :stroke border
     :stroke-width "2px"
     :fill month-bg}]])

(doseq [row (range 0 3)
        col (range 0 4)]
  (let [idx (+ col (* row 4))]
   (doc/add-child
     id
     [:g
      {:key idx}
      [:use
       {:href "#month"
        :x (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16)))))
        :y (doc/px (+ (* 6 16) (* row (+ rect-height (* 2 16)))))}]

      [:text
       {:x           (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16)))
                                (/ rect-width 2)))
        :y           (doc/px (+ (* 8 16) (* row (+ rect-height (* 2 16)))))
        :font-family "OperatorMono Nerd Font"
        :font-size   "24px"
        :font-style  "italic"
        :fill        (color/get :teal)
        :text-anchor "middle"}
       (str (nth months idx))]

      [:text
       {:x           (doc/px (+ (* 2.5 16) (* col (+ rect-width (* 2 16)))))
        :y           (doc/px (+ (* 8.5 16) (* row (+ rect-height (* 2 16)))))
        :font-family "OperatorMono Nerd Font"
        :font-size   "32px"
        :fill        (color/get :teal)
        :text-anchor "middle"}
       (str (nth emojis idx))]])))

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
   "Year 2024"])

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
  [width height])
