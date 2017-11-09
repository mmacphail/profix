(ns profix.http
  (:require [org.httpkit.client :as httpk]
            [clojure.data.json :as json]
            [clojure.string :as str]))

(def headers {"accept" "application/json"
              "DoNotCreateSession" "true"})

(defn opts [auth insecure]
  {:headers headers
   :basic-auth auth
   :insecure? insecure})

(defn client-get-request [{:keys [url auth insecure]}]
  @(httpk/get url (opts auth insecure)))

(defn throw-error-status [status]
  (throw (Exception. (str "The following HTTP code was received: " status))))

(defn client-http-request [request]
  (let [{:keys [body status]} (client-get-request request)]
    (if (not (str/starts-with? status "2"))
      (throw-error-status status))
      (json/read-str body :key-fn keyword)))

(defn def-resource
  ([request resource] (def-resource request resource ""))
  ([request resource resource-id]
   (let [rsc (if-not
               (empty? resource-id)
               (str "/" resource-id)
               "")]
     (assoc request
       :url
       (str (:cce-url request) "/" resource rsc)))))

(defn get-resource
  ([request resource] (get-resource request resource ""))
  ([request resource resource-id]
   (let [req (def-resource request resource resource-id)]
     (client-http-request req))))

(defn make-request [cce-url auth insecure]
  {:cce-url cce-url :auth auth :insecure insecure})