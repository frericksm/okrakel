(ns okrakel.app
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [om.core :as om  :include-macros true]
            [om.dom :as dom :include-macros true]
            ;[ajax.core :refer (GET)]
            [sablono.core :as html :refer-macros [html]]
            [goog.events :as events])
  (:import [goog.events EventType]))

(defn activate-view [app-model new-view]
  (assoc app-model :page new-view))

(defn game-init
  ""
  []
  {:page :home
   :pages {:home      {:title "Okrakels Tippspiel"}
           :ranking   {:title "Rangliste"}
           :matchdays {:title "Spieltage"}
           :groups    {:title "Gruppen"}
           :settings  {:title "Einstellungen"}
           }
   :user {:name "Paul Krake"
          :groups "Hinter Thailand"
          :logged-in true}
   })


