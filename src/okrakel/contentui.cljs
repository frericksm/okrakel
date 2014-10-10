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
      (let [event-bus (om/get-shared owner :event-bus)]
        (html
         [:u {:class "table-view"}
          [:li {:class "table-view-cell"}
           [:a {:class "navigate-right"
                :on-click (fn [e] (async/put! event-bus [:select-view :ranking]))}
            "Rangliste"]]
          [:li {:class "table-view-cell"}
           [:a {:class "navigate-right"
                :on-click (fn [e] (async/put! event-bus [:select-view :matchdays]))}
            "Spieltage"]]
          [:li {:class "table-view-cell"}
           [:a {:class "navigate-right"
                :on-click (fn [e] (async/put! event-bus [:select-view :groups]))}
            "Gruppen"]]
          [:li {:class "table-view-cell"}
           [:a {:class "navigate-right"
                :on-click (fn [e] (async/put! event-bus [:select-view :settings]))}
            "Einstellungen"]]
          ]
         )))))

(defn ranking-view [app-model owner ]
  (reify
    om/IRender
    (render [_]
      (let [event-bus (om/get-shared owner :event-bus)]
        (html
         [:u {:class "table-view"}
          [:li {:class "table-view-cell"}
           [:a {:class "navigate-right"
                :on-click (fn [e] (async/put! event-bus [:select-view :ranking]))}
            "Rangliste"]]
          [:li {:class "table-view-cell"}
           [:a {:class "navigate-right"
                :on-click (fn [e] (async/put! event-bus [:select-view :matchdays]))}
            "Spieltage"]]
          [:li {:class "table-view-cell"}
           [:a {:class "navigate-right"
                :on-click (fn [e] (async/put! event-bus [:select-view :groups]))}
            "Gruppen"]]
          [:li {:class "table-view-cell"}
           [:a {:class "navigate-right"
                :on-click (fn [e] (async/put! event-bus [:select-view :settings]))}
            "Einstellungen"]]
          ]
         ))))
)

(defn matchdays-view [app-model owner]
  (om/component (dom/p nil "Spieltage")))

(defn groups-view [app-model owner]
  (om/component (dom/p nil "Gruppen")))

(defn settings-view [app-model owner]
  (om/component (dom/p nil "Einstellungen")))

(defn select-view [app-model owner ]
  (let [p (:page app-model)]
    (cond
     (= p :ranking)   (ranking-view app-model owner)
     (= p :matchdays) (matchdays-view app-model owner)
     (= p :groups)    (groups-view app-model owner)
     (= p :settings)  (settings-view app-model owner)
     true             (home-view app-model owner)   
     )))
