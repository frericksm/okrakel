(ns okrakel.ui
  (:require-macros [okrakel.eventbus :refer [go-loop-sub]])
  (:require
    [rum :include-macros true]
    [cljs.core.async :as async]
    [okrakel.components.core :as comp]
    [okrakel.eventbus :as e]
    [okrakel.data :as od]
    [okrakel.contentui :as cu]
    [quile.component :as components]
    ))



;; COMPONENT DEFINITION
(defrecord UserInterface [event-bus database]
  components/Lifecycle
  (start [component]
    (let [in-ch  (:in-channel event-bus) 
          pub-ch (:publisher-channel event-bus)
          conn   (okrakel.database/conn database)]

      ;; when a view is selected
      (go-loop-sub pub-ch
                   :select-view 
                   [_ next-view]
                   (od/activate-view conn next-view))
      
      ;; when user logs in
      (go-loop-sub pub-ch
                   :login [_ name]
                   (if (not (nil? (od/login conn name)))
                     (async/put! event-bus [:select-view :home])))
      
      ;; RENDER MACHINERY
      ;; Mount ui
      (rum/mount (comp/title conn in-ch) 
                 (.getElementById js/document "title"))
      (rum/mount (comp/nav conn in-ch) 
                 (.getElementById js/document "nav"))
      (rum/mount (cu/select-view conn in-ch) 
                 (.getElementById js/document "content"))


      ;;Assemble component
      component))

  (stop [component]
    component))

;; CONSTRUCTOR
(defn new-ui []
  (map->UserInterface {}))





