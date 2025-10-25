#!/usr/bin/env bb

;; ╔══════════════════════════════════════════════════════════════════════════════╗
;; ║                                                                              ║
;; ║                        🌾 GRAINKEY SCRIPT 🌾                                ║
;; ║                                                                              ║
;; ║   Universal SSH & GnuPG Key Management for Grain Network                    ║
;; ║   Template-based, personalized, secure key generation and deployment         ║
;; ║                                                                              ║
;; ╚══════════════════════════════════════════════════════════════════════════════╝

(require '[babashka.fs :as fs]
         '[babashka.process :refer [shell]]
         '[clojure.string :as str]
         '[clojure.edn :as edn])

;; Load the core module
(load-file "src/grainkey/core.clj")

;; ═══════════════════════════════════════════════════════════════════════════════
;; MAIN EXECUTION
;; ═══════════════════════════════════════════════════════════════════════════════

(defn main
  "Main grainkey script entry point"
  [& args]
  (apply grainkey.core/main args))

;; Execute main function with command line arguments
(apply main *command-line-args*)
