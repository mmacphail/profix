(ns profix.cce
  (:require [profix.http :as http]
            [profix.resources :as rsc]))

(defn get-landscape-nodes
  [req]
  (:node (http/get-resource req rsc/landscape-nodes)))

(defn list-inventory-products
  ([req]
   (list-inventory-products req ""))
  ([req node-alias]
   (:productInfo (:productInfos (http/get-resource req rsc/inventory-products node-alias)))))

(defn extract-product-by-node
  [products-by-node [node-alias node-products]]
  (assoc products-by-node node-alias
                          (into [] (map :product node-products))))

(defn get-products-by-node
  [req]
  (let [products (group-by :nodeAlias (list-inventory-products req))]
    (reduce extract-product-by-node {} products)))

(defn list-repository-fixes
  [req fixes-repository node-alias platform]
  (let [params (format "/%s?nodeAlias=%s&platform=%s&products=INSTALLED"
                       fixes-repository
                       node-alias
                       platform)
        rsc (str rsc/repository-fixes-content params)]
    (-> (http/get-resource req rsc)
        :artifacts
        :artifact)))

(defn list-inventory-fixes
  ([req]
   (-> (http/get-resource req rsc/inventory-fixes)
       :fixInfos
       :fixInfo))
  ([req node-alias]
   (->> (list-inventory-fixes req)
        (filter #(= (:nodeAlias %) node-alias)))))

(defn list-detailed-inventory-fixes
  ([req]
    (map :fix (list-inventory-fixes req)))
  ([req node-alias]
    (map :fix (list-inventory-fixes req node-alias))))