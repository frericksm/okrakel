(ns okrakel.contentui
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [om.core :as om  :include-macros true]
            [om.dom :as dom :include-macros true]
            ;[ajax.core :refer (GET)]
            [sablono.core :as html :refer-macros [html]]
            [okrakel.app :as a]
            [goog.events :as events]

            [okrakel.pages.login]
            [okrakel.pages.ranking]
            [okrakel.pages.matchdays]
            [okrakel.pages.groups]
            [okrakel.pages.settings]
            [okrakel.pages.table]
            
            [okrakel.pages.home]
            )
  (:import [goog.events EventType]))

(def pages {:login     {:title "Anmelden"}
            :home      {:title "Übersicht"}
            :ranking   {:title "Rangliste"}
            :matchdays {:title "Spieltage"}
            :groups    {:title "Gruppen"}
            :settings  {:title "Einstellungen"}
            :table     {:title "Bundesliga-Tabelle"}
            })

(defn select-view [db owner]
  (let [p (a/active-view db)]
    (cond
     (= p :login)     (okrakel.pages.login/view db owner)
     (= p :ranking)   (okrakel.pages.ranking/view db owner)
     (= p :matchdays) (okrakel.pages.matchdays/view db owner)
     (= p :groups)    (okrakel.pages.groups/view db owner)
     (= p :settings)  (okrakel.pages.settings/view db owner)
     (= p :table)     (okrakel.pages.table/view db owner)
     :else            (okrakel.pages.home/view db owner)   
     ))
  )
