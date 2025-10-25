#!/usr/bin/env bb
(require '[babashka.classpath :refer [add-classpath]])
(add-classpath "src")
(require '[graindaemon.display :as display])

;; Forward to the display daemon
(apply display/-main *command-line-args*)
