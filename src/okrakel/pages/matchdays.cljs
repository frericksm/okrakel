(ns okrakel.pages.matchdays
    (:require [cljs.core.async :as async]
            [okrakel.data :as a]
            [rum :include-macros true]
       ))


(rum/defc view [db event-bus]
  [:div "Spieltage"]) 

