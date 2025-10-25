#!/usr/bin/env bb

(require '[babashka.fs :as fs])
(load-file "/home/xy/kae3g/grainkae3g/grainstore/kae3g/grain6-grainurl/src/grain6-grainurl/core.clj")
(require '[grain6-grainurl.core :as grainurl])

(println "🌾 Grain6 GrainURL Generation")
(println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

(let [sheaf-id "tde9n"
      graintime "12025-10-23--0319--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g"
      report (grainurl/generate-grainurl sheaf-id graintime)]
  (println (grainurl/format-grainurl-report report)))
