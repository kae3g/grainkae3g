#!/usr/bin/env bb
;; Load secrets from .secrets.edn
;; Usage: (load 'load-secrets) or source from other scripts

(ns load-secrets
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [babashka.process :refer [shell]]))

(def secrets-file ".secrets.edn")

(defn load-secrets
  "Load secrets from .secrets.edn file"
  []
  (if (.exists (io/file secrets-file))
    (edn/read-string (slurp secrets-file))
    (do
      (println "Warning: .secrets.edn not found")
      (println "Copy .secrets.template.edn to .secrets.edn")
      {})))

(defn get-secret
  "Get a secret value by path, e.g. [:github :token]"
  [path]
  (get-in (load-secrets) path))

(defn configure-git!
  "Configure git with secrets"
  []
  (let [secrets (load-secrets)
        git-config (:git secrets)]
    (when git-config
      (println "Configuring git...")
      (when-let [name (:user-name git-config)]
        (shell "git" "config" "--local" "user.name" name))
      (when-let [email (:user-email git-config)]
        (shell "git" "config" "--local" "user.email" email))
      (when-let [key (:signing-key git-config)]
        (shell "git" "config" "--local" "user.signingkey" key))
      (when (:sign-commits git-config)
        (shell "git" "config" "--local" "commit.gpgsign" "true"))
      (println "Git configuration complete"))))

(defn get-repo-info
  "Get repository information from secrets"
  []
  (let [secrets (load-secrets)]
    (merge (:repository secrets)
           (:github secrets))))

(defn setup-environment!
  "Setup environment variables from secrets"
  []
  (let [secrets (load-secrets)]
    ;; GitHub
    (when-let [token (get-in secrets [:github :token])]
      (System/setProperty "GITHUB_TOKEN" token))
    
    ;; GPG
    (when-let [key-id (get-in secrets [:gpg :key-id])]
      (System/setProperty "GPG_KEY_ID" key-id))
    
    ;; Deployment
    (doseq [[k v] (:deployment secrets)]
      (System/setProperty (str (name k)) (str v)))
    
    (println "Environment configured from secrets")))

;; Auto-load on require
(def secrets (load-secrets))

;; Helper functions
(defn github-token [] (get-secret [:github :token]))
(defn github-username [] (get-secret [:github :username]))
(defn repo-name [] (get-secret [:repository :name]))
(defn repo-url [] (get-secret [:repository :url]))
(defn gpg-key-id [] (get-secret [:gpg :key-id]))

