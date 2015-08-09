(ns okrakel.app
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [okrakel.ui :as ui]
            [okrakel.data :as od]
            [datascript :as d]))

(enable-console-print!)

;; DB
(def schema 
  {:app/page  {}
   :app/group {}
   :app/user  {:db/valueType :db.type/ref}
   :app/login-name   {}
   
   :user/id         {}
   :user/name       {}
   :ranking/rank    {}
   :ranking/points  {}
   :ranking/users   {:db/valueType :db.type/ref
                     :db/cardinality :db.cardinality/many}
   :group/name      {}
   :group/members   {:db/valueType :db.type/ref
                     :db/cardinality :db.cardinality/many}
   :spiel/spieltag  {}
   :spiel/beginn-timestamp    {}
   :spiel/heim      {} 
   :spiel/gast      {}
   :spiel/tore-heim {}
   :spiel/tore-gast {}
   })

(def conn (d/create-conn schema))

;; EVENT BUS

(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))


;; when a view is selected
(go-loop-sub event-bus-pub :select-view [_ next-view]
             (od/activate-view conn next-view))

;; when game is started
(go-loop-sub event-bus-pub :start [_]
             (od/game-init conn))

;; when game is started
(go-loop-sub event-bus-pub :update [_ e a v]
             (od/update-entity conn e a v))

;; when user logs in
(go-loop-sub event-bus-pub :login [_ name]
             (if (not (nil? (od/login conn name)))
               (async/put! event-bus [:select-view :home])))

;; ON PAGE LOAD

(defn ^:export start []
  (async/put! event-bus [:start])
  (ui/mount conn event-bus))
