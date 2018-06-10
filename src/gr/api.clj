(ns gr.api
  (:require [clojure.string :as str]
            [compojure.core :refer :all]
            [ring.middleware.json :as middleware]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

;;
;; Rest API
;;

(defn process [req]
  (let [name (or (get-in req [:params :name])
                 (get-in req [:body :name])
                 "John Doe")]
    {:status 200
     :body {:name name
            :desc (str "The name you sent to me was " name)}}))

(defroutes api-routes
           (GET "/" [] "Hello World")
           (GET "/records/gender" [] "gender")
           (GET "/records/birthdate" [] "birthdate")
           (GET "/records/name" [] "name")
           (POST "/records" req [] (process req))
           (route/not-found "Not Found"))

(def api
  (-> (handler/site api-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))
