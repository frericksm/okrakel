(ns okrakel.app
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
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
  (alter-var-root #'system system/start))

(defn stop
  "Shuts down and destroys the current development system."
  []
  (alter-var-root #'system
    (fn [s] (when s (system/stop s)))))

(defn go
  "Initializes the current development system and starts it running."
  []
  (init)
  (start))


;; EVENT BUS

(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))


;; when a view is selected
(go-loop-sub event-bus-pub :select-view [_ next-view]
             (od/activate-view conn next-view))

;; when game is started
(go-loop-sub event-bus-pub :start [_]
             (od/init conn)
             (p/init conn)
             (ui/mount conn event-bus))

;; when game is started
(go-loop-sub event-bus-pub :update [_ e a v]
             (od/update-entity conn e a v))

;; when user logs in
(go-loop-sub event-bus-pub :login [_ name]
             (if (not (nil? (od/login conn name)))
               (async/put! event-bus [:select-view :home])))

;; ON PAGE LOAD

(defn ^:export start []
  (go)
  
  (async/put! event-bus [:start]))
