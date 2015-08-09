(ns okrakel.ui
  (:require
    [rum :include-macros true]
    [okrakel.components.core :as comp]
    [okrakel.contentui :as cu] ))

;; RENDER MACHINERY

(defn mount [conn event-bus]
  (rum/mount (comp/title conn event-bus) (.getElementById js/document "title"))
  (rum/mount (comp/nav conn event-bus) (.getElementById js/document "nav"))
  (rum/mount (cu/select-view conn event-bus) (.getElementById js/document "content")))
