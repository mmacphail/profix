(ns profix.api
  (:require [profix.store :as store]
            [profix.cce :as cce]))

(defmulti load-source (fn [source] (:from source)))

(defmethod load-source :store [source]
  (let [{name :name} source]
    (store/load-json name)))

(defmethod load-source :repo [source]
  (let [{repo :name, req :req} source]
    (cce/list-repository-products-content req repo)))

(defmethod load-source :inventory [source]
  (let [{node :name, req :req} source]
    (cce/list-detailed-inventory-products req node)))

(defmethod load-source :default [source]
  (store/load-json source))

(defmulti save (fn [source] (:from source)))
(defmethod save :store [source] (println "This source is already written in the store"))
(defmethod save :repo [source] (store/store-json (:name source) (load-source source)))
(defmethod save :inventory [source] (store/store-json (:name source) (load-source source)))
(defmethod save :mem [source] (store/store-json (:name source) (:content source)))

(defn store-src [name] {:from :store, :name name})

(defn repo-src [name req] {:from :repo, :name name, :req req})

(defn inv-src [node-alias req] {:from :inventory, :name node-alias, :req req})

(defn mem-src [name content] {:from :mem, :name name, :content content})

(defn save-store [name content] (save (mem-src name content)))

(defn alias-source [source alias]
  (let [mem (mem-src alias (load-source source))]
    (save mem)))