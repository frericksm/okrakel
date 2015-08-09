(ns okrakel.contentui
  (:require [okrakel.data :as od]
            [okrakel.pages.login]
            [okrakel.pages.ranking]
            [okrakel.pages.matchdays]
            [okrakel.pages.groups]
            [okrakel.pages.settings]
            [okrakel.pages.table]
            [okrakel.pages.home]
            [rum :include-macros true]
            )
  (:import [goog.events EventType]))

(def pages {:login     {:title "Start"}
            :home      {:title "Übersicht"}
            :ranking   {:title "Rangliste"}
            :matchdays {:title "Spieltage"}
            :groups    {:title "Gruppen"}
            :settings  {:title "Einstellungen"}
            :table     {:title "Bundesliga-Tabelle"}
            })

(rum/defc select-view < rum/reactive [conn event-bus]
  (let [db (rum/react conn)
        p  (od/active-view db)]
    (cond
      (= p :login)     (okrakel.pages.login/view conn event-bus)
      (= p :ranking)   (okrakel.pages.ranking/view conn event-bus)
      (= p :matchdays) (okrakel.pages.matchdays/view conn event-bus)
      (= p :groups)    (okrakel.pages.groups/view conn event-bus)
      (= p :settings)  (okrakel.pages.settings/view conn event-bus)
      (= p :table)     (okrakel.pages.table/view conn event-bus)
      (= p :home)      (okrakel.pages.home/view conn event-bus)
      :else            (okrakel.pages.login/view conn event-bus))))
