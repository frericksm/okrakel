(ns okrakel.pages.groups
  (:require [rum.core :as rum :include-macros true]))

(rum/defc view [db event-bus]
  [:div "Gruppen"])

