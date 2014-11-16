(ns okrakel.pages.ranking
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [om.core :as om  :include-macros true]
            [om.dom :as dom :include-macros true]
            ;[ajax.core :refer (GET)]
            [sablono.core :as html :refer-macros [html]]
            [okrakel.app :as a]
            [goog.events :as events])
  (:import [goog.events EventType]))


(defn view [db owner ]
  (reify
    om/IRender
    (render [_]
      (let [event-bus (om/get-shared owner :event-bus)]
        (html
         [:u {:class "table-view"}
          (for [[rank points username] (a/rankings db)]
            (let [label (str rank ". " username)
                  badge-label (str points " Pkt.")]
              [:li {:class "table-view-cell"}
               [:a {:class "navigate-right"
                    :on-click (fn [e] (async/put! event-bus [:select-view :ranking]))}
                label
                [:span {:class "badge"} badge-label]]]))])))))
