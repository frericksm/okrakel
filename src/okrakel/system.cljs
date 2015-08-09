(ns okrakel.system
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [quile.component :as components]
            [cljs.core.async :as async]
            [okrakel.ui :as ui]
            [okrakel.data :as od]
            [okrakel.persist :as p]
            [okrakel.eventbus :as e]
            [datascript :as d]))


(defn system [config-options]
  (let [{:keys [host port]} config-options]
    (component/system-map
     :event-bus (e/event-bus)
     ;;:scheduler (new-scheduler)
     ;;:app (component/using
     ;;      (example-component config-options)
     ;;      {:database  :db
     ;;       :scheduler :scheduler}))))
