(ns nzgame-clj.solnfinder
  (:gen-class)
  (:require [nzgame-clj.core :refer [value-with-place-mask board-expand board-replace-value]]))

(defn valid?
  [board idx value]
  ;(println "valid?" (board-expand board) idx value)
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
  ;(println (board-expand board) tiles idx)
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

;(defn rec-has-solution
;  []

;(defn has-solution
;  [board tiles]
;  (rec-has-solution board tiles (find-solution-board )))

