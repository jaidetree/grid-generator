(ns grid.core
  (:require
    [clojure.pprint :refer [pprint]]
    [reagent.core :as r]
    [reagent.dom.client :as rdom]
    [grid.generator :as g]
    [grid.docs.letter-portrait]
    [grid.docs.letter-landscape]
    [grid.docs.spread]
    [grid.docs.year]
    [grid.docs.projects]
    [grid.docs.todo-item]
    [grid.docs.calendar]))

(defonce selected (r/atom "Calendar"))

(def docs {"Letter Portrait" #'grid.docs.letter-portrait/doc
           "Calendar"        #'grid.docs.calendar/doc})

(defonce root (rdom/create-root (js/document.getElementById "root")))

(defn selected-doc
  []
  (when-let [doc-fn (get docs @selected)]
    (doc-fn)))

(defn update-title
  []
  (let [title @selected]
    (set! (.-title js/window.document) title)))

(add-watch selected :update-title
           (fn [_key _atom _prev _next]
             (update-title)))

(update-title)

(defn app
  []
  [:div.flex.flex-col.items-center.justify-center.p-16.gap-8.print:p-0
    [:div
     [:select
      {:on-change #(reset! selected (keyword (.. % -currentTarget -value)))
       :value (name @selected)}
      (for [[title doc] docs]
        [:option {:key title :value title} title])]]
    [:div
     {:style {}}
     (g/generate-svg (selected-doc))]])

(defn ^:dev/after-load -main
  []
  (rdom/render root [app]))

(comment
  (js/alert "test")
  (reset! selected :spread)
  (reset! selected :letter-portrait)
  (reset! selected :letter-landscape)
  (reset! selected :year))
