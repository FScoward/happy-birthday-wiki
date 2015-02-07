(ns happy-birthday-wiki.core
  (:require [cheshire.core :refer :all]
            [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
  [["-w" "--word WORD" "検索語句入力(x月x日生まれ)"]])

(defn make-url [search-word]
  (let [base "http://ja.wikipedia.org/w/api.php?format=json&action=query&list=search&srwhat=text&srsearch="]
    (str base search-word)))

(defn encode-word [word]
  (java.net.URLEncoder/encode word "UTF-8"))

(defn get-titles [url]
  (let [content (slurp url :encoding "UTF-8")
        parsed (:search (:query (parse-string content true)))]
    (map #(:title %) parsed)))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        url (make-url (encode-word (:word options)))
        titles (get-titles url)]
    (println (str "あなたと同じ誕生日:" (:word options)))
    (doall (map #(println %) titles))))
