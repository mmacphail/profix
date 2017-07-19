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

(defn delete
  [path]
  (fs/delete (path->file path)))

(defn list-files
  [path]
  (->> (fs/list-dir (path->file path))
      (map file->path)))

; From https://github.com/funimage/funimage
; Copyright 2014-2015 Kyle Harrington
; License: Apache V2.0
(defn remove-file-extension
  "Remove a file extension from a string."
  [filename]
  (.substring filename 0 (.lastIndexOf filename ".")))
