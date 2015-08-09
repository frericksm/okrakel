(ns okrakel.eventbus
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [okrakel.ui :as ui]
            [okrakel.data :as od]
            [okrakel.persist :as p]
            [okrakel.system :as system]
            [datascript :as d]))

(defrecord EventBus [])

(defn event-bus [event-bus event-bus-pub]
  (map->EventBus {}))


(defn listen-to [eventbus key callback]
  (let [ebp (:event-bus-pub eventbus)]
    (go-loop-sub ebp key msg
                 (callback msg))))

(defn start [system]
  (let [event-bus (async/chan)
        event-bus-pub (async/pub event-bus first)]

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

    ;; Start
    (async/put! event-bus [:start])

    ;;Assemble system
    (as-> system x
      (assoc x :event-bus event-bus)
      (assoc x :event-bus-pub event-bus-pub))))

(defn stop [system]
  (let [event-bus (:event-bus system
        event-bus-pub (:event-bus-pub system)]
        (async/close! event-bus)
        (async/close! event-bus)
    ;;Assemble system
    (as-> system x
      (dissoc x :event-bus)
      (dissoc x :event-bus-pub)))))
