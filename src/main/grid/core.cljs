(ns grid.core
  (:require
    [reagent.core :as r]
    [reagent.dom.client :as rdom]
    [grid.generator :as g]))

(defonce root (rdom/create-root (js/document.getElementById "root")))

(defn update-title
  []
  (let [size (g/selected-size)]
    (set! (.-title js/window.document) (g/format-name size))))

(add-watch g/state :update-title
           (fn [_key _atom _prev _next]
             (update-title)))

(update-title)

(defn app
  []
  [:div.flex.flex-col.items-center.justify-center.p-16.print:p-0
    [:div
     {:style {}}
     [g/svg]]])

(defn ^:dev/after-load -main
  []
  (rdom/render root [app]))

(comment
  (js/alert "test")
  (swap! g/state assoc :selected :spread)
  (swap! g/state assoc :selected :letter-portrait)
  @g/state)
