(ns okrakel.data
  (:require [datascript :as d]))


(defn- app-entity-id [db]
  (ffirst (d/q '[:find  ?e
                 :where [?e :app/page]]
               db)))

(defn user
  ([db] (as-> (d/q '[:find  ?u
                     :where
                     [_ :app/user ?u]]
                   db)
              x
              (ffirst x)
              (if (not (nil? x))
                (d/entity db x))))
  
  ([db id] (if (not (nil? id))
             (->> (d/q '[:find  ?e
                         :in $ ?id 
                         :where
                         [?e :user/id ?id]]
                       db id)
                  ffirst
                  (d/entity db)))))

(defn login-name [db]
  (ffirst (d/q '[:find  ?name
                 :where [_ :app/login-name ?name]]
               db)))

(defn login [conn name]
  (let [db @conn
        e (ffirst (d/q '[:find  ?e
                         :in $ ?name
                         :where [?e :user/name ?name]]
                       db name))
        a (app-entity-id db)]
    (if (not (nil? e))
      (as-> (d/transact! conn [{:db/id a
                                :app/user e} ])
          x
        (user (:db-after x))))))

(defn active-view [db]
  (let [page (ffirst (d/q '[:find ?page
                            :where [_ :app/page ?page]]
                          db))]
    (if (nil? page) :login page)))

(defn update-entity [conn e a v]
  (d/transact! conn {:db/id e a v} ))

(defn activate-view [conn new-view]
  (let [db @conn
        e (app-entity-id db)]
      (d/transact! conn [ {:db/id e
                           :app/page new-view} ])))

(defn rankings [db]
  (as-> (d/q '[:find  ?r ?p ?un
               :where
               [?e :ranking/rank ?r]
               [?e :ranking/points ?p]
               [?e :ranking/users ?u]
               [?u :user/name ?un]]
             db) x
             (sort-by first x)))

(defn ranking [db user-id]
  (if (not  (nil? user-id))
    (as-> (d/q '[:find ?r ?p 
                 :in $ ?uid
                 :where
                 [?e :ranking/rank ?r]
                 [?e :ranking/points ?p]
                 [?e :ranking/users ?u]
                 [?u :user/id ?uid]]
               db user-id) x
               (first x))))

(defn game-init
  ""
  [conn]
  (d/transact! conn
               [{:db/id 1
                 :app/page :login}

                {:db/id -2
                 :user/id 1
                 :user/name "Hummi"}
                {:db/id -3
                 :user/id 2
                 :user/name "Paul Krake"}
                {:db/id -4
                 :user/id 3
                 :user/name "Chipie 1967"}
                {:db/id -5
                 :user/id 4
                 :user/name "Max 2003"} 
                
                {:db/id -100
                 :group/name "Hinter Thailand"
                 :group/members [-2 -3 -4 -5]}
                
                {:db/id -200
                 :spiel/spieltag 1
                 :spiel/heim "Schalke 04"
                 :spiel/gast "Bayern München"}

                {:db/id -1000
                 :ranking/rank 1
                 :ranking/points 87
                 :ranking/users [-2]}
                {:db/id -1001
                 :ranking/rank 2
                 :ranking/points 79
                 :ranking/users [-3]}
                {:db/id -1002
                 :ranking/rank 3
                 :ranking/points 78
                 :ranking/users [-4]}
                {:db/id -1003
                 :ranking/rank 4
                 :ranking/points 72
                 :ranking/users [-5]}]
               ))

