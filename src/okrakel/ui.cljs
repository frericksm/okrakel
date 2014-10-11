(ns okrakel.ui
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

;; event-bus
(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))

;; model
(def app-model (atom nil))

(defn title [app-model owner ]
  (reify
    om/IRender
    (render [_]
      (let [event-bus (om/get-shared owner :event-bus)]
        (html
         [:span
          [:a {:class "icon icon-gear pull-right"
               :on-click (fn [e] (async/put! event-bus
                                                [:select-view :settings]))}]  
          [:h1 {:class "title" }
           (get-in app-model [:pages (:page app-model) :title])]])))))

(defn nav [app-model owner ]
  (reify
    om/IRender
    (render [_]
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
            (let [active (= (:page tab) (:page app-model))
                  a-classes (if active "tab-item active" "tab-item")
                  icon-classes (clojure.string/join " " ["icon" (:icon tab)])]
              [:a {:class a-classes
                   :on-click (fn [e] (async/put! event-bus
                                                [:select-view (:page tab)]))}
               [:span {:class icon-classes}]
               [:span {:class "tab-label"} (:label tab)]]))
          ])))))

;; when game is started
(go-loop-sub event-bus-pub :select-view [_ next-view]
             (swap! app-model a/activate-view next-view))

;; when game is started
(go-loop-sub event-bus-pub :reset-game [_]
             (reset! app-model (a/game-init)))

;; Start the app
(defn run []
  (async/put! event-bus [:reset-game])

  (om/root title app-model
           {:target (.getElementById js/document "title")
            :shared {:event-bus event-bus }})

  (om/root nav app-model
           {:target (.getElementById js/document "nav")
            :shared {:event-bus event-bus }})
  
  (om/root cu/select-view app-model
           {:target (.getElementById js/document "content")
            :shared {:event-bus event-bus }}))

(run)

