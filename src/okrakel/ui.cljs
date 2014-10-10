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
            [goog.events :as events])
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
         [:h1 {:class "title" }
          (get-in app-model [:pages (:page app-model) :title])])))))

(defn nav [app-model owner ]
  (reify
    om/IRender
    (render [_]
      (let [event-bus (om/get-shared owner :event-bus)]
        (html
         [:nav {:class "bar bar-tab"}
          [:a {:class "tab-item active"}
           [:span {:class "icon icon-home"}]
           [:span {:class "tab-label"} "Home"]]
          [:a {:class "tab-item"}
           [:span {:class "icon icon-person"}]
           [:span {:class "tab-label"} "Profile"]]
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

