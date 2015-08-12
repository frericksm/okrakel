(ns okrakel.app
  (:require [quile.component :as component]
            [okrakel.system :as system]))

(enable-console-print!)

(defonce system-atom (atom nil))

;; ON PAGE LOAD
(defn ^:export start []
  (compare-and-set! system-atom nil (component/start (system/system nil))))


(start)
