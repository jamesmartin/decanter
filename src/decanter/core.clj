(ns decanter.core
  (:gen-class))

(require '[clojure.tools.cli :refer [cli]])

(def arg-defs [["-h" "--help" "Print this help" :default false :flag true]
               ["-u" "--username" :default false :flag false]
               ["-p" "--password" :default false :flag false]])

(def required-opts #{:username :password})
(defn missing-required?
  "Returns true if opts is missing any of its required-opts"
  [opts]
  (not-every? opts required-opts))

(defn -main [& args]
  (let [[opts args banner] (apply cli args arg-defs)]
    (when (or (:help opts)
              (missing-required? opts))
      (println banner))
    (println (str "Username: " (:username opts))))

  "I don't do a whole lot ... yet."
  (println "Hello, Wine!"))
