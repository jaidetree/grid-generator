(ns grid.generator
  (:require
    [grid.svg :as svg]
    [grid.color :as color]))

(defn generate-svg
  [{:keys [props defs children]}]
  (let [{:keys [width height]} props]
    [:svg
     {:width (svg/px width)
      :height (svg/px height)
      :xmlns "http://www.w3.org/2000/svg"
      :xmlnsXlink "http://www.w3.org/1999/xlink"
      :version "1.1"
      :viewBox (str "0 0 " width " " height)}
     (into [:defs] defs)
     [:g
      [:rect
       {:x "0"
        :y "0"
        :width  "100%"
        :height "100%"
        :fill   (color/get :background)}]
      (into [:g] children)]]))



