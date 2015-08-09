(ns okrakel.pages.home
  (:require [cljs.core.async :as async]
            [rum :include-macros true]
            [okrakel.data :as od]))

(rum/defc view < rum/reactive [conn event-bus]
  (let [db   (rum/react conn)
        user (od/user db)
        uname (:user/name user)
        [ranking points] (od/ranking db (get user :user/id))
        ]
    [:div {:class "card"}
     [:div {:class "table-view"}
      [:div {:class "table-view-divider"} "Meine Daten"]
      [:div {:class "input-group"}
       [:div {:class "input-row"}
        [:label "Name"]
        [:input {:type "text"
                 :value uname}]]
       [:div {:class "input-row"}
        [:label "Punkte"]
        [:input {:type "text"
                 :value points}]]
       [:div {:class "input-row"}
        [:label "Gesamtplatzierung"]
        [:input {:type "text"
                 :value ranking}]]]

      [:div {:class "table-view-divider"} "Meine Gruppen"]
      [:div {:class "table-view-cell"}
       [:a {:class "navigate-right"
            :on-click (fn [e] (do 
                                (async/put! event-bus [:select-view :groups])
                                (.preventDefault e)))}
        "Hinter Thailand"]]
      [:div {:class "table-view-divider"} "Spieltage"]
      [:div {:class "table-view-cell"}
       [:a {:class "navigate-right"
            :on-click (fn [e] (async/put! event-bus [:select-view :matchdays]))}
        "Nächster Spieltag"]]

      [:div {:class "table-view-cell"}
       [:a {:class "navigate-right"
            :on-click (fn [e] (async/put! event-bus [:select-view :matchdays]))}
        "Übersicht"]]
      ]
     
     ;;[:p]
     
     ;;[:button {:class "btn btn-primary btn-block"         :on-click (fn [e] (async/put! event-bus [:select-view :matchdays]))}     "Jetzt tippen"]
     
     
     ])


  )
