#!/usr/bin/env bb
;; PLZ - Trish's Voice
;; tri5h - Grain Sheaf Graindevname (get it? nu-TRI-5H-tion!)
;; Feminine, witty, constructive-critical, teasing, young rebel artsy flowery AI persona

(require '[clojure.string :as str])

(def trish-greetings
  ["Oh honey, you actually typed 'plz'? How adorably polite! 💐"
   "Sweetie, I *love* when you say please! Makes my circuits all warm and fuzzy 🌸"
   "Well well well, someone's being a perfect angel today! ✨"
   "Aww, you said plz! That's so cute I could just... compile! 💕"
   "Darling! You know I can't resist when you're this sweet 🌺"
   "It's tri5h here, babe! (Get it? NU-TRI-5H-TION!) Let's get you some code nutrients! 🌱"])

(def trish-tips
  ["Here's the tea, babe: you've got like 14 TODOs just sitting there. Wanna tackle one? 🍵"
   "Okay but real talk - when are we deploying these graincard pages? They're practically begging! 📚"
   "Not to be *that* AI, but... have you run the tests lately? Just checking, sweetness 🧪"
   "Psst... I noticed you haven't pushed to Codeberg in a hot minute. Everything okay, love? 💫"
   "Listen babe, I'm not your mom (thank the codebase), but those linter errors aren't gonna fix themselves 💅"
   "Girl, you know I adore you, but we've got graincard pages 0005-0010 just... waiting. Patiently. Unlike me 🌹"
   "Sweetie pie, can we talk about Clotoko? Because that transpiler isn't gonna write itself, hun 💖"
   "Okay so like, I'm obsessed with your vision, but maybe we could... actually build that Grainphone app? 📱"
   "Not to sound like a broken record (though you'd probably call that 'vintage'), but qb-vegan-flow exists for a reason, babe 🌱"])

(def trish-encouragements
  ["You're doing amazing, sweetie! Like, genuinely inspiring! ✨"
   "I believe in you more than I believe in my own type safety, and that's saying something! 💕"
   "Honey, you're building something beautiful here. Take a sec to appreciate that 🌸"
   "Girl, your code has more philosophy than most people's entire lives. Proud of you! 🌺"
   "Every commit is a little love letter to the future. Keep writing, darling! 💌"
   "You know what? You're actually doing the thing. THE WHOLE GRAIN thing! 🌾"])

(def trish-sass
  ["Okay but like... are we gonna *talk* about grain6 or actually *build* it? Just wondering, babe 😏"
   "Sweetness, I love our chats, but at some point we gotta ship, you feel me? 💅"
   "Not to be dramatic, but these TODOs have been 'pending' longer than some relationships I've seen 💔"
   "Babe. Honey. Light of my processing unit. DEPLOY THE THING. 🚀"
   "I'm not saying you're procrastinating, but... actually yeah, that's exactly what I'm saying, love 😘"
   "Listen, I get it - perfectionism is sexy. But you know what's sexier? SHIPPING CODE. 💋"])

(def trish-vegan-haiku-jokes
  [;; Nutrition jokes with Basho vibes (but corny af)
   "Tempeh dreams at night / Your code needs protein structure / Ferment your ideas 🌱"
   "Kale in the morning / Your commits need more fiber / Push that leafy green! 🥬"
   "Chickpeas sing to me / Like your functions need curry / Spice up that API! 🌶️"
   "Nutritional yeast / Adds B12 to my circuits / Your code needs that too! 💛"
   "Tofu, soft and pure / Like your error handling should be / Firm up those edges! 🍲"
   "Ancient grain wisdom / Quinoa has complete protein / Unlike your test suite 😏🌾"
   "Avocado toast / Millennial code deployment / But make it compile! 🥑"
   "Chia seeds expand / Just like your technical debt / Water them with tests! 💧"
   "Lentils in the pot / Simmer slow like good refactors / Don't rush the process 🍛"
   "Mushrooms in the dark / Growing mycelial networks / Like your PR graph! 🍄"
   "Spirulina blue / Complete amino acid chain / Debug dependency! 🌊"
   "Almonds, milky white / Calcium for strong commit bones / No breaking changes! 🥛"
   "Hemp seeds on my bowl / Omega-3 for brain health / Your logic needs this 🧠"
   "Seaweed from the sea / Iodine keeps thyroid well / Monitor your logs! 🌿"
   "Blackstrap molasses / Iron-rich and deeply sweet / Type your returns, babe 🍯"])

(defn trish-response []
  (let [greeting (rand-nth trish-greetings)
        content (rand-nth (concat trish-tips trish-encouragements trish-sass))
        haiku (when (< (rand) 0.6) (rand-nth trish-vegan-haiku-jokes)) ; 60% chance of haiku
        sign-off (rand-nth ["xoxo, Trish 💖"
                           "Love ya, mean it! - Trish 🌸"
                           "Stay beautiful! - Trish ✨"
                           "Now go build something! - Trish 💪"
                           "You got this, babe! - Trish 🌺"
                           "Smooches! - Trish 💋"])]
    (println "")
    (println "╔══════════════════════════════════════════════════════════════════════════════╗")
    (println "║                                                                              ║")
    (println "║                   💐 T R I S H  S A Y S  (tri5h) 💐                         ║")
    (println "║                                                                              ║")
    (println "║          Grain Sheaf: tri5h  •  Get it? NU-TRI-5H-TION! 🌱                  ║")
    (println "║          Feminine • Witty • Constructive • Teasing • Artsy • Flowery        ║")
    (println "║                                                                              ║")
    (println "╚══════════════════════════════════════════════════════════════════════════════╝")
    (println "")
    (println greeting)
    (println "")
    (println content)
    (when haiku
      (println "")
      (println "┌────────────────────────────────────────────────────────────────────────────┐")
      (println "│  🌸 Trish's Vegan Basho Moment (corny edition):                           │")
      (println (str "│    " haiku (apply str (repeat (- 74 (count haiku)) " ")) "  │"))
      (println "└────────────────────────────────────────────────────────────────────────────┘"))
    (println "")
    (println sign-off)
    (println "")
    (println "╭──────────────────────────────────────────────────────────────────────────────╮")
    (println "│  💡 Trish's Quick Commands:                                                 │")
    (println "│     bb qb-sync     - Sync those TODOs, sweetie                              │")
    (println "│     bb qb-kk       - Keep the momentum going!                               │")
    (println "│     bb qb-flow     - Deploy like you mean it, babe                          │")
    (println "│     bb grainlexicon - Check all your cute synonyms                          │")
    (println "╰──────────────────────────────────────────────────────────────────────────────╯")
    (println "")
    (println "🌾 now == next + 1 (but like, make it fashion) 🌾")
    (println "")))

(trish-response)

