(ns nzgame-clj.core
  (:gen-class))

(defn rec-board-condense
  [values i board]
  ;(println "rec-board-condense" i board)
  (if (= i (count values))
    board
    (recur values (+ i 1)
      (bit-or board 
        (bit-shift-left 
          (get values i) 
          (- 60 (bit-shift-left i 2)))))))

(defn board-condense
  "Converts vector board description to long"
  [values]
  (rec-board-condense values 0 0))

(defn value-with-place-mask
  [board i]
  (bit-and (bit-shift-right board (- 60 (bit-shift-left i 2))) 15))

(defn rec-board-expand
  [board size values]
  ;(println "rec-board-expand" values)
  (let [i (count values)]
    (if (= size i)
      values
      (recur board size
        (assoc values i
          (value-with-place-mask board i))))))

(defn board-expand
  "Converts long board description to vector"
  [board]
  (rec-board-expand board 16 []))

(defn board-replace-value
  [board i value]
  (let [and-mask (bit-not (bit-shift-left 15 (- 60 (bit-shift-left i 2))))]
    (bit-or (bit-and board and-mask)
      (bit-shift-left 
        value
        (- 60 (bit-shift-left i 2))))))

(def default-start-board [2 3 2 0 1 0 1 4 3 4 3 0 0 2 1 4]) ; 2D matrix of index|is-placed-mask
(def default-start-tiles [3 4 3 3 3])                       ; index -> count

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (println default-start-board)
  (let [board (board-condense default-start-board)]
    (println board)
    (println (board-expand board))
    (println (board-expand (board-replace-value board 3 1)))))
