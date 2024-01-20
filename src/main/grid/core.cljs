(ns grid.core
  (:require
    [reagent.dom.client :as rdom]))

(defonce root (rdom/create-root (js/document.getElementById "root")))

(def state (atom {:width "8.5in"
                  :height "11in"}))



(defn app
  []
  (let [{:keys [width height]} @state]
   [:div.flex.flex-col.items-center.justify-center.p-16.print:p-0
     [:div
      {:style {}}
      [:svg
       {:width width
        :height height
        :xmlns "http://www.w3.org/2000/svg"}
       [:defs
        [:pattern#dots
         {:patternUnits "userSpaceOnUse"
          :width "16"
          :height "16"}
         [:circle
          {:cx "8"
           :cy "8"
           :r "1.5"
           :fill "#5C414F"}]

         [:pattern#base-grid
          {:patternUnits "userSpaceOnUse"
           :width "16"
           :height "16"}
          [:line
           {:x1 "0"
            :y1 "0"
            :x2 "0"
            :y2 "16"
            :stroke "#334544"
            :strokeWidth "1"}]
          [:line
           {:x1 "0"
            :y1 "16"
            :x2 "16"
            :y2 "16"
            :stroke "#334544"
            :strokeWidth "1"}]]

         [:pattern#char-grid
          {:patternUnits "userSpaceOnUse"
           :width "8"
           :height "8"}
          [:line
           {:x1 "0"
            :y1 "0"
            :x2 "0"
            :y2 "8"
            :stroke "#273b4f"
            :strokeWidth "1"}]
          [:line
           {:x1 "0"
            :y1 "8"
            :x2 "8"
            :y2 "8"
            :stroke "#273b4f"
            :strokeWidth "1"}]]]]
       [:rect
         {:x "0"
          :y "0"
          :width "100%"
          :height "100%"
          :fill "rgb(19, 15, 17)"}]
       [:rect
        {:x "10"
         :y "10"
         :stroke "#453941"
         :strokeWidth "1"
         :style {:width "calc(8.5in - 20px)"
                 :height "calc(11in - 20px)"}
         :fill "url(#char-grid)"}]
       [:rect
        {:x "10"
         :y "10"
         :stroke "#453941"
         :strokeWidth "1"
         :style {:width "calc(8.5in - 20px)"
                 :height "calc(11in - 20px)"}
         :fill "url(#base-grid)"}]
       [:rect
        {:x "4.5"
         :y "4.5"
         :style {:width "calc(8.5in - 16px)"
                 :height "calc(11in - 16px)"
                 :transform "translate(8px, 8px)"}
         :fill "url(#dots)"}]]]]))



(defn ^:dev/after-load -main
  []
  (rdom/render root [app]))

(comment
  (js/alert "test"))
