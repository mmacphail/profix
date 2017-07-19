(ns profix.jfile
  (:require [me.raynes.fs :as fs]))

(defn path [file] (.toPath file))

(def home (path (fs/home)))

(defn resolvePath
  ([path part]
   (.resolve path part))
  ([path part & parts]
   (reduce resolvePath (resolvePath path part) parts)))

(defn file->path [f] (.toPath f))

(defn path->file [p] (.toFile p))

(defn file
  [path name]
  (path->file (resolvePath path name)))