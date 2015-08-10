(ns okrakel.eventbus
  (:require
   [cljs.core.async.macros :refer [go-loop]]))


;; PUBLIC API
(defmacro go-loop-sub [pub key binding & body]
 `(let [ch# (cljs.core.async/chan)]
    (cljs.core.async/sub ~pub ~key ch#)
    (go-loop []
      (let [~binding (cljs.core.async/<! ch#)]
        ~@body)
      (recur))))
