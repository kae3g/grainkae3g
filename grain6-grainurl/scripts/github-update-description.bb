#!/usr/bin/env bb

(require '[babashka.fs :as fs])
(load-file "/home/xy/kae3g/grainkae3g/grainstore/kae3g/grain6-grainurl/src/grain6-grainurl/core.clj")
(require '[grain6-grainurl.core :as grainurl])

(println "🌾 GitHub Description Update")
(println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

(let [grainpath-url "https://kae3g.github.io/grainkae3g/12025-10-23--0319--PDT--moon-vishakha------asc-gem000--sun-03rd--kae3g/grain6grainurlmodule/"
      result (grainurl/update-github-description grainpath-url)]
  (if (:success result)
    (do
      (println "✅ " (:message result))
      (println "   New description: " (:new-description result))
      (println "   Grainpath URL: " (:grainpath-url result)))
    (do
      (println "❌ " (:message result))
      (when (:response result)
        (println "   Response: " (:response result))))))
