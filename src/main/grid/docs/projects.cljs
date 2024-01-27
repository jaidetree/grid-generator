(ns grid.docs.projects
  (:require
    [grid.api :as doc]
    [grid.presets :as presets]
    [grid.color :as color]))

(def id (doc/create
          {:id :projects
           :title "Projects 2024"
           :out-dir "journal"
           :width (doc/in->px (* 2 8.5))
           :height (doc/in->px 11)}))

(doc/add-pattern id (presets/basegrid-pattern))
(doc/add-pattern id (presets/subgrid-pattern))
(doc/add-pattern id (presets/dots-pattern))

(def project-states
  ["To Do"
   "In Progress"
   "Complete"
   "Archived"])

(def emojis
  ["🏁"
   "🏗️"
   "✅"
   "🗑️"])

(defn quantize
  [increment value]
  (* (js/Math.round (/ value increment))
     increment))

(defn calc-rect-width
 [doc-width]
 (quantize
   16
   (- (/ (- doc-width (* 2 16))
         4)
      (* 2 16))))

(defn calc-rect-height
  [doc-height]
  (quantize
    16
    (- doc-height
       (* 7 16))))

(def doc (doc/get-doc id))

(def width (:width doc))
(def height (:height doc))

(def rect-width (calc-rect-width width))
(def rect-height (calc-rect-height height))

(def border
  (-> (color/get :outline)
      (color/saturate -10)
      (color/brightness 10)))

#_(def month-bg
    (-> (color/get :dark-grape)
        (color/saturate -5)
        (color/brightness 5)))

(def circle-bg
  (color/get :pink))

#_(doseq [i (range 0 35)]
   (let [x (+ (rand-int (- height (* 16 6)))
              (* 16 6))
         y (+ (rand-int (- height (* 16 6)))
              (* 16 6))
         r (rand-int 40)]
     (doc/add-child
       id
       [:circle
        {:cx (doc/px x)
         :cy (doc/px y)
         :r  (doc/px r)
         :fill circle-bg}])))


#_(doc/add-child
    id
    [:path
     {:d "M207.81,864.662C207.81,864.662 380.878,401.436 782.728,607.848C1188.92,816.491 1411.69,347.644 1411.69,347.644"
      :fill "none"
      :stroke (color/get :teal)
      :strokeLinecap "round"
      :strokeWidth "64px"}])

#_(doseq [i (range 0 35)]
    (let [x (rand-int width)
          y (+ (rand-int (- height (* 16 6)))
               (* 16 6))
          r (rand-int 40)]
      (doc/add-child
        id
        [:circle
         {:cx (doc/px x)
          :cy (doc/px y)
          :r  (doc/px r)
          :fill circle-bg}])))

(def project-state-colors (cycle [(color/get :pink) (color/get :teal)]))
(doseq [stroke (take 12 project-state-colors)]
  (let [x #_(rand-int width) -16
        y (+ (rand-int (- (/ height 2) (* 16 20)))
             (* 16 20))
        d #_(quantize 16 (+ 300 (rand-int 1200))) (* 16 4)
        x1 x
        y1 y
        x2 (+ width 24)
        y2 (+ d y)]
    (doc/add-child
      id
      [:path
       {#_#_:x1 (doc/px x)
        #_#_:y1 (doc/px y)
        #_#_:x2 (doc/px #_(+ (* 8 d) x) #_(/ width 2) (+ width 24))
        #_#_:y2 (doc/px #_(+ d y) #_(+ 64 height) (+ d y)j$)
        :d (str "M" x1 "," y1 " "
                "c800,-400 100,100 "
                x2 "," y2)

        :fill "none"
        :strokeWidth (doc/px (+ 2 (quantize 8 (rand-int 32))))
        :strokeLinecap "round"
        :stroke stroke}])))

(def project-state-hues
  [-10
   60
   80
   10])

(def icons
  ["pen-nib"
   "cogs"
   "glass-cheers"
   "skull-crossbones"])

(doseq [col (range 0 4)]
  (doc/add-child
    id
    [:g
      [:rect
       {:fill (-> (color/get :dark-grape)
                  (color/hue (nth project-state-hues col))
                  (color/saturate -10)
                  (color/brightness 5))
        :rx "10px"
        :ry "10px"
        :x (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16)))))
        :y (doc/px (* 6 16))
        :width (doc/px rect-width)
        :height (doc/px rect-height)}]
      [:text
       {:x           (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16)))
                                (- rect-width
                                   (* 16))))
        :y           (doc/px (* 15 16))
        :font-family "FontAwesome 5 Pro"
        :font-size   (doc/px (* 16 10))
        :fill        (-> (color/get :dark-grape)
                         (color/hue (nth project-state-hues col))
                         (color/saturate 0)
                         (color/brightness 9))
        :text-anchor "end"}
       (str (nth icons col))]]))



(doc/add-child id (presets/subgrid-layer id))
(doc/add-child id (presets/basegrid-layer id))
(doc/add-child id (presets/dots-layer id))

(doc/add-pattern
  id
  [:g
   {:id "project-state"}
   [:rect
    {:strokeWidth "2px"
     :stroke border
     :fill "none"
     :rx "10px"
     :ry "10px"
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
     :y2 (doc/px (* 16 3))}]])

(doseq [col (range 0 4)]
  (doc/add-child
    id
    [:g
     {:key col}
     [:use
      {:href "#project-state"
       :x (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16)))))
       :y (doc/px (* 6 16))}]

     [:text
      {:x           (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16)))
                               #_(/ rect-width 2)
                               (* 2 16)))
       :y           (doc/px (+ (* 8 16)
                               2))
       :font-family "OperatorMono Nerd Font"
       :font-size   "24px"
       :font-style  "italic"
       :text-anchor "middle"
       :fill (-> (color/get :dark-grape)
                 (color/hue (nth project-state-colors col))
                 (color/saturate -10)
                 (color/brightness 5))}

      (str (nth emojis col))]
     [:text
      {:x           (doc/px (+ (* 2 16) (* col (+ rect-width (* 2 16)))
                               (/ rect-width 2)))
       :y           (doc/px (* 8 16))
       :font-family "OperatorMono Nerd Font"
       :font-size   "24px"
       :font-style  "italic"
       :fill        (-> (color/get :dark-grape)
                        (color/hue (nth project-state-colors col))
                        (color/saturate 100)
                        (color/brightness 60))
       :text-anchor "middle"}
      (str (nth project-states col))]]))

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
   "Projects"])

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
