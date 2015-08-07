(ns okrakel.pages.settings2
  (:require [cljs.core.async :as async]
            [rum :include-macros true]))


(rum/defc view [db event-bus]
  [:div "Einstellungen"])

