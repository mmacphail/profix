(ns profix.store
  (:require [profix.jfile :as jf]
            [clojure.data.json :as json]))

(def profix-name ".profix")
(def profix-home (jf/resolvePath jf/home profix-name))

(defn json-of [name] (str name ".json"))

(defn profix-file [name] (jf/file profix-home (json-of name)))

(defn store-json [name content]
  (->> (json/write-str content)
      (spit (profix-file name))))

(defn read-json-kw [a] (json/read-str a :key-fn keyword))

(defn load-json [name]
  (-> (profix-file name)
      slurp
      read-json-kw))