(ns grid.docs.todo-item
  (:require
    [grid.api :as doc]
    [grid.presets :as presets]
    [grid.color :as color]))

(def width (* 32 19))
(def height (* 28 5))

(def id (doc/create
          {:id :todo-item
           :title "Task"
           :out-dir "stickers"
           :width width
           :height height}))

(doc/add-child
  id
  [:g
   [:rect
    {:x "0"
     :y "0"
     :width (doc/px width)
     :height (doc/px height)
     :fill (-> (color/get :bone)
               (color/saturate -40)
               (color/brightness -20))}]
   [:rect
    {:x "0"
     :y "0"
     :width (doc/px 32)
     :height (doc/px height)
     :fill (-> (color/get :bone)
               (color/saturate -40)
               (color/brightness -35))}]])
