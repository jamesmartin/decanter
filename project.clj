(defproject decanter "0.1.0-SNAPSHOT"
  :description "Your cellar data, aired and ready to drink"
  :url "https://github.com/jamesmartin/decanter"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "2.1.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [hickory "0.6.0"]]
  :main ^:skip-aot decanter.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
