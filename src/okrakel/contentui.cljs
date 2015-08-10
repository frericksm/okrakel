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

(rum/defc select-view < rum/reactive [conn in-ch]
  (let [db (rum/react conn)
        p  (od/active-view db)]
    (cond
      (= p :login)     (okrakel.pages.login/view conn in-ch)
      (= p :ranking)   (okrakel.pages.ranking/view conn in-ch)
      (= p :matchdays) (okrakel.pages.matchdays/view conn in-ch)
      (= p :groups)    (okrakel.pages.groups/view conn in-ch)
      (= p :settings)  (okrakel.pages.settings/view conn in-ch)
      (= p :table)     (okrakel.pages.table/view conn in-ch)
      (= p :home)      (okrakel.pages.home/view conn in-ch)
      :else            (okrakel.pages.login/view conn in-ch))))
