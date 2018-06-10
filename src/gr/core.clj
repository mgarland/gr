(ns gr.core
  (:import java.text.SimpleDateFormat)
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:gen-class))

;; field names
(def ^:const field-names [:LastName :FirstName :Gender :FavoriteColor :DateOfBirth])

;; date format
(def date-format (SimpleDateFormat. "M/d/yyyy"))

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
;; Input Processing
;;

;; one regex to handle all delimiter possibilities
(def ^:const delims-re #"\s+\|\s+|,\s+|\s+")

;; slurp the file (lazily)
(defn get-lines [file]
  (line-seq (io/reader file)))

;; separate the fields in the line
(defn split-lines [lines]
  (map #(str/split % delims-re) lines))

;; do field transformations here
(defn transform-record [record]
  (let [dob-dt (.parse date-format (:DateOfBirth record))]
    (assoc record :DateOfBirth dob-dt)))

;; creates a record (HashMap) from the field values
(defn create-record [names values]
  (zipmap names values))

(defn create-records [lines]
  (map #(transform-record %)
       (map #(create-record field-names %) lines)))

;;
;; Output Processing
;;


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


