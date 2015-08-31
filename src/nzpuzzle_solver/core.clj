(ns nzpuzzle-solver.core
  (:gen-class))

(def default-start-board [2 3 2 0 1 0 1 4 3 4 3 0 0 2 1 4]) ; 2D matrix of index|is-placed-mask
(def default-start-tiles [3 4 3 3 3])                       ; index -> count

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (println default-start-board)
  (board-print (board-condense default-start-board))
  (let [board (board-condense default-start-board)]
    (println board)
    (println (board-expand board))
    (println (board-expand (board-replace-value board 3 1)))))
