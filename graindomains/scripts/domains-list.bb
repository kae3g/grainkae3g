#!/usr/bin/env bb

(require '[babashka.fs :as fs])
(load-file "/home/xy/kae3g/grainkae3g/grainstore/kae3g/graindomains/src/graindomains/core.clj")
(require '[graindomains.core :as domains])

(println "🌾 Grain6 Domains Status")
(println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

(let [report (domains/check-all-domains)]
  (println (domains/format-domain-report report)))
