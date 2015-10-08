(ns okrakel.pages.table
  (:require [rum.core :as rum]))

(rum/defc view [db event-bus]
  [:div "Tabelle"])
