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

(deftest main-validation-failure
  (testing "testing main"
    (is (= (-main) (usage)))
    (is (= (-main "resources/test1.txt") (usage)))
    (is (= (-main "resources/test1.txt" "0") (usage)))))

(deftest get-lines-validation
  (testing "testing get-lines"
    (is (= (count (get-lines "resources/test1.txt")) 6))))

