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

(defn get-ids-of-repos
  [repo & repos]
  (let [all-repos (conj repos repo)
        ids-of (fn [ps] (into #{} (map #(:id %) ps)))]
    (map ids-of all-repos)))

(defn compare-repos
  [fn repo & repos]
  (let [all-products-by-id (group-by :id (apply concat repo repos))
        find-product #(first (get all-products-by-id %))
        repos-id (apply get-ids-of-repos repo repos)
        common-products-id (apply fn repos-id)]
    (map find-product common-products-id)))

(defn list-common-products-for-repos
  [repo & repos]
  (apply compare-repos clojure.set/intersection repo repos))

(defn list-available-fixes
  [fixes-in-inventory fixes-in-repo]
  (remove
    #(fix/fix-already-installed? % fixes-in-inventory)
    fixes-in-repo))