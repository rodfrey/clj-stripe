(ns clj-stripe.connect
  "Functions for the Stripe Connect API"
  (:use [ clj-stripe.common :only (execute *stripe-token* api-root)])
  (:require [clj-stripe.util :as util]))

(defn address1 [a] {"line1" a})
(defn address2 [a] {"line2" a})
(defn province [p] {"state" p})
(defn postalcode [p] {"postal_code" p})
(defn country [c] {"country" c})
(defn city [c] {"city" c})
(defn firstname [n] {"first_name" n})
(defn lastname [n] {"last_name" n})
(defn address [a] {"address" (util/merge-maps a)})
(defn dob [y m d] {"dob" {"day" d "month" m "year" y}})
(defn entity_type [t] {"type" t})
(defn personal_id [id] {"personal_id_number" id})
(defn tos_agreement [date ip] {"tos_acceptance" {"date" date "ip" ip}})
(defn legal_entity [e] {"legal_entity" (util/merge-maps e)})
(defn external_account [token] {"external_account" token})


(defn create-managed-account
  "Creates a create-managed-account operation."
  [country email]
  {:operation :create-managed-account "country" country "email" email "managed" true})

(defmethod execute :create-managed-account [op-data]
  (util/post-request *stripe-token* 
                     (str api-root "/accounts") 
                     (dissoc op-data :operation)))

(defn update-legal-entity
  "Update the information in a managed account"
  [account-id legal-entity]
  (assoc legal-entity :operation :update-managed-account :account-id account-id))

(defmethod execute :update-managed-account [op-data]
  (prn "EXECTUING UPDATE MANAGED ACCOUNT " op-data)
  (util/post-request *stripe-token*
                     (str api-root "/accounts/" (get op-data :account-id))
                     (dissoc op-data :account-id :operation)))
(defn get-managed-account
  "Creates an operation to get a managed account from Stripe Connect.
  Requires the Stripe-assigned id of the managed account.
  Execute with common/execute."
  [account-id]
  {:operation :get-managed-account :account-id account-id})

(defmethod execute :get-managed-account
  [op-data]
  (util/get-request *stripe-token* (str api-root "/accounts/" (get op-data :account-id))) 
  )

(defn get-managed-accounts
  "Create an operation to get all the Stripe managed accounts.
  Execute with common/execute"
  [] 
  {:operation :get-managed-accounts})

(defmethod execute :get-managed-accounts
  [op-data]
  (util/get-request *stripe-token* (str api-root "/accounts")))



(defn delete-managed-account
  "Creates an operation to delete a managed account. Requires the account id.
  Execute with common/execute."
  [account-id]
  {:operation :delete-managed-account :account-id account-id})

(defmethod execute :delete-managed-account
  [op-data ]
  (util/delete-request *stripe-token* (str api-root "/accounts/" (get op-data :account-id))) 
  )
;(with-token "sk_test_EVC7fUEfhQgfKmmwdABhQ8nG"
;  (execute  (get-managed-accounts))
;  )
