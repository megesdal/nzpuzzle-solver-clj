;; Board implementation that stores all board state in a single unsigned 64-bit long

(ns nzpuzzle-solver.board)

(defn rec-board-condense
  [values i board]
  ;(println "rec-board-condense" i board)
  (if (= i (count values))
    board
    (recur values (inc i)
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

(defn board-print
  "Prints board to console"
  [board]
  (println ">>>>>><<<<<<")
  (print
    (clojure.string/join
      (map-indexed
        (fn [i k]
          (let [placed (= (bit-and k 8) 8)]
            (format "%s%d%s%s"
              (if placed "[" "(")
              (bit-and k 7)
              (if placed "]" ")")
              (if (= (mod i 4) 3) "\n" ""))))
        (board-expand board))))
  (println "<<<<<<>>>>>>"))

(defn board-replace-value
  [board i value]
  (let [and-mask (bit-not (bit-shift-left 15 (- 60 (bit-shift-left i 2))))]
    (bit-or (bit-and board and-mask)
      (bit-shift-left
        value
        (- 60 (bit-shift-left i 2))))))

(defn placed?
  [board idx]
  (= (bit-and (value-with-place-mask board idx) 8) 8))

(defn- board-options-at-mask
  [board idx]
  (if (placed? board idx)
    [false false false false]
    (let
      [
       x (quot idx 4)
       y (mod idx 4)
       xs (if (> x 0) (- x 1) x)
       xf (if (< x 3) (+ x 1) x)
       ys (if (> y 0) (- y 1) y)
       yf (if (< y 3) (+ y 1) y)
      ]
      (reduce
        (fn [options value] (assoc options value false))
        [true true true true true] ;TODO: bitmask?
        (map
          (fn [k] (bit-and (value-with-place-mask board k) 7))
          (filter
            (fn [k]
              (let [kx (quot k 4) ky (mod k 4)]
                (and (>= kx xs) (<= kx xf) (>= ky ys) (<= ky yf))))
            (range 16)))))))

; TODO: should I reduce to a list?
(defn board-options-at
  [board idx]
  (let [mask (board-options-at-mask board idx)]
    (filter
      (fn [i] (get mask i))
      (range 5))))

; TODO: reduce to a list?
(defn board-options
  [board]
  (map
    (fn [[k option-k]] [(board-replace-value board k (bit-or option-k 8)) option-k k])
    (mapcat
      (fn [k]
        (map
          (fn [option-k] [k option-k])
          (board-options-at board k)))
      (range 16))))

(defn complete?
  [board]
  (loop [i 0]
    (if (> i 15)
      true
      (if (< (value-with-place-mask board i) 8)
        false
        (recur (inc i))))))
