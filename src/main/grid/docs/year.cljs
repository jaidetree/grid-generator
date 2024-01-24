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
  ["🎊"
   "⛄"
   "💐"
   "🌧️"
   "😸"
   "😎"
   "🌭"
   "💼"
   "🤷"
   "🎃"
   "🎂"
   "🎄"])

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

(doseq [row (range 0 3)
        col (range 0 4)]
   (doc/add-child
     id
     [:rect
      {#_#_:strokeWidth "2px"
       #_#_:stroke border
       :fill (-> (color/get :dark-grape)
                 (color/saturate -10)
                 (color/brightness -2))
       :rx "5px"
       :ry "5px"
       :x (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16)))))
       :y (doc/px (+ (* 6 16) (* row (+ rect-height (* 2 16)))))
       :width (doc/px rect-width)
       :height (doc/px rect-height)}]))



(doc/add-child id (presets/subgrid-layer id))
(doc/add-child id (presets/basegrid-layer id))
(doc/add-child id (presets/dots-layer id))

(doseq [row (range 0 3)
        col (range 0 4)]
  (let [idx (+ col (* row 4))]
   (doc/add-child
     id
     [:g
      {:key idx}
      [:rect
       {:strokeWidth "2px"
        :stroke border
        :fill "none"
        :rx "5px"
        :ry "5px"
        :x (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16)))))
        :y (doc/px (+ (* 6 16) (* row (+ rect-height (* 2 16)))))
        :width (doc/px rect-width)
        :height (doc/px rect-height)}]

      [:line
       {:strokeWidth "2px"
        :stroke      border
        :x1 (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16)))))
        :x2 (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16))) rect-width))
        :y1 (doc/px (+ (* 9 16) (* row (+ rect-height (* 2 16)))))
        :y2 (doc/px (+ (* 9 16) (* row (+ rect-height (* 2 16)))))}]

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

      [:circle
       {:cx (doc/px (+ (* 2.5 16) (* col (+ rect-width (* 2 16)))))
        :cy (doc/px (+ (* 7.5 16) (* row (+ rect-height (* 2 16)))))
        :r "28px"
        :stroke border
        :stroke-width "2px"
        :fill (color/get :dark-grape)}]

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
