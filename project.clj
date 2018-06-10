(defproject gr "0.1.0-SNAPSHOT"
  :description "GR HW"
  :url "https://github.com/mgarland/gr"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :main ^:skip-aot gr.core
  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.2"]]}}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring "1.7.0-RC1"]
                 [ring/ring-core "1.7.0-RC1"]
                 [ring/ring-jetty-adapter "1.7.0-RC1"]]

  :plugins [[lein-ring "0.12.4"]]

  :ring {:handler gr.core/api}


  )
