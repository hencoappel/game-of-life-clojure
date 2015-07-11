(ns ^:figwheel-always om-tut.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [om-tut.grids :as grids]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:squares grids/ten-cell-inf}))

(defn gen-grid-style
  [data]
  (let [sqr-size 5
        width-str (str (* sqr-size (count (data 0))) "px")
        height-str (str (* sqr-size (count data)) "px")]
    #js {:width width-str :height height-str}))

(defn get-default [coll idx dflt]
  "Get with default"
  (cond
    (< idx 0) dflt
    (>= idx (count coll)) dflt
    :else (coll idx)))

(defn grid-get
  ([grid idx]
    (let [width (count (grid 0))
          height (count grid)
          x (mod idx width)
          y (quot idx height)]
      (grid-get grid x y)))
  ([grid x y]
   (get-default (get-default grid y []) x 0)))

(defn sum-vec [v1 v2]
  (vec (map + v1 v2)))

(defn count-neighbours [grid x y]
  (let [coords
        [[-1 -1] [0 -1] [1 -1]
         [-1  0]        [1  0]
         [-1  1] [0  1] [1  1]]]
    (->> coords
         (map sum-vec (repeat 8 [x y]))
         (map (fn [xy] (grid-get grid (xy 0) (xy 1))))
         (reduce +))))

(defn grid-count-neighbours [grid]
  (let [width (count (grid 0))
        height (count grid)]
    (vec (for [y (range height)]
      (vec (for [x (range width)]
        [(count-neighbours grid x y) (grid-get grid x y)]))))))

(defn vecmap [f coll] (vec (map f coll)))

(defn update-grid
  [grid]
   (->> grid
        (grid-count-neighbours)
        (vecmap
          (fn [v]
            (vecmap
              (fn [x]
                (cond
                  (= (first x) 2) (second x)
                  (= (first x) 3) 1
                  :else 0)) v)))))

(defn swap-grid []
  ;;(print (@app-state :squares))
  (swap! app-state assoc
         :squares
         (update-grid (@app-state :squares))))

(om/root
  (fn [data owner]
    ; (print (:squares data))
    (om/component
      (apply dom/div #js {:className "grid" :style (gen-grid-style (:squares data))}
        (->> (:squares data) (flatten) (map #(["dead" "live"] %)) (map (fn [s] (dom/div #js {:className (str "square " s)} nil)))))))
  app-state
  {:target (. js/document (getElementById "app"))})

(defn schedule-periodic
  ([callback ms reps] (schedule-periodic callback ms reps 0))
  ([callback ms reps repnum]
    (if (< repnum reps)
      (do
        (callback)
        (js/setTimeout
          #(schedule-periodic callback ms reps (inc repnum))
          ms)))))

(def job (schedule-periodic swap-grid 0 1000))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

