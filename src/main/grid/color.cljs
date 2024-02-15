(ns grid.color
  (:refer-clojure
    :exclude [get])
  (:require
    [clojure.core :as cc]
    [clojure.string :as s]
    [reagent.core :as r]))

(defn hex->rgb
  [hexstr]
  (let [hexstr (s/replace hexstr #"^#" "")
        intval (js/parseInt hexstr 16)]
    [(bit-and (bit-shift-right intval 16) 255)
     (bit-and (bit-shift-right intval 8) 255)
     (bit-and intval 255)]))

(defn rgb->hex
  [[r g b]]
  (str "#"
       (-> (bit-or
            (bit-shift-left 1 24)
            (bit-shift-left r 16)
            (bit-shift-left g 8)
            b)
           (.toString 16)
           (.slice 1))))

(defn rgb->hsl
  [[r g b]]
  (let [r (/ r 255)
        g (/ g 255)
        b (/ b 255)
        max (js/Math.max r g b)
        min (js/Math.min r g b)
        delta (- max min)
        lightness (/ (+ max min) 2)
        saturation (cond (= max min)       0
                         (> lightness 0.5) (/ delta
                                              (- 2 max min))
                         :else             (/ delta
                                              (+ max min)))
        hue        (-> (condp = max
                         min 0
                         r   (mod (/ (- g b)
                                     delta)
                                  6)
                         g   (+ (/ (- b r)
                                   delta)
                                2)
                         b   (+ (/ (- r g)
                                   delta)
                                4))
                       (* 60)
                       (js/Math.round))]
    [(if (neg? hue)
       (+ hue 360)
       hue)
     (js/Math.round (* saturation 100))
     (js/Math.round (* lightness 100))]))

(defn between
  [min h' max]
  (and (<= min h') (< h' max)))

(defn hue->rgb
  [h' c x]
  (cond (between 0 h' 1) [c x 0]
        (between 1 h' 2) [x c 0]
        (between 2 h' 3) [0 c x]
        (between 3 h' 4) [0 x c]
        (between 4 h' 5) [x 0 c]
        (between 5 h' 6) [c 0 x]))

(defn hsl->rgb
  [[h s l]]
  (let [s (/ s 100)
        l (/ l 100)
        c (* (- 1 (js/Math.abs (- (* 2 l) 1)))
             s)
        h' (/ h 60)
        x (* c (- 1 (js/Math.abs (- (mod h' 2) 1))))
        [r g b] (hue->rgb h' c x)
        m (- l (/ c 2))]
    (vec (for [v [r g b]]
          (-> v
              (+ m)
              (* 255)
              (js/Math.round))))))

(defn clamp
  [min max x]
  (-> x
      (js/Math.min max)
      (js/Math.max min)))

(defn saturate
  [hexstr amount]
  (let [rgb (hex->rgb hexstr)
        [h s l] (rgb->hsl rgb)
        hsl #_[h (* s (+ 1 (/ amount 100))) l]
              [h (clamp 0 100 (+ s amount)) l]]
    (-> hsl
        (hsl->rgb)
        (rgb->hex))))

(defn hue
  [hexstr amount]
  (let [rgb (hex->rgb hexstr)
        [h s l] (rgb->hsl rgb)
        hsl [(mod (+ h (* 360 (/ amount 100)))
                  360) s l]]
    (-> hsl
        (hsl->rgb)
        (rgb->hex))))

(defn brightness
  [hexstr amount]
  (let [rgb (hex->rgb hexstr)
        [h s l] (rgb->hsl rgb)
        hsl #_[h s (* l (+ 1 (/ amount 100)))]
              [h s (clamp 0 100 (+ l amount))]]
    (-> hsl
        (hsl->rgb)
        (rgb->hex))))

(def colors
  {:pink       "#FF58C4"
   :teal       "#36FFC6"
   :bone       "#FFFAE7"
   :marker     "#393939"
   :dark-grape "#191320"})

(def palette
  (r/atom (merge
            colors
            {:background (:dark-grape colors)
             :outline    (-> (:dark-grape colors)
                             (saturate -10)
                             (brightness 20))
             :basegrid   (-> (:dark-grape colors)
                             (saturate -50)
                             (brightness 10))
             :subgrid    (-> (:dark-grape colors)
                             (hue -20)
                             (saturate -5)
                             (brightness 5))
             :dots       (-> (:dark-grape colors)
                             (saturate 5)
                             (brightness 15)
                             (hue 10))})))

(defn get
  [kw-or-str]
  (if-let [hex (cc/get @palette kw-or-str)]
    hex
    kw-or-str))

(comment
  (rgb->hex
    (hex->rgb
      "#FF5511"))

  (rgb->hex
    [128
     128
     128])

  (saturate "#808080" 100)

  [255 88 196]
  (-> (hex->rgb "#FF58C4")
      (rgb->hsl)
      (hsl->rgb)
      (rgb->hex))

  (saturate (get :pink) -50)
  (hue (get :pink) 100)
  (brightness "#000000" 50))

