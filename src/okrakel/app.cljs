(ns okrakel.app
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [quile.component :as components]
            [cljs.core.async :as async]
            [okrakel.ui :as ui]
            [okrakel.data :as od]
            [okrakel.persist :as p]
            [okrakel.system :as system]
            [datascript :as d]))

(enable-console-print!)

(defonce system nil)

(defn init
  "Constructs the current development system."
  []
  (alter-var-root #'system
    (constantly (system/system))))

(defn start
  "Starts the current development system."
  []
  (alter-var-root #'system component/start))

(defn stop []
  (alter-var-root #'system
    (fn [s] (when s (component/stop s)))))

(defn go
  "Initializes the current development system and starts it running."
  []
  (init)
  (start))

;; ON PAGE LOAD

(defn ^:export start []
  (go))
