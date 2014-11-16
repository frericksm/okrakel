(ns okrakel.ui
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [om.core :as om  :include-macros true]
            [om.dom :as dom :include-macros true]
            ;[ajax.core :refer (GET)]
            [sablono.core :as html :refer-macros [html]]
            [okrakel.app :as a]
            [okrakel.contentui :as cu]
            [okrakel.components.core :as comp]
            [goog.events :as events]
            [clojure.string])
  (:import [goog.events EventType]))

(enable-console-print!)

;; event-bus
(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))

;; when a view is selected
(go-loop-sub event-bus-pub :select-view [_ next-view]
             (a/activate-view next-view))

;; when game is started
(go-loop-sub event-bus-pub :reset-game [_]
             (a/game-init))

;; Start the app
(defn run []
  (async/put! event-bus [:reset-game])

  (om/root comp/title a/conn
           {:target (.getElementById js/document "title")
            :shared {:event-bus event-bus}})

  (om/root comp/nav a/conn
           {:target (.getElementById js/document "nav")
            :shared {:event-bus event-bus}})
  
  (om/root cu/select-view a/conn
           {:target (.getElementById js/document "content")
            :shared {:event-bus event-bus}}))

(run)

