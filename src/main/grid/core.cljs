(ns grid.core
  (:require
    [clojure.pprint :refer [pprint]]
    [reagent.core :as r]
    [reagent.dom.client :as rdom]
    [grid.api :as doc]
    [grid.docs]
    [grid.generator :as g]))

(defonce selected (r/atom :letter-portrait))

(defn selected-doc
  []
  (doc/get-doc @selected))

(defonce root (rdom/create-root (js/document.getElementById "root")))

(defn update-title
  []
  (let [doc (selected-doc)]
    (set! (.-title js/window.document) (:title doc))))

(add-watch selected :update-title
           (fn [_key _atom _prev _next]
             (update-title)))

(update-title)

(defn app
  []
  [:div.flex.flex-col.items-center.justify-center.p-16.print:p-0
    [:div
     {:style {}}
     [g/generate-svg (selected-doc)]]])

(defn ^:dev/after-load -main
  []
  (rdom/render root [app]))

(comment
  (js/alert "test")
  (pprint (selected-doc))
  (reset! selected :spread)
  (reset! selected  :letter-portrait)
  (reset! selected  :letter-landscape))
