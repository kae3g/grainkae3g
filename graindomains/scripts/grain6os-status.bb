#!/usr/bin/env bb

(require '[babashka.fs :as fs])
(load-file "/home/xy/kae3g/grainkae3g/grainstore/kae3g/graindomains/src/graindomains/grain6os.clj")
(require '[graindomains.grain6os :as grain6os])

(println "🌾 Grain6OS Integration Status")
(println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

(let [report (grain6os/sync-grain6os-grainsixos)]
  (println (grain6os/format-grain6os-report report)))
