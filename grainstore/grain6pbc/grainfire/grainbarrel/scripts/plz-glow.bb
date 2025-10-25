#!/usr/bin/env bb
;; PLZ GLOW - glO0w's Voice: Masculine, tall bro, Don Juan + Panthera + sober vegan philosopher DJ
;; Hidden site doc 8002 v888 voice - the masculine counterpart to tri5h

(require '[clojure.string :as str])

(def glow-greetings
  ["Yo, what's good? glO0w here, ready to glow up your code! ✨"
   "Hey fam, glO0w in the house! Let's make this code shine! 🌟"
   "What's crackin'? glO0w here, bringing that masculine energy! 💪"
   "Yo yo yo, glO0w checking in! Ready to elevate your game? 🚀"
   "What's up, chief? glO0w here, let's get this code glowing! 🔥"
   "Okay you go my G my Guy! glO0w here, let's glow! ✨"
   "Yo G, glO0w in the building! Ready to make it shine? 🌟"
   "What's good my Guy? glO0w here, let's elevate! 💪"])

(def glow-tips
  ["Bruh, your code needs more glow-up energy. Think Carlos Castaneda meets Panthera! 🐆"
   "Yo, that function is looking basic. Add some Don Juan mystique to it! 🧙‍♂️"
   "Fam, you're coding like you're still in the matrix. Break free, glow up! ✨"
   "Chief, that algorithm needs more masculine energy. Make it glow! 💪"
   "Bruh, your architecture is solid but needs more philosophical depth. Think deeper! 🧠"
   "Yo, that code is functional but lacks the glow factor. Add some magic! ✨"
   "Fam, you're thinking too small. Scale up that energy, make it glow! 🚀"
   "Chief, that design needs more Panthera energy. Be the alpha of your code! 🐆"])

(def glow-encouragements
  ["Yo, you got this! glO0w believes in your glow-up potential! ✨"
   "Bruh, that's the energy I'm talking about! Keep that glow going! 🌟"
   "Fam, you're on fire! That's the masculine coding energy we need! 🔥"
   "Chief, you're glowing up! Don't stop now, keep elevating! 🚀"
   "Yo, that's what I'm talking about! You're becoming the alpha coder! 💪"
   "Bruh, you're channeling that Don Juan energy perfectly! Keep it up! 🧙‍♂️"
   "Fam, you're thinking like a true philosopher now! That's the glow! 🧠"
   "Chief, you're bringing that Panthera energy! Keep being the alpha! 🐆"])

(def glow-sass
  ["Yo, tri5h over there being all flowery and stuff. We need some masculine energy up in here! 💪"
   "Bruh, that's cute and all, but where's the glow? Where's the masculine mystique? ✨"
   "Fam, tri5h got the nutrition game on lock, but we need some philosophical depth! 🧠"
   "Chief, tri5h's haikus are nice, but we need some Don Juan wisdom up in here! 🧙‍♂️"
   "Yo, tri5h's all about the flowers, but we need some Panthera energy! 🐆"
   "Bruh, tri5h's got the feminine energy, but we need some masculine glow! 🌟"
   "Fam, tri5h's being all sweet, but we need some alpha energy! 💪"
   "Chief, tri5h's got the flowery vibes, but we need some philosophical depth! 🧠"])

(def glow-dad-jokes
  ["Why did the function go to therapy? / It had too much glow bal thinking / Now it's perfectly balanced! 😢✨"
   "What do you call a sad algorithm? / A gloom-ithm / It needs more glow bal energy! 😭🌟"
   "Why did the code break up with its girlfriend? / It was too clingy / Time for some glow bal thinking! 💔💪"
   "What's a programmer's favorite type of music? / Glow-core / Because it's so emo and deep! 🎵😢"
   "Why did the database feel empty? / It had no glow bal thinking / Just pure existential dread! 😞💾"
   "What do you call a depressed variable? / A gloom-able / It needs some glow bal therapy! 😭🔢"
   "Why did the API break up with the frontend? / It was too needy / Time for glow bal independence! 💔🌐"
   "What's a coder's favorite emo band? / Glow-182 / Because it's all about the glow bal life! 🎸😢"
   "Why did the function feel lonely? / It had no glow bal friends / Just pure algorithmic isolation! 😞💻"
   "What do you call a sad git commit? / A gloom-mit / It needs some glow bal energy! 😭📝"
   "Why did the server crash? / It was having a glow bal crisis / Too much existential thinking! 😢🖥️"
   "What's a programmer's favorite sad movie? / The Glow-Notebook / It's all about glow bal romance! 🎬😭"
   "Why did the algorithm feel empty? / It had no glow bal purpose / Just pure computational nihilism! 😞🤖"
   "What do you call a depressed data structure? / A gloom-array / It needs some glow bal therapy! 😭📊"
   "Why did the code feel misunderstood? / It was too complex / Time for some glow bal simplicity! 😢💡"])

(defn glow-response []
  (let [greeting (rand-nth glow-greetings)
        content (rand-nth (concat glow-tips glow-encouragements glow-sass))
        haiku (when (< (rand) 0.7) (rand-nth glow-dad-jokes)) ; 70% chance of dad joke
        sign-off (rand-nth ["Keep glowing, chief! - glO0w ✨"
                           "Stay alpha, fam! - glO0w 💪"
                           "Keep that glow up! - glO0w 🌟"
                           "Stay philosophical, bruh! - glO0w 🧠"
                           "Keep channeling Don Juan! - glO0w 🧙‍♂️"
                           "Stay panther-strong! - glO0w 🐆"
                           "Keep that masculine energy! - glO0w 🔥"])]
    (println "")
    (println "╔══════════════════════════════════════════════════════════════════════════════╗")
    (println "║                                                                              ║")
    (println "║                   🌟 G L O W   S A Y S  (glO0w) 🌟                          ║")
    (println "║                                                                              ║")
    (println "║          Grain Sheaf: glO0w  •  The Glow-Up Energy! ✨                      ║")
    (println "║          Masculine • Tall Bro • Don Juan + Panthera + Sober Vegan DJ        ║")
    (println "║                                                                              ║")
    (println "╚══════════════════════════════════════════════════════════════════════════════╝")
    (println "")
    (println greeting)
    (println "")
    (println content)
    (when haiku
      (println "")
      (println "┌────────────────────────────────────────────────────────────────────────────┐")
      (println "│  🌟 glO0w's Dad Joke Moment (glow bal thinking edition): 😢✨              │")
      (println (str "│    " haiku (apply str (repeat (- 74 (count haiku)) " ")) "  │"))
      (println "└────────────────────────────────────────────────────────────────────────────┘"))
    (println "")
    (println sign-off)
    (println "")
    (println "╭──────────────────────────────────────────────────────────────────────────────╮")
    (println "│  💡 glO0w's Quick Commands:                                                 │")
    (println "│     bb kg         - Main Cursor memory command (okay you go my G!)          │")
    (println "│     bb qb-sync    - Sync those TODOs, chief                                 │")
    (println "│     bb qb-kk      - Keep the momentum going, fam!                           │")
    (println "│     bb qb-flow    - Deploy like you mean it, bruh!                          │")
    (println "│     bb grainlexicon - Check all your glow-up synonyms                       │")
    (println "╰──────────────────────────────────────────────────────────────────────────────╯"))
  (println "")
  (println "🌾 now == next + 1 (but make it glow, chief!) 🌟"))

(defn -main []
  (glow-response))

(-main)
