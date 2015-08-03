(ns nzgame-clj.solnfinder-test
  (:require [clojure.test :refer :all]
            [nzgame-clj.solnfinder :refer :all]
            [nzgame-clj.core :refer :all]))

;(deftest a-test
;  (testing "default board solutions"
;    (find-solution-boards (board-condense default-start-board) default-start-tiles)
;    (is (= 1 1))))

(deftest b-test
  (testing "default board solutions"
    (find-solutions (board-condense default-start-board) default-start-tiles)
    (is (= 1 1))))
