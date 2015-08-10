(ns okrakel.server
  (:require
   [cljs.reader]
   [cljs.core.async :as async]
   [datascript :as d]
   [okrakel.utils :as u])
  (:import
   [goog.net XhrIo])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]] ))


#_(def fixtures [
  {:room/title "World domination"     :room/source "web/rooms/world_domination.edn"}
  {:room/title "Pussies"              :room/source "web/rooms/pussies.edn"}
  {:room/title "Internet of cats"     :room/source "web/rooms/cats.edn"}
  {:room/title "Paw care"             :room/source "web/rooms/paw_care.edn"}
  {:room/title "Puss in Boots sequel" :room/source "web/rooms/puss_in_boots.edn"}
  {:room/title "Afterlife"            :room/source "web/rooms/afterlife.edn"}
  {:user/name "Starry"             :user/avatar "web/avatars/a1.jpg"}
  {:user/name "Friar Tuck"         :user/avatar "web/avatars/a2.jpg"}
  {:user/name "Toom"               :user/avatar "web/avatars/a3.jpg"}
  {:user/name "Hansel"             :user/avatar "web/avatars/a4.jpg"}
  {:user/name "Cuddlebug"          :user/avatar "web/avatars/a5.jpg"}
  {:user/name "Georgie"            :user/avatar "web/avatars/a6.jpg"}
  {:user/name "Jean-Paul Gizmondo" :user/avatar "web/avatars/a7.jpg"}
  {:user/name "Gorgeous Furboy"    :user/avatar "web/avatars/a8.jpg"}
  {:user/name "Jiggle Belly"       :user/avatar "web/avatars/a9.jpg"}
  {:user/name "Invitation"         :user/avatar "web/avatars/a10.jpg"}
  {:user/name "The Phantom"        :user/avatar "web/avatars/a11.jpg"}
  {:user/name "Rupert"             :user/avatar "web/avatars/a12.jpg"}
  {:user/name "Obstinate"          :user/avatar "web/avatars/a13.jpg"}
  {:user/name "Bunter"             :user/avatar "web/avatars/a14.jpg"}
  {:user/name "Porsche"            :user/avatar "web/avatars/a15.jpg"}
  {:user/name "Puka"               :user/avatar "web/avatars/a16.jpg"}
  {:user/name "Tabba To"           :user/avatar "web/avatars/a17.jpg"}
  {:user/name "Artful Dodger"      :user/avatar "web/avatars/a18.jpg"}
  {:user/name "Half Hot Chocolate" :user/avatar "web/avatars/a19.jpg"}
  {:user/name "Budmeister"         :user/avatar "web/avatars/a20.jpg"}
  {:user/name "Scsi2"              :user/avatar "web/avatars/a21.jpg"}
  {:user/name "BigMouth"           :user/avatar "web/avatars/a22.jpg"}
  {:user/name "Splinter"           :user/avatar "web/avatars/a23.jpg"}
  {:user/name "Isidor"             :user/avatar "web/avatars/a24.jpg"}
  {:user/name "Chanel"             :user/avatar "web/avatars/a25.jpg"}
])

;; UTILS

#_(defn- ajax [url callback]
  (.send goog.net.XhrIo url
    (fn [reply]
      (-> (.-target reply)
          (.getResponseText)
          (cljs.reader/read-string)
          (callback)))))

#_(defn- rand-n [min max]
  (+ min (rand-int (- max min))))

#_(defn- rand-pred [pred f]
  (let [x (f)]
    (if (pred x) x (recur pred f))))

#_(defn- later [f]
  (js/setTimeout f (rand-n 1000 2000)))

;; FIXTURES

#_(def conn (d/create-conn {
  :room/messages {:db/cardinality :db.cardinality/many}
}))

;; pre-populate rooms list and user names
#_(d/transact! conn fixtures)

;; load all room messages variants
#_(doseq [[id url title] (u/-q '[:find ?id ?src ?title
                               :where [?id :room/source ?src]
                                      [?id :room/title ?title]] @conn)]
  (ajax url
    (fn [msgs]
      (println "Loaded messages for room" id title url)
      (d/transact! conn [ {:db/id id
                           :room/messages msgs} ]))))

;; GENERATORS

#_(defn- rand-user-id [db]
  (d/q '[:find  (rand ?id) .
         :where [?id :user/name]]
       db))

#_(defn- rand-room [db]
  (d/q '[:find  (rand ?id) .
         :where [?id :room/title]]
       db))

#_(defn- rand-message [db room-id]
  (-> (d/datoms db :aevt :room/messages room-id) (rand-nth) :v))

;; HELPERS

#_(defn- user-by-id [db id]
  (-> (d/entity db id)
      (select-keys [:db/id :user/name :user/avatar])))

;; "REST" API

#_(def ^:private me (atom nil))

#_(defn call
  "Used to emulate async server calls"
  [f args callback]
  (later #(callback (apply f args))))

#_(defn get-rooms
  "Return list of rooms"
  []
  (->> @conn
    (u/-q '[:find ?id ?title
            :where [?id :room/title ?title]])
    (mapv #(zipmap [:db/id :room/title] %))))

#_(defn get-user
  "Return specific user entity"
  [id]
  (user-by-id @conn id))

#_(defn whoami
  "Return current user entity"
  []
  (let [db @conn
        id (or @me (reset! me (rand-user-id db)))]
    (user-by-id db id)))


;; MESSAGING

#_(def ^:private next-msg-id (atom 10000))
#_(def ^:private msgs-chan (async/chan))

#_(defn send
  "Send message to server"
  [msg]
  (async/put! msgs-chan
    (assoc msg
      :db/id (swap! next-msg-id inc)
      :message/timestamp (js/Date.))))

#_(go-loop []
  (<! (async/timeout (rand-n 500 1500)))
  (let [db @conn
        room-id   (rand-room db)
        text      (rand-message db room-id)
        author-id (rand-pred #(not= % @me) #(rand-user-id db))
        msg     { :db/id             (swap! next-msg-id inc)
                  :message/text      text
                  :message/author    author-id
                  :message/room      room-id
                  :message/unread    true
                  :message/timestamp (js/Date.) }]
    (when text
      (>! msgs-chan msg)))
  (recur))

#_(defn subscribe
  "Subscribe for server messages push"
  [on-msg]
  (go-loop []
    (let [msg (<! msgs-chan)]
      (on-msg msg))
    (recur)))
