(ns okrakel.pages.ranking
  (:require [rum.core :as rum :include-macros true]
            [cljs.core.async :as async]
            [okrakel.data :as d]))

(rum/defc view [conn in-ch]
  (let [db    @conn]
    [:u {:class "table-view"}
     (for [[rank points username] (d/rankings db)]
       (let [label (str rank ". " username)
             badge-label (str points " Pkt.")]
         [:li {:class "table-view-cell"}
          [:a {:class "navigate-right"
               :on-click (fn [e] (async/put! in-ch [:select-view :ranking]))}
           label
           [:span {:class "badge"} badge-label]]]))]))
