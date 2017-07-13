(ns profix.fix)

(defn test-fix? [x]
  (boolean (re-find #"(?i)test" (:displayName x))))

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

(defn newest-fix?
  [f1 f2]
  (and (= (:id f1) (:id f2))
       (<= 0 (compare (:version f1) (:version f2)))))

(defn newest-version-installed
  [fix inventory-fixes]
  (let [searched-fix (select-keys fix [:id :version])
        installed-fixes (into #{}
                              (map #(select-keys % [:id :version]) inventory-fixes))
        newest-fix? (fn [f] (and (= (:id f) (:id fix))
                                 (> (:version f) (:version fix))))]
    (some newest-fix? installed-fixes)))