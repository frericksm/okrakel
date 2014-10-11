(ns okrakel.contentui
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [om.core :as om  :include-macros true]
            [om.dom :as dom :include-macros true]
            ;[ajax.core :refer (GET)]
            [sablono.core :as html :refer-macros [html]]
            [okrakel.app :as a]
            [goog.events :as events])
  (:import [goog.events EventType]))

(defn home-view [app-model owner ]
  (reify
    om/IRender
    (render [_]
      (let [event-bus (om/get-shared owner :event-bus)
            user (a/user app-model)
            name (:name user)
            points "78"
            ranking 449
            ]
        (html
         [:div
          [:div {:class "card"}
           [:div {:class "input-group"}
            [:div {:class "input-row"}
             [:label "Name"]
             [:input {:type "text"
                      :value name}]]
            [:div {:class "input-row"}
             [:label "Punkte"]
             [:input {:type "text"
                      :value points}]]
            [:div {:class "input-row"}
             [:label "Platzierung"]
             [:input {:type "text"
                      :value ranking}]]]
           [:p]
           [:button {:class "btn btn-primary btn-block"
                     :on-click (fn [e] (async/put! event-bus [:select-view :matchdays]))}
            "Jetzt tippen"]]
          [:div {:class "card"}
           [:u {:class "table-view"}
            [:li {:class "table-view-divider"} "Gruppen"]
            [:li {:class "table-view-cell"}
             [:a {:class "navigate"
                  :on-click (fn [e] (async/put! event-bus [:select-view :matchdays]))}
              "Hinter Thailand"]]
            ]]
          [:div {:class "card"}
           [:u {:class "table-view"}
            [:li {:class "table-view-cell"}
             [:a {:class "navigate-right"
                  :on-click (fn [e] (async/put! event-bus [:select-view :matchdays]))}
              "Spieltage"]]
            ]]
          
          ]
         )))))

(defn ranking-view [app-model owner ]
  (reify
    om/IRender
    (render [_]
      (let [event-bus (om/get-shared owner :event-bus)]
        (html
         [:u {:class "table-view"}
          (for [rank (:ranking app-model)]
            (for [user-id (:user-ids rank)]
              (let [user (a/user app-model user-id)
                    label (str  (:rank rank) ". " (:name user))
                    badge-label (str (:points rank) " Pkt.")]
                [:li {:class "table-view-cell"}
                 [:a {:class "navigate-right"
                      :on-click (fn [e] (async/put! event-bus [:select-view :ranking]))}
                  label
                  [:span {:class "badge"} badge-label]]])
              )
            
            
            )]
         )))))

(defn matchdays-view [app-model owner]
  (om/component (dom/p nil "Spieltage")))

(defn groups-view [app-model owner]
  (om/component (dom/p nil "Gruppen")))

(defn settings-view [app-model owner]
  (om/component (dom/p nil "Einstellungen")))

(defn table-view [app-model owner]
  (om/component (dom/p nil "Tabelle")))

(defn select-view [app-model owner ]
  (let [p (:page app-model)]
    (cond
     (= p :ranking)   (ranking-view app-model owner)
     (= p :matchdays) (matchdays-view app-model owner)
     (= p :groups)    (groups-view app-model owner)
     (= p :settings)  (settings-view app-model owner)
     (= p :table)     (table-view app-model owner)
     true             (home-view app-model owner)   
     )))
