(ns nzpuzzle-solver.solnfinder-test
  (:require [clojure.test :refer :all]
            [nzpuzzle-solver.solnboardfinder :refer :all]
            [nzpuzzle-solver.core :refer :all]))

(deftest a-test
  (testing "default board solutions"
    (find-solution-boards (board-condense default-start-board) default-start-tiles)
    (is (= 1 1))))
