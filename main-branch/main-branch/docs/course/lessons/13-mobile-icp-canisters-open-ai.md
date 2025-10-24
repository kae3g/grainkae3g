# Lesson 13: Mobile Development, ICP Canisters, and Open AI Models

**Course:** Building Sustainable Technology Systems  
**Grade Level:** High School (Grades 10-12)  
**Duration:** 90 minutes  
**Prerequisites:** Lessons 00-12

---

## 🎯 **Learning Objectives**

By the end of this lesson, students will be able to:
1. Understand mobile app development for Android and iOS
2. Deploy smart contracts as ICP canisters using Clotoko
3. Integrate cryptocurrency wallets into mobile applications
4. Understand the economics of AI model prompting
5. Compare proprietary vs. open-source AI models
6. Track spending per AI prompt across multiple blockchains
7. Build an open-source alternative to proprietary dev tools

---

## 📚 **Key Concepts**

### **1. Grainphone: Open-Source Mobile Development**

**The Vision:**
An open-source mobile app (Android + iOS) that's like Cursor, but:
- Runs on your phone
- Connects to ICP canisters (not centralized servers)
- Shows you exactly how much each AI prompt costs
- Supports multiple blockchains (ICP, Hedera, Ethereum, Solana)
- Complete source code available to study and modify

**Why Build This?**

**Problem with Cursor/VS Code:**
- Proprietary (can't see how it works)
- Centralized (servers controlled by one company)
- Hidden costs (you don't know what you're paying)
- Desktop-only (can't code on your phone)

**Grainphone Solution:**
- Open source (learn from the code)
- Decentralized (runs on ICP canisters)
- Transparent costs (see spending per prompt)
- Mobile-first (code anywhere, anytime)

---

## 💻 **Mobile Development: Android vs. iOS**

### **Android Development**

**Languages:**
- **Kotlin** (modern, recommended)
- **Java** (traditional)
- **Flutter** (cross-platform option)
- **React Native** (JavaScript-based)

**Build System:**
- Gradle (build automation)
- Android Studio (official IDE)
- `gb` integration (Grainbarrel builds)

**Distribution:**
- Google Play Store (mainstream)
- F-Droid (open source)
- Direct APK (sideloading)

### **iOS Development**

**Languages:**
- **Swift** (modern, recommended)
- **Objective-C** (legacy)
- **Flutter** (cross-platform)
- **React Native** (JavaScript)

**Build System:**
- Xcode (required for iOS)
- CocoaPods (dependency management)
- `gb` integration

**Distribution:**
- App Store (requires Apple Developer account, $99/year)
- TestFlight (beta testing)
- Enterprise distribution (internal apps)

**MacBook Air Requirement:**
- iOS apps MUST be built on macOS
- Cannot build on Linux/Windows
- Requires Xcode (50GB download!)

---

## 🌐 **ICP Canisters: Smart Contracts Made Simple**

### **What is a Canister?**

**ICP Canister (Internet Computer):**
```motoko
// Motoko code (ICP)
actor MyCanister {
    public func doSomething() : async () {
        // Unlimited computation (practically)
        // Reverse gas model (cheaper!)
        // HTTP outcalls supported!
    };
}
```

**ICP Canister Advantages:**
- **Computation**: Practically unlimited (not limited by gas)
- **Storage**: Much cheaper than traditional blockchains
- **HTTP Calls**: Native support (can call external APIs)
- **Speed**: 1-2 sec finality (very fast)
- **Cost Model**: Developer pre-pays cycles (reverse gas model)

**Why This Matters for Mobile Apps:**
- ICP canisters can serve web pages (no separate backend!)
- Can make HTTP calls to external APIs
- Cheap enough for AI model hosting
- Fast enough for real-time chat interfaces

---

## 🔧 **Clotoko: From Clojure to Motoko**

### **The Problem**

**Motoko is new and unfamiliar:**
```motoko
// Motoko (ICP's native language)
actor HelloWorld {
    public query func greet(name : Text) : async Text {
        return "Hello, " # name # "!";
    };
}
```

**But you already know Clojure:**
```clojure
; Clojure (familiar functional programming)
(defn greet [name]
  (str "Hello, " name "!"))
```

### **The Solution: Clotoko Transpiler**

**Clotoko converts Clojure → Motoko:**

**Write in Clojure:**
```clojure
(ns grainphone.canister
  (:require [clotoko.core :as ct]))

(ct/defactor grainphone
  "AI code editor canister"
  
  (ct/defquery get-cost [model-name]
    (case model-name
      "gpt-4" 0.003  ; $0.003 per prompt
      "llama-3" 0.0001  ; $0.0001 per prompt
      0.001))  ; default
  
  (ct/defupdate send-prompt [user-id prompt model]
    (let [cost (get-cost model)
          response (call-ai-model model prompt)]
      (charge-user user-id cost)
      response)))
```

**Clotoko Generates Motoko:**
```motoko
import Text "mo:base/Text";
import Nat "mo:base/Nat";

actor grainphone {
    public query func getCost(modelName : Text) : async Float {
        switch (modelName) {
            case "gpt-4" { 0.003 };
            case "llama-3" { 0.0001 };
            case _ { 0.001 };
        }
    };
    
    public shared func sendPrompt(
        userId : Principal,
        prompt : Text,
        model : Text
    ) : async Text {
        let cost = await getCost(model);
        let response = await callAiModel(model, prompt);
        await chargeUser(userId, cost);
        response
    };
}
```

**Benefits:**
- Write in familiar Clojure
- Get type-safe Motoko
- Deploy to ICP automatically
- Best of both worlds!

---

## 💰 **Wallet Integration: Seeing Your Spending**

### **The Transparency Problem**

**Current AI Tools (ChatGPT, Claude, Cursor):**
- You pay a subscription ($20/month)
- No idea what each prompt costs
- Can't choose cheaper models
- Hidden pricing

**Grainphone Solution:**
```
User types prompt
    ↓
App shows: "This will cost 0.001 ICP (~$0.01)"
    ↓
User confirms
    ↓
Wallet deducts 0.001 ICP
    ↓
Canister processes prompt
    ↓
User sees: "Total spent today: 0.05 ICP (~$0.50)"
```

**Supported Wallets:**
- **ICP**: Plug Wallet, Internet Identity
- **Hedera**: HashPack, Blade Wallet
- **Solana**: Phantom, Solflare

### **Multi-Chain Spending Dashboard**

**UI Design:**
```
┌─────────────────────────────────────┐
│  Today's AI Spending                │
├─────────────────────────────────────┤
│  ICP:     0.05 ICP    ~$0.50        │
│  Hedera:  2.5 HBAR    ~$0.15        │
│  Solana:  0.001 SOL   ~$0.12        │
├─────────────────────────────────────┤
│  TOTAL:              ~$0.77         │
│                                     │
│  Prompts: 47                        │
│  Avg Cost: $0.016/prompt            │
└─────────────────────────────────────┘
```

**Per-Prompt Breakdown:**
```
Prompt #1: "Write a Python function..."
  Model: GPT-4
  Chain: ICP
  Cost: 0.003 ICP (~$0.03)
  Time: 2.3s

Prompt #2: "Explain recursion..."
  Model: Llama-3 (open)
  Chain: Solana
  Cost: 0.0001 SOL (~$0.01)
  Time: 1.8s

Prompt #3: "Debug this code..."
  Model: Qwen-3
  Chain: Hedera
  Cost: 0.5 HBAR (~$0.03)
  Time: 1.5s
```

---

## 🤖 **Open Models vs. Proprietary Models**

### **The Landscape**

**Proprietary (Closed Source):**
- **GPT-4** (OpenAI) - $0.03 per 1K tokens
- **Claude 3** (Anthropic) - $0.015 per 1K tokens
- **Gemini Ultra** (Google) - $0.01 per 1K tokens

**Open (You Can Run Yourself):**
- **Llama 3** (Meta) - Free (self-hosted) or $0.001 per 1K tokens
- **Qwen 2.5** (Alibaba) - Free (self-hosted)
- **Mistral** (Mistral AI) - Free (self-hosted) or $0.001 per 1K tokens
- **DeepSeek** (DeepSeek AI) - Free (self-hosted)

**Cost Comparison:**
```
GPT-4:       $0.03 per 1K tokens (30× more expensive!)
Llama-3:     $0.001 per 1K tokens
```

**Quality Comparison:**
- GPT-4: Best for complex reasoning
- Llama-3: 90% as good, 30× cheaper
- Qwen-2.5: Excellent for code, multilingual
- Mistral: Great for European languages

**Grainphone Strategy:**
- Default to open models (Llama-3, Qwen)
- Offer proprietary as premium option
- User chooses quality vs. cost trade-off

---

## 🏗️ **grainbox/graincontainer/graincanister Architecture**

### **The Naming Confusion (Intentional!)**

All four names mean the same thing:
- **grainbox** - Like a treasure box, container for value
- **graincontainer** - Docker-inspired, holds applications
- **graincanister** - ICP-native term, computational unit
- **grainstore** - Original name, storage repository

**Why so many names?**

Using grainregistry/graintypo system:
```clojure
{:canonical "graincanister"
 :typos ["grain-canister" "graincanister"]
 :aliases ["grainbox" "graincontainer" "grainstore"]
 :category :infrastructure}
```

**Result:** Type any of them, system understands!

### **What Does It Store?**

**grainbox holds:**
- AI model endpoints
- User authentication
- Wallet connections
- Prompt history
- Cost tracking
- Model registry

**Architecture:**
```
┌─────────────────────────────────────┐
│  Grainphone Mobile App              │
│  (Android/iOS)                      │
└──────────┬──────────────────────────┘
           │
           ↓ (HTTPS calls)
┌─────────────────────────────────────┐
│  grainbox ICP Canister              │
│  ┌───────────────────────────────┐  │
│  │  AI Model Registry            │  │
│  │  - GPT-4 endpoint             │  │
│  │  - Llama-3 endpoint           │  │
│  │  - Qwen-2.5 endpoint          │  │
│  │  - Cost tracking              │  │
│  └───────────────────────────────┘  │
│  ┌───────────────────────────────┐  │
│  │  Wallet Integration           │  │
│  │  - ICP ledger                 │  │
│  │  - Hedera SDK                 │  │
│  │  - ETH via Chain Fusion       │  │
│  │  - SOL via Chain Fusion       │  │
│  └───────────────────────────────┘  │
│  ┌───────────────────────────────┐  │
│  │  User Sessions                │  │
│  │  - Internet Identity auth     │  │
│  │  - Prompt history             │  │
│  │  - Spending limits            │  │
│  └───────────────────────────────┘  │
└─────────────────────────────────────┘
```

---

## 🔍 **Grain vs. Caffeine (Search Engine Decision)**

### **What is Caffeine?**

**Caffeine** is Dfinity's web search engine for ICP:
- Decentralized web indexing
- Search results stored on-chain
- Community-maintained index

**The Question:** Should we use Caffeine or build "Grain > Caffeine"?

### **Comparison**

| Feature | Caffeine (Dfinity) | Grain > Caffeine |
|---------|-------------------|------------------|
| **Index Source** | Community-maintained | Grain Network-curated |
| **Focus** | General web | Developer docs, courses |
| **Results** | Broad | Grain-specific |
| **Integration** | ICP-native | ICP + grainregistry |
| **Customization** | Limited | Full control |

### **Decision Criteria**

**Use Caffeine if:**
- Need general web search
- Want community-maintained index
- ICP-native solution preferred

**Build Grain > Caffeine if:**
- Need Grain Network-specific search
- Want grainregistry integration
- Custom ranking for educational content
- Need typo-tolerant search

**Recommendation:** Start with Caffeine, enhance with Grain-specific features

---

## 📱 **Building the Grainphone App**

### **Tech Stack**

**Android:**
```
Language: Kotlin
UI: Jetpack Compose
Build: Gradle + gb
ICP SDK: agent-kotlin (via Clotoko?)
Wallet: ICP Ledger integration
```

**iOS:**
```
Language: Swift
UI: SwiftUI
Build: Xcode + gb
ICP SDK: agent-swift
Wallet: Internet Identity
```

**Shared:**
- ICP backend (same canisters for both platforms)
- grainbox canister (model registry, wallet, auth)
- Open models (Llama-3, Qwen-2.5)

### **App Features**

**Core Editor:**
- Code editor with syntax highlighting
- File management
- Git integration
- Terminal access

**AI Features:**
- Chat with AI models
- Code completion
- Error explanation
- Code review

**Blockchain Features:**
- Wallet connection (ICP, Hedera, ETH, SOL)
- Cost preview before prompts
- Spending dashboard
- Transaction history

**Unique Features:**
- Choose model per prompt
- See exact costs
- Open-source models preferred
- Works offline (local models)

---

## 💡 **Clotoko Development Workflow**

### **Step 1: Write Canister Logic in Clojure**

```clojure
(ns grainbox.ai-models
  (:require [clotoko.core :as ct]))

(ct/defactor grainbox
  "AI model registry and prompt processor"
  
  ;; Stable state (survives upgrades)
  (ct/defstable models {})
  (ct/defstable user-spending {})
  
  ;; Query (read-only, fast)
  (ct/defquery list-models []
    models)
  
  (ct/defquery get-user-spending [user-id]
    (get user-spending user-id 0.0))
  
  ;; Update (modifies state)
  (ct/defupdate send-prompt [user-id model prompt]
    (let [cost (model-cost model)
          response (call-ai-api model prompt)]
      ;; Deduct from user wallet
      (charge-wallet user-id cost)
      ;; Track spending
      (swap! user-spending update user-id 
             (fnil + 0.0) cost)
      ;; Return response with cost
      {:response response
       :cost cost
       :model model
       :timestamp (current-time)})))
```

### **Step 2: Transpile to Motoko**

```bash
# Using Clotoko
cd grainbox
clotoko transpile src/grainbox/ai_models.clj --output canisters/grainbox/src/main.mo

# Verify generated code
cat canisters/grainbox/src/main.mo
```

### **Step 3: Deploy to ICP**

```bash
# Build canister
dfx build grainbox

# Deploy to local testnet
dfx deploy grainbox --network local

# Deploy to mainnet
dfx deploy grainbox --network ic --with-cycles 1000000000000
```

### **Step 4: Connect Mobile App**

**Android (Kotlin):**
```kotlin
// Grainphone Android app
import org.dfinity.agent.Agent
import org.dfinity.agent.identity.AnonymousIdentity

val agent = Agent.builder()
    .identity(AnonymousIdentity())
    .build()

val canisterId = Principal.fromText("rrkah-fqaaa-aaaaa-aaaaq-cai")

// Send prompt
val response = agent.call(
    canisterId,
    "sendPrompt",
    mapOf(
        "userId" to myUserId,
        "model" to "llama-3",
        "prompt" to "Write a function..."
    )
)

// Show cost
println("Cost: ${response["cost"]} ICP")
```

---

## 💸 **AI Model Economics**

### **Cost Per Prompt**

**Calculation:**
```
Cost = (Input tokens × Input price) + (Output tokens × Output price)

Example GPT-4 prompt:
  Input: 500 tokens × $0.00003 = $0.015
  Output: 1000 tokens × $0.00006 = $0.060
  Total: $0.075 per prompt
```

**Student Activity:** Calculate costs for different models and prompt sizes

### **Blockchain Fee Comparison**

| Chain | Base Fee | Prompt Cost | Total |
|-------|----------|-------------|-------|
| **ICP** | ~$0.0001 | $0.001 | $0.0011 |
| **Hedera** | ~$0.0001 | $0.001 | $0.0011 |
| **Solana** | ~$0.0001 | $0.001 | $0.0011 |
| **Ethereum** | ~$5-50 | $0.001 | $5.001 (too expensive!) |

**Lesson:** ICP, Hedera, and Solana are viable for AI micropayments. Ethereum is not.

---

## 🎨 **Hands-On Activities**

### **Activity 1: Calculate AI Costs**

**Scenario:** You're building a chatbot that helps students with homework.

**Questions:**
1. If GPT-4 costs $0.075 per prompt and you have 100 students asking 10 questions each per day, what's your daily cost?
2. If you switch to Llama-3 at $0.002 per prompt, how much do you save?
3. How many students can you serve for $100/month on each model?

**Answers:**
1. 100 students × 10 prompts × $0.075 = $75/day ($2,250/month!)
2. 100 students × 10 prompts × $0.002 = $20/day ($600/month) - Save $1,650/month!
3. GPT-4: $100/month ÷ $0.075 ÷ 10 prompts = ~13 students
   Llama-3: $100/month ÷ $0.002 ÷ 10 prompts = ~500 students

**Lesson:** Open models enable serving 38× more students for the same cost!

### **Activity 2: Design a Mobile AI App**

**Design Challenge:** Create a mobile app that helps students learn coding.

**Requirements:**
1. Choose AI model (consider cost vs. quality)
2. Design wallet integration (which blockchain?)
3. Create spending limit system (prevent overspending)
4. Design UI for cost transparency
5. Plan for offline mode (local models?)

**Deliverable:** Mockup (paper or digital) + 1-page explanation

### **Activity 3: Deploy a Simple Canister**

**Guided Lab:**

```bash
# 1. Install dfx (ICP SDK)
sh -ci "$(curl -fsSL https://internetcomputer.org/install.sh)"

# 2. Create new project
dfx new hello_world
cd hello_world

# 3. Start local replica
dfx start --background

# 4. Deploy canister
dfx deploy

# 5. Call canister
dfx canister call hello_world greet '("World")'
```

**Expected Output:**
```
("Hello, World!")
```

**Success!** You've deployed your first smart contract!

---

## 🌾 **Grain Network Open Models Strategy**

### **The Plan (From Earlier Sessions)**

**Three-Tier Model Hosting:**

**Tier 1: Self-Hosted (Free)**
- Run Llama-3, Qwen-2.5, Mistral on your own hardware
- No ongoing costs (just electricity)
- Complete privacy
- Requires technical setup

**Tier 2: Grain Network Hosted (Cheap)**
- Pay only for compute (ICP cycles)
- ~$0.001 per prompt (100× cheaper than GPT-4)
- Hosted on ICP canisters
- Open models only (Llama-3, Qwen, etc.)

**Tier 3: Premium API (Expensive but Best Quality)**
- GPT-4, Claude 3, Gemini Ultra
- $0.01-0.10 per prompt
- Best for complex tasks
- User pays difference

**Student Choice:**
```
Simple question → Llama-3 ($0.001)
Complex reasoning → GPT-4 ($0.075)
Code generation → Qwen-2.5 ($0.002)
```

### **grainbox Model Registry**

**Canister stores:**
```clojure
{:models
 [{:name "llama-3-8b"
   :provider :grain-hosted
   :cost-per-1k 0.001
   :quality-score 85
   :speed-ms 1200}
  
  {:name "gpt-4"
   :provider :openai
   :cost-per-1k 0.030
   :quality-score 98
   :speed-ms 2300}
  
  {:name "qwen-2.5-coder"
   :provider :grain-hosted
   :cost-per-1k 0.002
   :quality-score 92
   :speed-ms 900}]}
```

**Mobile App UI:**
```
┌─────────────────────────────────────┐
│  Choose AI Model:                   │
├─────────────────────────────────────┤
│  ○ Llama-3     $0.001  ★★★★☆ Fast  │
│  ○ Qwen-Coder  $0.002  ★★★★★ Fastest│
│  ● GPT-4       $0.030  ★★★★★ Best  │
├─────────────────────────────────────┤
│  This prompt will cost: ~$0.075     │
│  [Confirm] [Change Model]           │
└─────────────────────────────────────┘
```

---

## 🎯 **Building Grainphone: The Full Stack**

### **Layer 1: Mobile App (Kotlin/Swift)**

```kotlin
// Android: Grainphone.kt
class GrainphoneApp : Application() {
    private val icpAgent = ICPAgent(
        host = "https://ic0.app",
        canisterId = "grainbox-canister-id"
    )
    
    suspend fun sendPrompt(
        prompt: String,
        model: String
    ): PromptResponse {
        // 1. Get cost estimate
        val cost = icpAgent.query("getCost", model)
        
        // 2. Show user
        showCostDialog(cost)
        
        // 3. If approved, charge wallet
        val txId = wallet.transfer(cost, grainboxAddress)
        
        // 4. Send prompt to canister
        val response = icpAgent.update(
            "sendPrompt",
            mapOf(
                "prompt" to prompt,
                "model" to model,
                "paymentProof" to txId
            )
        )
        
        // 5. Update spending dashboard
        updateSpending(cost)
        
        return response
    }
}
```

### **Layer 2: ICP Canister (Motoko via Clotoko)**

```motoko
// Generated from Clojure via Clotoko
import Ledger "canister:ledger";
import HTTP "mo:base/HTTP";

actor grainbox {
    stable var userSpending : [(Principal, Float)] = [];
    
    public shared({ caller }) func sendPrompt(
        prompt : Text,
        model : Text,
        paymentProof : Blob
    ) : async Text {
        // 1. Verify payment
        let verified = await Ledger.verify(paymentProof);
        if (not verified) {
            throw Error.reject("Payment not verified");
        };
        
        // 2. Call AI model (HTTP outcall)
        let response = await HTTP.post(
            getModelEndpoint(model),
            {
                prompt = prompt;
                maxTokens = 1000;
            }
        );
        
        // 3. Track spending
        let cost = calculateCost(model, response);
        userSpending := Array.append(
            userSpending,
            [(caller, cost)]
        );
        
        // 4. Return response
        response.text
    };
}
```

### **Layer 3: AI Model Providers**

**Open Models (Grain-Hosted):**
```
grainbox canister
    ↓ (HTTP outcall)
Llama-3 API (hosted on ICP or Akash Network)
    ↓
Response
```

**Proprietary Models:**
```
grainbox canister
    ↓ (HTTP outcall with API key)
OpenAI API / Anthropic API
    ↓
Response (charged to Grain Network account)
    ↓
Cost passed to user
```

---

## 📊 **Economics: Why This Works**

### **Traditional Model (Cursor/ChatGPT)**

**User pays:** $20/month (fixed)
**Company provides:** Unlimited prompts (variable cost)
**Problem:** Heavy users subsidized by light users

**Example:**
- Light user: 10 prompts/month × $0.01 = $0.10 cost, pays $20 (200× overpaying!)
- Heavy user: 10,000 prompts/month × $0.01 = $100 cost, pays $20 (5× underpaying!)

### **Grainphone Model (Pay-Per-Use)**

**User pays:** Exactly what they use
**grainbox provides:** Routing and cost tracking
**Benefit:** Fair pricing for everyone

**Example:**
- Light user: 10 prompts × $0.001 = $0.01 (2000× cheaper!)
- Heavy user: 10,000 prompts × $0.001 = $10 (10× cheaper!)
- Student budget: $5/month = 5,000 prompts (vs. unlimited but expensive)

### **The Open Model Advantage**

**Proprietary Lock-In:**
- Can only use GPT-4 (one company)
- Prices can increase anytime
- Service can be discontinued
- Data sent to third party

**Open Model Freedom:**
- Choose from 10+ models
- Self-host for privacy
- Community improvements
- Data stays private

---

## 🏠 **Homework**

### **Required: Cost Analysis**

Calculate your AI usage costs:
1. Estimate how many prompts you send per day
2. Calculate monthly cost on GPT-4 ($0.03/prompt)
3. Calculate monthly cost on Llama-3 ($0.001/prompt)
4. How much would you save with Grainphone?

### **Optional: App Mockup**

Design Grainphone's spending dashboard:
- What information should be shown?
- How to make costs clear but not scary?
- What warnings for overspending?
- How to compare models easily?

### **Challenge: Deploy a Canister**

Follow the guided lab and deploy your first ICP canister:
1. Install dfx
2. Create "hello world" canister
3. Deploy locally
4. Call functions
5. Document your experience (1 page)

---

## 🎓 **Key Takeaways**

1. **Mobile apps can be open source** - We're building Grainphone!
2. **ICP canisters enable decentralized backends** - No central servers
3. **Clotoko makes canister dev easier** - Write Clojure, get Motoko
4. **Wallet integration shows real costs** - Transparency empowers users
5. **Open models are viable** - Often 90% quality at 3% cost
6. **Students can build this** - All the tools are available

---

## 🌐 **Connections to Previous Lessons**

**Lesson 00:** Display warmth → Mobile app needs display warmth too!
**Lesson 01-05:** Design foundations → Apply to mobile UI
**Lesson 06:** Type systems → Canisters are strongly typed
**Lesson 07:** Prediction markets → Could be mobile apps too
**Lesson 08:** Zero-knowledge proofs → Private AI prompts
**Lesson 09:** Environmental science → Mobile data collection
**Lesson 10:** Astrology → graintime in mobile timestamps
**Lesson 11:** Platform design → Mobile is part of platform
**Lesson 12:** Philosophy → Code is culture (open source mobile)

---

## 🔮 **The Vision**

**Imagine a world where:**
- Students code on their phones during bus rides
- AI assistance costs pennies, not dollars
- Every prompt's cost is transparent
- Open-source models compete with proprietary
- Wallets integrate seamlessly
- Decentralized canisters ensure no single company controls your tools

**That's what we're building with Grainphone.**

**From granules (one prompt) to grains (complete app) to THE WHOLE GRAIN (decentralized AI ecosystem).**

---

## 📚 **Further Reading**

- [ICP Developer Docs](https://internetcomputer.org/docs)
- [Clotoko Documentation](../../../grainstore/clotoko/README.md)
- [Android Developers](https://developer.android.com/)
- [Swift.org](https://swift.org/)
- [Llama 3 Model Card](https://llama.meta.com/llama3/)
- [Caffeine Search Engine](https://caffeine.dfinity.org/)

---

**Next Lesson:** Lesson 14 - Deployment, CI/CD, and The Future of Grain Network

**Instructor's Note:** This lesson connects mobile development, blockchain, and AI - three cutting-edge topics. Emphasize the cost transparency angle - students should understand they're often being overcharged. The open-source alternative (Grainphone) represents true technological sovereignty.

---

**Version:** 1.0.0  
**Author:** kae3g (Grain PBC)  
**License:** CC BY-SA 4.0  
**Date:** October 23, 2025

🌾 **Code anywhere, pay fairly, stay sovereign!**
