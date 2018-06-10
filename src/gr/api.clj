(ns gr.api
  (:require [gr.db :as db]
            [gr.util :as util]
            [compojure.core :refer :all]
            [ring.middleware.json :as middleware]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:import (java.text SimpleDateFormat)))

;; date format
(def date-format (SimpleDateFormat. "M/d/yyyy"))

(defn create-db-record [line]
  "Create the db record from the input line"
  (util/transform-record
    (util/create-record util/field-names (util/split-line line))))

;;
;; Rest API
;;

(defn add-record-to-db [req]
  "Adds the line specified in request to the db"
  (let [rec (or (get-in req [:params :record])
                (get-in req [:body :record]))]
    (if rec
      (do
        (db/add (create-db-record rec))
        {:status 201})
      {:status 400 :body "Invalid record format"})))

(defn format-record [record]
  "Format a record for the response"
  (assoc record :DateOfBirth (.format date-format (:DateOfBirth record))))

(defn retrieve-sorted-records [sort-option]
  "Retrieves all records from the db and sorts by sort-option"
  (let [db-recs (map second (db/retrieve-all)) ; drop the record key
        sorted-recs (util/sort-record-fns sort-option db-recs)
        recs (map format-record sorted-recs)]
    {:status 200 :body recs}))

(defroutes api-routes
           (GET "/" [] "")
           (GET "/records/gender" [] (retrieve-sorted-records 1))
           (GET "/records/birthdate" [] (retrieve-sorted-records 2))
           (GET "/records/name" [] (retrieve-sorted-records 3))
           (POST "/records" req [] (add-record-to-db req))
           (route/not-found "Not Found"))

(def api
  (-> (handler/site api-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))
