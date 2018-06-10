(defproject gr "0.1.0-SNAPSHOT"
  :description "GR HW"
  :url "https://github.com/mgarland/gr"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :main ^:skip-aot gr.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}

  :dependencies [[org.clojure/clojure "1.9.0"]]
  )
