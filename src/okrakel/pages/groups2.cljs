(ns okrakel.pages.groups2
  (:require [rum :include-macros true]
            [cljs.core.async :as async]
            [okrakel.data :as a]))

(rum/defc view [db event-bus]
  [:div "Gruppen"])

