(ns okrakel.app2
  (:require [cljs.core.async :as async]
            [okrakel.ui2 :as ui]
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

;; ON PAGE LOAD

(defn ^:export start []
  (od/game-init conn)
  (ui/mount conn event-bus))
