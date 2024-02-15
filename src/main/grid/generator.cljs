(ns grid.generator
  (:require
    [clojure.pprint :refer [pprint]]
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
      :style {:background "#000"}}
     (into
       [:defs
        {}]
       defs)
     [:g
      [:rect
       {:x "0"
        :y "0"
        :width  (svg/px width)
        :height (svg/px height)
        :fill   (or background (color/get :background))}]
      (into [:g] children)]]))



