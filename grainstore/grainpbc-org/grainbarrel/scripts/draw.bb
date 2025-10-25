#!/usr/bin/env bb

;;
;;  ╔════════════════════════════════════════════════════════════════════╗
;;  ║                                                                    ║
;;  ║    🎨 GB DRAW - Generate Beautiful ASCII Art Documentation 🎨     ║
;;  ║                                                                    ║
;;  ║         "An old silent pond                                        ║
;;  ║          A frog jumps into the pond—                               ║
;;  ║          Splash! Silence again."                                   ║
;;  ║                                                                    ║
;;  ║    This script generates artistic documentation                    ║
;;  ║    with ASCII art comments for the Grain Network.                  ║
;;  ║                                                                    ║
;;  ╚════════════════════════════════════════════════════════════════════╝
;;

(require '[babashka.process :refer [shell]]
         '[clojure.string :as str]
         '[clojure.java.io :as io])

(defn print-banner []
  (println "
╔═══════════════════════════════════════════════════════════════════════════╗
║                                                                           ║
║      🎨 G B   D R A W - ASCII Art Documentation Generator 🎨             ║
║                                                                           ║
║         Inspired by Basho • Built for Grain Network                      ║
║         Collegiate Car Manual • High School Educational                  ║
║         Geometric Spacing • Anime Doodler Friendly                       ║
║                                                                           ║
╚═══════════════════════════════════════════════════════════════════════════╝
"))

(defn draw-graincard []
  (println "
┌───────────────────────────────────────────────────────────────────────────┐
│                                                                           │
│                        🌾 G R A I N C A R D 🌾                           │
│                                                                           │
│   ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   │
│   ░                                                                 ░   │
│   ░   80 characters wide × 110 lines tall                           ░   │
│   ░   Perfect portrait format for knowledge cards                   ░   │
│   ░   10,000 page capacity (0000-9999)                              ░   │
│   ░                                                                 ░   │
│   ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   │
│                                                                           │
└───────────────────────────────────────────────────────────────────────────┘
"))

(defn draw-88-philosophy []
  (println "
   🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾
   🌾                                                            🌾
   🌾    ⚛️  T H E   8 8   C O U N T E R   P H I L O S O P H Y  ⚛️    🌾
   🌾                                                            🌾
   🌾    88 × 10^0 = 88        [Individual grain]               🌾
   🌾    88 × 10^1 = 880       [Small bundle]                   🌾
   🌾    88 × 10^2 = 8,800     [Large sheaf]                    🌾
   🌾    88 × 10^3 = 88,000    [Warehouse]                      🌾
   🌾    88 × 10^n = ∞         [THE WHOLE GRAIN]                🌾
   🌾                                                            🌾
   🌾              now == next + 1                               🌾
   🌾                                                            🌾
   🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾🌾
"))

(defn draw-grain-network []
  (println "
   ╔═══════════════════════════════════════════════════════════════╗
   ║                                                               ║
   ║        🌐 T H E   G R A I N   N E T W O R K 🌐               ║
   ║                                                               ║
   ║                    ICP          Hedera        Solana          ║
   ║                     ◯              ◯             ◯           ║
   ║                      \\            |            /            ║
   ║                       \\           |           /             ║
   ║                        ◯──────────◯──────────◯              ║
   ║                       /     Grain Network     \\             ║
   ║                      /                         \\            ║
   ║                     ◯                           ◯            ║
   ║                  grain6                    grainphone        ║
   ║                                                               ║
   ║   Multi-Chain Sovereignty • Template/Personal Separation      ║
   ║   Local Control, Global Intent • 88 × 10^n Scaling           ║
   ║                                                               ║
   ╚═══════════════════════════════════════════════════════════════╝
"))

(defn draw-basho-haiku []
  (let [haikus [
                "\"An old silent pond\n   A frog jumps into the pond—\n   Splash! Silence again.\""
                "\"In the cicada's cry\n   No sign can foretell\n   How soon it must die.\""
                "\"The light of a candle\n   Is transferred to another candle—\n   Spring twilight\""
                "\"Temple bells die out.\n   The fragrant blossoms remain.\n   A perfect evening!\""
                "\"From the bough\n   Floating downriver,\n   Insect singing.\""
                "\"First autumn morning\n   The mirror I stare into\n   Shows my father's face.\""]]
    (println "\n   ┌─────────────────────────────────────────────────────┐")
    (println "   │                                                     │")
    (println "   │        🌸 Basho's Wisdom for Coders 🌸             │")
    (println "   │                                                     │")
    (println (str "   │   " (rand-nth haikus)))
    (println "   │                                                     │")
    (println "   └─────────────────────────────────────────────────────┘\n")))

(defn draw-emoji-library []
  (println "
   ╔═══════════════════════════════════════════════════════════════╗
   ║                                                               ║
   ║     📱 G R A I N   E M O J I   L I B R A R Y 📱              ║
   ║                                                               ║
   ║   Status Indicators:                                          ║
   ║     🟢 Running  🔴 Stopped  🟡 Warning  💥 Crashed           ║
   ║                                                               ║
   ║   Grain Symbols:                                              ║
   ║     🌾 Grain  🌱 Sprout  🍂 Harvest  🌸 Blossom              ║
   ║                                                               ║
   ║   System Icons:                                               ║
   ║     🚀 Launch  🛑 Stop  ⏸️ Pause  🔄 Restart                 ║
   ║                                                               ║
   ║   Blockchain:                                                 ║
   ║     ⛓️ Chain  🔐 Secure  💎 Token  🪙 Coin                   ║
   ║                                                               ║
   ║   Education:                                                  ║
   ║     🎓 Learn  📚 Docs  🧪 Test  🔬 Research                  ║
   ║                                                               ║
   ╚═══════════════════════════════════════════════════════════════╝
"))

(defn draw-instagram-fonts []
  (println "
   ┌───────────────────────────────────────────────────────────────┐
   │                                                               │
   │     ＩＮＳＴＡＧＲＡＭ ＦＯＮＴ ＳＴＹＬＥＳ                      │
   │                                                               │
   │   𝔾𝕣𝕒𝕚𝕟 ℕ𝕖𝕥𝕨𝕠𝕣𝕜 (𝔻𝕠𝕦𝕓𝕝𝕖 𝕊𝕥𝕣𝕦𝕔𝕜)                    │
   │   𝓖𝓻𝓪𝓲𝓷 𝓝𝓮𝓽𝔀𝓸𝓻𝓴 (𝓢𝓬𝓻𝓲𝓹𝓽)                              │
   │   𝐆𝐫𝐚𝐢𝐧 𝐍𝐞𝐭𝐰𝐨𝐫𝐤 (𝐁𝐨𝐥𝐝)                                │
   │   𝘎𝘳𝘢𝘪𝘯 𝘕𝘦𝘵𝘸𝘰𝘳𝘬 (𝘐𝘵𝘢𝘭𝘪𝘤)                             │
   │                                                               │
   │   ░▒▓█  Ｇｒａｉｎ  Ｎｅｔｗｏｒｋ  █▓▒░                     │
   │                                                               │
   └───────────────────────────────────────────────────────────────┘
"))

(defn generate-all-art []
  (print-banner)
  (draw-basho-haiku)
  (draw-graincard)
  (draw-88-philosophy)
  (draw-grain-network)
  (draw-emoji-library)
  (draw-instagram-fonts)
  (println "\n✨ ASCII art documentation generated!")
  (println "🌾 now == next + 1\n"))

;; Main execution
(generate-all-art)

