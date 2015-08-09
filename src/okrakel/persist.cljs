(ns okrakel.persist
  (:require
   [datascript :as d]
   [cognitect.transit :as transit]
   [okrakel.dom :as dom]
   [okrakel.util :as u])
  (:require-macros
   [okrakel.ui :refer [profile]]))


(declare persist)

(defn reset-conn! [conn db]
  (reset! conn db)
  ;(render db)
  (persist db))

;; Entity with id=0 is used for storing auxilary view information
;; like filter value and selected group

#_(defn set-system-attrs! [& args]
  (d/transact! conn 
    (for [[attr value] (partition 2 args)]
      (if value
        [:db/add 0 attr value]
        [:db.fn/retractAttribute 0 attr]))))

#_(defn system-attr
  ([db attr]
    (get (d/entity db 0) attr))
  ([db attr & attrs]
    (mapv #(system-attr db %) (concat [attr] attrs))))

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

(defonce conn (d/create-conn schema))

;; History

(defonce history (atom []))
(def ^:const history-limit 10)

;; history



;; transit serialization

(deftype DatomHandler []
  Object
  (tag [_ _] "datascript/Datom")
  (rep [_ d] #js [(.-e d) (.-a d) (.-v d) (.-tx d)])
  (stringRep [_ _] nil))

(def transit-writer
  (transit/writer :json
    { :handlers
      { datascript.core/Datom (DatomHandler.)
        datascript.btset/BTSetIter (transit/VectorHandler.) }}))

(def transit-reader
  (transit/reader :json
    { :handlers
      { "datascript/Datom" (fn [[e a v tx]] (d/datom e a v tx)) }}))

(defn db->string [db]
  (profile "db serialization"
    (transit/write transit-writer (d/datoms db :eavt))))

(defn string->db [s]
  (profile "db deserialization"
    (let [datoms (transit/read transit-reader s)]
      (d/init-db datoms schema))))

;; persisting DB between page reloads
(defn persist [db]
  (js/localStorage.setItem "datascript-todo/db" (db->string db)))




(defn init [conn]
  
  (d/listen! conn :persistence
             (fn [tx-report] ;; FIXME do not notify with nil as db-report
               ;; FIXME do not notify if tx-data is empty
               (when-let [db (:db-after tx-report)]
                 (js/setTimeout #(persist db) 0))))

  ;; restoring once persisted DB on page load
  (if-let [stored (js/localStorage.getItem "datascript-todo/db")]
    (do
      (reset-conn! conn (string->db stored))
      (swap! history conj @conn))
    (d/transact! conn u/fixtures))


  (d/listen! conn :history
             (fn [tx-report]
               (let [{:keys [db-before db-after]} tx-report]
                 (when (and db-before db-after)
                   (swap! history (fn [h]
                                    (-> h
                                        (u/drop-tail #(identical? % db-before))
                                        (conj db-after)
                                        (u/trim-head history-limit))))))))
  )

#_(js/localStorage.clear)


