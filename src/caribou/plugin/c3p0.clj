(ns caribou.plugin.c3p0
  (:require [caribou.plugin.protocol :as plugin]
            [org.tobereplaced.jdbc-pool :as pool]))

(defrecord C3p0Plugin [make-pool])

(plugin/make C3p0Plugin
             {:update-config (fn [this config]
                               (let [db (:database config)]
                                 (assoc config
                                   :database ((:make-pool this) db)
                                   :raw-database db)))})

(defn create [& opts]
  (let [opts (merge opts {:max-statements 33
                          :max-connections 12})
        pooler (fn pooler [config]
                 (apply pool/pool [config]))]
    (C3p0Plugin. pooler)))
