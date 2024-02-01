(ns grid.generator
  (:require
    [grid.svg :as svg]
    [grid.color :as color]))

(defn generate-svg
  [{:keys [props defs children]}]
  (let [{:keys [width height background]} props]
    [:svg
     {:width (svg/px width)
      :height (svg/px height)
      :xmlns "http://www.w3.org/2000/svg"
      :xmlnsXlink "http://www.w3.org/1999/xlink"
      :version "1.1"
      :viewBox (str "0 0 " width " " height)
      :fill "#000"}
     (for [[idx def] (map-indexed vector defs)]
       [:<> {:key idx} def])
     [:g
      [:rect
       {:x "0"
        :y "0"
        :width  "100%"
        :height "100%"
        :fill   (or background (color/get :background))}]
      (into [:g] children)]]))



