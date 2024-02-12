(ns grid.constants
  (:require
    [grid.color :as color]))

(def weekday-color-interval (/ 260 7)) ;; Covers red to blue

(def weekdays
 (->> [{:title "Sunday"}
       {:title "Monday"}
       {:title "Tuesday"}
       {:title "Wednesday"}
       {:title "Thursday"}
       {:title "Friday"}
       {:title "Saturday"}]
      (map-indexed
        (fn [idx m]
          (assoc
            m :color
            (-> [(* idx weekday-color-interval) 80 70]
                (color/hsl->rgb)
                (color/rgb->hex)))))))

(def habit-color-interval (/ 240 5))

(def habits
  (->> [{:title "Meditate AM"
         :icon  "sunrise"}
        {:title "Meditate PM"
         :icon  "moon"}
        {:title "Exercise"
         :icon  "dumbbell"}
        {:title "Gaming"
         :icon  "gamepad-modern"}
        {:title "Read"
         :icon  "book-open-cover"}]
       (map-indexed vector)
       (mapv
         (fn [[idx m]]
           (assoc m :color (-> [(* idx habit-color-interval) 50 50]
                               (color/hsl->rgb)
                               (color/rgb->hex)))))))
