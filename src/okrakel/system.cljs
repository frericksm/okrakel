(ns okrakel.system
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [okrakel.ui :as ui]
            [okrakel.data :as od]
            [okrakel.persist :as p]
            [datascript :as d]))


(defn system []
  {:event-bus })




(defn ^:export main []

   

  (async/put! event-bus [:start]))
