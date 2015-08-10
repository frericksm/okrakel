(ns okrakel.ui
  (:require
    [cljs.core.async.macros :refer [go-loop]]))


(defmacro profile [k & body]
  `(let [k# ~k]
     (.time js/console k#)
     (let [res# (do ~@body)]
       (.timeEnd js/console k#)
       res#)))
