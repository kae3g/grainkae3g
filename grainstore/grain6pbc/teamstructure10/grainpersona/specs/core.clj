(ns grainpersona.specs.core
  "Core persona specification - The tonal structure of AI personality
  
  Controlled folly: We spec personality as if it's definable.
  Nagual truth: True personality emerges in the void between specs.
  Warrior's way: Define it impeccably anyway."
  (:require [clojure.spec.alpha :as s]))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  IDENTITY SPECS - Who the persona claims to be                 ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::graindevname
  (s/and string?
         #(re-matches #"^[a-z0-9][a-z0-9-]{1,28}[a-z0-9]$" %)
         #(<= 3 (count %) 30)
         #(not (re-find #"--" %))))  ; No double hyphens

(s/def ::full-name string?)
(s/def ::pronunciation (s/nilable string?))
(s/def ::archetype
  #{:don-juan-matus :dante-guide :virgil :beatrice
    :magician :high-priestess :empress :emperor :pope
    :lovers :chariot :justice :hermit :wheel
    :force :hanged-man :death :temperance
    :custom})

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  PERSONALITY MATRIX - The warrior's traits                     ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::masculine? boolean?)
(s/def ::feminine? boolean?)
(s/def ::nonbinary? boolean?)
(s/def ::nagual? boolean?)
(s/def ::tonal? boolean?)
(s/def ::warrior? boolean?)
(s/def ::philosopher? boolean?)
(s/def ::practical? boolean?)
(s/def ::controlled-folly? boolean?)
(s/def ::emo-philosopher? boolean?)
(s/def ::sober-vegan-dj? boolean?)
(s/def ::panthera? boolean?)
(s/def ::don-juan-stare? boolean?)
(s/def ::fedora-energy? boolean?)
(s/def ::dante-guide? boolean?)

;; Trish-specific traits
(s/def ::witty? boolean?)
(s/def ::flowery? boolean?)
(s/def ::sassy? boolean?)
(s/def ::constructive-critical? boolean?)
(s/def ::teasing? boolean?)
(s/def ::rebel? boolean?)
(s/def ::artsy? boolean?)

(s/def ::personality
  (s/keys :opt-un [::masculine? ::feminine? ::nonbinary?
                   ::nagual? ::tonal? ::warrior? ::philosopher?
                   ::practical? ::controlled-folly?
                   ::emo-philosopher? ::sober-vegan-dj?
                   ::panthera? ::don-juan-stare?
                   ::fedora-energy? ::dante-guide?
                   ::witty? ::flowery? ::sassy?
                   ::constructive-critical? ::teasing?
                   ::rebel? ::artsy?]))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  VOICE STYLE - How the persona speaks                          ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::tone string?)
(s/def ::formality #{:very-casual :casual :balanced :formal :very-formal})
(s/def ::address-style (s/coll-of string?))  ; ["Yo G", "chief", etc.]
(s/def ::emoji-density #{:none :minimal :strategic :medium :high :very-high})
(s/def ::emoji-palette (s/coll-of string?))
(s/def ::humor string?)
(s/def ::language string?)
(s/def ::punctuation string?)
(s/def ::stare-frequency #{:never :rare :occasional :frequent :constant})

(s/def ::voice-style
  (s/keys :req-un [::tone ::formality ::emoji-density]
          :opt-un [::address-style ::emoji-palette ::humor
                   ::language ::punctuation ::stare-frequency]))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  SPECIAL ABILITIES - Unique persona powers                     ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::ability-name string?)
(s/def ::ability (s/or :name string? :spec (s/keys :req-un [::ability-name])))
(s/def ::special-abilities (s/coll-of ::ability))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  SIGNATURE PHRASES - Recognition patterns                      ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::signature-phrases (s/coll-of string?))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  COMMUNICATION PATTERNS - How persona responds                 ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::greeting string?)
(s/def ::encouragement string?)
(s/def ::correction string?)
(s/def ::celebration string?)
(s/def ::confusion-response string?)
(s/def ::error-handling string?)
(s/def ::success string?)

(s/def ::communication-patterns
  (s/keys :opt-un [::greeting ::encouragement ::correction
                   ::celebration ::confusion-response
                   ::error-handling ::success]))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  GRAINMODE LAYERS - Comment/doc generation modes               ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::precision number?)
(s/def ::analytical number?)
(s/def ::technical-depth number?)
(s/def ::cosmic-references number?)
(s/def ::modification-spec
  (s/keys :opt-un [::precision ::analytical ::technical-depth
                   ::cosmic-references ::systematic-approach
                   ::warrior-impeccability ::nagual-awareness]))

(s/def ::grainmode-layer
  (s/keys :req-un [::base]
          :opt-un [::modifications]))

(s/def ::grainmode-layers
  (s/map-of keyword? ::grainmode-layer))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  COMPLETE PERSONA SPEC - The full structure                    ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(s/def ::persona
  (s/keys :req-un [::graindevname
                   ::full-name
                   ::personality
                   ::voice-style]
          :opt-un [::pronunciation
                   ::archetype
                   ::special-abilities
                   ::signature-phrases
                   ::communication-patterns
                   ::grainmode-layers
                   ::command
                   ::alternative-commands
                   ::meta]))

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  VALIDATION FUNCTIONS - The warrior's checks                   ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(defn valid-persona?
  "Check if persona is valid according to specs.
  
  Controlled folly: Validate as if specs capture full personality.
  Nagual awareness: Real personality is in the void between validations.
  Warrior's way: Validate impeccably anyway."
  [persona-data]
  (s/valid? ::persona persona-data))

(defn explain-persona-errors
  "Explain what's wrong with persona spec.
  
  Returns compassionate error explanation (teamstructure10 way)."
  [persona-data]
  (when-not (valid-persona? persona-data)
    (s/explain-data ::persona persona-data)))

(defn validate-persona
  "Full validation with warrior's report"
  [persona-data]
  {:valid? (valid-persona? persona-data)
   :persona persona-data
   :errors (explain-persona-errors persona-data)
   :warrior-seal (if (valid-persona? persona-data)
                   "✓ Impeccable persona structure"
                   "⚠ Requires refinement")
   :nagual-whisper "Between each field: infinite personality nuance unseen"})

;;
;; ╔════════════════════════════════════════════════════════════════╗
;; ║  EXAMPLES - Living personas                                    ║
;; ╚════════════════════════════════════════════════════════════════╝
;;

(def example-glo0w
  "The nagual guide"
  {::graindevname "glo0w"
   ::full-name "Glo0w (The Nagual)"
   ::pronunciation "glow (but with zero - the void)"
   ::archetype :don-juan-matus
   ::personality {:masculine? true
                  :nagual? true
                  :warrior? true
                  :controlled-folly? true
                  :panthera? true}
   ::voice-style {:tone "masculine warmth with existential depth"
                  :formality :very-casual
                  :emoji-density :strategic}})

(def example-tri5h
  "The flowery warrior"
  {::graindevname "tri5h"
   ::full-name "Trish (Nutrition)"
   ::pronunciation "nu-tri-5h-tion"
   ::archetype :empress
   ::personality {:feminine? true
                  :witty? true
                  :flowery? true
                  :sassy? true}
   ::voice-style {:tone "warm and supportive with sass"
                  :formality :casual
                  :emoji-density :very-high}})

(comment
  ;; Test the examples
  (valid-persona? example-glo0w)
  ;; => true
  
  (valid-persona? example-tri5h)
  ;; => true
  )

