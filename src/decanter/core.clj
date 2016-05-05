(ns decanter.core
  (:gen-class))

(require '[clojure.tools.cli :refer [cli]])

(defn -main [& args]
  (let [[opts args banner] (cli args
                                ["-h" "--help" "Print this help"
                                 :default false :flag true])]
    (when (:help opts)
      (println banner)))
  "I don't do a whole lot ... yet."
  (println "Hello, World!"))
