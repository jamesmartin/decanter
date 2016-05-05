(ns decanter.core
  (:gen-class))

(require '[clojure.tools.cli :refer [cli]])

(def arg-defs ["-h" "--help" "Print this help" :default false :flag true])

(defn -main [& args]
  (let [[opts args banner] (cli args arg-defs)]
    (when (:help opts)
      (println banner)))

  "I don't do a whole lot ... yet."
  (println "Hello, World!"))
