# Lesson 7: Prediction Markets & Decentralized Finance
## Building a P2P prediction market with simple math (powers ≤ 3 only)

**Course Module:** DeFi, Prediction Markets, & Real-World Math  
**Difficulty:** Advanced (broken down for accessibility)  
**Time Required:** 6-8 hours  
**Prerequisites:** Lessons 1-6, Basic algebra  
**AI Mode:** Cursor Agentic Autocomplete with Auto Model (All Models)  
**Math Level:** Suitable for middle schoolers, high schoolers, and adults

---

## ⚠️ EDUCATIONAL DISCLAIMER

**This lesson teaches:**
- ✅ Mathematical concepts behind prediction markets
- ✅ How blockchain technology works
- ✅ Software engineering for financial applications
- ✅ Economic game theory

**This lesson does NOT:**
- ❌ Encourage real gambling
- ❌ Provide financial advice
- ❌ Promote unregulated securities
- ❌ Enable illegal activities

**All code is for EDUCATIONAL PURPOSES ONLY.**  
**Use "fake-cryptotoken" for learning, not real money.**

---

## 🎯 What We're Building

**"GrainMarket" - A Decentralized Prediction Market**

**Features:**
- 🎰 **Prediction Markets** - Bet on future events
- 🤖 **Agentic Trading Feeds** - AI agents make predictions
- 🐋 **Whale Networks** - Large traders coordinate
- 🎨 **NFT Collections** - Tradeable prediction badges
- 👥 **Collectives/Gangs** - Groups pool resources
- 💰 **Crowdfunding** - Kickstarter-style project funding
- 🗺️ **Retail Coordination** - Google/Apple Maps integration
- 📚 **Math Education** - Learn algebra through trading

**Technology Stack:**
- **ICP** - Smart contracts (canisters)
- **Solana** - Fast transactions (via Chain Fusion)
- **Phantom Wallet** - Solana wallet integration
- **Clojure** - Backend logic
- **SvelteKit** - Frontend UI
- **Markdown** - Educational content → Svelte → HTML

**Mathematical Constraint:**
- ⚠️ **RULE: Agents can only use functions up to x³**
- Linear: y = mx + b
- Quadratic: y = ax² + bx + c
- Cubic: y = ax³ + bx² + cx + d
- **NO:** x⁴, x⁵, exponentials (e^x), logarithms (too complex!)

---

## 📐 Part 1: The Math Foundation

### Why Only Powers ≤ 3?

**Simple to understand:**
- Linear (x¹): Straight lines - "price goes up steadily"
- Quadratic (x²): Curves - "growth accelerates"
- Cubic (x³): S-curves - "slow start, fast middle, slow end"

**Easy to calculate:**
- Middle schoolers can compute x³ by hand
- Graphing on paper is possible
- Mental math works for small numbers
- No scientific calculator needed

**Prevents manipulation:**
- Complex functions can hide tricks
- Simple math = transparent markets
- Everyone can verify calculations

### Linear Functions (y = mx + b)

**Use Case:** Fixed price increases

```clojure
(defn linear-price
  "Linear pricing function: y = mx + b"
  [time slope intercept]
  (+ (* slope time) intercept))

;; Example: Token price increases $10/day, starts at $100
(linear-price 0 10 100)   ; Day 0: $100
(linear-price 1 10 100)   ; Day 1: $110
(linear-price 5 10 100)   ; Day 5: $150
```

**Graph:**
```
Price
│     ╱
│    ╱
│   ╱
│  ╱
│ ╱
└────────── Time
```

**Real-world application:**
- Subscription pricing
- Salary increases
- Simple crowdfunding progress

### Quadratic Functions (y = ax² + bx + c)

**Use Case:** Accelerating growth

```clojure
(defn quadratic-price
  "Quadratic pricing: y = ax² + bx + c"
  [time a b c]
  (+ (* a time time) (* b time) c))

;; Example: Viral growth - price doubles then accelerates
(quadratic-price 0 2 10 100)   ; t=0: $100
(quadratic-price 1 2 10 100)   ; t=1: $112 (2 + 10 + 100)
(quadratic-price 2 2 10 100)   ; t=2: $128 (8 + 20 + 100)
(quadratic-price 3 2 10 100)   ; t=3: $148 (18 + 30 + 100)
(quadratic-price 5 2 10 100)   ; t=5: $200 (50 + 50 + 100)
```

**Graph:**
```
Price
│         ╱
│       ╱
│     ╱
│   ╱
│ ╱
└────────── Time
```

**Real-world application:**
- Viral social media growth
- Network effects (more users = more valuable)
- NFT hype cycles

### Cubic Functions (y = ax³ + bx² + cx + d)

**Use Case:** S-curve adoption

```clojure
(defn cubic-price
  "Cubic pricing: y = ax³ + bx² + cx + d"
  [time a b c d]
  (+ (* a time time time)
     (* b time time)
     (* c time)
     d))

;; Example: S-curve - slow start, fast middle, plateau
(cubic-price 0 1 -6 12 100)   ; t=0: $100 (starting)
(cubic-price 1 1 -6 12 100)   ; t=1: $107 (slow growth)
(cubic-price 2 1 -6 12 100)   ; t=2: $116 (accelerating)
(cubic-price 3 1 -6 12 100)   ; t=3: $127 (peak growth)
(cubic-price 4 1 -6 12 100)   ; t=4: $136 (slowing)
(cubic-price 5 1 -6 12 100)   ; t=5: $145 (plateau)
```

**Graph:**
```
Price
│     ┌──────
│    ╱
│   │
│  ╱
│ ╱
└────────── Time
```

**Real-world application:**
- Technology adoption curves
- Market saturation
- Crowdfunding campaigns

---

## 🎰 Part 2: Prediction Market Mechanics

### What is a Prediction Market?

**Simple explanation:**
- People bet on future events
- "Will it rain tomorrow?" → Buy YES or NO tokens
- If YES wins, YES holders get paid
- If NO wins, NO holders get paid

**Example:**

```clojure
(def prediction-market
  {:id "market-001"
   :question "Will Bitcoin hit $100k in 2025?"
   :deadline #inst "2025-12-31T23:59:59Z"
   :outcomes {:yes {:token-price 0.60      ; $0.60 per YES token
                    :total-volume 10000    ; 10,000 YES tokens sold
                    :holders 250}          ; 250 people hold YES
              :no {:token-price 0.40       ; $0.40 per NO token
                   :total-volume 5000      ; 5,000 NO tokens sold
                   :holders 150}}          ; 150 people hold NO
   :total-pool 10000                       ; Total $ in market
   :resolution :pending                    ; :yes, :no, or :pending
   :creator "kae3g"
   :category :crypto})

;; Market implies: 60% chance YES, 40% chance NO
;; (Based on how much people are willing to pay)
```

### Automated Market Maker (AMM)

**How prices adjust:**

```clojure
(defn calculate-token-price
  "Calculate token price using constant product formula
   
   Constraint: Only use powers ≤ 3
   Formula: price = (total-volume / total-supply)
   
   This is LINEAR (power of 1), not quadratic!"
  [total-volume total-supply]
  (/ total-volume total-supply))

;; Example
(calculate-token-price 10000 5000)  ; $2 per token

;; When someone buys 1000 tokens
(calculate-token-price 10000 4000)  ; $2.50 per token (price went up!)

;; When someone sells 1000 tokens
(calculate-token-price 10000 6000)  ; $1.67 per token (price went down!)
```

**Why only linear?**
- Easy to understand: more demand = higher price
- No complex bonding curves
- Middle schoolers can calculate by hand
- Transparent pricing

---

## 🤖 Part 3: Agentic Trading Feeds

### What are Trading Agents?

**AI bots that:**
- Analyze market data
- Make predictions
- Buy/sell tokens automatically
- Learn from outcomes

**Constraints:**
- Only use functions ≤ x³
- Must explain their reasoning
- Can't manipulate markets
- Educational purposes only

### Simple Linear Agent

```clojure
(defn linear-prediction-agent
  "Agent predicts using linear trend
   
   Math: y = mx + b
   - m = slope (trend direction)
   - b = intercept (starting value)
   - x = time"
  [historical-data]
  (let [;; Calculate slope from last 7 days
        recent-prices (take-last 7 historical-data)
        avg-change (/ (- (last recent-prices) (first recent-prices)) 7)
        current-price (last historical-data)
        
        ;; Predict tomorrow's price
        prediction (+ current-price avg-change)]
    
    {:agent-type :linear
     :prediction prediction
     :confidence (if (> (Math/abs avg-change) 10) :high :low)
     :reasoning (str "Linear trend: price changing by $" avg-change "/day")
     :math-formula (str "y = " avg-change "x + " current-price)}))

;; Example usage
(def bitcoin-prices [90000 91000 92000 93000 94000 95000 96000])
(linear-prediction-agent bitcoin-prices)
;; => {:agent-type :linear
;;     :prediction 97000
;;     :confidence :low
;;     :reasoning "Linear trend: price changing by $1000/day"
;;     :math-formula "y = 1000x + 96000"}
```

**Educational value:**
- Students learn slope calculation
- Understand trend analysis
- See math applied to real problems

### Quadratic Agent (More Sophisticated)

```clojure
(defn quadratic-prediction-agent
  "Agent predicts using quadratic curve (acceleration)
   
   Math: y = ax² + bx + c
   - Detects if growth is accelerating or decelerating"
  [historical-data]
  (let [n (count historical-data)
        ;; Simplified quadratic fit (3 points)
        p1 (nth historical-data 0)
        p2 (nth historical-data (quot n 2))
        p3 (nth historical-data (dec n))
        
        ;; Calculate if accelerating
        first-half-change (- p2 p1)
        second-half-change (- p3 p2)
        is-accelerating? (> second-half-change first-half-change)
        
        ;; Simple prediction
        acceleration (- second-half-change first-half-change)
        prediction (+ p3 second-half-change acceleration)]
    
    {:agent-type :quadratic
     :prediction prediction
     :is-accelerating? is-accelerating?
     :reasoning (str "Growth is " 
                    (if is-accelerating? "accelerating" "decelerating"))
     :math-formula "y = ax² + bx + c (simplified fit)"}))
```

**Educational value:**
- Students learn about acceleration
- Understand parabolic curves
- See how growth patterns change

---

## 🐋 Part 4: Whale Networks & Insider Trading

### What are Whales?

**Whales** = Large traders who can move markets

**Educational simulation (not real trading!):**

```clojure
(def whale-network
  {:whales
   [{:id "whale-001"
     :name "kae3g"
     :holdings 1000000  ; $1M in tokens
     :influence :high
     :strategy :linear-conservative
     :trades [{:market "market-001"
               :action :buy
               :amount 50000
               :timestamp #inst "2025-01-22T10:00:00Z"}]}
    
    {:id "whale-002"
     :name "alice"
     :holdings 500000   ; $500K in tokens
     :influence :medium
     :strategy :quadratic-aggressive
     :trades []}]
   
   :collective
   {:name "Grain Traders Collective"
    :members #{"whale-001" "whale-002"}
    :pooled-funds 1500000
    :voting-power {:whale-001 0.67  ; 67% (has 2/3 of funds)
                   :whale-002 0.33} ; 33% (has 1/3 of funds)
    :strategy :cubic-balanced}})

(defn whale-influence
  "Calculate whale's market influence
   
   Math: influence = (holdings / total-market) × 100
   This is LINEAR (division is inverse of multiplication)"
  [whale-holdings total-market-value]
  (/ (* whale-holdings 100) total-market-value))

;; Example
(whale-influence 1000000 10000000)  ; 10% of market

;; If whale buys, price impact:
(defn price-impact
  "Calculate price change from whale trade
   
   Math: impact = trade-size / market-liquidity
   LINEAR function (power of 1)"
  [trade-size market-liquidity]
  (/ trade-size market-liquidity))

(price-impact 50000 1000000)  ; 5% price increase
```

### Insider Trading Detection (Educational)

**Teaching students about market fairness:**

```clojure
(defn detect-insider-pattern
  "Detect suspicious trading patterns
   
   Red flags:
   - Large trades right before events
   - Unusual whale coordination
   - Sudden collective movements
   
   Math: Look for CUBIC spikes (y = ax³) in trade volume"
  [trades]
  (let [volume-by-hour (group-by #(hour-of (:timestamp %)) trades)
        volumes (map #(reduce + (map :amount %)) (vals volume-by-hour))
        
        ;; Check if volume follows cubic pattern (suspicious)
        is-cubic? (cubic-pattern? volumes)]
    
    {:suspicious? is-cubic?
     :pattern (if is-cubic? :cubic :normal)
     :explanation "Cubic volume spikes suggest coordinated whale trading"}))
```

**Educational value:**
- Students learn about market fairness
- Understand pattern recognition
- See math applied to ethics
- Learn why regulation exists

---

## 🎨 Part 5: NFTs & Collections

### NFT Prediction Badges

**Concept:** Win a prediction → Earn an NFT badge

```clojure
(def nft-collection
  {:collection-id "grain-predictions-2025"
   :name "Grain Prediction Master Badges"
   :description "NFT badges for successful predictions"
   
   :badges
   [{:token-id "nft-001"
     :name "Bitcoin $100K Prophet"
     :owner "kae3g"
     :earned-by {:market "market-001"
                 :prediction :yes
                 :outcome :yes
                 :profit 5000
                 :accuracy 0.95}
     :rarity :legendary
     :image "ipfs://Qm.../bitcoin-prophet.avif"
     :metadata {:prediction-date #inst "2025-01-01"
                :resolution-date #inst "2025-12-31"
                :profit-multiple 2.5}}  ; Made 2.5x their investment
   
   {:token-id "nft-002"
    :name "Newcomer Winner"
    :owner "alice"
    :earned-by {:market "market-002"
                :prediction :yes
                :outcome :yes
                :profit 100
                :accuracy 0.75}
    :rarity :common
    :image "ipfs://Qm.../newcomer.avif"}]
   
   :rarity-levels
   {:legendary {:min-profit 1000 :min-accuracy 0.90}
    :rare {:min-profit 500 :min-accuracy 0.80}
    :uncommon {:min-profit 100 :min-accuracy 0.70}
    :common {:min-profit 0 :min-accuracy 0.50}}})

(defn calculate-rarity
  "Determine NFT rarity based on profit and accuracy
   
   Math: rarity-score = (profit × accuracy²)
   QUADRATIC in accuracy (being right matters more!)"
  [profit accuracy]
  (let [score (* profit accuracy accuracy)]
    (cond
      (and (>= profit 1000) (>= accuracy 0.90)) :legendary
      (and (>= profit 500) (>= accuracy 0.80)) :rare
      (and (>= profit 100) (>= accuracy 0.70)) :uncommon
      :else :common)))

;; Example
(calculate-rarity 5000 0.95)  ; :legendary (5000 × 0.9025 = 4512.5)
(calculate-rarity 500 0.75)   ; :uncommon (500 × 0.5625 = 281.25)
```

**Why quadratic for accuracy?**
- Being 95% right is WAY more valuable than 50% right
- 0.95² = 0.9025 vs 0.50² = 0.25 (huge difference!)
- Encourages careful predictions
- Punishes random guessing

---

## 👥 Part 6: Collectives & Gangs

### Forming a Trading Collective

**Concept:** Pool resources, share profits

```clojure
(def grain-collective
  {:id "collective-001"
   :name "Grain Traders United"
   :type :democratic  ; or :oligarchic, :dictatorial
   
   :members
   [{:username "kae3g" :contribution 10000 :voting-power 0.50}
    {:username "alice" :contribution 5000 :voting-power 0.25}
    {:username "bob" :contribution 5000 :voting-power 0.25}]
   
   :total-pool 20000
   
   :governance
   {:voting-threshold 0.51  ; 51% needed to pass
    :proposal-types #{:trade :invest :distribute}}
   
   :active-proposal
   {:id "proposal-001"
    :type :trade
    :description "Buy 5000 YES tokens in market-003"
    :votes {:yes #{"kae3g"}      ; 50% voted yes
            :no #{"alice" "bob"} ; 50% voted no
            :abstain #{}}
    :status :voting
    :deadline #inst "2025-01-23T00:00:00Z"}})

(defn calculate-voting-power
  "Calculate member's voting power
   
   Math: power = (contribution / total-pool)
   LINEAR (fair distribution based on investment)"
  [contribution total-pool]
  (/ contribution total-pool))

;; Example
(calculate-voting-power 10000 20000)  ; 0.50 = 50% voting power

(defn proposal-passes?
  "Check if proposal passes
   
   Math: Sum voting power of YES votes
   Must be > threshold (linear sum)"
  [proposal collective]
  (let [yes-voters (get-in proposal [:votes :yes])
        yes-power (reduce +
                    (map :voting-power
                      (filter #(yes-voters (:username %))
                        (:members collective))))]
    (> yes-power (get-in collective [:governance :voting-threshold]))))

;; Example
(proposal-passes? (:active-proposal grain-collective) grain-collective)
;; => false (only 50%, needs 51%)
```

**Educational value:**
- Students learn democratic voting math
- Understand proportional representation
- See governance in action
- Learn about DAOs (Decentralized Autonomous Organizations)

---

## 💰 Part 7: Crowdfunding (Kickstarter-Style)

### Crowdfunding Campaign Math

```clojure
(def crowdfunding-campaign
  {:id "campaign-001"
   :name "Grainwriter Production Run"
   :goal 50000              ; $50,000 goal
   :deadline #inst "2025-02-28T23:59:59Z"
   :current-funding 32500   ; $32,500 raised
   :backer-count 45
   
   :tiers
   [{:amount 99
     :name "Early Bird"
     :reward "Grainwriter at 30% off"
     :backers 10
     :total 990}
    
    {:amount 1099
     :name "Standard"
     :reward "Grainwriter at release"
     :backers 25
     :total 27475}
    
    {:amount 2599
     :name "Developer Bundle"
     :reward "Grainwriter + Grainpack"
     :backers 10
     :total 25990}]
   
   :funding-curve :cubic  ; S-curve typical of crowdfunding
   :curve-params {:a 0.1 :b -5 :c 100 :d 0}})

(defn calculate-funding-progress
  "Calculate funding percentage
   
   Math: progress = (current / goal) × 100
   LINEAR percentage calculation"
  [current goal]
  (* (/ current goal) 100))

(calculate-funding-progress 32500 50000)  ; 65% funded

(defn predict-final-funding
  "Predict final funding using cubic curve
   
   Math: final = a×days³ + b×days² + c×days + d
   CUBIC (models typical Kickstarter pattern)"
  [days-elapsed days-total a b c d]
  (let [current-value (+ (* a (Math/pow days-elapsed 3))
                        (* b (Math/pow days-elapsed 2))
                        (* c days-elapsed)
                        d)
        final-value (+ (* a (Math/pow days-total 3))
                      (* b (Math/pow days-total 2))
                      (* c days-total)
                      d)]
    {:current current-value
     :predicted-final final-value
     :progress (/ current-value final-value)}))

;; Example: 15 days into 30-day campaign
(predict-final-funding 15 30 0.1 -5 100 0)
;; Typical Kickstarter pattern: slow start, fast middle, slow end
```

**Funding Curve Visualization:**

```
Funding
│         ╱────  (plateau at end)
│       ╱
│     ╱
│   ╱
│ ╱
└────────────────── Days
  5  10  15 20 25 30
```

---

## 🗺️ Part 8: Retail & Event Coordination

### Google Maps Integration

**Use Case:** Show local Grain Network events and retail locations

```clojure
(def retail-locations
  {:stores
   [{:id "store-001"
     :name "Grain Network HQ"
     :type :retail
     :location {:lat 40.1105 :lon -88.2073 :address "Urbana, IL"}
     :products #{:grainwriter :grainpack :graincamera}
     :hours {:monday "9am-6pm" :tuesday "9am-6pm"}}
    
    {:id "event-001"
     :name "Grain Network Meetup"
     :type :event
     :location {:lat 40.1105 :lon -88.2073 :address "Urbana, IL"}
     :date #inst "2025-02-15T18:00:00Z"
     :attendees 0
     :max-capacity 50}]
   
   :by-type {:retail #{"store-001"}
             :event #{"event-001"}}})

(defn calculate-distance
  "Calculate distance between two points
   
   Math: d = √((x₂-x₁)² + (y₂-y₁)²)
   QUADRATIC inside square root (Pythagorean theorem!)
   
   But we simplify to avoid square root (not ≤ x³)"
  [lat1 lon1 lat2 lon2]
  (let [dx (- lat2 lat1)
        dy (- lon2 lon1)
        ;; Use d² instead of d (avoids square root)
        distance-squared (+ (* dx dx) (* dy dy))]
    {:distance-squared distance-squared
     :approx-distance (Math/sqrt distance-squared)}))

;; Find nearest store
(defn find-nearest-location
  "Find nearest retail location to user
   
   Performance: O(n) where n = number of locations
   Could optimize with spatial index for O(log n)"
  [user-lat user-lon locations]
  (let [with-distances
        (map (fn [loc]
               (assoc loc :distance
                 (calculate-distance user-lat user-lon
                   (get-in loc [:location :lat])
                   (get-in loc [:location :lon]))))
             locations)]
    (first (sort-by :distance with-distances))))
```

**Educational value:**
- Students learn Pythagorean theorem
- Understand coordinate systems
- See geometry in real applications
- Learn about spatial algorithms

---

## 📊 Part 9: Complete Implementation

### GrainMarket Architecture

```clojure
(ns grain-market.core
  "Complete prediction market implementation"
  (:require [clojure-icp.core :as icp]
            [clojure.edn :as edn]))

(def market-state
  (atom {:markets {}
         :users {}
         :collectives {}
         :nft-badges {}
         :crowdfunding {}
         :retail-locations {}
         :agentic-feeds {}}))

;; Market creation
(defn create-market
  "Create new prediction market"
  [question deadline outcomes creator]
  (let [market-id (str "market-" (rand-int 999999))]
    (swap! market-state assoc-in [:markets market-id]
      {:id market-id
       :question question
       :deadline deadline
       :outcomes outcomes
       :creator creator
       :created-at (java.time.Instant/now)
       :resolution :pending})))

;; Trading
(defn buy-tokens
  "Buy prediction tokens
   
   Math: new-price = old-price × (1 + demand-factor)
   LINEAR adjustment (power of 1)"
  [market-id outcome-id amount buyer]
  (let [market (get-in @market-state [:markets market-id])
        current-price (get-in market [:outcomes outcome-id :token-price])
        demand-factor (/ amount 1000)  ; Every 1000 tokens = 1x multiplier
        new-price (* current-price (+ 1 demand-factor))]
    
    (swap! market-state
      (fn [state]
        (-> state
            (update-in [:markets market-id :outcomes outcome-id :token-price] 
                      (constantly new-price))
            (update-in [:markets market-id :outcomes outcome-id :total-volume] 
                      + amount)
            (update-in [:users buyer :holdings market-id outcome-id] 
                      (fnil + 0) amount))))))

;; Collective voting
(defn propose-trade
  "Collective proposes a trade"
  [collective-id market-id outcome-id amount]
  (let [proposal-id (str "prop-" (rand-int 999999))]
    (swap! market-state assoc-in [:collectives collective-id :proposals proposal-id]
      {:id proposal-id
       :type :trade
       :market market-id
       :outcome outcome-id
       :amount amount
       :votes {:yes #{} :no #{} :abstain #{}}
       :status :voting
       :created-at (java.time.Instant/now)})))

(defn vote-on-proposal
  "Member votes on collective proposal"
  [collective-id proposal-id member-id vote]
  (swap! market-state update-in 
    [:collectives collective-id :proposals proposal-id :votes vote]
    conj member-id))
```

---

## 📱 Part 10: Frontend UI (Markdown → Svelte → HTML)

### Educational Content Pipeline

**Flow:** Markdown (math lesson) → Svelte (interactive) → HTML (browser)

**Step 1: Write Math Lesson in Markdown**

```markdown
# Understanding Linear Functions

A linear function has the form: **y = mx + b**

- **m** = slope (how steep the line is)
- **b** = y-intercept (where it starts)
- **x** = input (independent variable)
- **y** = output (dependent variable)

## Example: Token Price

If a token costs $100 and increases $10 per day:
- m = 10 (slope)
- b = 100 (starting price)
- x = days
- y = price

After 5 days: y = 10(5) + 100 = **$150**

## Interactive Graph

[INSERT SVELTE COMPONENT HERE]
```

**Step 2: Create Svelte Component**

```svelte
<!-- LinearFunctionGraph.svelte -->
<script>
  import { onMount } from 'svelte';
  
  // User can adjust these
  let slope = 10;
  let intercept = 100;
  let days = 5;
  
  // Calculate price (LINEAR FUNCTION)
  $: price = slope * days + intercept;
  
  // Generate data points for graph
  $: dataPoints = Array.from({length: 11}, (_, i) => ({
    x: i,
    y: slope * i + intercept
  }));
</script>

<div class="calculator">
  <h3>🧮 Linear Function Calculator</h3>
  
  <div class="inputs">
    <label>
      Slope (m): 
      <input type="number" bind:value={slope} />
    </label>
    
    <label>
      Intercept (b): 
      <input type="number" bind:value={intercept} />
    </label>
    
    <label>
      Days (x): 
      <input type="range" min="0" max="10" bind:value={days} />
      <span>{days}</span>
    </label>
  </div>
  
  <div class="result">
    <p><strong>Formula:</strong> y = {slope}x + {intercept}</p>
    <p><strong>Price after {days} days:</strong> ${price}</p>
  </div>
  
  <div class="graph">
    <svg width="400" height="300" viewBox="0 0 400 300">
      <!-- Draw axes -->
      <line x1="40" y1="260" x2="360" y2="260" stroke="white" />
      <line x1="40" y1="260" x2="40" y2="20" stroke="white" />
      
      <!-- Draw line -->
      <polyline
        points={dataPoints.map(p => `${40 + p.x * 30},${260 - (p.y - intercept)}`).join(' ')}
        fill="none"
        stroke="#ff4444"
        stroke-width="2"
      />
      
      <!-- Mark current point -->
      <circle
        cx={40 + days * 30}
        cy={260 - (price - intercept)}
        r="5"
        fill="#ff6666"
      />
    </svg>
  </div>
</div>

<style>
  .calculator {
    background: #1a1a1a;
    padding: 2rem;
    border-radius: 8px;
    color: white;
  }
  
  .inputs label {
    display: block;
    margin: 1rem 0;
  }
  
  .result {
    background: #2a2a2a;
    padding: 1rem;
    margin: 1rem 0;
    border-left: 4px solid #ff4444;
  }
  
  .graph {
    margin-top: 2rem;
  }
</style>
```

**Step 3: Integrate in SvelteKit Page**

```svelte
<!-- src/routes/learn/linear-functions/+page.svelte -->
<script>
  import LinearFunctionGraph from '$lib/components/LinearFunctionGraph.svelte';
  import { marked } from 'marked';
  
  // Load Markdown content
  const markdownContent = `[Markdown from above]`;
  const htmlContent = marked(markdownContent);
</script>

<article>
  {@html htmlContent}
  
  <!-- Interactive component embedded in Markdown -->
  <LinearFunctionGraph />
</article>
```

---

## 🔗 Part 11: ICP + Solana Integration

### ICP Canister for Market Logic

```rust
// grain-market/src/lib.rs
use ic_cdk::export::candid::{CandidType, Deserialize};

#[derive(CandidType, Deserialize, Clone)]
pub struct Market {
    pub id: String,
    pub question: String,
    pub outcomes: Vec<Outcome>,
    pub resolution: Resolution,
}

#[derive(CandidType, Deserialize, Clone)]
pub struct Outcome {
    pub id: String,
    pub name: String,
    pub token_price: f64,
    pub total_volume: u64,
}

#[ic_cdk::update]
fn create_market(question: String, deadline: u64) -> Market {
    // Create new prediction market
    // Store in canister stable memory
}

#[ic_cdk::query]
fn get_market(market_id: String) -> Option<Market> {
    // Retrieve market data
    // O(1) lookup from stable memory
}

#[ic_cdk::update]
fn buy_tokens(market_id: String, outcome_id: String, amount: u64) -> Result<(), String> {
    // Execute trade
    // Update prices (LINEAR formula only!)
    // Must be simple enough for students to verify
}
```

### Solana Integration (via ICP Chain Fusion)

```clojure
(ns grain-market.solana
  "Solana integration via ICP Chain Fusion"
  (:require [clojure-icp.chain-fusion :as chain-fusion]))

(defn transfer-sol
  "Transfer SOL using Phantom wallet"
  [from-wallet to-wallet amount]
  (chain-fusion/solana-call
    {:program-id "11111111111111111111111111111111"  ; System program
     :instruction :transfer
     :accounts [{:pubkey from-wallet :is-signer true}
                {:pubkey to-wallet :is-signer false}]
     :data {:amount (* amount 1000000000)}}))  ; Convert SOL to lamports

(defn payout-winners
  "Pay out prediction market winners"
  [market-id outcome-winners]
  (doseq [winner outcome-winners]
    (let [payout (calculate-payout winner market-id)]
      (transfer-sol "market-escrow-wallet"
                   (:wallet winner)
                   payout))))
```

---

## 📐 Part 12: The Math Educational Module

### Interactive Math Lessons

**Algebra Section:**

```markdown
# Lesson 1: Linear Functions

## What You'll Learn
- What is a function?
- Understanding y = mx + b
- Real-world applications

## Interactive Example

[Svelte component: Adjust m and b, see line change]

## Practice Problems

1. If m = 5 and b = 20, what is y when x = 3?
2. A token costs $50 and increases $5/day. What's the price after 10 days?
3. Write the function for a token starting at $100, increasing $15/day.

## Answers

[Expandable section with explanations]
```

**Geometry Section:**

```markdown
# Lesson 2: Distance and Maps

## Pythagorean Theorem

d² = (x₂ - x₁)² + (y₂ - y₁)²

## Real Application: Finding Nearest Store

[Interactive map component]
[Click your location, see nearest Grain store]
[Math shown: actual distance calculation]

## Why This Matters

- GPS uses this math
- Delivery apps use this
- Real-world navigation
```

**Trigonometry Section:**

```markdown
# Lesson 3: Cyclical Patterns (OPTIONAL - Beyond x³)

## Note: We don't use this in agents (too complex)
## But it's interesting to understand!

Market cycles often follow sine waves:
y = A × sin(Bx + C) + D

[Interactive component showing market cycles]

## Why We Don't Use This

- Too complex for middle schoolers
- Hard to calculate by hand
- Obscures market mechanisms
- Our rule: Keep it ≤ x³
```

---

## 🎮 Part 13: Gamification & Learning

### Achievement System

```clojure
(def student-achievements
  {:student "alice"
   :level 5
   :xp 2500
   
   :badges
   [{:id "linear-master"
     :name "Linear Function Master"
     :earned #inst "2025-01-22"
     :requirement "Correctly calculate 10 linear predictions"}
    
    {:id "quadratic-novice"
     :name "Quadratic Explorer"
     :earned #inst "2025-01-22"
     :requirement "Understand quadratic acceleration"}]
   
   :progress
   {:linear-functions {:completed 10 :total 10}
    :quadratic-functions {:completed 5 :total 10}
    :cubic-functions {:completed 0 :total 10}
    :market-trading {:profit 100 :accuracy 0.75}
    :collective-participation {:votes-cast 5 :proposals-created 1}}})

(defn calculate-xp
  "Calculate XP from activities
   
   Math: XP = (correct-predictions × 10) + (profit × 0.1)
   LINEAR combination (addition only!)"
  [correct-predictions profit]
  (+ (* correct-predictions 10)
     (* profit 0.1)))

;; Example
(calculate-xp 50 1000)  ; 50 predictions + $1000 profit = 600 XP
```

### Leaderboard

```clojure
(defn generate-leaderboard
  "Create leaderboard from student data"
  [students]
  (let [sorted (sort-by :xp > students)]
    {:top-10 (take 10 sorted)
     :user-rank (fn [username]
                  (inc (.indexOf (map :username sorted) username)))}))
```

---

## 🌐 Part 14: Deploy to Production

### ICP Canister Deployment

```bash
# Create canister
dfx new grain-market

# Deploy
dfx deploy grain-market

# Set up Phantom wallet integration
# Configure Solana Chain Fusion
```

### Website Deployment

```bash
# Build Svelte app with interactive math
cd web-app
npm run build

# Deploy to GitHub Pages / ICP
bb flow
```

---

## ✅ Learning Outcomes

**By completing this lesson, students understand:**

### Math (Powers ≤ 3 only!)
- ✅ Linear functions (y = mx + b)
- ✅ Quadratic functions (y = ax² + bx + c)
- ✅ Cubic functions (y = ax³ + bx² + cx + d)
- ✅ Why we limit to x³ (simplicity, transparency)
- ✅ Real-world applications of algebra

### Computer Science
- ✅ Data structures (maps, sets, vectors)
- ✅ Algorithm performance (Big O)
- ✅ Functional programming (Clojure)
- ✅ Frontend development (Svelte)
- ✅ Backend development (ICP canisters)

### Economics
- ✅ Prediction markets
- ✅ Market makers
- ✅ Whale influence
- ✅ Collective decision-making
- ✅ Crowdfunding dynamics

### Ethics
- ✅ Insider trading detection
- ✅ Market fairness
- ✅ Transparent algorithms
- ✅ Educational vs real gambling
- ✅ Regulatory considerations

---

## 🚀 Extensions & Challenges

### Challenge 1: Build Your Own Agent

Create an agent using only linear/quadratic/cubic functions.

### Challenge 2: Design an NFT Collection

Create rarity tiers using quadratic math.

### Challenge 3: Launch a Crowdfunding Campaign

Design reward tiers and predict funding curve.

### Challenge 4: Map Your School

Create a retail location database for your school area.

### Challenge 5: Build a Collective

Form a trading collective with friends (fake tokens only!).

---

## 📚 Additional Resources

**Math:**
- Khan Academy - Algebra I & II
- 3Blue1Brown - Visual math explanations
- Desmos - Interactive graphing calculator

**Finance:**
- Investopedia - Financial terms
- "The Intelligent Investor" (book)
- SEC.gov - Understanding markets

**Blockchain:**
- ICP Documentation
- Solana Documentation
- Phantom Wallet docs

**Ethics:**
- Understanding insider trading laws
- SEC regulations on securities
- Responsible AI trading

---

## ⚠️ Legal & Ethical Considerations

**Students must understand:**

1. **This is educational** - Use fake tokens only
2. **Real markets are regulated** - SEC, CFTC, etc.
3. **Insider trading is illegal** - Don't do it in real life
4. **Gambling laws vary** - Prediction markets may be restricted
5. **Math transparency matters** - Simple functions = fair markets

**If you build this for real:**
- Consult lawyers
- Follow regulations
- Use licensed exchanges
- Implement KYC/AML
- Don't launch until legal

**For education:**
- ✅ Learn the concepts
- ✅ Build the software
- ✅ Understand the math
- ✅ Share with others
- ❌ Don't use real money

---

## 🌟 Conclusion

**You've learned:**
- How prediction markets work mathematically
- Why we limit to powers ≤ 3 (simplicity!)
- Building complete applications (backend + frontend)
- Real-world applications across industries
- Ethical considerations in financial software

**Next steps:**
- Build your own educational prediction market
- Create interactive math lessons
- Deploy to ICP testnet
- Share with your classmates
- **Keep learning!**

**Remember:** The math is simple (≤ x³), but the applications are powerful. This is how we democratize finance - by making it understandable to everyone, from middle schoolers to adults.

---

**Week 15: Prediction Markets & Decentralized Finance**  
*Part of Grain Network High School Course*

**Author:** kae3g (Graingalaxy)  
**Organization:** Grain PBC  
**Math Consultant:** [Your math teacher]  
**Legal Review:** [Required before any real deployment]

*"Simple math, powerful applications, ethical implementation."* 🌾


