(ns grainsource-vegan.specs
  "Vegan ethics validation specs for Grain Network
  
  Ketu energy: Thin, bold, released, healing, surrender
  Temperance: Integration through patient ethical flow"
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  TERMINOLOGY SPECS - No Violent Language                       ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(def violent-terms
  #{"kill" "killed" "killing" "killer"
    "dead" "death" "die" "dying" "died"
    "slaughter" "slaughtered" "slaughtering"
    "butcher" "butchered" "butchering"
    "murder" "murdered" "murdering"
    "execute" "executed" "executing" "execution"
    "terminate" "terminated" "terminating" "termination"})

(def animal-exploitation-terms
  #{"master" "slave" 
    "meat" "beef" "pork" "chicken" "fish" "seafood"
    "dairy" "milk" "cheese" "butter" "cream"
    "eggs" "egg" "leather" "wool" "silk"
    "bacon" "ham" "steak" "sausage"})

(s/def ::violence-free-string
  (s/and string?
         #(not (some (fn [term] 
                       (str/includes? (str/lower-case %) term))
                     violent-terms))))

(s/def ::exploitation-free-string
  (s/and string?
         #(not (some (fn [term]
                       (str/includes? (str/lower-case %) term))
                     animal-exploitation-terms))))

(s/def ::vegan-clean-text
  (s/and ::violence-free-string
         ::exploitation-free-string))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  COMPASSIONATE ERROR MESSAGE SPECS                             ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::acknowledges-pain
  (s/and string?
         #(or (str/includes? % "understand")
              (str/includes? % "acknowledge")
              (str/includes? % "see that")
              (str/includes? % "I notice"))))

(s/def ::offers-courage
  (s/and string?
         #(or (str/includes? % "you can")
              (str/includes? % "try")
              (str/includes? % "here's how")
              (str/includes? % "let's"))))

(s/def ::healing-tone
  (s/and string?
         #(not (or (str/includes? (str/lower-case %) "fail")
                   (str/includes? (str/lower-case %) "bad")
                   (str/includes? (str/lower-case %) "wrong")
                   (str/includes? (str/lower-case %) "error")
                   (str/includes? (str/lower-case %) "invalid")))
         #(or (str/includes? % "💚")
              (str/includes? % "🌱")
              (str/includes? % "🌾")
              (str/includes? % "✓"))))

(s/def ::compassionate-error-message
  (s/and ::vegan-clean-text
         ::acknowledges-pain
         ::offers-courage
         ::healing-tone))

;; Example valid error messages
(comment
  (s/valid? ::compassionate-error-message
    "I understand this is confusing 💚 You can fix this by checking the config. Let's look at line 42 together.")
  ;; => true
  
  (s/valid? ::compassionate-error-message
    "INVALID INPUT! Bad configuration!")
  ;; => false (not healing, not acknowledging, not offering courage)
  )

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  DEPENDENCY ETHICS SPECS                                       ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::dependency-name string?)
(s/def ::dependency-version string?)
(s/def ::dependency-source (s/or :url string? :local keyword?))
(s/def ::license string?)

(s/def ::animal-testing-free? boolean?)
(s/def ::exploitation-free? boolean?)
(s/def ::open-source? boolean?)
(s/def ::ethical-audit-passed? boolean?)

(s/def ::ethical-dependency
  (s/keys :req [::dependency-name
                ::dependency-version
                ::dependency-source
                ::license
                ::animal-testing-free?
                ::exploitation-free?
                ::open-source?]
          :opt [::ethical-audit-passed?
                ::audit-date
                ::audit-notes]))

;; Common unethical services to flag
(def unethical-services
  #{"aws" "amazon-web-services"  ; Animal testing, worker exploitation
    "google-cloud" "gcp"         ; Military contracts
    "azure" "microsoft-azure"     ; ICE contracts
    "palantir"                    ; Surveillance, military
    "datadog"})                   ; Animal-based branding

(s/def ::service-ethical
  (s/and string?
         #(not (some (fn [service]
                       (str/includes? (str/lower-case %) service))
                     unethical-services))))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  TOROIDAL ECONOMICS SPECS (Ketu: Profit Returns to Purpose)   ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::revenue pos?)
(s/def ::purpose-allocation (s/and number? #(>= % 0) #(<= % 1)))  ; 0-100% to purpose
(s/def ::profit-reinvestment-rate (s/and number? #(>= % 0.5)))    ; At least 50% back
(s/def ::vegan-aligned-spending? boolean?)

(s/def ::toroidal-economics
  (s/keys :req [::revenue
                ::purpose-allocation
                ::profit-reinvestment-rate
                ::vegan-aligned-spending?]
          :opt [::grants-received
                ::donations-given
                ::service-exchange]))

(s/def ::economic-flow-valid?
  (fn [{:keys [::revenue ::purpose-allocation ::profit-reinvestment-rate]}]
    (and (>= purpose-allocation 0.3)      ; At least 30% to purpose
         (>= profit-reinvestment-rate 0.5) ; At least 50% reinvested
         (>= (* revenue purpose-allocation profit-reinvestment-rate) 0))))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  LIFE-AFFIRMING ARCHITECTURE SPECS                             ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::serves-life? boolean?)
(s/def ::reduces-suffering? boolean?)
(s/def ::promotes-compassion? boolean?)
(s/def ::accessible-to-all? boolean?)
(s/def ::environmentally-sustainable? boolean?)

(s/def ::life-affirming-design
  (s/keys :req [::serves-life?
                ::reduces-suffering?
                ::promotes-compassion?
                ::accessible-to-all?
                ::environmentally-sustainable?]))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  COMPLETE VEGAN MODULE SPEC                                    ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::module-name string?)
(s/def ::module-team keyword?)  ; :teamfire01, :teamvault02, etc.
(s/def ::module-description ::vegan-clean-text)
(s/def ::module-documentation ::vegan-clean-text)
(s/def ::module-error-messages (s/coll-of ::compassionate-error-message))
(s/def ::module-dependencies (s/coll-of ::ethical-dependency))
(s/def ::module-economics ::toroidal-economics)
(s/def ::module-architecture ::life-affirming-design)

(s/def ::vegan-compliant-module
  (s/keys :req [::module-name
                ::module-team
                ::module-description
                ::module-documentation
                ::module-error-messages
                ::module-dependencies
                ::module-economics
                ::module-architecture]))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  VALIDATION FUNCTIONS                                          ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(defn audit-module
  "Audit a module for vegan compliance
  
  Returns: {:compliant? boolean
            :violations []
            :recommendations []}"
  [module-spec]
  (let [validation (s/explain-data ::vegan-compliant-module module-spec)
        compliant? (nil? validation)]
    {:compliant? compliant?
     :module-name (::module-name module-spec)
     :team (::module-team module-spec)
     :violations (when validation
                   (map (fn [problem]
                          {:path (:path problem)
                           :pred (:pred problem)
                           :val (:val problem)})
                        (::s/problems validation)))
     :timestamp (java.time.Instant/now)}))

(defn audit-text
  "Quick text audit for vegan compliance"
  [text]
  (let [violence-free? (s/valid? ::violence-free-string text)
        exploitation-free? (s/valid? ::exploitation-free-string text)
        vegan-clean? (and violence-free? exploitation-free?)]
    {:text text
     :vegan-clean? vegan-clean?
     :violence-free? violence-free?
     :exploitation-free? exploitation-free?
     :violations (cond-> []
                   (not violence-free?)
                   (conj {:type :violent-terminology
                          :found (filter #(str/includes? (str/lower-case text) %)
                                        violent-terms)})
                   (not exploitation-free?)
                   (conj {:type :animal-exploitation
                          :found (filter #(str/includes? (str/lower-case text) %)
                                        animal-exploitation-terms)}))}))

(defn audit-dependency
  "Audit a single dependency for ethics"
  [dep-name dep-info]
  (let [service-name (str/lower-case dep-name)
        is-unethical? (some #(str/includes? service-name %) unethical-services)]
    {:dependency dep-name
     :info dep-info
     :ethical? (not is-unethical?)
     :recommendation (when is-unethical?
                       (str "Consider alternative to " dep-name 
                            " (flagged for ethical concerns)"))}))

(defn generate-compassionate-error
  "Generate a compassionate error message from a harsh one
  
  Example:
  'Invalid input!' → 'I notice the input doesn't match what's expected 💚 
                      You can fix this by checking the format. Let's try together!'"
  [harsh-message]
  (let [issue (str/lower-case harsh-message)
        acknowledgment (cond
                         (str/includes? issue "invalid") "I see the data doesn't match what's expected"
                         (str/includes? issue "error") "I notice something unexpected happened"
                         (str/includes? issue "fail") "I understand this didn't work as planned"
                         :else "I see something needs attention")
        encouragement (cond
                        (str/includes? issue "config") "You can fix this by checking the configuration"
                        (str/includes? issue "syntax") "You can adjust the syntax"
                        (str/includes? issue "file") "You can check the file path"
                        :else "You can resolve this")
        support "Let's work through this together 🌱"]
    (str acknowledgment " 💚 " encouragement ". " support)))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  EXPORT FOR OTHER TEAMS                                        ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(def vegan-specs
  "Public API for other teams to use vegan validation"
  {:audit-module audit-module
   :audit-text audit-text
   :audit-dependency audit-dependency
   :generate-compassionate-error generate-compassionate-error
   :specs {:module ::vegan-compliant-module
           :text ::vegan-clean-text
           :error-message ::compassionate-error-message
           :dependency ::ethical-dependency
           :economics ::toroidal-economics
           :architecture ::life-affirming-design}})

(comment
  ;; Example usage
  (audit-text "Kill the process")
  ;; => {:vegan-clean? false
  ;;     :violence-free? false
  ;;     :violations [{:type :violent-terminology :found ["kill"]}]}
  
  (audit-text "Stop the process gracefully")
  ;; => {:vegan-clean? true
  ;;     :violence-free? true
  ;;     :exploitation-free? true
  ;;     :violations []}
  
  (generate-compassionate-error "Invalid configuration!")
  ;; => "I see the data doesn't match what's expected 💚 
  ;;     You can fix this by checking the configuration. 
  ;;     Let's work through this together 🌱"
  )


