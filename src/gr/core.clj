(ns gr.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:gen-class))

;; field names
(def ^:const field-names [:LastName :FirstName :Gender :FavoriteColor :DateOfBirth])

;;
;; command line argument validation
;;
(defn usage []
  (do
    (println "Usage: file-name sort-option")
    (println "   file-name: full path to file to load")
    (println "   sort-option: 1 - sort by Gender asc then by LastName asc")
    (println "                2 - sort by DateOfBirth asc")
    (println "                3 - sort by LastName desc")))

(defn validate-sort-option [val]
  (#{1 2 3} val))

;;
;; file load
;;
;; read lines from file
(defn get-lines [file]
  (line-seq (io/reader file)))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [[file-name sort-option] args
        is-sort-option-valid? (validate-sort-option sort-option)]
    (if is-sort-option-valid?
      (count (get-lines file-name))
      (usage))))


