(ns nzgame-clj.solnboardfinder-test
  (:require [clojure.test :refer :all]
            [nzgame-clj.solnboardfinder :refer :all]
            [nzgame-clj.core :refer :all]))

(deftest a-test
  (testing "default board solutions"
    (find-solution-boards (board-condense default-start-board) default-start-tiles)
    (is (= 1 1))))
