#!/usr/bin/env bb

(require '[babashka.fs :as fs])
(load-file "/home/xy/kae3g/grainkae3g/grainstore/kae3g/grain6-grainurl/src/grain6-grainurl/core.clj")
(require '[grain6-grainurl.core :as grainurl])

(println "🌾 TDE9N Sheaf Creation (1-of-88)")
(println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

(let [report (grainurl/create-tde9n-sheaf)]
  (println (grainurl/format-tde9n-report report)))
