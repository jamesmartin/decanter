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
        (println (str "response: " (:status response)))
         (if (>= (:status response) 400)
           :error :ok))
      (catch Exception e ( :error )))
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

(defn login-test [username password]
  (try
    (let [response (http/get (str (wine-xchange-url) "/Login/login"))]
      [:ok {:message (request-verification-token (login-inputs response))}])
    (catch Exception e [:error {:message (str "/Login unavailable: " (.getMessage e))}]))
  )

(defn -main [& args]
  "Entry point. Checks args, connectivity, syncs and queries."
  (let [[opts args banner] (apply cli args arg-defs)]
    (when (or (:help opts)
              (missing-required? opts))
      (println banner)
      (System/exit 1))
    (println (str "Username: " (:username opts)))
    (if (= :error (homepage-test))
      (do
        (println "Homepage unavailable")
        (System/exit 1))
      (let [[status value] (login-test (:username opts) (:password opts))]
        (if (= :error status)
          (println (str "Problem with login token: " (:message value)))
          (println (str "Login token OK: " (:message value)))
        )
      )))
  )

