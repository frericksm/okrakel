(ns okrakel.pages.settings
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
  (om/component (dom/p nil "Einstellungen")))

