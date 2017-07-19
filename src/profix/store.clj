(ns profix.store
  (:require [profix.jfile :as jf]
            [clojure.data.json :as json]))

(def profix-name ".profix")
(def profix-home (jf/resolvePath jf/home profix-name))
(def store-name "store")
(def store-home (jf/resolvePath profix-home store-name))

(defn json-of [name] (str name ".json"))

(defn profix-file [name] (jf/file store-home (json-of name)))

(defn write-entry [name content]
  (->> (json/write-str content)
      (spit (profix-file name))))

(defn read-json-kw [a] (json/read-str a :key-fn keyword))

(defn load-entry [name]
  (-> (profix-file name)
      slurp
      read-json-kw))

(defn delete-entry [name]
  (jf/delete (profix-file name)))

(defn list-entries []
  (let [paths (jf/list-files store-home)
        file-paths (map #(.getFileName %) paths)
        str-paths (map str file-paths)]
    (map jf/remove-file-extension str-paths)))