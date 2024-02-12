(ns grid.utils.date)

(defn add
  [date unit x]
  (let [date (js/Date. date)]
    (case unit
      :years  (doto date
                (.setFullYear (+ x (.getYear date))))
      :months (doto date
                (.setMonth (+ x (.getMonth date))))
      :days   (doto date
                (.setDate  (+ x (.getDate date)))))))

(defn set-month
  ([date month]
   (set-month date month (.getDate date)))
  ([date month day]
   (let [date (js/Date. date)]
     (doto date
       (.setMonth month day)))))

(defn set-date
  [date day]
  (doto date
    (.setDate day)))

(defn date->first-weekday-of-month
  [date]
  (let [date (js/Date. date)]
    (-> date
        (set-date 1)
        (.getDay))))

(defn date->days-in-month
  [date]
  (let [date (js/Date. date)]
    (-> date
        (set-date 1)
        (add :months 1)
        (add :days -1)
        (.getDate))))

(defn date->week
  [date]
  (let [weekday (.getDay date)
        start (add date :days (* -1 weekday))
        end (add start :days 6)]
    [start end]))

(defn date->month-name
  [date]
  (.toLocaleDateString date "en-US" #js {:month "long"}))

(defn date->iso-string
  [date]
  (let [year (.getFullYear date)
        month (inc (.getMonth date))
        day  (.getDate date)]
    (str year
         "-" (.padStart (str month) 2 "0")
         "-" (.padStart (str day) 2 "0"))))

(defn date->full-str
  [date]
  (.toLocaleDateString date "en-US" #js {:year "numeric"
                                         :month "long"
                                         :day   "numeric"}))

(defn date->weekday-name
  [date]
  (.toLocaleDateString date "en-US" #js {:weekday "long"}))

(defn date->month-day
  [date]
  (.toLocaleDateString date "en-US" #js {:month "long"
                                         :day   "numeric"}))
