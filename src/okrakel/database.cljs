(ns okrakel.database
  (:require
   [quile.component :as component]
   [datascript.core :as d]))

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


;; PUBLIC API 

(defn conn [database]
  (:conn database))


;; COMPONENT DEFINITION
(defrecord Database []
  component/Lifecycle
  (start [component]
   (let [conn (d/create-conn schema)]

      ;;Assemble component
      (as-> component x
        (assoc x :conn conn))))

  (stop [component]
              
    ;;Disassemble component
    (as-> component x
      (dissoc x :conn))))


;; CONSTRUCTOR
(defn new-database []
  (map->Database {}))





