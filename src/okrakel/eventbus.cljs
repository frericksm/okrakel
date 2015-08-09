(ns okrakel.eventbus
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [okrakel.ui :as ui]
            [okrakel.data :as od]
            [okrakel.persist :as p]
            [okrakel.system :as system]
            [datascript :as d]))


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




