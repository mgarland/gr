(ns gr.core-test
  (:require [clojure.test :refer :all]
            [gr.core :refer :all]
            [ring.mock.request :as mock]))

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
    (is (= (-main "resources/test/test1.txt") (usage)))
    (is (= (-main "resources/test/test1.txt" "0") (usage)))))

(deftest get-lines-validation
  (testing "testing get-lines"
    (is (= (count (get-lines "resources/test/test1.txt")) 6))))

(deftest split-line-validation
  (testing "testing split-lines"
    (let [result (split-lines ["Last | First | Male | Color | 06/07/2018"
                               "Last, First, Male, Color, 06/07/2018"
                               "Last First Male Color 06/07/2018"])
          answer ["Last" "First" "Male" "Color" "06/07/2018"]]
      (is (= (count result) 3))                             ; 3 answers in result
      (is (= (into #{} result) #{answer})))))               ; but they are all the same

(deftest create-record-validation
  (testing "testing create-record"
    (let [record (create-record field-names ["Last" "First" "Male" "Color" "06/07/2018"])
          answer {:LastName "Last" :FirstName "First" :Gender "Male" :FavoriteColor "Color" :DateOfBirth "06/07/2018"}]
      (is (= record answer)))))

(deftest create-records-validation
  (testing "testing create-records"
    (let [records (create-records [["Last1" "First1" "Male" "Color1" "06/07/2018"]
                                   ["Last2" "First2" "Male" "Color2" "07/08/2018"]
                                   ["Last3" "First3" "Female" "Color3" "08/09/2018"]])
          answer [{:LastName "Last1" :FirstName "First1" :Gender "Male" :FavoriteColor "Color1" :DateOfBirth (.parse date-format "06/07/2018")}
                  {:LastName "Last2" :FirstName "First2" :Gender "Male" :FavoriteColor "Color2" :DateOfBirth (.parse date-format "07/08/2018")}
                  {:LastName "Last3" :FirstName "First3" :Gender "Female" :FavoriteColor "Color3" :DateOfBirth (.parse date-format "08/09/2018")}]]
      (is (= records answer)))))

(deftest transform-record-validation
  (testing "testing transform-record"
    (let [record (transform-record {:LastName "Last1" :FirstName "First1" :Gender "Male" :FavoriteColor "Color1" :DateOfBirth "06/07/2018"})
          date (.parse date-format "06/07/2018")
          answer {:LastName "Last1" :FirstName "First1" :Gender "Male" :FavoriteColor "Color1" :DateOfBirth date}]
      (is (= record answer)))))


(deftest sort-record-fns-validation
  (testing "testing sort-record-fns-validation"
    (is (thrown? IllegalArgumentException (sort-record-fns 0 [])))))

(deftest format-record-validation
  (testing "testing transform-record"
    (let [record (format-record {:LastName "Last1" :FirstName "First1" :Gender "Male" :FavoriteColor "Color1" :DateOfBirth (.parse date-format "06/07/2018")})
          answer "Last1 | First1 | Male | Color1 | 6/7/2018"]
      (is (= record answer)))))


;;
;; Rest API tests
;;

(deftest test-api
         (testing "main route"
                  (let [response (api (mock/request :get "/"))]
                       (is (= (:status response) 200))
                       (is (= (:body response) "Hello World"))))

         (testing "not-found route"
                  (let [response (api (mock/request :get "/invalid"))]
                       (is (= (:status response) 404)))))
