#!/usr/bin/env bb

(ns init-grainpbc-repos
  "Initialize all grainpbc GitHub organization repositories.
   
   This script creates and configures all official Grain Network repositories
   under the grainpbc organization."
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]))

;; =============================================================================
;; Repository Definitions
;; =============================================================================

(def repositories
  "All repositories to create in the grainpbc organization."
  
  ;; Core Libraries
  [{:name "clojure-s6"
    :description "s6 supervision wrapper for Clojure"
    :topics ["clojure" "s6" "supervision" "sixos" "daemon"]
    :source-path "grainstore/clojure-s6"}
   
   {:name "clojure-sixos"
    :description "SixOS integration with intelligent typo handling for Grain Network"
    :topics ["clojure" "sixos" "typo-correction" "fuzzy-matching" "grain"]
    :source-path "grainstore/clojure-sixos"}
   
   {:name "clojure-icp"
    :description "ICP/DFINITY integration for Clojure with Chain Fusion support"
    :topics ["clojure" "icp" "dfinity" "web3" "canister" "blockchain"]
    :source-path "grainstore/clojure-icp"}
   
   {:name "clotoko"
    :description "Clojure-to-Motoko transpiler for ICP canister development"
    :topics ["clojure" "motoko" "transpiler" "icp" "compiler" "web3"]
    :source-path "grainstore/clotoko"}
   
   {:name "grain-metatypes"
    :description "Foundational type definitions for Grain Network (zero dependencies)"
    :topics ["types" "grain" "clojure" "spec" "foundation"]
    :source-path "grainstore/grain-metatypes"}
   
   ;; Platform Components
   {:name "grainweb"
    :description "Browser + Git Explorer + AI Atlas - decentralized web platform"
    :topics ["browser" "git" "ai" "decentralized" "clojure" "humble-ui"]
    :source-path "grainstore/grainweb"}
   
   {:name "grainspace"
    :description "Unified decentralized platform - App Store meets social network"
    :topics ["decentralized" "platform" "web3" "social" "marketplace"]
    :source-path "grainstore/grainspace"}
   
   {:name "grainmusic"
    :description "Decentralized music streaming platform with artist sovereignty"
    :topics ["music" "streaming" "decentralized" "clotoko" "icp" "artist-rights"]
    :source-path "grainstore/grainmusic"}
   
   {:name "grainconv"
    :description "Universal type conversion system (FFmpeg for Grain types)"
    :topics ["conversion" "ffmpeg" "types" "utility" "multimedia"]
    :source-path "grainstore/grainconv"}
   
   {:name "grainclay"
    :description "Networked rolling release package manager with immutable paths"
    :topics ["package-manager" "networking" "rolling-release" "clay" "immutable"]
    :source-path "grainstore/clojure-s6/grainclay"}
   
   ;; Hardware Projects
   {:name "grainwriter"
    :description "RAM-only e-ink writing device with 64GB refurbished RAM"
    :topics ["hardware" "eink" "writing" "ram-only" "coreboot" "open-hardware"]
    :source-path "grainstore/grainwriter"}
   
   {:name "graincamera"
    :description "Open-hardware AVIF-compatible mirrorless camera"
    :topics ["hardware" "camera" "avif" "open-source" "photography"]
    :source-path "grainstore/grainphotos/hardware"}
   
   {:name "grainpack"
    :description "External GPU jetpack attachment for Grainwriter"
    :topics ["hardware" "gpu" "external" "amd" "refurbished"]
    :source-path "grainstore/grainwriter/grainpack"}
   
   ;; Infrastructure
   {:name "grainsource"
    :description "Git-compatible decentralized version control system"
    :topics ["git" "version-control" "decentralized" "p2p"]
    :source-path "grainstore/grainsource"}
   
   {:name "grainnetwork"
    :description "Main Grain Network documentation and ecosystem coordination"
    :topics ["documentation" "network" "coordination" "grain" "ecosystem"]
    :source-path "."}
   
   {:name "grainstore"
    :description "Verified dependencies and submodule management system"
    :topics ["dependencies" "submodules" "packages" "verification"]
    :source-path "grainstore"}
   
   ;; Templates
   {:name "grainnixos-qemu-ubuntu-framework16"
    :description "NixOS QEMU template for Ubuntu 24.04 LTS on Framework 16"
    :topics ["nixos" "qemu" "ubuntu" "framework" "template" "virtualization"]
    :source-path "grainstore/grainsource-nixos-qemu-within-ubuntu-24-04-lts-for-framework-16-laptop"}])

;; =============================================================================
;; Helper Functions
;; =============================================================================

(defn run-cmd
  "Run a shell command and return the result."
  [& args]
  (let [result (apply sh args)]
    (when-not (zero? (:exit result))
      (println "❌ Command failed:" (str/join " " args))
      (println "   Error:" (:err result)))
    result))

(defn create-repo
  "Create a GitHub repository in the grainpbc organization."
  [{:keys [name description topics]}]
  (println (format "📦 Creating repository: grainpbc/%s" name))
  (let [result (run-cmd "gh" "repo" "create" (str "grainpbc/" name)
                        "--public"
                        "--description" description
                        "--confirm")]
    (when (zero? (:exit result))
      (println (format "   ✓ Created grainpbc/%s" name))
      ;; Add topics
      (doseq [topic topics]
        (run-cmd "gh" "repo" "edit" (str "grainpbc/" name)
                 "--add-topic" topic))
      (println (format "   ✓ Added topics: %s" (str/join ", " topics))))
    result))

(defn repo-exists?
  "Check if a repository exists."
  [org repo-name]
  (let [result (run-cmd "gh" "repo" "view" (str org "/" repo-name))]
    (zero? (:exit result))))

(defn init-repo-content
  "Initialize repository with content from source path."
  [{:keys [name source-path]}]
  (println (format "📄 Initializing content for grainpbc/%s from %s" name source-path))
  (let [repo-dir (str "/tmp/grainpbc-" name)]
    ;; Clone the empty repo
    (run-cmd "gh" "repo" "clone" (str "grainpbc/" name) repo-dir)
    
    ;; Copy content from source
    (when (.exists (clojure.java.io/file source-path))
      (run-cmd "cp" "-r" (str source-path "/.") repo-dir))
    
    ;; Commit and push
    (run-cmd "git" "-C" repo-dir "add" ".")
    (run-cmd "git" "-C" repo-dir "commit" "-m" "Initial commit: Grain Network foundation")
    (run-cmd "git" "-C" repo-dir "push" "origin" "main")
    
    ;; Cleanup
    (run-cmd "rm" "-rf" repo-dir)
    (println (format "   ✓ Initialized grainpbc/%s" name))))

;; =============================================================================
;; Main Script
;; =============================================================================

(defn verify-organization []
  "Verify that grainpbc organization exists."
  (println "🔍 Verifying grainpbc organization exists...")
  (let [result (run-cmd "gh" "api" "orgs/grainpbc")]
    (if (zero? (:exit result))
      (do
        (println "   ✓ Organization grainpbc exists")
        true)
      (do
        (println "   ❌ Organization grainpbc does not exist!")
        (println "   📖 Please create it first:")
        (println "      https://github.com/settings/organizations")
        (println "   📄 See: docs/setup/CREATE-GRAINPBC-ORGANIZATION.md")
        false))))

(defn create-all-repos []
  "Create all repositories in the grainpbc organization."
  (println "\n🚀 Creating grainpbc repositories...\n")
  (doseq [repo repositories]
    (if (repo-exists? "grainpbc" (:name repo))
      (println (format "   ⏭️  Repository grainpbc/%s already exists, skipping" (:name repo)))
      (create-repo repo))
    (println)))

(defn init-all-content []
  "Initialize content for all repositories."
  (println "\n📄 Initializing repository content...\n")
  (doseq [repo repositories]
    (init-repo-content repo)
    (println)))

(defn print-summary []
  "Print summary of created repositories."
  (println "\n✨ Summary\n")
  (println (format "   Created %d repositories in grainpbc organization:" (count repositories)))
  (doseq [repo repositories]
    (println (format "   ✓ https://github.com/grainpbc/%s" (:name repo))))
  (println "\n🌾 Grain Network organization is now ready!"))

(defn -main []
  (println "╔═══════════════════════════════════════════════════════════════╗")
  (println "║        Initialize grainpbc GitHub Organization Repos         ║")
  (println "╚═══════════════════════════════════════════════════════════════╝\n")
  
  (if (verify-organization)
    (do
      (create-all-repos)
      (init-all-content)
      (print-summary))
    (do
      (println "\n⚠️  Cannot proceed without grainpbc organization")
      (println "   Create it first, then run this script again.")
      (System/exit 1))))

(-main)

