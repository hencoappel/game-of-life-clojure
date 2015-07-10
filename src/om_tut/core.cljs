(ns ^:figwheel-always om-tut.core
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [om-tut.grids :as grids]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:squares grids/gosper-glider-gun}))

(defn gen-grid-style
  [data]
  (let [sqr-size 10
        width (str (* sqr-size (count (get data 0))) "px")
        height (str (* sqr-size (count data)) "px")]
    #js {:width width :height height}))

; (defn set-interval [callback ms]
;   (future (while true (do (Thread/sleep ms) (callback)))))

; (def job (set-interval update grid 1000))

; (future-cancel job)

; (defn timer [] (java.util.Timer.))
; (doseq (range 1000)

(om/root
  (fn [data owner]
    (om/component
      (apply dom/div #js {:style (gen-grid-style (:squares data))}
        (->> (:squares data) (flatten) (map #(get ["dead" "live"] %)) (map (fn [s] (dom/div #js {:className (str "square " s)} nil)))))))
  app-state
  {:target (. js/document (getElementById "app"))})


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

