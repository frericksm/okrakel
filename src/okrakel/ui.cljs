(ns okrakel.ui
  (:require-macros [okrakel.eventbus :refer [go-loop-sub]])
  (:require
    [rum :include-macros true]
    [secretary.core :as secretary :include-macros true]
    [cljs.core.async :as async]
    [okrakel.components.core :as comp]
    [okrakel.eventbus :as e]
    [okrakel.data :as od]
    [okrakel.contentui :as cu]
    [quile.component :as components]

    [goog.events :as events]
    [goog.history.EventType :as EventType]
    )
  (:import goog.History))

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(defn init-routes [conn]

  (secretary/defroute "/" []
    (od/activate-view conn :home))

  (secretary/defroute "/login" []
    (od/activate-view conn :login)))


;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; COMPONENT DEFINITION
(defrecord UserInterface [event-bus database]
  components/Lifecycle
  (start [component]
    (let [in-ch  (:in-channel event-bus) 
          pub-ch (:publisher-channel event-bus)
          conn   (okrakel.database/conn database)]

      (init-routes conn)
      (hook-browser-navigation!)

      ;; when a view is selected
      (go-loop-sub pub-ch
                   :select-view 
                   [_ next-view]
                   (od/activate-view conn next-view))
      
      ;; when user logs in
      (go-loop-sub pub-ch
                   :login [_ name]
                   (if (not (nil? (od/login conn name)))
                     (async/put! in-ch [:select-view :home])))
      
      ;; RENDER MACHINERY
      ;; Mount ui
      (rum/mount (comp/title conn in-ch) 
                 (.getElementById js/document "title"))
      (rum/mount (comp/nav conn in-ch) 
                 (.getElementById js/document "nav"))
      (rum/mount (cu/select-view conn in-ch) 
                 (.getElementById js/document "content"))

      (secretary/dispatch! "/")

      ;;Assemble component
      component))

  (stop [component]
    component))

;; CONSTRUCTOR
(defn new-ui []
  (map->UserInterface {}))





