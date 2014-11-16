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

(defn view [db owner]
  (reify
    om/IRender
    (render [_]
      (let [event-bus (om/get-shared owner :event-bus)]
        (html
         [:div {;:class "slider"
                :style {:background "url(images/krake2.jpg)"
                        :background-size "100% 800px"
                        :background-repeat "no-repeat"}
                }

          " AAAAAAAAAAAAAAAAAAAAA"
          [:p]
          " AAAAAAAAAAAAAAAAAAAAA"
          [:p]          " AAAAAAAAAAAAAAAAAAAAA"
          [:p]          " AAAAAAAAAAAAAAAAAAAAA"
          [:p]          " AAAAAAAAAAAAAAAAAAAAA"
          [:p]
          ])))
    ))
