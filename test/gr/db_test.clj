(ns gr.db-test
  (:require [clojure.test :refer :all]
            [gr.db :refer :all]))

(deftest db-validation
  (testing "db method validation"
    (let [_ (do (add "rec1") (add "rec2") (add "rec3"))])
    (is (= (count (:data @db)) 3))
    (is (= "rec1" ((:data @db) 1)))
    (is (= "rec2" ((:data @db) 2)))
    (is (= "rec3" ((:data @db) 3)))
    (is (nil? ((:data @db) 0)))
    (is (nil? ((:data @db) 4)))
    (is (= 4 (:next-seq @db)))
    (is (= "rec1" (retrieve 1)))
    (is (= "rec2" (retrieve 2)))
    (is (= "rec3" (retrieve 3)))))
