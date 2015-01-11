(ns okrakel.pages.login
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

;; COMMUNICATION
(defn- login [chan name]
  (async/put! chan [:login name]))

(defn- update-login-name [chan name]
  (async/put! chan [:update 1 :app/login-name name]))

(defn- button-clicked [callback]
  (fn [e]
    (do
      (callback)
      (.preventDefault e))))

(defn- text-change [callback]
  (fn [e]
    (do
      (callback (.. e -target -value))
      (.preventDefault e))))

(defn- text-keydown [callback]
  (fn [e]
    (if (and (== (.-keyCode e) 13) ;; enter
             (not (.-shiftKey e))) ;; no shift
      (do
        (callback (.. e -target -value))
        (set! (.. e -target -value) "")
        (.preventDefault e)))))

;; UI

(defn view [db owner]
  (reify
    om/IRender
    (render [_]
      (let [event-bus (om/get-shared owner :event-bus)
            name (a/login-name db)]
        (html
         [:div {:class "card"}
          [:form
           [:input {:type "text"
                    :placeholder "Email oder Username"
                    :value name
                    :auto-focus true
                    ;:on-change (text-change #(update-login-name event-bus %))
                    }]
           [:input {:type "password"
                    :placeholder "Passwort"
                    :on-key-down (text-keydown #(login event-bus %))
                    }]
           [:button {:class "btn btn-positive btn-block"
                     :on-click (button-clicked (partial login event-bus name))}
            "Login"] 
           ]
          ])))
    ))



