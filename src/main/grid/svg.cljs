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

(defn v-line-relative
  [height]
  (str "v" height))

(defn line
  [x y]
  (str "L" x "," y))

(defn line-relative
  [x y]
  (str "l" x "," y))

(defn arc
  ([r x y]
   (arc r r 0 0 0 x y))
  ([rx ry x y]
   (arc rx ry 0 0 0 x y))
  ([rx ry x-axis-rotation large-arc-flag sweep-flag x y]
   (s/join " " ["A" rx ry x-axis-rotation large-arc-flag sweep-flag x y])))

(defn arc-relative
  ([r dx dy]
   (arc-relative r r 0 0 1 dx dy))
  ([rx ry dx dy]
   (arc-relative rx ry 0 0 1 dx dy))
  ([rx ry x-axis-rotation large-arc-flag sweep-flag dx dy]
   (s/join " " ["a" rx ry x-axis-rotation large-arc-flag sweep-flag dx dy])))

(defn curve
  [x1 y1 x2 y2 x y]
  (str "C" (s/join ", " [(str x1 " " y1)
                         (str x2 " " y2)
                         (str x " " y)])))

(defn curve-relative
  [dx1 dy1 dx2 dy2 dx dy]
  (str "c" (s/join ", " [(str dx1 " " dy1)
                         (str dx2 " " dy2)
                         (str dx " " dy)])))

(defn transform
  [& parts]
  (s/join " " parts))

(defn rotate
  ([angle]
   (str "rotate(" angle ")"))
  ([angle x y]
   (str "rotate(" (s/join " " [angle x y]) ")")))

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
         (dissoc :r)
         (merge
           {:d (path (move   (+ x tl) y)
                     (h-line (+ x (- width tr)))
                     (arc-relative tr tr tr)
                     (v-line (+ y (- height br)))
                     (arc-relative br (* -1 br) br)
                     (h-line (+ x bl))
                     (arc-relative bl (* -1 bl) (* -1 bl))
                     (v-line (+ y tl))
                     (arc-relative tl tl (* -1 tl)))}))]))

(defn swoosh
  [{:keys [curve] :as props}]
  (let [[x y dx1 dy1 dx2 dy2 dx dy] curve]
    [:path
     (-> props
         (dissoc :curve)
         (merge
           {:d (path (move x y)
                     (curve-relative dx1 dy1 dx2 dy2 dx dy))}))]))

(defn circle-cluster
  [{:keys [n min-radius max-radius x-jitter y-jitter cx cy] :as props}]
  [:g
   (for [i (range 0 n)]
     (let [x-offset (- (rand x-jitter) (/ x-jitter 2))
           y-offset (- (rand y-jitter) (/ y-jitter 2))
           r (+ (rand (- max-radius min-radius)) min-radius)]
       [:circle
        (merge
          (dissoc props
                  :n
                  :max-radius
                  :min-radius
                  :x-jitter
                  :y-jitter
                  :cx
                  :cy)
          {:key i
           :cx (+ cx x-offset)
           :cy (+ cy y-offset)
           :r r})]))])

(defn calc-dividers
  [{:keys [total margin spacing]}]
  (-> total
      (- (* 2 margin))
      (/ spacing)
      (- 0.5)
      (js/Math.ceil)))

(comment
  (seq? [10 10])
  (normalize-corner-radius [10 10]))
