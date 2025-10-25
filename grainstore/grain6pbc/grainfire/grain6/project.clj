(defproject org.grainpbc/grain6 "0.1.0-SNAPSHOT"
  :description "Time-aware process supervision - graintime + s6 + Behn-inspired timer queue"
  :url "https://github.com/grainpbc/grain6"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.grainpbc/clojure-s6 "0.1.0-SNAPSHOT"]
                 [org.grainpbc/clojure-sixos "0.1.0-SNAPSHOT"]
                 [org.grainpbc/graintime "0.1.0-SNAPSHOT"]
                 [babashka/fs "0.5.27"]
                 [org.clojure/tools.cli "1.0.219"]
                 [org.clojure/core.async "1.6.681"]
                 [clj-time "0.15.2"]]
  
  :source-paths ["src"]
  :test-paths ["test"]
  :resource-paths ["resources"]
  
  :profiles {:dev {:dependencies [[org.clojure/test.check "1.1.1"]
                                  [nubank/matcher-combinators "3.8.8"]]
                   :source-paths ["dev"]}
             
             ;; Alpine/musl-optimized uberjar
             :alpine {:aot :all
                     :jvm-opts ["-Dclojure.compiler.direct-linking=true"
                               "-Dclojure.compiler.elide-meta=[:doc :file :line :added]"
                               "-Dclojure.spec.skip-macros=true"]
                     :uberjar-name "grain6-alpine-standalone.jar"}
             
             :uberjar {:aot :all
                      :jvm-opts ["-Dclojure.compiler.direct-linking=true"]
                      :uberjar-name "grain6-standalone.jar"}}
  
  :aliases {"alpine" ["with-profile" "+alpine" "uberjar"]
            "musl" ["with-profile" "+alpine" "uberjar"]
            "apk-build" ["do" "alpine," "run" "-m" "grain6.packaging.apk"]}
  
  :main ^:skip-aot grain6.core
  :target-path "target/%s"
  
  :repositories [["central" {:url "https://repo1.maven.org/maven2/"}]
                 ["clojars" {:url "https://repo.clojars.org/"}]]
  
  :min-lein-version "2.9.0"
  
  ;; Grainpbc metadata - 88 Counter Philosophy
  :grainpbc {:module-type :template
             :sheaf-position "1-of-88"  ; Kae3g genesis sheaf
             :philosophy "88 × 10^n >= 0 | -1, now == next + 1"
             :separation :template-personal
             :infuse-pattern true
             :dependencies [:graintime :clojure-s6 :clojure-sixos]
             :target-distros [:alpine :nixos :ubuntu :debian]
             :priority-distro :alpine
             :libc-preference :musl
             :package-formats [:apk :nix :deb :rpm]
             :canister-deployable true  ; Via Clotoko
             :behn-inspired true  ; Urbit timer queue architecture
             :s6-integration true})
