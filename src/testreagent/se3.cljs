(ns testreagent.se3
  (:require-macros [testreagent.se2 :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs.core.async :as async]
            [om.core :as om  :include-macros true]
            [om.dom :as dom :include-macros true]
            ;[ajax.core :refer (GET)]
            [sablono.core :as html :refer-macros [html]]
            [testreagent.game :as g]
            [goog.events :as events])
  (:import [goog.events EventType]))

(enable-console-print!)

;; event-bus
(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))

;; model
(def game-model (atom nil))

(defn title [game-model owner ]
  (reify
    om/IRender
    (render [_]
      (let [event-bus (om/get-shared owner :event-bus)]
        (html
         [:h1 {:class "title"}
          "Guess a number"])))))

(defn nav [game-model owner ]
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

(defn guess-number-game [game-model owner ]
  (reify
    om/IRender
    (render [_]
      (let [event-bus (om/get-shared owner :event-bus)]
        (html
         [:span
          [:div {:class "content-padded"}
           [:p "... between 1 and 100"]
           [:div (:message game-model)]]
          
          [:input {:type "number"
                   :placeholder "Your guess"
                   :value (:my-guess game-model)
                   :disabled (:game-over? game-model)
                   :on-key-down (fn [e]
                                  (if (= 13 (.-keyCode e))
                                    (async/put! event-bus [:make-guess])))
                   :on-change (fn [e] (async/put! event-bus [:update-guess (-> e .-target .-value int)]))}]
          [:button {:type "button"
                    :class "btn btn-positive btn-block"
                    :on-click (fn [e] (async/put! event-bus
                                                 (if (:game-over? @game-model)
                                                   [:reset-game]
                                                   [:make-guess])))}
           (if (:game-over? game-model) "Start" "Guess!")]]
         )))))

(defn app [game-model owner]
  (reify
    om/IRender
    (render [this]
      (dom/body nil 
       (om/build title game-model)
       (om/build guess-number-game game-model)))))

;; when game is started
(go-loop-sub event-bus-pub :reset-game [_]
             (reset! game-model (g/game-init)))

;; when button 'Rate' is clicked
(go-loop-sub event-bus-pub :make-guess [_] (swap! game-model g/make-guess))

;; when guessed number gets edited
(go-loop-sub event-bus-pub :update-guess [_ number]
             (swap! game-model (partial g/edit-my-guess number)))

;; Start the app
(defn run []
  (async/put! event-bus [:reset-game])

  (om/root title game-model
           {:target (.getElementById js/document "title")
            :shared {:event-bus event-bus }})

  (om/root nav game-model
           {:target (.getElementById js/document "nav")
            :shared {:event-bus event-bus }})
  
  (om/root guess-number-game game-model
           {:target (.getElementById js/document "content")
            :shared {:event-bus event-bus }}))

(run)

