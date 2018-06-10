(ns gr.core
  (:import java.text.SimpleDateFormat)
  (:require [gr.cli :as cli]
            [gr.api :as api]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

;; field names
(def ^:const field-names [:LastName :FirstName :Gender :FavoriteColor :DateOfBirth])

;; date format
(def date-format (SimpleDateFormat. "M/d/yyyy"))

;;
;; Input Processing
;;

;; one regex to handle all delimiter possibilities
(def ^:const delims-re #"\s+\|\s+|,\s+|\s+")

(defn get-lines [file]
  "Slurps the file (lazily)"
  (line-seq (io/reader file)))

(defn split-lines [lines]
  "Separate the fields in the line"
  (map #(str/split % delims-re) lines))

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

;;
;; Main
;;
(defn -main
  "Read a file of records and display them ordered by the sort-option."
  [& args]
  (let [args-map (cli/parse-args args)]
    (cond
      ;; help
      (:help args-map) (println cli/usage)

      ;; run in server mode
      (:server args-map)
      (do
        (println "Running as server on port 3000...")
        (run-jetty api/api {:port 3000}))

      ;; run as a script
      (and (:sort args-map) (:file args-map))
      (let [sort-records (partial sort-record-fns (:sort args-map))]
        (-> (:file args-map)
            get-lines
            split-lines
            create-records
            sort-records
            format-for-display
            display))

      ; problem
      :else (println cli/usage))))
