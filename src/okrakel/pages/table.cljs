(ns okrakel.pages.table
  (:require [cljs.core.async :as async]
            [rum :include-macros true]))

(rum/defc view [db event-bus]
  [:div "Tabelle"])
