(ns ^:figwheel-always om-tut.grids)

(def small
  ;; For testing
  [[0 0 0 0]
   [0 0 0 1]
   [0 0 0 0]
   [0 0 1 1]])

(defn rep-zero [n]
  (vec (replicate n 0)))

(defn pad [grid width height]
  "Pads the grid to be of size width by height."
  (let [grid-w (count (grid 0))
        grid-h (count grid)
        top (quot (- height grid-h) 2)
        bot (- height (+ grid-h top))
        lef (quot (- width grid-w) 2)
        rig (- width (+ grid-w lef))]
    (vec (concat
           (replicate top (rep-zero width))
           (map (fn [v](vec (concat
                              (rep-zero lef)
                              v
                              (rep-zero rig))))
                grid)
           (replicate top (rep-zero width))))))

(def gosper-glider-gun
  (pad
    [[0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0]
     [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 0 0 0]
     [0 0 0 0 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 1 1]
     [0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 1 0 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 1 1]
     [1 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 1 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
     [1 1 0 0 0 0 0 0 0 0 1 0 0 0 1 0 1 1 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 0 0 0]
     [0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0]
     [0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
     [0 0 0 0 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]]
    50
    50))

(def ten-cell-inf
  (pad
    [[0 0 0 0 0 0 1 0]
     [0 0 0 0 1 0 1 1]
     [0 0 0 0 1 0 1 0]
     [0 0 0 0 1 0 0 0]
     [0 0 1 0 0 0 0 0]
     [1 0 1 0 0 0 0 0]]
    100
    100))
