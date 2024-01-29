(ns grid.docs.projects
  (:require
    [grid.presets :as presets]
    [grid.color :as color]
    [grid.svg :as svg]))

(def x-start (* 2 16))
(def x-gutter (* 2 16))
(def y-start (* 6 16))
;; Since the columns extend to the bottom of the page, no need for y-gutter

(def states
  [{:title "To Do"
    :emoji "🏁"
    :icon  "pen-nib"
    :hue   -10}
   {:title "In Progress"
    :emoji "🏗️"
    :icon  "cogs"
    :hue   60}
   {:title "Complete"
    :emoji "✅"
    :icon  "glass-cheers"
    :hue   80}
   {:title "Archived"
    :emoji "🗑️"
    :icon  "skull-crossbones"
    :hue   10}])

(def swoosh-colors
  (cycle [(color/get :pink)
          (color/get :teal)]))

(def border
  (-> (color/get :outline)
      (color/saturate -10)
      (color/brightness 10)))

(defn project-state-box-symbol
  [{:keys [rect-width rect-height]}]
  [:g
   {:id "project-state-box"}
   [:rect
    {:strokeWidth "2px"
     :stroke border
     :fill "none"
     :rx "10px"
     :ry "10px"
     :x "0px"
     :y "0px"
     :width (svg/px rect-width)
     :height (svg/px rect-height)}]

   [:line
    {:strokeWidth "2px"
     :stroke      border
     :x1 "0px"
     :y1 (svg/px (* 16 3))
     :x2 rect-width
     :y2 (svg/px (* 16 3))}]])

(defn swooshes
  [{:keys [width height]}]
  [:g
    (for [[idx stroke] (map-indexed vector (take 12 swoosh-colors))]
     (let [x -16
           y (+ (rand-int (- (/ height 2) (* 16 20)))
                (* 16 20))
           x2 (+ width 24)]
       [svg/swoosh
         {:key idx
          :curve [x y 400 800 400 -400 x2 y]
          :fill "none"
          :strokeWidth (svg/px (+ 2 (svg/quantize 8 (rand-int 32))))
          :strokeLinecap "round"
          :stroke stroke}]))])

(defn project-state-fills
  [{:keys [rect-width rect-height]}]
  [:g
    (for [col (range 0 4)]
      (let [state (nth states col)]
        [:g
         {:key col}
         [:rect
          {:fill (-> (color/get :dark-grape)
                     (color/hue (:hue state))
                     (color/saturate -10)
                     (color/brightness 5))
           :rx "10px"
           :ry "10px"
           :x (svg/px (+ x-start (* col (+ rect-width x-gutter))))
           :y (svg/px y-start)
           :width (svg/px rect-width)
           :height (svg/px rect-height)}]
         [:text
          {:x           (svg/px (+ x-start (* col (+ rect-width x-gutter))
                                   (- rect-width 16)))
           :y           (svg/px (* 15 16))
           :font-family "FontAwesome 5 Pro"
           :font-size   (svg/px (* 16 10))
           :fill        (-> (color/get :dark-grape)
                            (color/hue (:hue state))
                            (color/saturate 0)
                            (color/brightness 9))
           :text-anchor "end"}
          (str (:icon state))]]))])

(defn project-emoji
  [{:keys [rect-width col state]}]
  [:text
   {:x           (svg/px (+ x-start (* col (+ rect-width x-gutter))
                            (* 2 16)))
    :y           (svg/px (+ y-start
                            (* 2 16)
                            2))
    :font-family "OperatorMono Nerd Font"
    :font-size   "24px"
    :font-style  "italic"
    :text-anchor "middle"
    :fill (-> (color/get :dark-grape)
              (color/hue (:hue state))
              (color/saturate -10)
              (color/brightness 5))}

   (str (:emoji state))])

(defn project-label
  [{:keys [rect-width col state]}]
  [:text
   {:x           (svg/px (+ x-start (* col (+ rect-width x-gutter))
                            (/ rect-width 2)))
    :y           (svg/px (+ y-start (* 2 16)))
    :font-family "OperatorMono Nerd Font"
    :font-size   "24px"
    :font-style  "italic"
    :fill        (-> (color/get :dark-grape)
                     (color/hue (:hue state))
                     (color/saturate 100)
                     (color/brightness 60))
    :text-anchor "middle"}
   (str (:title state))])

(defn project-state-boxes
  [{:keys [rect-width] :as props}]
  [:g
   (for [col (range 0 4)]
     (let [state (nth states col)
           props (assoc props
                        :col   col
                        :state state)]
       [:g
        {:key col}
        [:use
         {:href "#project-state-box"
          :x (svg/px (+ x-start (* col (+ rect-width x-gutter))))
          :y (svg/px y-start)}]
        [project-emoji props]
        [project-label props]]))])

(defn create-props
  []
  (let [width (svg/in->px (* 2 8.5))
        height (svg/in->px 11)]
    {:width       width
     :height      height
     :rect-width  (svg/calc-grid-size
                    {:items (count states)
                     :total width
                     :start x-start
                     :gutter x-gutter})
     :rect-height (svg/quantize
                    16
                    (- height (* 7 16)))}))

(defn doc
  []
  (let [props (create-props)]
    {:file "journal/Projects"
     :props props
     :defs [[presets/basegrid-pattern]
            [presets/subgrid-pattern]
            [presets/dots-pattern]
            [project-state-box-symbol    props]]
     :children [[:g
                 [swooshes               props]
                 [project-state-fills    props]]
                [:g
                 [presets/subgrid-layer  props]
                 [presets/basegrid-layer props]
                 [presets/dots-layer     props]
                 [presets/outline-layer  props]
                 [project-state-boxes     props]
                 [presets/title-layer    props "Projects"]]]}))

(defn -main
  [& _args]
  (list (doc)))
