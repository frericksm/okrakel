(ns okrakel.pages.table
  (:require [rum :include-macros true]))

(rum/defc view [db event-bus]
  [:div "Tabelle"])
