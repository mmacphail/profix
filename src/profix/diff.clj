(ns profix.diff
  (:require [profix.cce :as cce]
            [profix.resources :as rsc]))

(defn get-landscape-nodes
  [req]
  (:node (cce/get-resource req rsc/landscape-nodes)))

(defn list-inventory-products
  ([req]
   (list-inventory-products req ""))
  ([req node-alias]
   (:productInfo (:productInfos (cce/get-resource req rsc/inventory-products node-alias)))))

(defn extract-product-by-node
  [products-by-node [node-alias node-products]]
  (assoc products-by-node node-alias
                          (into [] (map :product node-products))))

(defn get-products-by-node
  [req]
  (let [products (group-by :nodeAlias (list-inventory-products req))]
    (reduce extract-product-by-node {} products)))

(defn product-present-in-each-node?
  [products-by-node product]
  (let [node-products (into [] (vals products-by-node))]
    (println (node-products))
    (every? #(contains? % product) node-products)))

(defn list-products-in-all-nodes
  [req]
  (let [products-by-node (get-products-by-node req)
        product-present? (partial product-present-in-each-node? products-by-node)
        all-products (into [] (flatten (vals products-by-node)))]
    (filter product-present? all-products)))