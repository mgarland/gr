(ns gr.core
  (:require [gr.cli :as cli]
            [gr.api :as api]
            [gr.util :as util]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

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
      (let [sort-records (partial util/sort-record-fns (:sort args-map))]
        (-> (:file args-map)
            util/get-lines
            util/split-lines
            util/create-records
            sort-records
            util/format-for-display
            util/display))

      ; problem
      :else (println cli/usage))))
