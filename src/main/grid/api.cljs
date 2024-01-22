(ns grid.api
  (:require
    [reagent.core :as r]))

(def state (r/atom {}))

(def ppi 96)

(defn in->px
  [in]
  (* in ppi))

(defn px
  [x]
  (str x "px"))

(defn create
  [{:keys [id title width height out-dir]}]
  (if (get @state key)
    (throw (js/Error. (str "Document by key " key " already exists")))
    (do
      (swap! state assoc id
             {:id     id
              :title  title
              :width  width
              :height height
              :out-dir (or out-dir "")
              :patterns []
              :children []})
      id)))

(defn get-doc
  [id]
  (get @state id))

(defn add-pattern
  [id hiccup-vec]
  (swap! state update-in [id :patterns] conj hiccup-vec)
  nil)

(defn add-child
  [id hiccup-vec]
  (swap! state update-in [id :children] conj hiccup-vec)
  nil)


(defn grid-pattern
  [{:keys [id size color]}]
  [:pattern
   {:id id
    :patternUnits "userSpaceOnUse"
    :width size
    :height size}
   [:line
    {:x1 "0"
     :y1 "0"
     :x2 "0"
     :y2 size
     :stroke color
     :strokeWidth "1px"
     :fill "none"}]
   [:line
    {:x1 "0"
     :y1 size
     :x2 size
     :y2 size
     :stroke color
     :strokeWidth "1px"
     :fill "none"}]])

(defn grid-layer
  [{:keys [id fill-id padding]}]
  (let [doc (get-doc id)]
    [:rect
     {:x (px padding)
      :y (px padding)
      :width (- (:width doc) (* 2 padding))
      :height (- (:height doc) (* 2 padding))
      :fill (str "url(#" fill-id ")")}]))
