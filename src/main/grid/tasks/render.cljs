(ns grid.tasks.render
  (:require
    [promesa.core :as p]
    [reagent.dom.server :as rdom]
    [grid.generator :refer [state svg selected-size format-name]]
    ["fs/promises" :as fs]
    ["svgo" :refer [optimize loadConfig]]))

(def config
  {:multipass true
   :plugins [{:name "preset-default"
              :params {:overrides {:cleanupIds false}}}]})

(defn -main
  [size]
  (swap! state assoc :selected (keyword size))
  (let [html (rdom/render-to-static-markup [svg])
        size (selected-size)
        filepath (str "out/" (format-name size) ".svg")
        optimize-result (optimize html (-> config
                                           (merge {:path filepath})
                                           (clj->js)))]
      (p/do
        (.writeFile fs filepath (.-data optimize-result) #js {:encoding "utf-8"})
        (println "Rendered" filepath))))
