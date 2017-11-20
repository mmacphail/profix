(ns profix.fix)

(defn test-fix? [x]
  (if (nil? (:displayName x))
    false
    (boolean (re-find #"(?i)test" (:displayName x)))))

(defn support-patch? [x]
  (if (= (:supportPatch x) "true") true false))

(defn manual? [x]
  (or (= (:preInstallSteps x) "true")
      (= (:postInstallSteps x) "true")))

(defn last-fix-version
  [fixes]
  (let [ordered-fixes (sort-by :version fixes)]
    (last ordered-fixes)))

(defn filter-stable-fixes
  [fixes]
  (->> fixes
       (remove test-fix?)
       (remove support-patch?)
       (remove manual?)
       (group-by :id)
       (vals)
       (map last-fix-version)
       (flatten)))

(defn contains-fix-any-version?
  [fix inventory-fixes]
  (let [select-id-only #(select-keys % [:id])
        inv-of-ids (into #{} (map select-id-only inventory-fixes))
        fix-id (select-id-only fix)]
    (contains? inv-of-ids fix-id)))

(defn newest-fix?
  [f1 f2]
  (and (= (:id f1) (:id f2))
       (<= 0 (compare (:version f1) (:version f2)))))

(defn newest-version-of-fix-in-inventory?
  [fix inventory-fixes]
  (let [is-same-fix-id? (fn [f] (= (:id f) (:id fix)))
        fixes-same-id (filter is-same-fix-id? inventory-fixes)]
    (every? #(newest-fix? % fix) fixes-same-id)))

(defn fix-already-installed?
  [fix inventory-fixes]
  (if (contains-fix-any-version? fix inventory-fixes)
    (newest-version-of-fix-in-inventory? fix inventory-fixes)
    false))