(defproject testreagent "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]

                 ;; UI
                 [org.clojure/clojurescript "1.7.48"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
   
                 ;; UI om
                 [org.om/om "0.8.0"]
                 [sablono "0.2.22"]
                 [cljs-ajax "0.2.6"]

                 ;; UI regent
                 [reagent "0.4.2"]

                 ;; UI rum
                 [rum "0.2.6"]

                 ;; DB
                 [datascript "0.11.6"]

]
  
  :plugins [[lein-cljsbuild "1.0.6"]]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :compiler {
                                   :main  okrakel.app2
                                   :output-to "target/client.js"
                                   :output-dir "target/client"
                                   :optimizations :none
                                   :source-map true
                                   ;;:source-map "client.js.map"
                                   :warnings      {:single-segment-namespace false}}}]}
  )
