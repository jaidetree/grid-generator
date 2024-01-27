(ns grid.svg
  (:require
    [clojure.string :as s]))

(defn path
  [& parts]
  (s/join " " parts))

(defn move
  [x y]
  (str "M" x "," y))

(defn move-relative
  [x y]
  (str "m" x "," y))

(defn h-line
  [width]
  (str "H" width))

(defn h-line-relative
  [width]
  (str "h" width))

(defn v-line
  [height]
  (str "V" height))

(defn v-line-local
  [height]
  (str "v" height))

(defn arc
  ([r x y]
   (arc r r 0 0 0 x y))
  ([rx ry x y]
   (arc rx ry 0 0 0 x y))
  ([rx ry x-axis-rotation large-arc-flag sweep-flag x y]
   (s/join " " ["A" rx ry x-axis-rotation large-arc-flag sweep-flag x y])))

(defn arc-local
  ([r dx dy]
   (arc-local r r 0 0 1 dx dy))
  ([rx ry dx dy]
   (arc-local rx ry 0 0 1 dx dy))
  ([rx ry x-axis-rotation large-arc-flag sweep-flag dx dy]
   (s/join " " ["a" rx ry x-axis-rotation large-arc-flag sweep-flag dx dy])))

(defn normalize-corner-radius
  [radius]
  (cond (not radius)     [0 0 0 0]
        (number? radius) [radius radius radius radius]
        :else            (case (count radius)
                           1 (let [[r] radius]           [r r r r])
                           2 (let [[rx ry] radius]       [rx ry rx ry])
                           4 (let [[tl tr bl br] radius] [tl tr bl br]))))


(defn rounded-rect
  [{:keys [r x y width height] :as props}]
  (let [[tl tr bl br] (normalize-corner-radius r)]
    [:path
     (merge props
            {:d (path (move   (+ x tl) y)
                      (h-line (+ x (- width tr)))
                      (arc-local tr tr tr)
                      (v-line (+ y (- height br)))
                      (arc-local br (* -1 br) br)
                      (h-line (+ x bl))
                      (arc-local bl (* -1 bl) (* -1 bl))
                      (v-line (+ y tl))
                      (arc-local tl tl (* -1 tl)))})]))


(comment
  (seq? [10 10])
  (normalize-corner-radius [10 10]))
