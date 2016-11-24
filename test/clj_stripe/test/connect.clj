(ns clj-stripe.test.connect
  (:use [ clj-stripe common connect])
  (:require [clojure.test :as test]))

(with-token "sk_test_EVC7fUEfhQgfKmmwdABhQ8nG"
  (def create-account-op (create-managed-account
                           "us" "test1@gmail.com"))
  (def managed-account (dissoc  (execute create-account-op) :keys))
  (def get-account-op (get-managed-account (:id managed-account)))
  (def get-account-result (execute get-account-op))
  (def get-all-accounts-op (get-managed-accounts))
  (def get-all-accounts-result (execute get-all-accounts-op))

  (test/deftest managed-accounts-test
    (test/is (= get-account-result managed-account))
    (test/is (some #{(:id get-account-result)} (map :id (:data get-all-accounts-result))))
    )

  (def delete-account-op (delete-managed-account (:id managed-account)))
  (def delete-account-result (execute delete-account-op))
  (def get-all-accounts-result-2 (execute get-all-accounts-op))

  (test/deftest delete-account-test
    (test/is (nil? (some #{(:id get-account-result)} (map :id (:data get-all-accounts-result-2)))))))


