(ns grid.svg
  (:require
    [clojure.string :as s]))

(def ppi 96)

(defn in->px
  [in]
  (* in ppi))

(defn px
  [x]
  (str x "px"))

(defn quantize
  [increment value]
  (* (js/Math.round (/ value increment))
     increment))

(defn calc-grid-size
 [{:keys [total items start gutter] :or {start 1}}]
 (quantize
    16
    (-> (- total start)
        (- gutter)
        (/ items)
        (- gutter))))

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

(defn curve
  [x1 y1 x2 y2 x y]
  (str "C" (s/join ", " [(str x1 " " y1)
                         (str x2 " " y2)
                         (str x " " y)])))

(defn curve-local
  [dx1 dy1 dx2 dy2 dx dy]
  (str "c" (s/join ", " [(str dx1 " " dy1)
                         (str dx2 " " dy2)
                         (str dx " " dy)])))

(defn transform
  [& parts]
  (s/join " " parts))

(defn rotate
  [angle x y]
  (str "rotate(" (s/join " " [angle x y]) ")"))

(defn translate
  [x & [y]]
  (str "translate(" (s/join " " [x y]) ")"))

(defn scale
  [sx & [sy]]
  [str "scale(" (s/join [sx sy]) ")"])

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
     (-> props
         (dissoc :r
          (merge
            {:d (path (move   (+ x tl) y)
                      (h-line (+ x (- width tr)))
                      (arc-local tr tr tr)
                      (v-line (+ y (- height br)))
                      (arc-local br (* -1 br) br)
                      (h-line (+ x bl))
                      (arc-local bl (* -1 bl) (* -1 bl))
                      (v-line (+ y tl))
                      (arc-local tl tl (* -1 tl)))})))]))

(defn swoosh
  [{:keys [curve] :as props}]
  (let [[x y dx1 dy1 dx2 dy2 dx dy] curve]
    [:path
     (-> props
         (dissoc :curve)
         (merge
           {:d (path (move x y)
                     (curve-local dx1 dy1 dx2 dy2 dx dy))}))]))

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
  [{:keys [width height fill-id padding]}]
  [:rect
    {:x (px padding)
     :y (px padding)
     :width (- width (* 2 padding))
     :height (- height (* 2 padding))
     :fill (str "url(#" fill-id ")")}])

(comment
  (seq? [10 10])
  (normalize-corner-radius [10 10]))
