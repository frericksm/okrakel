(defproject testreagent "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]

                 ;; UI
                 [org.clojure/clojurescript "1.7.58"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
   
                 ;; UI rum
                 [rum "0.2.7"]

                 ;; DB
                 [datascript "0.11.6"]

                 [com.cognitect/transit-cljs "0.8.220"]

                 ;; Components
                 [quile/component-cljs "0.2.4"]


                 [secretary "1.2.3"]
]
  
  :plugins [[lein-cljsbuild "1.0.6"]]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :compiler {
                                   :main  okrakel.app
                                   :output-to "target/client.js"
                                   :output-dir "target/client"
                                   :optimizations :none
                                   :source-map true
                                   ;;:source-map "client.js.map"
                                   ;;:warnings      {:single-segment-namespace false}
                                   }}]}
  )
