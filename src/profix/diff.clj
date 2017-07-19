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

(defn list-common-products-for-nodes
  [products-by-node]
  (let [common-product-to-all? (partial common-product? products-by-node)
        all-products (into [] (flatten (vals products-by-node)))
        common-products (filter common-product-to-all? all-products)]
    (into #{} (map #(dissoc % :installTime) common-products))))

(defn get-ids-of-sources-products
  [source & sources]
  (let [all-sources (conj sources source)
        ids-of (fn [ps] (into #{} (map #(:id %) ps)))]
    (map ids-of all-sources)))

(defn compare-products
  [fn source & sources]
  (let [all-products-by-id (group-by :id (apply concat source sources))
        find-product #(first (get all-products-by-id %))
        sources-id (apply get-ids-of-sources-products source sources)
        common-products-id (apply fn sources-id)]
    (map find-product common-products-id)))

(defn list-common-products
  [repo & repos]
  (apply compare-products clojure.set/intersection repo repos))

(defn list-available-fixes
  [fixes-in-inventory fixes-in-repo]
  (remove
    #(fix/fix-already-installed? % fixes-in-inventory)
    fixes-in-repo))