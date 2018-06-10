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
;; File load processing
;;

;; one regex to handle all delimiter possibilities
(def ^:const delims-re #"\s+\|\s+|,\s+|\s+")

;; slurp the file (lazily)
(defn get-lines [file]
  (line-seq (io/reader file)))

;; separate the fields in the line
(defn split-lines [lines]
  (map #(str/split % delims-re) lines))

;; creates a record (HashMap) from the field values
(defn create-record [names values]
  (zipmap names values))

(defn create-records [lines]
       (map #(create-record field-names %) lines))

(defn -main
  "Read a file of records and display them ordered by the sort-option."
  [& args]
  (let [[file-name sort-option] args
        is-sort-option-valid? (validate-sort-option sort-option)]
    (if is-sort-option-valid?
      (-> file-name
          get-lines
          split-lines
          create-records)
      (usage))))


