(ns nzpuzzle-solver.solnfinder-test
  (:require [clojure.test :refer :all]
            [nzpuzzle-solver.solnfinder :refer :all]
            [nzpuzzle-solver.core :refer :all]))

(deftest b-test
  (testing "default board solutions"
    (find-solutions (board-condense default-start-board) default-start-tiles)
    (is (= 1 1))))
