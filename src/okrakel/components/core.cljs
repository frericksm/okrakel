(ns okrakel.components.core
  (:require [cljs.core.async :as async]
            [okrakel.data :as od]
            [okrakel.contentui :as cu]
            [clojure.string]
            [rum :include-macros true]))

(rum/defc title < rum/reactive [conn in-ch]
  (let [db       (rum/react conn)
        av       (od/active-view db)
        loggedIn (not= :login av)]
    (if loggedIn
      [:span
       [:a {:class "icon icon-gear pull-right"
            :on-click (fn [e] (async/put! in-ch [:select-view :settings]))}]  
       [:h1 {:class "title" }
        (get-in cu/pages [av :title])]]
      [:span
       [:h1 {:class "title" }
        (get-in cu/pages [av :title])]])))

(rum/defc nav < rum/reactive [conn in-ch]
  (let [db       (rum/react conn)
        av       (od/active-view db)
        loggedIn (not= :login av)
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
         (let [active (= (:page tab) (od/active-view db))
               a-classes (if active "tab-item active" "tab-item")
               icon-classes (clojure.string/join " " ["icon" (:icon tab)])]
           [:a {:key (:page tab)
                :class a-classes
                :on-click (fn [e] (do  
                                    (async/put! in-ch [:select-view (:page tab)])
                                    (.preventDefault e)))}
            [:span {:class icon-classes}]
            [:span {:class "tab-label"} (:label tab)]]))
       ]
      [:nav {:class "bar bar-tab"}])
    ))
