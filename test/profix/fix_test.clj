(ns profix.fix-test
  (:require [clojure.test :refer :all]
            [profix.fix :refer :all]))

(def cli-fix-1 {:id "wMFix.CCECLI", :version "9.12.0.0001-0259"})
(def cli-fix-9 {:id "wMFix.CCECLI", :version "9.12.0.0009-0325"})
(def num-fix-7 {:id "wMFix.NUMRealmServer", :version "9.12.0.0007-93910"})
(def plat-fix-1 {:id "wMFix.OSGI.Platform", :version "9.12.0.0001-0460"})

(deftest newest-fix?-diff-id-test
  (testing "a fix is never newest compared to a fix with a different ID"
    (let [newest (newest-fix? cli-fix-1 num-fix-7)]
      (is (not newest)))))

(deftest newest-fix?-more-recent-test
  (testing "a fix is always newest compared to the same fix with a less recent version"
    (let [newest (newest-fix? cli-fix-9 cli-fix-1)]
      (is newest))))

(deftest fix-already-installed?-never-installed-test
  (testing "a fix that was never installed is not already installed"
    (let [inventory [cli-fix-9 plat-fix-1]]
      (is (not (fix-already-installed? num-fix-7 inventory))))))

(deftest fix-already-installed?-already-installed-most-recent-version-test
  (testing "a fix that was already installed at the most recent version is already installed"
    (let [inventory [cli-fix-9 plat-fix-1 num-fix-7]]
      (is (fix-already-installed? cli-fix-1 inventory)))))

(deftest fix-already-installed?-already-installed-not-most-recent-version-test
  (testing "a fix that was already installed at the not most recent version is not already installed"
    (let [inventory [cli-fix-1 plat-fix-1 num-fix-7]]
      (is (not (fix-already-installed? cli-fix-9 inventory))))))

(deftest fix-already-installed?-never-already-installed-empty-inv-test
  (testing "a fix is never installed in an empty inventory"
    (let [inventory []]
      (is (not (fix-already-installed? cli-fix-9 inventory))))))