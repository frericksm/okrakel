;;http://bl.ocks.org/pleasetrythisathome/d1d9b1d74705b6771c20

(ns okrakel.browser
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [goog.events :as events]
            [clojure.string :as str]
            [cljs.core.async :refer [put! <! chan]])
  (:import goog.History
           goog.history.Html5History
           goog.history.Html5History.TokenTransformer
           goog.history.EventType))

(defn init-history
  "creates a new history object that correctly interprets urls"
  []
  (let [transformer (goog.history.Html5History.TokenTransformer.)]
    (set! (.. transformer -retrieveToken)
          (fn [path-prefix location]
            (str (.-pathname location) (.-search location))))
    (set! (.. transformer -createUrl)
          (fn [token path-prefix location]
            (str path-prefix token)))
    (doto (goog.history.Html5History. js/window transformer)
      (.setUseFragment false)
      (.setPathPrefix "")
      (.setEnabled true))))

(defn listen-navigation
  "returns a channel of navigation events"
  [history]
  (let [out (chan)]
    (events/listen history goog.history.EventType.NAVIGATE (fn [e]
                                                             (when (.-isNavigation e)
                                                               (put! out (.-token e)))))
    out))

(defn map->params [query]
  (let [params (map #(name %) (keys query))
        values (vals query)
        pairs (partition 2 (interleave params values))]
    (str/join "&" (map #(str/join "=" %) pairs))))

(defn navigate!
  "add a browser history entry. updates window/location"
  ([history route] (navigate! history route {}))
  ([history route query]
     (let [token (.getToken history)
           old-route (first (str/split token "?"))
           query-string (map->params (reduce-kv (fn [valid k v]
                                                  (if v
                                                    (assoc valid k v)
                                                    valid)) {} query))
           with-params (if (empty? query-string)
                         route
                         (str route "?" query-string))]
       (if (= old-route route)
         (. history (replaceToken with-params))
         (. history (setToken with-params))))))
