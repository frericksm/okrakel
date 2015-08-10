(ns okrakel.system
  (:require [quile.component :as component]
            [okrakel.ui :as ui]
            [okrakel.data :as od]
            [okrakel.database]
            [okrakel.persist :as p]
            [okrakel.eventbus :as e]
            ))

(defn system [config-options]
  (component/system-map
   :database     (okrakel.database/new-database)
   :data-access  (component/using (okrakel.data/new-data-access) [:database :event-bus])
   :event-bus    (e/new-event-bus)
   :ui           (component/using (ui/new-ui) [:event-bus :database])
   ))
