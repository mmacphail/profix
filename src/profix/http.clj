(ns profix.http
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as str]))

(defn client-get-request [request]
  (client/get (request :url) {:headers    {"accept"             "application/json"
                                           "DoNotCreateSession" "true"}
                              :basic-auth (request :auth)}))

(defn throw-error-status [status]
  (throw (Exception. (str "The following HTTP code was received: " status))))

(defn client-http-request [request]
  (let [response (client-get-request request)
        status (str (:status response))
        body (json/read-str (:body response)
                            :key-fn keyword)]
    (if (not (str/starts-with? status "2"))
      (throw-error-status status))
    body))

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

(defn make-request [cce-url auth]
  {:cce-url cce-url :auth auth})