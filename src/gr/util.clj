(ns gr.util
  (:import java.text.SimpleDateFormat)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

;;
;; Input Processing
;;


;; field names
(def ^:const field-names [:LastName :FirstName :Gender :FavoriteColor :DateOfBirth])

;; date format
(def date-format (SimpleDateFormat. "M/d/yyyy"))

;; one regex to handle all delimiter possibilities
(def ^:const delims-re #"\s+\|\s+|,\s+|\s+")

(defn get-lines [file]
  "Slurps the file (lazily)"
  (line-seq (io/reader file)))

(defn split-line [line]
  (str/split line delims-re))

(defn split-lines [lines]
  "Separate the fields in the line"
  (map #(split-line %) lines))

(defn transform-record [record]
  "Perform field transformations"
  (let [dob-dt (.parse date-format (:DateOfBirth record))]
    (assoc record :DateOfBirth dob-dt)))

(defn create-record [names values]
  "Creates a record (HashMap) from the field values"
  (zipmap names values))

(defn create-records [lines]
  "Creates all of the records and invokes field transformations"
  (map #(transform-record %)
       (map #(create-record field-names %) lines)))

(defn sort-record-fns [sort-option records]
  "Sort option functions"
  (cond
    (= 1 sort-option) (sort-by (juxt :Gender :LastName) records)
    (= 2 sort-option) (sort-by :DateOfBirth records)
    (= 3 sort-option) (sort-by :LastName (comp - compare) records)
    :else (throw (IllegalArgumentException. (str "Invalid sort-option: " sort-option)))))

;;
;; Output Processing
;;
(defn format-record [record]
  "Format a record for display"
  (let [{last-name  :LastName
         first-name :FirstName
         gender     :Gender
         color      :FavoriteColor
         dob        :DateOfBirth} record]
    (str/join " | " [last-name first-name gender color (.format date-format dob)])))

(defn format-for-display [records]
  "Format all records for display"
  (map #(format-record %) records))

(defn display [records]
  "Display the records"
  (doseq [record records]
    (println record)))
