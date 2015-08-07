(ns okrakel.components.core2
  (:require [cljs.core.async :as async]
            [okrakel.data :as a]
            [okrakel.contentui2 :as cu]
            [clojure.string]
            [rum :include-macros true]))

(rum/defc title < rum/reactive [conn event-bus]
  (let [db       (rum/react conn)
        loggedIn (not= :login (a/active-view db))]
    (if loggedIn
      [:span
       [:a {:class "icon icon-gear pull-right"
            :on-click (fn [e] (async/put! event-bus
                                         [:select-view :settings]))}]  
       [:h1 {:class "title" }
        (get-in cu/pages [(a/active-view db) :title])]]
      [:span
       [:h1 {:class "title" }
        (get-in cu/pages [(a/active-view db) :title])]])))

(rum/defc nav < rum/reactive [conn event-bus]
  (let [db       (rum/react conn)
        loggedIn (not= :login (a/active-view db))
        tabs     [{:page :home
                   :icon "icon-home"
                   :label "Home"}
                  {:page :ranking
                   :icon "icon-more-vertical"
                   :label "Ranking"}
                  {:page :table
                   :icon "icon-list"
                   :label "Tabelle"}]]
    (if loggedIn
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
       ]
      [:nav {:class "bar bar-tab"}])
    ))
