(ns grid.tasks.render
  (:require
    [clojure.string :as s]
    [promesa.core :as p]
    [reagent.dom.server :as rdom]
    [grid.docs]
    [grid.api :refer [get-doc]]
    [grid.generator :refer [generate-svg]]
    ["path" :as path]
    ["child_process" :as cp]
    ["fs/promises" :as fs]
    ["svgo" :refer [optimize loadConfig]]))

(def config
  {:multipass true
   :plugins [{:name "preset-default"
              :params {:overrides {:cleanupIds false}}}]})

(def inkscape "/usr/local/bin/inkscape")

(defn svg->pdf
  [svg-path]
  (p/create
    (fn [resolve reject]
      (.exec cp
             (s/join
               " "
               [inkscape
                (str "\"" svg-path "\"")
                "--export-filename"
                (str "\"" (s/replace svg-path #"\.svg$" ".pdf") "\"")])
             (fn [err stdout stderr]
               (if (not (nil? err))
                 (reject err)
                 (do
                   (js/console.error stderr)
                   (resolve stdout))))))))

(defn -main
  [id]
  (let [doc (get-doc (keyword id))
        html (rdom/render-to-static-markup [generate-svg doc])
        filepath (.join path "out" (:out-dir doc) (str (:title doc) ".svg"))
        optimize-result (optimize html (-> config
                                           (merge {:path filepath})
                                           (clj->js)))]
      (p/do
        (.writeFile fs filepath (.-data optimize-result) #js {:encoding "utf-8"})
        (println "Rendered" filepath)
        (svg->pdf filepath)
        (println "Exported svg -> pdf"))))
