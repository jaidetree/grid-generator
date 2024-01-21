(ns grid.generator
  (:require
    [reagent.core :as r]))

(def ppi 96)

(defn in->px
  [in]
  (* in ppi))

(defn px
  [x]
  (str x "px"))

(def sizes
  {:letter-portrait  {:size "Letter"
                      :orientation "Portrait"
                      :width (in->px 8.5)
                      :height (in->px 11)}
   :letter-landscape {:size "Letter"
                      :orientation "Landscape"
                      :width (in->px 11)
                      :height (in->px 8.5)}
   :spread           {:size "Spread"
                      :orientation "Portrait"
                      :width (in->px (* 8.5 2))
                      :height (in->px 11)}})

(defonce state (r/atom {:selected :spread}))

(defn selected-size
  []
  (let [{:keys [selected]} @state]
    (get sizes selected)))

(defn format-name
  [{:keys [size orientation]}]
  (str size " 2024 " orientation))

(defn calc
  [kw padding]
  (let [size (selected-size)
        value (get size kw)]
    (- value (* 2 padding))))

(defn svg
  []
  (let [{:keys [width height]} (selected-size)]
    [:svg
     {:width (px width) #_"8.5in"
      :height (px height) #_"11in"
      :xmlns "http://www.w3.org/2000/svg"
      :xmlnsXlink "http://www.w3.org/1999/xlink"
      :version "1.1"
      :viewBox (str "0 0 " width " " height)
      :on-click (fn [e]
                  (js/window.print (.-currentTarget e)))}
     [:defs
      [:pattern#dots
       {:patternUnits "userSpaceOnUse"
        :width "16px"
        :height "16px"}
       [:circle
        {:cx "8px"
         :cy "8px"
         :r "1.5px"
         :fill "#5C414F"}]]
      [:pattern#base-grid
        {:patternUnits "userSpaceOnUse"
         :width "16px"
         :height "16px"}
        [:line
         {:x1 "0"
          :y1 "0"
          :x2 "0"
          :y2 "16px"
          :stroke "#334544"
          :strokeWidth "1px"
          :fill "none"}]
        [:line
         {:x1 "0"
          :y1 "16px"
          :x2 "16px"
          :y2 "16px"
          :stroke "#334544"
          :strokeWidth "1px"
          :fill "none"}]]
      [:pattern#subgrid
       {:patternUnits "userSpaceOnUse"
        :width "8px"
        :height "8px"}
       [:line
        {:x1 "0"
         :y1 "0"
         :x2 "0"
         :y2 "8px"
         :stroke "#24253a"
         :strokeWidth "1px"
         :fill "none"}]
       [:line
        {:x1 "0"
         :y1 "8px"
         :x2 "8px"
         :y2 "8px"
         :stroke "#24253a"
         :strokeWidth "1px"
         :fill "none"}]]]
     [:rect
      {:x "0"
       :y "0"
       :width "100%"
       :height "100%"
       :fill "#191320"}]
     [:rect
      {:x "10px"
       :y "10px"
       :stroke "#453941"
       :strokeWidth "1px"
       :width (calc :width 10)
       :height (calc :height 10)
       :fill "url(#subgrid)"}]
     [:rect
      {:x "10px"
       :y "10px"
       :stroke "#453941"
       :strokeWidth "1px"
       :width (calc :width 10)
       :height (calc :height 10)
       :fill "url(#base-grid)"}]
     [:rect
      {:x "4px"
       :y "4px"
       :width (calc :width 8.25)
       :height (calc :height 8)
       :transform "translate(8.25, 8)"
       :fill "url(#dots)"}]]))
