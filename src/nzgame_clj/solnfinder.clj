(ns nzgame-clj.solnfinder
  (:gen-class)
  (:require [nzgame-clj.core :refer [board-print board-options complete?]]))

(defn- rec-find-solutions
  [path idx tiles input-paths]
  (let [board (get path idx)
        prev-board (if (> idx 0) (get path (- idx 1)) nil)]
    (if (contains? @input-paths board)
      (when prev-board
        (swap! input-paths assoc board (conj (get @input-paths board) prev-board)))
      (do
        (when prev-board
          (swap! input-paths assoc board [prev-board]))
        (if (complete? board)
          (do
            (println "!!!!!!!Complete path")
            (dorun (map board-print  path)))
          (let [options (board-options board)]
            (dorun (map
                (fn [[new-board option-k]]
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
  (let [input-paths (atom {})]
    (rec-find-solutions
      [board]
      0
      tiles
      input-paths ; input-paths to prevent going down the same path twice... and to build the solution graph
      ; solutionPaths to store solutions
      ; impasses
      )
    (println (count @input-paths))))

;(defn rec-has-solution
;  []

;(defn has-solution
;  [board tiles]
;  (rec-has-solution board tiles (find-solution-board )))
