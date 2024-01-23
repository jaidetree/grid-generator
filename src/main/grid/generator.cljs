(ns grid.generator
  (:require
    [grid.api :as doc]
    [grid.color :as color]))

(defn generate-svg
  [doc]
  (let [{:keys [width height children patterns]} doc]
    [:svg
     {:width (doc/px width)
      :height (doc/px height)
      :xmlns "http://www.w3.org/2000/svg"
      :xmlnsXlink "http://www.w3.org/1999/xlink"
      :version "1.1"
      :viewBox (str "0 0 " width " " height)
      :on-click (fn [e]
                  (js/window.print (.-currentTarget e)))}
     [:defs
      (for [[idx pattern] (map-indexed vector patterns)]
        [:<>  {:key idx} pattern])]
     [:rect
      {:x "0"
       :y "0"
       :width  "100%"
       :height "100%"
       :fill   (color/get :background)}]
     (into [:g {}] children)]))



