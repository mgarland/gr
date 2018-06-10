(ns gr.core
    (:import java.text.SimpleDateFormat)
    (:require [clojure.java.io :as io]
              [clojure.string :as str]
              [compojure.core :refer :all]
              [compojure.route :as route]
              [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
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
;; Rest API
;;

(defroutes api-routes
           (GET "/" [] "Hello World")
           (route/not-found "Not Found"))

(def api
  (wrap-defaults api-routes site-defaults))

;;
;; Main
;;
(defn -main
      "Read a file of records and display them ordered by the sort-option."
      [& args]
      (let [[file-name args-sort-option] args
            sort-option (if (instance? String args-sort-option)
                          (Integer/parseInt args-sort-option) args-sort-option)
            is-sort-option-valid? (validate-sort-option sort-option)
            sort-record-fn (partial sort-record-fns sort-option)]
           (if is-sort-option-valid?
             (-> file-name
                 get-lines
                 split-lines
                 create-records
                 sort-record-fn
                 format-for-display
                 display)
             (usage))))


