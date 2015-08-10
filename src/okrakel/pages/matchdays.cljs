(ns okrakel.pages.matchdays
  (:require [rum :include-macros true]))

(rum/defc view [db event-bus]
  [:div "Spieltage"]) 

