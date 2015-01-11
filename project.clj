(defproject testreagent "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]

                 ;; UI
                 [org.clojure/clojurescript "0.0-2511"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]

                 ;; UI om
                 [org.om/om "0.8.0"]
                 [sablono "0.2.22"]
                 [cljs-ajax "0.2.6"]
                 [datascript "0.5.1"]

                 ;; UI regent
                 [reagent "0.4.2"]]
  
  :plugins [[lein-cljsbuild "1.0.3"]]
  :hooks [leiningen.cljsbuild]
  :profiles {:prod {:cljsbuild
                    {:builds
                     {:client {:compiler
                               {:optimizations :advanced
                                :preamble ^:replace ["reagent/react.min.js"]
                                :pretty-print false}}}}}
             :srcmap {:cljsbuild
                      {:builds
                       {:client {:compiler
                                 {
                                  :source-map "target/client.js.map"
                                  }}}}}}
  :source-paths ["src"]
  
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :compiler {
                                   :output-to "target/client.js"
                                   :output-dir "target/client"
                                   :optimizations :none
                                   :source-map true}}]}
  )
