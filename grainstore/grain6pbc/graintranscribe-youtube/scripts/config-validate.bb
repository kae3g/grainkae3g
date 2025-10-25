#!/usr/bin/env bb
;;; Validate graintranscribe-youtube configuration

(load-file "src/graintranscribe/core.clj")
(require '[graintranscribe.core :as gt])

(let [result (gt/validate-config)]
  (if (:success result)
    (System/exit 0)
    (System/exit 1)))
