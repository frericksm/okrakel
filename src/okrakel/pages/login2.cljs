(ns okrakel.pages.login2
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [rum :include-macros true]
            [cljs.core.async :as async]
            [okrakel.data :as od]
            [okrakel.dom :as dom]))

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

(rum/defc view [conn event-bus]
  (let [db   @conn
        name (od/login-name db)]
    [:div {:class "card"}
     [:form
          [:input.username {:type "text"
                   :placeholder "Email oder Username"
                   :value name
                   :auto-focus true
                   ;;:on-change (text-change #(update-login-name event-bus %))
                   }]
      [:input#password {:type "password"
               :placeholder "Passwort"
               :on-key-down (text-keydown #(login event-bus %))
               }]
      [:button {:class "btn btn-positive btn-block"
                :on-click (button-clicked 
                           (partial login event-bus  
                                    (dom/value (dom/q ".username"))
                                    ))
                }
       "Login"] 
      ]
     ]))
