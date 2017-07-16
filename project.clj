(defproject profix "1.0.0-rc2"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [clj-http "3.6.1"]
                 [org.clojure/tools.cli "0.3.5"]]
  :profiles {:uberjar {:aot :all}
             :dev {:plugins [[lein-binplus "0.6.2"]]}}
  :main profix.core)
