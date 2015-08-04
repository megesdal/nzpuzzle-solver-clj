(ns nzgame-clj.solnfinder
  (:gen-class)
  (:require [nzgame-clj.core :refer [value-with-place-mask board-expand board-replace-value board-options complete?]]))

(defn valid?
  [board idx value]
  (if (= (value-with-place-mask board idx) (- value 8))
    false
    (let
      [
       x (quot idx 4)
       y (mod idx 4)
       srow (if (> x 0) (- x 1) x)
       erow (if (< x 3) (+ x 1) x)
       scol (if (> y 0) (- y 1) y)
       ecol (if (< y 3) (+ y 1) y)
      ]
      (loop [i srow]
        (if (> i erow)
          true
          (if (not
            (loop [j scol]
              (if (> j ecol)
                true
                (if (= value (value-with-place-mask board (+ (* i 4) j)))
                  false
                  (recur (+ j 1))))))
            false
            (recur (+ i 1))))))))

(defn rec-find-solution-boards
  [board tiles idx solutions]
  (if (= idx 16)
    (do
      (println "Add solution:" (board-expand board))
      '(board))
    (if (> (value-with-place-mask board idx) 7) ; if already placement at idx
      (rec-find-solution-boards board tiles (+ idx 1) solutions)
      (reduce concat
        (map
          (fn [k]
            (rec-find-solution-boards
              (board-replace-value board idx (+ k 8))
              (assoc tiles k (- (get tiles k) 1))
              (+ idx 1)
              solutions))
          (filter
            (fn [k]
              (and
                (> (get tiles k) 0)
                (valid? board idx (+ k 8)))); if valid and tiles available
            (range (count tiles))))))))


(defn find-solution-boards
  [board tiles]
  (rec-find-solution-boards board tiles 0 []))

(defn- rec-find-solutions
  [path idx tiles input-paths]
  (let [board (get path idx)]
    ; check input paths
    (if (contains? @input-paths board)
      (comment "TODO: add input path")
      ;(println "Already tried" (board-expand board) "...")  
      (do
        (println "Trying" (board-expand board) tiles "...")  
        (when (> idx 0)
          (swap! input-paths assoc board [(get path (- idx 1))]))
        (if (complete? board)
          (println "!!!!!!!Complete path" path)
          (let [options (board-options board)]
            (dorun (map
              (fn [[new-board option-k]] 
                ;(println "Trying board" option-k (board-expand new-board) (+ idx 1) (assoc tiles option-k (- (get tiles option-k) 1)))
                (rec-find-solutions 
                  (conj path new-board) 
                  (+ idx 1) 
                  (assoc tiles option-k (- (get tiles option-k) 1)) 
                  input-paths))
              (filter
                (fn [[new-board option-k]] 
                  (> (get tiles option-k) 0))
                options)))))))))

(defn find-solutions
  [board tiles]
  (println (board-expand board) tiles)
  (rec-find-solutions
    [board]
    0
    tiles
    (atom {}) ; input-paths to prevent going down the same path twice...
    ; solutionPaths to store solutions
    ; impasses
    ))

;(defn rec-has-solution
;  []

;(defn has-solution
;  [board tiles]
;  (rec-has-solution board tiles (find-solution-board )))
