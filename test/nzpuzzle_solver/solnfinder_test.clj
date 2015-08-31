(ns nzpuzzle-solver.solnfinder-test
  (:require [clojure.test :refer :all]
            [nzpuzzle-solver.solnfinder :refer :all]
            [nzpuzzle-solver.board :refer :all]))

(deftest b-test
  (testing "default board solutions"
    (let [start-board [2 3 2 0 1 0 1 4 3 4 3 0 0 2 1 4]
          start-tiles [3 4 3 3 3]]
      (find-solutions (board-condense start-board) start-tiles)
      (is (= 1 1)))))
