(ns okrakel.contentui2
  (:require [okrakel.data :as a]
            [okrakel.pages.login2]
            [okrakel.pages.ranking2]
            [okrakel.pages.matchdays2]
            [okrakel.pages.groups2]
            [okrakel.pages.settings2]
            [okrakel.pages.table2]
            [okrakel.pages.home2]
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

(defn select-view [conn event-bus]
  (let [p  (a/active-view @conn)]
    (cond
      (= p :login)     (okrakel.pages.login2/view conn event-bus)
      (= p :ranking)   [(okrakel.pages.ranking2/view conn event-bus)]
      (= p :matchdays) [(okrakel.pages.matchdays2/view conn event-bus)]
      (= p :groups)    [(okrakel.pages.groups2/view conn event-bus)]
      (= p :settings)  [(okrakel.pages.settings2/view conn event-bus)]
      (= p :table)     [(okrakel.pages.table2/view conn event-bus)]
      (= p :home)      [(okrakel.pages.home2/view conn event-bus)]
      :else            [(okrakel.pages.login2/view conn event-bus)]   
     ))
  )
