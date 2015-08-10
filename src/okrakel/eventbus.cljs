(ns okrakel.eventbus
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [quile.component :as components]
            [cljs.core.async :as async]))

;; PUBLIC API
(defmacro go-loop-sub [pub key binding & body]
 `(let [ch# (cljs.core.async/chan)]
    (cljs.core.async/sub ~pub ~key ch#)
    (go-loop []
      (let [~binding (cljs.core.async/<! ch#)]
        ~@body)
      (recur))))

;; COMPONENT DEFINITION
(defrecord EventBus []
  components/Lifecycle
  (start [component]
    (let [in-channel (async/chan)
          publisher-channel (async/pub in-channel first)]
      ;;Assemble component
      (as-> component x
        (assoc x :in-channel in-channel)
        (assoc x :publisher-channel publisher-channel))))

  (stop [component]
    (let [input-channel (:input-channel component)
          publisher-channel (:publisher-channel component)]
      (async/close! publisher-channel)
      (async/close! input-channel)
          
      ;;Disassemble component
      (as-> component x
        (dissoc x :input-channel)
        (dissoc x :publisher-channel)))))

;; CONSTRUCTOR
(defn new-event-bus []
  (map->EventBus {}))
