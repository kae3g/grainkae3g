(defproject org.grain6pbc/graintranscribe-youtube "0.1.0-SNAPSHOT"
  :description "YouTube video transcription using Gemini 2.5 Pro - Template module with personal separation"
  :url "https://github.com/grain6pbc/graintranscribe-youtube"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [babashka/fs "0.5.27"]
                 [clj-http "3.12.3"]
                 [cheshire "5.12.0"]
                 [org.clojure/tools.cli "1.0.219"]
                 [org.clojure/data.json "2.4.0"]]
  
  :source-paths ["src"]
  :test-paths ["test"]
  
  :profiles {:dev {:dependencies [[org.clojure/test.check "1.1.1"]
                                  [nubank/matcher-combinators "3.8.8"]]
                   :source-paths ["dev"]}
             
             :uberjar {:aot :all
                      :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  
  :aliases {"transcribe" ["run" "-m" "graintranscribe.cli" "transcribe"]
            "config" ["run" "-m" "graintranscribe.cli" "config"]
            "validate" ["run" "-m" "graintranscribe.cli" "validate"]}
  
  :main ^:skip-aot graintranscribe.cli
  :target-path "target/%s"
  
  :repositories [["central" {:url "https://repo1.maven.org/maven2/"}]
                 ["clojars" {:url "https://repo.clojars.org/"}]]
  
  :min-lein-version "2.0.0"
  
  ;; Grainpbc metadata
  :grain6pbc {:module-type :template
             :separation :template-personal
             :infuse-pattern true  ; Uses sixos-inspired deep merge
             :dependencies [:grainconfig :grainenvvars]
             :external-apis [:gemini-2.5-pro]})
