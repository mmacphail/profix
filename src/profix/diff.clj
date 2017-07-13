(ns profix.diff
  (:require [profix.http :as http]
            [profix.resources :as rsc]
            [profix.fix :as fix]))

(defn common-product?
  [products-by-node product]
  (let [node-products (vals products-by-node)
        node-products-id (map #(into #{} (map :id %)) node-products)
        searched-id (:id product)]
    (every? #(contains? % searched-id) node-products-id)))

(defn list-common-products
  [products-by-node]
  (let [common-product-to-all? (partial common-product? products-by-node)
        all-products (into [] (flatten (vals products-by-node)))
        common-products (filter common-product-to-all? all-products)]
    (into #{} (map #(dissoc % :installTime) common-products))))

(defn list-available-fixes
  [fixes-in-inventory fixes-in-repo]
  ())



