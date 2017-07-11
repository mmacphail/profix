(ns profix.diff
  (:require [profix.http :as http]
            [profix.resources :as rsc]))

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

(defn is-test-fix [x]
  (boolean (re-find #"(?i)test" (:displayName x))))

(defn is-support-patch [x]
  (if (= (:supportPatch x) "true") true false))

(defn is-manual [x]
  (or (= (:preInstallSteps x) "true")
      (= (:postInstallSteps x) "true")))

(defn last-fix-version
  [fixes]
  (let [ordered-fixes (sort-by :version fixes)]
    (last ordered-fixes)))

(defn filter-stable-fixes
  [fixes]
  (let [stable-fixes (remove is-test-fix fixes)
        no-patch (remove is-support-patch fixes)
        no-manual (remove is-manual no-patch)
       fixes-by-id (group-by :id no-manual)]
   (flatten (map last-fix-version (vals fixes-by-id)))))