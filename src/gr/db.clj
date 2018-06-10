(ns gr.db
  (:require [gr.util :as util]))

(def db (atom {:data     {}
               :next-seq 1}))

(defn add [record]
  "Adds a record to the db"
  (println record)
  (swap! db
         (fn [d]
           (assoc d :data (assoc (:data d) (:next-seq d) record) :next-seq (inc (:next-seq d))))))

(defn retrieve-all []
  "Do a Select *"
  (:data @db))


(defn retrieve [record-key]
  "Returns a record by its key"
  (get (:data @db) record-key))

(defn delete [record-key]
  "Delete record by its key"
  (swap! db
         (fn [d]
           (assoc d :data (dissoc (:data d) record-key)))))

(defn upd [record-key record]
  "Update a record by its key"
  (swap! db
         (fn [d]
           (assoc d :data (assoc (:data d) record-key record) :next-seq (:next-seq d)))))
