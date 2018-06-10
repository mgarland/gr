(ns gr.cli-test
  (:require [clojure.test :refer :all]
            [gr.cli :refer :all]))

(deftest has-args-option?-validation
  (testing "Test has-args-option?"
    (is (not (has-args-option? args-file ["-foo" "-bar" "--server" "--help"])))
    (is (has-args-option? args-file ["-foo" "-bar" "--server" "--help" "-f"]))
    (is (has-args-option? args-file ["-foo" "--file" "--server" "--help"]))))

(deftest arg-pos-validation
  (testing "Test arg-pos"
    (is (= (arg-pos args-server ["-foo" "-bar" "--baz" "--help"]) -1))
    (is (= (arg-pos args-server ["-foo" "-bar" "--server" "--sort" "-f"]) 2))
    (is (= (arg-pos args-sort ["-o" "--file" "--server" "--help"]) 0))))

(deftest arg-value-validation
  (testing "Test arg-value"
    (is (= (arg-value args-sort ["-foo" "-bar" "--server" "--help"]) nil))
    (is (= (arg-value args-sort ["-foo" "-bar" "--server" "--help" "--sort"]) nil))
    (is (= (arg-value args-sort ["-foo" "-bar" "--server" "--sort" 3]) 3))
    (is (= (arg-value args-file ["-o" 1 "--file" "foo.txt" "--server" "--help"]) "foo.txt"))))


(deftest parse-args-validation
  (testing "Test parse-args"
    (let [args-map (parse-args ["--file" "file.txt" "-o" 2 "--server" "-h"])]
      (is (:server args-map))
      (is (:help args-map))
      (is (= (:file args-map) "file.txt"))
      (is (= (:sort args-map) 2)))))

