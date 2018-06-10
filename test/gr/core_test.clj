(ns gr.core-test
  (:require [clojure.test :refer :all]
            [gr.core :refer :all]))

(deftest sort-option-validation
  (testing "Test sort-option arg"
    (is (= (validate-sort-option nil) (usage)))
    (is (= (validate-sort-option "0") (usage)))
    (is (= (validate-sort-option "1") nil))
    (is (= (validate-sort-option "2") nil))
    (is (= (validate-sort-option "3") nil))
    (is (= (validate-sort-option "4") (usage)))
    (is (= (validate-sort-option "a") (usage)))))
