(ns grid.cli.render
  (:require
     [clojure.string :as s]
     [clojure.pprint :refer [pprint]]
     [promesa.core :as p]
     [reagent.dom.server :as rdom]
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

(defn svg-doc
  [{:keys [doc] :as state}]
  (let [{:keys [file]} doc
        relpath (.join path "out" (str file ".svg"))
        svg (generate-svg doc)
        svg-xml (rdom/render-to-static-markup svg)]
    (println "  -> Generated svg markup")
    (merge
       state
       {:svg-xml svg-xml
        :svg-file relpath
        :svg-path (.join path (js/process.cwd) relpath)})))

(defn optimize-svg
  [{:keys [svg-xml svg-file] :as state}]
  (let [result (optimize svg-xml (-> config
                                     (merge {:path svg-file})
                                     (clj->js)))]
    (assoc state :svg-xml (.-data result))))

(defn write-svg-file
  [{:keys [svg-file svg-path svg-xml] :as state}]
  (p/do
    (.mkdir fs (.dirname path svg-path) #js {:recursive true})
    (.writeFile fs svg-path svg-xml #js {:encoding "utf-8"})
    (println "  -> Wrote svg file" svg-file)
    state))

(defn svg->pdf
  [{:keys [svg-file] :as state}]
  (let [pdf-file (s/replace svg-file #"\.svg$" ".pdf")]
    (p/do
      (p/create
        (fn [resolve reject]
          (.exec cp
                 (s/join
                   " "
                   [inkscape
                    (str "\"" svg-file "\"")
                    "--export-filename" (str "\"" pdf-file "\"")])
                 (fn [err stdout stderr]
                   (if (not (nil? err))
                     (reject err)
                     (do
                       (js/console.error (s/trim stderr))
                       (resolve stdout)))))))
      (println "  -> Wrote pdf file" pdf-file)
      (assoc state :pdf-file pdf-file))))

(defn -main
  [class-name & args]
  (let [ns (symbol (str "grid.docs." class-name))]
   (p/do
     (require ns)
     (let [generate-docs (ns-resolve ns '-main)
           docs          (apply generate-docs args)]
       (p/doseq [doc docs]
         (println (str "Generating doc " (->> (:file doc)
                                              (.basename path))))
         (p/-> {:doc doc}
               (svg-doc)
               (optimize-svg)
               (write-svg-file)
               (svg->pdf))))
     nil)))

