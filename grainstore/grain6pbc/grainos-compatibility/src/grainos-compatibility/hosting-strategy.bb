#!/usr/bin/env bb
;; GrainOS Hosting Strategy Manager
;; Manages hosting strategy decisions and implementations for GrainOS

(require '[babashka.process :refer [shell]]
         '[clojure.string :as str]
         '[clojure.data.json :as json])

(defn get-current-hosting-strategy []
  "Get current hosting strategy configuration"
  (try
    (let [config-file "grainos-hosting-config.edn"]
      (if (.exists (java.io.File. config-file))
        (read-string (slurp config-file))
        {:strategy :github-pages
         :cost {:monthly 0 :annual 0}
         :features {:static true :dynamic false}
         :domains ["grain6pbc.com"]
         :ssl :automatic}))
    (catch Exception e
      {:strategy :github-pages
       :cost {:monthly 0 :annual 0}
       :features {:static true :dynamic false}
       :domains ["grain6pbc.com"]
       :ssl :automatic})))

(defn analyze-hosting-needs [requirements]
  "Analyze hosting needs based on requirements"
  (let [needs {:static-content (get requirements :static-content true)
               :dynamic-features (get requirements :dynamic-features false)
               :user-accounts (get requirements :user-accounts false)
               :real-time (get requirements :real-time false)
               :database (get requirements :database false)
               :api-endpoints (get requirements :api-endpoints false)
               :budget (get requirements :budget 0)}]
    
    (cond
      ;; GitHub Pages sufficient
      (and (:static-content needs)
           (not (:dynamic-features needs))
           (not (:user-accounts needs))
           (not (:real-time needs))
           (not (:database needs))
           (not (:api-endpoints needs)))
      {:recommendation :github-pages
       :reason "Static content only, no dynamic features needed"
       :cost {:monthly 0 :annual 0}
       :features {:static true :dynamic false}
       :implementation "Continue with GitHub Pages"}
      
      ;; AWS AMI required
      (or (:dynamic-features needs)
          (:user-accounts needs)
          (:real-time needs)
          (:database needs)
          (:api-endpoints needs))
      {:recommendation :aws-ami
       :reason "Dynamic features, user accounts, or database required"
       :cost {:monthly 25 :annual 300}
       :features {:static true :dynamic true}
       :implementation "Migrate to AWS AMI"}
      
      ;; Hybrid approach
      :else
      {:recommendation :hybrid
       :reason "Mixed requirements, consider phased approach"
       :cost {:monthly 12 :annual 150}
       :features {:static true :dynamic true}
       :implementation "Start with GitHub Pages, add AWS services as needed"})))

(defn generate-hosting-plan [strategy]
  "Generate implementation plan for hosting strategy"
  (case (:recommendation strategy)
    :github-pages
    {:phase-1 {:duration "0-12 months"
               :cost 0
               :tasks ["Optimize static site performance"
                       "Add more static features"
                       "Monitor usage and feedback"
                       "Build community and user base"]}
     :phase-2 {:duration "12-24 months"
               :cost 0
               :tasks ["Continue with GitHub Pages"
                       "Evaluate dynamic feature needs"
                       "Plan for future migration"
                       "Monitor budget and requirements"]}
     :phase-3 {:duration "24+ months"
               :cost 0
               :tasks ["Reassess hosting needs"
                       "Consider migration if needed"
                       "Plan for long-term sustainability"
                       "Monitor technology trends"]}}
    
    :aws-ami
    {:phase-1 {:duration "0-6 months"
               :cost 150
               :tasks ["Plan AWS architecture"
                       "Develop AMI build process"
                       "Create migration strategy"
                       "Budget for additional costs"]}
     :phase-2 {:duration "6-12 months"
               :cost 300
               :tasks ["Build AMI infrastructure"
                       "Migrate core services"
                       "Implement monitoring"
                       "Test and optimize"]}
     :phase-3 {:duration "12+ months"
               :cost 300
               :tasks ["Complete migration"
                       "Implement advanced features"
                       "Scale infrastructure"
                       "Monitor performance and costs"]}}
    
    :hybrid
    {:phase-1 {:duration "0-6 months"
               :cost 0
               :tasks ["Continue with GitHub Pages"
                       "Identify specific AWS needs"
                       "Plan hybrid architecture"
                       "Budget for AWS services"]}
     :phase-2 {:duration "6-12 months"
               :cost 150
               :tasks ["Add AWS services"
                       "Implement hybrid solution"
                       "Monitor costs and performance"
                       "Optimize resource usage"]}
     :phase-3 {:duration "12+ months"
               :cost 300
               :tasks ["Evaluate full migration"
                       "Plan for scaling"
                       "Monitor costs and benefits"
                       "Optimize hybrid solution"]}}))

(defn check-domain-status [domain]
  "Check domain status and configuration"
  (try
    (let [result (shell {:out :string} "nslookup" domain)
          output (:out result)]
      {:domain domain
       :status (if (str/includes? output "Non-authoritative answer")
                 :active
                 :inactive)
       :dns-records (if (str/includes? output "Address:")
                      (str/split-lines output)
                      [])
       :last-checked (java.time.Instant/now)})
    (catch Exception e
      {:domain domain
       :status :error
       :error (.getMessage e)
       :last-checked (java.time.Instant/now)})))

(defn manage-domains [domains]
  "Manage domain configuration and status"
  (println "🌐 GrainOS Domain Management")
  (println "═══════════════════════════════════════════════════════════════════════════════")
  
  (doseq [domain domains]
    (let [status (check-domain-status domain)]
      (println (str "\n📋 Domain: " (:domain status)))
      (println (str "Status: " (:status status)))
      (when (:dns-records status)
        (println "DNS Records:")
        (doseq [record (:dns-records status)]
          (println (str "  " record))))
      (when (:error status)
        (println (str "Error: " (:error status))))))
  
  (println "\n✅ Domain management complete"))

(defn generate-hosting-report [requirements]
  "Generate comprehensive hosting strategy report"
  (let [current-strategy (get-current-hosting-strategy)
        needs-analysis (analyze-hosting-needs requirements)
        implementation-plan (generate-hosting-plan needs-analysis)]
    
    {:current-strategy current-strategy
     :needs-analysis needs-analysis
     :implementation-plan implementation-plan
     :recommendation (:recommendation needs-analysis)
     :cost-comparison {:current (:cost current-strategy)
                       :recommended (:cost needs-analysis)
                       :difference (- (:annual (:cost needs-analysis))
                                       (:annual (:cost current-strategy)))}
     :generated-at (java.time.Instant/now)}))

(defn -main [& args]
  "Main entry point for GrainOS Hosting Strategy Manager"
  (let [command (first args)]
    (case command
      "analyze" (let [requirements (if (second args)
                                     (read-string (slurp (second args)))
                                     {:static-content true
                                      :dynamic-features false
                                      :user-accounts false
                                      :real-time false
                                      :database false
                                      :api-endpoints false
                                      :budget 0})
                      report (generate-hosting-report requirements)]
                  (println "🌾 GrainOS Hosting Strategy Analysis")
                  (println "═══════════════════════════════════════════════════════════════════════════════")
                  (println (str "Recommendation: " (:recommendation report)))
                  (println (str "Reason: " (:reason (:needs-analysis report))))
                  (println (str "Cost: $" (:annual (:cost (:needs-analysis report))) "/year"))
                  (println (str "Implementation: " (:implementation (:needs-analysis report)))))
      
      "domains" (let [domains (if (second args)
                                (str/split (second args) #",")
                                ["grain6pbc.com" "grainpbc.com"])]
                  (manage-domains domains))
      
      "plan" (let [strategy (if (second args)
                              (read-string (slurp (second args)))
                              {:recommendation :github-pages})
                   plan (generate-hosting-plan strategy)]
               (println "🌾 GrainOS Hosting Implementation Plan")
               (println "═══════════════════════════════════════════════════════════════════════════════")
               (doseq [[phase data] plan]
                 (println (str "\n📋 " (str/upper-case (name phase))))
                 (println (str "Duration: " (:duration data)))
                 (println (str "Cost: $" (:cost data)))
                 (println "Tasks:")
                 (doseq [task (:tasks data)]
                   (println (str "  - " task)))))
      
      "status" (let [strategy (get-current-hosting-strategy)]
                 (println "🌾 Current GrainOS Hosting Strategy")
                 (println "═══════════════════════════════════════════════════════════════════════════════")
                 (println (str "Strategy: " (:strategy strategy)))
                 (println (str "Cost: $" (:annual (:cost strategy)) "/year"))
                 (println (str "Features: " (:features strategy)))
                 (println (str "Domains: " (str/join ", " (:domains strategy))))
                 (println (str "SSL: " (:ssl strategy))))
      
      (do
        (println "GrainOS Hosting Strategy Manager")
        (println "Usage: grainos-hosting [command] [args]")
        (println "Commands:")
        (println "  analyze [requirements-file] - Analyze hosting needs")
        (println "  domains [domain-list] - Manage domain status")
        (println "  plan [strategy-file] - Generate implementation plan")
        (println "  status - Show current hosting strategy")))))

;; Export functions for use in other namespaces
(def get-current-hosting-strategy get-current-hosting-strategy)
(def analyze-hosting-needs analyze-hosting-needs)
(def generate-hosting-plan generate-hosting-plan)
(def manage-domains manage-domains)
(def generate-hosting-report generate-hosting-report)
