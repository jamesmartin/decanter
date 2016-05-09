(ns decanter.core
  (:gen-class))

(require '[clojure.tools.cli :refer [cli]])

(defn wine-xchange-url [] "https://www.wine-xchange.com.au")

(def arg-defs [["-h" "--help" "Print this help" :default false :flag true]
               ["-u" "--username" "Required" :default false :flag false]
               ["-p" "--password" "Required" :default false :flag false]])

(def required-opts #{:username :password})
(defn missing-required?
  "Returns true if opts is missing any of its required-opts"
  [opts]
  (not-every? opts required-opts))

(require '[clj-http.client :as http])

(defn homepage-test [& fake-conn?]
  (if fake-conn? "Fake it 'til you make it..."
    (try
      (let [response (http/get (wine-xchange-url))]
         (str "Homepage OK: " (:status response)))
      (catch Exception e (str "homepage unavailable: " (.getMessage e))))
    ))

(use 'hickory.core)
(require '[hickory.select :as s])

(defn login-inputs [response]
  (let [login-page (-> response :body parse as-hickory)]
    (-> (s/select (s/child (s/id :login) (s/tag :input)) login-page))
    )
  )

(defn request-verification-token [inputs]
  (:value (:attrs (first (filter (fn [el] (== 0 (compare "__RequestVerificationToken" (:name (:attrs el))))) inputs))))
  )

(defn login-test []
  (try
    (let [response (http/get (str (wine-xchange-url) "/Login/login"))]
      (str "Login Request Token: " (request-verification-token (login-inputs response))))
    (catch Exception e (str "/Login unavailable: " (.getMessage e))))
  )

(defn -main [& args]
  (let [[opts args banner] (apply cli args arg-defs)]
    (when (or (:help opts)
              (missing-required? opts))
      (println banner)
      (System/exit 1))
    (println (str "Username: " (:username opts))))

  "I don't do a whole lot ... yet."
  (println (homepage-test))
  (println (login-test)))
