(ns okrakel.components.core
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [om.core :as om  :include-macros true]
            [om.dom :as dom :include-macros true]
            ;[ajax.core :refer (GET)]
            [sablono.core :as html :refer-macros [html]]
            [okrakel.app :as a]
            [okrakel.contentui :as cu]
            [goog.events :as events]
            [clojure.string])
  (:import [goog.events EventType]))

(enable-console-print!)

(defn title [db owner]
  (reify
    om/IRender
    (render [_]
      (if (= :login (a/active-view db))
        (html[:span
              [:h1 {:class "title" }
               (get-in cu/pages [(a/active-view db) :title])]])
        (let [event-bus (om/get-shared owner :event-bus)]
          (html
           [:span
            [:a {:class "icon icon-gear pull-right"
                 :on-click (fn [e] (async/put! event-bus
                                              [:select-view :settings]))}]  
            [:h1 {:class "title" }
             (get-in cu/pages [(a/active-view db) :title])]]))))))

(defn nav [db owner ]
  (reify
    om/IRender
    (render [_]
      (if (= :login (a/active-view db))
        (html
             [:nav {:class "bar bar-tab"}])
        (let [event-bus (om/get-shared owner :event-bus)
                tabs [{:page :home
                       :icon "icon-home"
                       :label "Home"}
                      {:page :ranking
                       :icon "icon-more-vertical"
                       :label "Ranking"}
                      {:page :table
                       :icon "icon-list"
                       :label "Tabelle"}]]
            (html
             [:nav {:class "bar bar-tab"}
              (for [tab tabs]
                (let [active (= (:page tab) (a/active-view db))
                      a-classes (if active "tab-item active" "tab-item")
                      icon-classes (clojure.string/join " " ["icon" (:icon tab)])]
                  [:a {:class a-classes
                       :on-click (fn [e] (async/put! event-bus
                                                    [:select-view (:page tab)]))}
                   [:span {:class icon-classes}]
                   [:span {:class "tab-label"} (:label tab)]]))
              ]))))))
