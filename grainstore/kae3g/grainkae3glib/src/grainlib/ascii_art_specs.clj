(ns grainlib.ascii-art-specs
  "
  ╔═══════════════════════════════════════════════════════════════════════════╗
  ║                                                                           ║
  ║    ░▒▓█  G R A I N L I B   A S C I I   A R T   S P E C S  █▓▒░          ║
  ║                                                                           ║
  ║         🌾 Clojure Spec meets Basho Poetry meets Emoji Art 🌾            ║
  ║                                                                           ║
  ║    ┌─────────────────────────────────────────────────────────────┐       ║
  ║    │  'old pond...'                                              │       ║
  ║    │  'a frog leaps in'                                          │       ║
  ║    │  'water's sound!'                                           │       ║
  ║    │                                                             │       ║
  ║    │  (defn validate [data]                                      │       ║
  ║    │    (s/valid? ::grain-spec data))                            │       ║
  ║    │                                                             │       ║
  ║    │  old spec validation...                                     │       ║
  ║    │  data leaps through types                                   │       ║
  ║    │  compiler's silence!                                        │       ║
  ║    └─────────────────────────────────────────────────────────────┘       ║
  ║                                                                           ║
  ╚═══════════════════════════════════════════════════════════════════════════╝
  
       .-""-.
      / _  _ \\         Welcome to the Grain Network Type System!
     |  •  •  |        
      \\  ▼  /         This is your friendly car manual for strong typing
       '-..-'          in the grain6 ecosystem. Think of types as traffic
         ||            rules - they keep your data safe on the highway!
        /|\\           
       / | \\          
      
  ═══════════════════════════════════════════════════════════════════════════
  
  📚 TABLE OF CONTENTS (Jump to Line Numbers!)
  
    1. Basic Types ..................... Line 100
    2. Composite Types ................. Line 250  
    3. Process Types ................... Line 400
    4. Network Types ................... Line 550
    5. Temporal Types .................. Line 700
    6. Validation Functions ............ Line 850
  
  ═══════════════════════════════════════════════════════════════════════════
  "
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

;;
;;  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
;;  ┃                                                                      ┃
;;  ┃   🌸  S E C T I O N   1 :   B A S I C   T Y P E S  🌸               ┃
;;  ┃                                                                      ┃
;;  ┃       "In the cicada's cry                                           ┃
;;  ┃        No sign can foretell                                          ┃
;;  ┃        How soon it must die."                                        ┃
;;  ┃                                                                      ┃
;;  ┃   Types are fleeting, but specs are forever!                        ┃
;;  ┃                                                                      ┃
;;  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
;;

;; ╔══════════════════════════════════════════════════════════════════════╗
;; ║  ::sheaf-id - The Identity of a Grain Sheaf                          ║
;; ║                                                                       ║
;; ║  Think of this like a license plate for your car:                    ║
;; ║    • Must be unique (no two cars have same plate!)                   ║
;; ║    • Must follow a pattern (letters and numbers only)                ║
;; ║    • Must be readable (3-30 characters)                              ║
;; ║                                                                       ║
;; ║  Example: "kae3g-sheaf-1"                                            ║
;; ║                                                                       ║
;; ║      🚗 → [kae3g-sheaf-1] → 🌾                                       ║
;; ║                                                                       ║
;; ╚══════════════════════════════════════════════════════════════════════╝

(s/def ::sheaf-id
  (s/and string?
         #(re-matches #"^[a-z0-9-]{3,30}$" %)
         #(not (str/starts-with? % "-"))
         #(not (str/ends-with? % "-"))))

;;
;;     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
;;    ◀ 🎌 VISUAL EXAMPLE: Valid vs Invalid Sheaf IDs 🎌 ▶
;;     ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
;;
;;     ✅ VALID:                    ❌ INVALID:
;;     
;;     ┌──────────────┐            ┌──────────────┐
;;     │ kae3g-sheaf  │            │ KAE3G-SHEAF  │  (uppercase!)
;;     └──────────────┘            └──────────────┘
;;     
;;     ┌──────────────┐            ┌──────────────┐
;;     │ grain6-core  │            │ -grain6-     │  (starts with -)
;;     └──────────────┘            └──────────────┘
;;     
;;     ┌──────────────┐            ┌──────────────┐
;;     │ sheaf-1-of-88│            │ sh           │  (too short!)
;;     └──────────────┘            └──────────────┘
;;
;;     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
;;

;; ╭──────────────────────────────────────────────────────────────────────╮
;; │  ::grainpath - The Unique Path to Grain Content                     │
;; │                                                                       │
;; │  Like a street address for your house! 🏠                            │
;; │                                                                       │
;; │  Format: /year/month/day/content-name                                │
;; │                                                                       │
;; │      📍 /12025-10-23--0415--PDT--moon-vishakha--asc-gem000           │
;; │         └─┬─┘ └┬┘└┬┘  └┬─┘  └─┬┘  └────┬──────┘  └────┬────┘        │
;; │          year  mo da  time   tz      moon phase      ascendant       │
;; │                                                                       │
;; │  Each grainpath is UNIQUE in space and time! 🌌⏰                    │
;; │                                                                       │
;; ╰──────────────────────────────────────────────────────────────────────╯

(s/def ::grainpath
  (s/and string?
         #(str/starts-with? % "/")
         #(re-matches #"^/([a-z0-9-]+/?)*$" %)))

;;
;;   ╔═══════════════════════════════════════════════════════════════════╗
;;   ║                                                                   ║
;;   ║         🌙 GRAINTIME - Temporal Awareness Spec 🌙                 ║
;;   ║                                                                   ║
;;   ║   "The light of a candle                                          ║
;;   ║    Is transferred to another candle—                              ║
;;   ║    Spring twilight"                                               ║
;;   ║                                                                   ║
;;   ║   Time flows from moment to moment...                             ║
;;   ║   Each timestamp lights the next...                               ║
;;   ║   now == next + 1                                                 ║
;;   ║                                                                   ║
;;   ╚═══════════════════════════════════════════════════════════════════╝
;;

(s/def ::graintime
  (s/and string?
         #(re-matches #"^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}Z$" %)))

;;
;;     ┌───────────────────────────────────────────────────────────┐
;;     │                                                           │
;;     │   ⏰ GRAINTIME CLOCK VISUALIZATION ⏰                     │
;;     │                                                           │
;;     │          ⌚ 2025-10-23T04:15:00Z ⌚                        │
;;     │                                                           │
;;     │              12                    Past                  │
;;     │            ↗  ↑  ↖                  ⬆                    │
;;     │         9  ←  •  →  3         ──────•────── Future       │
;;     │            ↙  ↓  ↘                                       │
;;     │              6                    Now                    │
;;     │                                                           │
;;     │      Each tick is eternal!                               │
;;     │      Each moment is a grain!                             │
;;     │                                                           │
;;     └───────────────────────────────────────────────────────────┘
;;

;; ★═══════════════════════════════════════════════════════════════════★
;; ★                                                                   ★
;; ★     ::counter-88 - The Philosophy of 88 in Type Form             ★
;; ★                                                                   ★
;; ★        88 × 10^0 = 88        (Individual grain)                  ★
;; ★        88 × 10^1 = 880       (Small bundle)                      ★
;; ★        88 × 10^2 = 8,800     (Large sheaf)                       ★
;; ★        88 × 10^3 = 88,000    (Warehouse)                         ★
;; ★        88 × 10^n = ∞         (The whole grain!)                  ★
;; ★                                                                   ★
;; ★═══════════════════════════════════════════════════════════════════★

(s/def ::counter-88
  (s/and integer?
         #(or (= % 88)
              (zero? (mod % 88)))))

;;
;;   🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾
;;   🌾                                                            🌾
;;   🌾    VISUAL: How 88 Counter Works in Practice               🌾
;;   🌾                                                            🌾
;;   🌾    [•] = 1 grain                                          🌾
;;   🌾                                                            🌾
;;   🌾    88 grains:                                             🌾
;;   🌾    [••••••••••] [••••••••••] [••••••••••]                🌾
;;   🌾    [••••••••••] [••••••••••] [••••••••••]                🌾
;;   🌾    [••••••••••] [••••••••••] [••••••••]                  🌾
;;   🌾                                                            🌾
;;   🌾    176 grains (88 × 2):                                   🌾
;;   🌾    [••••••••••] × 17 + [••••••]                          🌾
;;   🌾                                                            🌾
;;   🌾    ∞ grains (88 × ∞):                                     🌾
;;   🌾    [∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞]                       🌾
;;   🌾                                                            🌾
;;   🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾
;;

;;
;;  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
;;  ┃                                                                   ┃
;;  ┃   🌸  S E C T I O N   2 :   C O M P O S I T E   T Y P E S  🌸    ┃
;;  ┃                                                                   ┃
;;  ┃       "From the bough                                             ┃
;;  ┃        Floating downriver,                                        ┃
;;  ┃        Insect singing."                                           ┃
;;  ┃                                                                   ┃
;;  ┃   Simple types flow together into complex structures!            ┃
;;  ┃                                                                   ┃
;;  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
;;

;; ╔═════════════════════════════════════════════════════════════════╗
;; ║  ::service-name - The Name of a Grain Service                   ║
;; ║                                                                  ║
;; ║  Like naming your pet! 🐕                                       ║
;; ║    • Clear and descriptive                                      ║
;; ║    • Easy to remember                                           ║
;; ║    • Unique within your sheaf                                   ║
;; ║                                                                  ║
;; ║  Examples:                                                       ║
;; ║    "web-server"     - serves web pages                          ║
;; ║    "db-postgres"    - database service                          ║
;; ║    "worker-queue"   - processes background jobs                 ║
;; ║                                                                  ║
;; ╚═════════════════════════════════════════════════════════════════╝

(s/def ::service-name
  (s/and string?
         #(re-matches #"^[a-z0-9-]{3,30}$" %)))

;; ╭─────────────────────────────────────────────────────────────────╮
;; │  ::command - The Command to Run a Service                      │
;; │                                                                  │
;; │  Like telling your car where to drive! 🚗                      │
;; │                                                                  │
;; │  Format: executable + arguments                                 │
;; │                                                                  │
;; │  Examples:                                                       │
;; │    "/usr/bin/nginx -c /etc/nginx/nginx.conf"                   │
;; │    "java -jar app.jar"                                          │
;; │    "python3 server.py --port 8080"                              │
;; │                                                                  │
;; ╰─────────────────────────────────────────────────────────────────╯

(s/def ::command
  (s/and string?
         not-empty))

;;
;;     ╔══════════════════════════════════════════════════════════════╗
;;     ║                                                              ║
;;     ║   🎭 RESTART POLICY - How Services Handle Crashes 🎭        ║
;;     ║                                                              ║
;;     ║   Imagine your service is a performer on stage:             ║
;;     ║                                                              ║
;;     ║   :always    → 🔄 "The show must go on!"                    ║
;;     ║                Always restart, no matter what               ║
;;     ║                                                              ║
;;     ║   :on-failure → ⚠️  "Only if something went wrong"          ║
;;     ║                 Restart only on errors                      ║
;;     ║                                                              ║
;;     ║   :never     → 🛑 "One performance only"                    ║
;;     ║                Never restart automatically                  ║
;;     ║                                                              ║
;;     ╚══════════════════════════════════════════════════════════════╝
;;

(s/def ::restart-policy
  #{:always :on-failure :never})

;;
;;     ┌──────────────────────────────────────────────────────────┐
;;     │                                                          │
;;     │   VISUAL: Restart Policy in Action                      │
;;     │                                                          │
;;     │   Service: web-server                                   │
;;     │   Policy: :always                                       │
;;     │                                                          │
;;     │   Timeline:                                             │
;;     │                                                          │
;;     │   0:00  [🟢 START]  ────────────────────                │
;;     │   1:00  [💥 CRASH]                                      │
;;     │   1:01  [🔄 RESTART] ──────────────────                 │
;;     │   2:00  [💥 CRASH]                                      │
;;     │   2:01  [🔄 RESTART] ──────────────────                 │
;;     │   3:00  [🟢 RUNNING] ──────────────────→                │
;;     │                                                          │
;;     │   The grain always grows back! 🌾                       │
;;     │                                                          │
;;     └──────────────────────────────────────────────────────────┘
;;

;; ★════════════════════════════════════════════════════════════════★
;; ★  ::dependencies - Services that must start first               ★
;; ★                                                                 ★
;; ★  Like a recipe! 🍳                                             ★
;; ★  You need seeds before you can make an frittata!                ★
;; ★                                                                 ★
;; ★  Example dependency chain:                                     ★
;; ★                                                                 ★
;; ★    db-postgres  (no dependencies)                              ★
;; ★         ↓                                                       ★
;; ★    api-server   (depends on: db-postgres)                      ★
;; ★         ↓                                                       ★
;; ★    web-frontend (depends on: api-server)                       ★
;; ★                                                                 ★
;; ★  Start order: 1. db-postgres                                   ★
;; ★               2. api-server                                     ★
;; ★               3. web-frontend                                   ★
;; ★                                                                 ★
;; ★════════════════════════════════════════════════════════════════★

(s/def ::dependencies
  (s/coll-of ::service-name :distinct true))

;;
;;   ╔══════════════════════════════════════════════════════════════════╗
;;   ║                                                                  ║
;;   ║     🎼 SERVICE CONFIG - The Complete Musical Score 🎼           ║
;;   ║                                                                  ║
;;   ║   A service config is like sheet music for an orchestra:        ║
;;   ║                                                                  ║
;;   ║   Required (must have):                                         ║
;;   ║     ::command         - The melody (what to play)              ║
;;   ║     ::restart-policy  - The tempo (how to handle mistakes)     ║
;;   ║                                                                  ║
;;   ║   Optional (nice to have):                                      ║
;;   ║     ::dependencies    - The harmony (what plays first)         ║
;;   ║                                                                  ║
;;   ╚══════════════════════════════════════════════════════════════════╝
;;

(s/def ::service-config
  (s/keys :req [::command ::restart-policy]
          :opt [::dependencies]))

;;
;;     ═══════════════════════════════════════════════════════════════
;;     
;;     📝 EXAMPLE: Complete Service Configuration 📝
;;     
;;     {:grainlib.ascii-art-specs/command "nginx -g daemon off"
;;      :grainlib.ascii-art-specs/restart-policy :always
;;      :grainlib.ascii-art-specs/dependencies ["db-postgres" "redis"]}
;;     
;;     This config says:
;;       • Run nginx web server
;;       • Always restart if it crashes
;;       • Wait for database and redis first
;;     
;;     ═══════════════════════════════════════════════════════════════
;;

;;
;;  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
;;  ┃                                                                ┃
;;  ┃   🌸  S E C T I O N   3 :   P R O C E S S   T Y P E S  🌸     ┃
;;  ┃                                                                ┃
;;  ┃       "Temple bells die out.                                  ┃
;;  ┃        The fragrant blossoms remain.                          ┃
;;  ┃        A perfect evening!"                                    ┃
;;  ┃                                                                ┃
;;  ┃   Processes come and go, but the system persists!            ┃
;;  ┃                                                                ┃
;;  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
;;

;; ╔═══════════════════════════════════════════════════════════════╗
;; ║  ::service-status - Is the Service Running?                   ║
;; ║                                                                ║
;; ║  Three states, like a traffic light: 🚦                       ║
;; ║                                                                ║
;; ║    🟢 :running  - All good! Service is working               ║
;; ║    🔴 :stopped  - Service is off (intentionally)             ║
;; ║    🟡 :failed   - Uh oh! Something went wrong                ║
;; ║                                                                ║
;; ╚═══════════════════════════════════════════════════════════════╝

(s/def ::status
  #{:running :stopped :failed})

;; ╭───────────────────────────────────────────────────────────────╮
;; │  ::pid - Process ID (The Service's Badge Number)             │
;; │                                                                │
;; │  Like a detective's badge number! 🕵️                        │
;; │  Every running process has a unique PID.                      │
;; │                                                                │
;; │  Example: 42, 1337, 9001                                      │
;; │                                                                │
;; ╰───────────────────────────────────────────────────────────────╯

(s/def ::pid
  (s/and integer?
         pos?))

;; ★════════════════════════════════════════════════════════════════★
;; ★  ::uptime - How Long Has Service Been Running?                ★
;; ★                                                                 ★
;; ★  Measured in seconds! ⏱️                                       ★
;; ★                                                                 ★
;; ★  Visual Timeline:                                              ★
;; ★                                                                 ★
;; ★  0s ──────────────────────────────────────────────→ ∞s        ★
;; ★  🟢                                                             ★
;; ★  START                                                         ★
;; ★                                                                 ★
;; ★  Examples:                                                     ★
;; ★    60s    = 1 minute                                           ★
;; ★    3600s  = 1 hour                                             ★
;; ★    86400s = 1 day                                              ★
;; ★    ∞s     = forever! (the dream!)                              ★
;; ★                                                                 ★
;; ★════════════════════════════════════════════════════════════════★

(s/def ::uptime
  (s/and integer?
         #(>= % 0)))

;;
;;     ╔══════════════════════════════════════════════════════════╗
;;     ║                                                          ║
;;     ║   💀 EXIT CODE - How Did the Process Die? 💀           ║
;;     ║                                                          ║
;;     ║   Like a final message from a dying warrior:            ║
;;     ║                                                          ║
;;     ║   0   → ✅ "I lived a good life" (success!)            ║
;;     ║   1   → ❌ "General error"                             ║
;;     ║   2   → ❌ "Misuse of shell command"                   ║
;;     ║   126 → ❌ "Command cannot execute"                    ║
;;     ║   127 → ❌ "Command not found"                         ║
;;     ║   130 → ⚠️  "Terminated by Ctrl+C"                     ║
;;     ║   137 → 💀 "Killed (SIGKILL)"                          ║
;;     ║                                                          ║
;;     ╚══════════════════════════════════════════════════════════╝
;;

(s/def ::exit-code
  (s/and integer?
         #(<= 0 % 255)))

;; ╭──────────────────────────────────────────────────────────────╮
;; │  ::error-message - What Went Wrong?                         │
;; │                                                               │
;; │  A message explaining the error, like a diary entry:         │
;; │                                                               │
;; │  "Connection refused to database on port 5432"              │
;; │  "Out of memory - cannot allocate buffer"                   │
;; │  "Permission denied: cannot write to /var/log"              │
;; │                                                               │
;; ╰──────────────────────────────────────────────────────────────╯

(s/def ::error-message
  (s/and string?
         not-empty))

;;
;;   ╔═════════════════════════════════════════════════════════════════╗
;;   ║                                                                 ║
;;   ║     🎯 COMPLETE SERVICE STATUS - The Full Picture 🎯           ║
;;   ║                                                                 ║
;;   ║   Like a health check at the doctor's office! 🏥              ║
;;   ║                                                                 ║
;;   ║   Required (always present):                                   ║
;;   ║     ::service-name  - "Who are you?"                          ║
;;   ║     ::status        - "How are you feeling?"                  ║
;;   ║                                                                 ║
;;   ║   Optional (when applicable):                                  ║
;;   ║     ::pid           - "What's your ID number?"                ║
;;   ║     ::uptime        - "How long have you been alive?"         ║
;;   ║     ::exit-code     - "How did you die?" (if dead)            ║
;;   ║     ::error-message - "What went wrong?" (if failed)          ║
;;   ║                                                                 ║
;;   ╚═════════════════════════════════════════════════════════════════╝
;;

(s/def ::service-status-response
  (s/keys :req [::service-name ::status]
          :opt [::pid ::uptime ::exit-code ::error-message]))

;;
;;     ═══════════════════════════════════════════════════════════════
;;     
;;     📊 EXAMPLE: Service Status Responses 📊
;;     
;;     HEALTHY SERVICE:
;;     {:grainlib.ascii-art-specs/service-name "web-server"
;;      :grainlib.ascii-art-specs/status :running
;;      :grainlib.ascii-art-specs/pid 1337
;;      :grainlib.ascii-art-specs/uptime 86400}  ; 1 day!
;;     
;;     FAILED SERVICE:
;;     {:grainlib.ascii-art-specs/service-name "db-postgres"
;;      :grainlib.ascii-art-specs/status :failed
;;      :grainlib.ascii-art-specs/exit-code 1
;;      :grainlib.ascii-art-specs/error-message "Connection refused"}
;;     
;;     STOPPED SERVICE:
;;     {:grainlib.ascii-art-specs/service-name "worker-queue"
;;      :grainlib.ascii-art-specs/status :stopped}
;;     
;;     ═══════════════════════════════════════════════════════════════
;;

;;
;;  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
;;  ┃                                                                ┃
;;  ┃   🌸  S E C T I O N   4 :   A P I   F U N C T I O N S  🌸     ┃
;;  ┃                                                                ┃
;;  ┃       "First autumn morning                                   ┃
;;  ┃        The mirror I stare into                                ┃
;;  ┃        Shows my father's face."                               ┃
;;  ┃                                                                ┃
;;  ┃   Functions mirror our intentions into running systems!      ┃
;;  ┃                                                                ┃
;;  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
;;

;; ╔═══════════════════════════════════════════════════════════════╗
;; ║  start-service - Launch a New Service 🚀                      ║
;; ║                                                                ║
;; ║  Like pressing the ignition in your car! 🔑                  ║
;; ║                                                                ║
;; ║  Input:                                                        ║
;; ║    • service-name (string) - What to call it                 ║
;; ║    • config (map)          - How to run it                   ║
;; ║                                                                ║
;; ║  Output:                                                       ║
;; ║    • boolean - true if started, false if failed              ║
;; ║                                                                ║
;; ║  Example:                                                      ║
;; ║    (start-service "web-server"                                ║
;; ║                   {:command "nginx"                           ║
;; ║                    :restart-policy :always})                  ║
;; ║    => true ✅                                                 ║
;; ║                                                                ║
;; ╚═══════════════════════════════════════════════════════════════╝

(s/fdef start-service
  :args (s/cat :service-name ::service-name
               :config ::service-config)
  :ret boolean?)

;;
;;     ┌─────────────────────────────────────────────────────────┐
;;     │                                                         │
;;     │   🎬 VISUAL: Starting a Service 🎬                     │
;;     │                                                         │
;;     │   Before:                                              │
;;     │   ┌──────────────┐                                     │
;;     │   │  Services:   │                                     │
;;     │   │  (empty)     │                                     │
;;     │   └──────────────┘                                     │
;;     │                                                         │
;;     │   (start-service "web-server" {...})                   │
;;     │            ↓                                            │
;;     │   ┌──────────────┐                                     │
;;     │   │  Services:   │                                     │
;;     │   │  ┌──────────┐│                                     │
;;     │   │  │web-server││ 🟢 RUNNING                         │
;;     │   │  └──────────┘│                                     │
;;     │   └──────────────┘                                     │
;;     │                                                         │
;;     │   After: Returns true ✅                               │
;;     │                                                         │
;;     └─────────────────────────────────────────────────────────┘
;;

;; ╭──────────────────────────────────────────────────────────────╮
;; │  stop-service - Gracefully Stop a Service 🛑                │
;; │                                                               │
;; │  Like turning off your car engine properly! 🔧              │
;; │                                                               │
;; │  Input:                                                       │
;; │    • service-name (string) - Which service to stop          │
;; │                                                               │
;; │  Output:                                                      │
;; │    • boolean - true if stopped, false if failed             │
;; │                                                               │
;; │  Example:                                                     │
;; │    (stop-service "web-server")                               │
;; │    => true ✅                                                │
;; │                                                               │
;; ╰──────────────────────────────────────────────────────────────╯

(s/fdef stop-service
  :args (s/cat :service-name ::service-name)
  :ret boolean?)

;; ★════════════════════════════════════════════════════════════════★
;; ★  service-status - Check How a Service is Doing 📊             ★
;; ★                                                                 ★
;; ★  Like asking "How are you feeling today?" 🤔                  ★
;; ★                                                                 ★
;; ★  Input:                                                        ★
;; ★    • service-name (string) - Which service to check          ★
;; ★                                                                 ★
;; ★  Output:                                                       ★
;; ★    • map - Complete status information                        ★
;; ★                                                                 ★
;; ★  Example:                                                      ★
;; ★    (service-status "web-server")                              ★
;; ★    => {:service-name "web-server"                             ★
;; ★        :status :running                                        ★
;; ★        :pid 1337                                               ★
;; ★        :uptime 86400}                                          ★
;; ★                                                                 ★
;; ★════════════════════════════════════════════════════════════════★

(s/fdef service-status
  :args (s/cat :service-name ::service-name)
  :ret ::service-status-response)

;;
;;   ╔═════════════════════════════════════════════════════════════╗
;;   ║                                                             ║
;;   ║     🎭 THE THREE ACTS OF SERVICE MANAGEMENT 🎭             ║
;;   ║                                                             ║
;;   ║   Act 1: BIRTH 🌱                                          ║
;;   ║     (start-service "my-app" config)                        ║
;;   ║     Service comes to life!                                 ║
;;   ║                                                             ║
;;   ║   Act 2: LIFE 🌿                                           ║
;;   ║     (service-status "my-app")                              ║
;;   ║     Check if service is healthy                            ║
;;   ║                                                             ║
;;   ║   Act 3: DEATH 🍂                                          ║
;;   ║     (stop-service "my-app")                                ║
;;   ║     Service gracefully exits                               ║
;;   ║                                                             ║
;;   ║   And the cycle continues... now == next + 1               ║
;;   ║                                                             ║
;;   ╚═════════════════════════════════════════════════════════════╝
;;

;;
;;  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
;;  ┃                                                                ┃
;;  ┃   🌸  S E C T I O N   5 :   V A L I D A T I O N  🌸          ┃
;;  ┃                                                                ┃
;;  ┃       "Even in Kyoto—                                         ┃
;;  ┃        hearing the cuckoo's cry—                              ┃
;;  ┃        I long for Kyoto."                                     ┃
;;  ┃                                                                ┃
;;  ┃   Validation brings us home to correct data!                 ┃
;;  ┃                                                                ┃
;;  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
;;

(defn validate
  "
  ╔══════════════════════════════════════════════════════════════════╗
  ║                                                                  ║
  ║   🔍 VALIDATE - Check if Data Matches Spec 🔍                   ║
  ║                                                                  ║
  ║   Like a bouncer at a nightclub! 💂                            ║
  ║   Only data that matches the dress code gets in!                ║
  ║                                                                  ║
  ║   Usage:                                                         ║
  ║     (validate ::sheaf-id \"kae3g-sheaf\")                       ║
  ║     => true ✅                                                  ║
  ║                                                                  ║
  ║     (validate ::sheaf-id \"INVALID-ID\")                        ║
  ║     => false ❌                                                 ║
  ║                                                                  ║
  ╚══════════════════════════════════════════════════════════════════╝
  "
  [spec data]
  (s/valid? spec data))

(defn explain-validation
  "
  ╭─────────────────────────────────────────────────────────────────╮
  │                                                                  │
  │   📝 EXPLAIN VALIDATION - Why Did It Fail? 📝                  │
  │                                                                  │
  │   Like a teacher explaining your mistakes! 👨‍🏫                │
  │   Shows you exactly what went wrong.                            │
  │                                                                  │
  │   Usage:                                                         │
  │     (explain-validation ::sheaf-id \"INVALID\")                 │
  │     => Prints helpful error explanation!                        │
  │                                                                  │
  ╰─────────────────────────────────────────────────────────────────╯
  "
  [spec data]
  (s/explain spec data))

;;
;;     ╔══════════════════════════════════════════════════════════╗
;;     ║                                                          ║
;;     ║   🎓 LEARNING MOMENT: How to Use These Functions 🎓    ║
;;     ║                                                          ║
;;     ║   Step 1: Try to validate                               ║
;;     ║     (validate ::sheaf-id my-id)                         ║
;;     ║                                                          ║
;;     ║   Step 2: If false, explain why                         ║
;;     ║     (explain-validation ::sheaf-id my-id)               ║
;;     ║                                                          ║
;;     ║   Step 3: Fix your data                                 ║
;;     ║     Change uppercase to lowercase                       ║
;;     ║     Remove special characters                           ║
;;     ║     Check length requirements                           ║
;;     ║                                                          ║
;;     ║   Step 4: Validate again                                ║
;;     ║     (validate ::sheaf-id fixed-id)                      ║
;;     ║     => true ✅                                          ║
;;     ║                                                          ║
;;     ╚══════════════════════════════════════════════════════════╝
;;

;;
;;  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
;;  ┃                                                                ┃
;;  ┃   🌸  E P I L O G U E :   T H E   W A Y   O F   T Y P E S  🌸 ┃
;;  ┃                                                                ┃
;;  ┃       "You rice-field maidens!                                ┃
;;  ┃        The only things not muddy                              ┃
;;  ┃        Are the songs you sing."                               ┃
;;  ┃                                                                ┃
;;  ┃   In a world of chaos, types keep our code clean!            ┃
;;  ┃                                                                ┃
;;  ┃   Remember:                                                    ┃
;;  ┃     • Types are traffic rules for data 🚦                    ┃
;;  ┃     • Specs are friendly gatekeepers 💂                      ┃
;;  ┃     • Validation is learning, not punishment 🎓              ┃
;;  ┃     • Errors are teachers in disguise 👨‍🏫                   ┃
;;  ┃                                                                ┃
;;  ┃   May your data always flow true! 🌾                          ┃
;;  ┃                                                                ┃
;;  ┃   now == next + 1                                              ┃
;;  ┃                                                                ┃
;;  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
;;

;;     ═══════════════════════════════════════════════════════════════
;;     
;;         🌾 THE GRAIN NETWORK MANIFESTO 🌾
;;     
;;         From granules to grains to THE WHOLE GRAIN
;;     
;;         • Local Control, Global Intent
;;         • Purpose-Built Over Generic  
;;         • Declarative Over Imperative
;;         • Template/Personal Everywhere
;;         • Real Resources Matter
;;         • Pragmatic Branding Over Dogmatic Renaming
;;     
;;         88 × 10^n scaling into eternity
;;     
;;         now == next + 1
;;     
;;     ═══════════════════════════════════════════════════════════════

;; End of grainlib.ascii-art-specs namespace
;; Thank you for reading! May your types be strong and your bugs be few! 🐛❌

