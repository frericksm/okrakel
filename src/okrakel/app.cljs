(ns okrakel.app
  (:require-macros [okrakel.ui :refer [go-loop-sub]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async]
            [om.core :as om  :include-macros true]
            [om.dom :as dom :include-macros true]
            ;[ajax.core :refer (GET)]
            [sablono.core :as html :refer-macros [html]]
            [goog.events :as events])
  (:import [goog.events EventType]))

(defn activate-view [app-model new-view]
  (assoc app-model :page new-view))


(defn user
  ([app-model id]
     (->> app-model
          :users
          (filter #(= id (:id %)))
          first))
  ([app-model]
     (user app-model (get-in app-model [:user :id]))))

(defn game-init
  ""
  []
  {:page :home
   :pages {:home      {:title "Meine Daten"}
           :ranking   {:title "Rangliste"}
           :matchdays {:title "Spieltage"}
           :groups    {:title "Gruppen"}
           :settings  {:title "Einstellungen"}
           :table     {:title "Bundesliga-Tabelle"}
           }
   :user {:id 2}

   :users [{:id 1 :name "Hummi"      }
           {:id 2 :name "Paul Krake" }]
   
   :ranking [{:rank 1 :points 87 :user-ids [1]}
             {:rank 2 :points 78 :user-ids [2]}]
   
   :groups [{:id 1 :name "Hinter Thailand"
             :member-ids [1 2] }]

   :spielplan [{:spieltag 1
                :spiele []}
               ]
   
   })


