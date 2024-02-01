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
            (-> [(* idx weekday-color-interval) 80 60]
                (color/hsl->rgb)
                (color/rgb->hex)))))))

