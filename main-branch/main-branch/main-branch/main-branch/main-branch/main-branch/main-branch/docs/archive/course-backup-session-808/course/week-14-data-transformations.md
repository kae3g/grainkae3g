# 📊 Week 14: Data Transformations & Algorithm Performance
## *"From EDN to Markdown: Building Real-World Applications"*

**Course Module:** Week 14 - Data Structures & Transformations  
**Difficulty:** Intermediate  
**Time Required:** 4-5 hours  
**Prerequisites:** Basic Clojure, understanding of data structures  
**AI Mode:** Cursor Agentic Autocomplete with Auto Model (All Models)

---

## 🎯 Learning Objectives

By the end of this module, you will:

1. ✅ Understand EDN vs Markdown data formats
2. ✅ Build bidirectional transformers (EDN ↔ Markdown)
3. ✅ Analyze algorithm performance with Big O notation
4. ✅ Choose optimal data structures for real-world use cases
5. ✅ Build practical applications for social media, e-commerce, and more
6. ✅ Optimize for different trade industries (healthcare, finance, retail)

---

## 📚 Part 1: Understanding Data Formats

### What is EDN?

**EDN (Extensible Data Notation)** - Clojure's native data format

**Strengths:**
- ✅ Rich data types (maps, vectors, sets, keywords)
- ✅ Immutable by default
- ✅ Native Clojure interop
- ✅ Type-safe
- ✅ Fast to parse and serialize

**Example:**

```clojure
{:user {:id 12345
        :username "kae3g"
        :email "kj3x39@gmail.com"
        :roles #{:admin :developer}
        :created-at #inst "2025-01-22T10:30:00Z"
        :metadata {:bio "Building the Grain Network"
                   :location "Urbana, IL"
                   :projects ["grainkae3g" "grainweb" "grainspace"]}}}
```

### What is Markdown?

**Markdown** - Human-readable text format

**Strengths:**
- ✅ Human-readable and writable
- ✅ Version control friendly (git diff works well)
- ✅ Universal support (GitHub, blogs, documentation)
- ✅ Simple syntax
- ✅ SEO-friendly

**Example:**

```markdown
# User Profile: kae3g

**Email:** kj3x39@gmail.com  
**Roles:** Admin, Developer  
**Joined:** January 22, 2025

## Bio
Building the Grain Network

## Location
Urbana, IL

## Projects
- grainkae3g
- grainweb
- grainspace
```

### When to Use Each

| Use Case | EDN | Markdown | Why? |
|----------|-----|----------|------|
| **Configuration** | ✅ | ❌ | Structured, type-safe |
| **Documentation** | ❌ | ✅ | Human-readable |
| **User Profiles** | ✅ | ⚠️ | Rich data, but markdown for display |
| **Blog Posts** | ⚠️ | ✅ | Content-focused |
| **Database Storage** | ✅ | ❌ | Queryable, structured |
| **Email Templates** | ❌ | ✅ | Text-based |
| **API Responses** | ✅ | ❌ | Machine-readable |
| **README Files** | ❌ | ✅ | GitHub-friendly |

---

## 🔄 Part 2: Building Bidirectional Transformers

### EDN → Markdown Transformer

**Use Case:** Convert user profile data to profile page

```clojure
(ns grain-transformers.edn-to-markdown
  "Transform EDN data to Markdown format"
  (:require [clojure.string :as str]))

(defn edn-user-to-markdown
  "Transform user EDN to Markdown profile"
  [user-edn]
  (let [{:keys [id username email roles created-at metadata]} (:user user-edn)
        {:keys [bio location projects]} metadata]
    (str "# User Profile: " username "\n\n"
         "**Email:** " email "\n"
         "**Roles:** " (str/join ", " (map name roles)) "\n"
         "**Joined:** " created-at "\n\n"
         "## Bio\n" bio "\n\n"
         "## Location\n" location "\n\n"
         "## Projects\n"
         (str/join "\n" (map #(str "- " %) projects)))))

;; Usage
(def user-data
  {:user {:id 12345
          :username "kae3g"
          :email "kj3x39@gmail.com"
          :roles #{:admin :developer}
          :created-at #inst "2025-01-22T10:30:00Z"
          :metadata {:bio "Building the Grain Network"
                     :location "Urbana, IL"
                     :projects ["grainkae3g" "grainweb"]}}})

(println (edn-user-to-markdown user-data))
```

**Performance:** O(n) where n = number of projects (linear scaling)

### Markdown → EDN Transformer

**Use Case:** Parse user-edited profile back to structured data

```clojure
(ns grain-transformers.markdown-to-edn
  "Transform Markdown to EDN format"
  (:require [clojure.string :as str]))

(defn parse-markdown-section
  "Extract content from Markdown section"
  [markdown section-name]
  (when-let [match (re-find (re-pattern (str "## " section-name "\\s*\\n([^#]+)"))
                            markdown)]
    (str/trim (second match))))

(defn markdown-to-edn-user
  "Transform Markdown profile to user EDN"
  [markdown]
  (let [username (second (re-find #"# User Profile: (.*)\n" markdown))
        email (second (re-find #"\*\*Email:\*\* (.*)\n" markdown))
        roles-str (second (re-find #"\*\*Roles:\*\* (.*)\n" markdown))
        roles (set (map keyword (str/split roles-str #",\s*")))
        joined (second (re-find #"\*\*Joined:\*\* (.*)\n" markdown))
        bio (parse-markdown-section markdown "Bio")
        location (parse-markdown-section markdown "Location")
        projects-str (parse-markdown-section markdown "Projects")
        projects (when projects-str
                  (map str/trim
                       (map #(subs % 2)  ; Remove "- " prefix
                            (str/split-lines projects-str))))]
    {:user {:username username
            :email email
            :roles roles
            :created-at joined
            :metadata {:bio bio
                       :location location
                       :projects (vec projects)}}}))

;; Usage
(def markdown-profile "# User Profile: kae3g\n...")
(markdown-to-edn-user markdown-profile)
```

**Performance:** O(n) where n = length of markdown string (regex parsing)

---

## ⚡ Part 3: Algorithm Performance Analysis

### Big O Notation Crash Course

**Common complexities (from fast to slow):**

| Notation | Name | Example | Real-World Meaning |
|----------|------|---------|-------------------|
| O(1) | Constant | Hash map lookup | Instant, always same speed |
| O(log n) | Logarithmic | Binary search | Very fast, scales well |
| O(n) | Linear | Array scan | Speed doubles when size doubles |
| O(n log n) | Linearithmic | Merge sort | Efficient sorting |
| O(n²) | Quadratic | Nested loops | Slow for large data |
| O(2ⁿ) | Exponential | Recursive fibonacci | Unusable for large n |

### Performance by Data Structure

```clojure
;; Vector (Array-like)
(def users-vector [user1 user2 user3 ...])

;; Lookup by index: O(1)
(nth users-vector 100)  ; Instant

;; Search: O(n)
(filter #(= (:id %) 12345) users-vector)  ; Linear scan

;; Append: O(1)
(conj users-vector new-user)  ; Efficient


;; Hash Map (Dictionary)
(def users-map {12345 user1, 67890 user2, ...})

;; Lookup by key: O(1)
(get users-map 12345)  ; Instant

;; Search by value: O(n)
(filter #(= (:email %) "kj3x39@gmail.com") (vals users-map))

;; Insert: O(1)
(assoc users-map 99999 new-user)  ; Efficient


;; Set (Unique collection)
(def admin-ids #{12345 67890 99999})

;; Membership test: O(1)
(contains? admin-ids 12345)  ; Instant

;; Add: O(1)
(conj admin-ids 11111)

;; Union/Intersection: O(n)
(clojure.set/union admin-ids moderator-ids)
```

---

## 🏢 Part 4: Real-World Use Cases

### Use Case 1: Social Media Feed

**Industry:** Social media (Twitter, Instagram, LinkedIn)

**Problem:** Display user posts efficiently

**Data Structure Choice:**

```clojure
;; Bad: Vector of posts (searching is O(n))
(def posts [{:id 1 :author "alice" :content "..."}
            {:id 2 :author "bob" :content "..."}])

;; Good: Map with post ID as key (lookup is O(1))
(def posts-map {1 {:author "alice" :content "..."}
                2 {:author "bob" :content "..."}})

;; Better: Add index for common queries
(def posts-db
  {:by-id {1 post1, 2 post2}           ; O(1) lookup by ID
   :by-author {"alice" #{1 3 5}        ; O(1) lookup by author
               "bob" #{2 4 6}}
   :by-date {#inst "2025-01-22" #{1 2} ; O(1) lookup by date
             #inst "2025-01-21" #{3 4}}})
```

**EDN Format:**

```clojure
{:feed
 {:posts
  [{:id 1
    :author {:username "alice" :display-name "Alice"}
    :content "Just grainversioned the NixOS template!"
    :timestamp #inst "2025-01-22T14:30:00Z"
    :likes 42
    :comments 5
    :tags #{:nixos :grainversion :tutorial}}
   
   {:id 2
    :author {:username "bob" :display-name "Bob"}
    :content "Built my first Grainweb extension"
    :timestamp #inst "2025-01-22T15:45:00Z"
    :likes 28
    :comments 3
    :tags #{:grainweb :clojure :development}}]}}
```

**Transform to Markdown for Display:**

```clojure
(defn feed-to-markdown
  "Transform social feed to Markdown"
  [feed-edn]
  (str/join "\n\n---\n\n"
    (for [post (get-in feed-edn [:feed :posts])]
      (str "### " (get-in post [:author :display-name])
           " (@" (get-in post [:author :username]) ")\n\n"
           (:content post) "\n\n"
           "**" (:likes post) " likes** • "
           (:comments post) " comments • "
           (:timestamp post) "\n\n"
           "*Tags:* " (str/join ", " (map name (:tags post)))))))
```

**Performance:** O(n) where n = number of posts

### Use Case 2: E-Commerce Product Catalog

**Industry:** E-commerce (Amazon, Shopify, Etsy)

**Problem:** Display products with filters and search

**Data Structure Choice:**

```clojure
;; Product catalog with multiple indexes
(def catalog
  {:products
   {:by-id {"prod-001" {:name "Grainwriter"
                        :price 1399
                        :category :hardware
                        :tags #{:e-ink :sustainable}
                        :in-stock true}
    "prod-002" {:name "Grainpack"
                :price 1399
                :category :hardware
                :tags #{:gpu :refurbished}
                :in-stock true}}
   
   :by-category {:hardware #{"prod-001" "prod-002"}
                 :software #{}}
   
   :by-tag {:e-ink #{"prod-001"}
            :sustainable #{"prod-001"}
            :gpu #{"prod-002"}
            :refurbished #{"prod-002"}}
   
   :by-price {1399 #{"prod-001" "prod-002"}}})
```

**Fast Queries:**

```clojure
;; Get product by ID: O(1)
(get-in catalog [:products :by-id "prod-001"])

;; Get all hardware products: O(1) + O(k) where k = result size
(let [hardware-ids (get-in catalog [:products :by-category :hardware])]
  (map #(get-in catalog [:products :by-id %]) hardware-ids))

;; Get sustainable products: O(1) + O(k)
(let [sustainable-ids (get-in catalog [:products :by-tag :sustainable])]
  (map #(get-in catalog [:products :by-id %]) sustainable-ids))
```

**Transform to Markdown Catalog:**

```clojure
(defn catalog-to-markdown
  "Transform product catalog to Markdown"
  [catalog]
  (str "# Product Catalog\n\n"
       (str/join "\n\n"
         (for [[id product] (get-in catalog [:products :by-id])]
           (str "## " (:name product) "\n\n"
                "**Price:** $" (:price product) "\n"
                "**Category:** " (name (:category product)) "\n"
                "**Status:** " (if (:in-stock product) "In Stock ✅" "Out of Stock ❌") "\n"
                "**Tags:** " (str/join ", " (map name (:tags product))))))))
```

**Performance:** O(n) where n = number of products

---

## 💼 Part 5: Industry-Specific Applications

### Healthcare: Patient Records

**Requirements:**
- Fast patient lookup by ID
- Quick search by symptoms
- Privacy (HIPAA compliant)
- Audit trail

**Data Structure:**

```clojure
(def patient-db
  {:patients
   {:by-id {"P12345" {:name "John Doe"
                      :dob "1990-05-15"
                      :blood-type "O+"
                      :allergies #{:penicillin :latex}
                      :conditions #{:hypertension}
                      :last-visit #inst "2025-01-15"}}
    "P67890" {:name "Jane Smith"
              :dob "1985-03-22"
              :blood-type "A-"
              :allergies #{}
              :conditions #{:diabetes}
              :last-visit #inst "2025-01-20"}}
   
   :by-condition {:hypertension #{"P12345"}
                  :diabetes #{"P67890"}}
   
   :by-allergy {:penicillin #{"P12345"}
                :latex #{"P12345"}}}})

;; Fast queries
;; Get patient: O(1)
(get-in patient-db [:patients :by-id "P12345"])

;; Find patients with condition: O(1) + O(k)
(let [patient-ids (get-in patient-db [:patients :by-condition :diabetes])]
  (map #(get-in patient-db [:patients :by-id %]) patient-ids))

;; Check allergies before prescribing: O(1)
(contains? (get-in patient-db [:patients :by-id "P12345" :allergies])
           :penicillin)
```

**Transform to Markdown Report:**

```clojure
(defn patient-to-markdown
  "Generate patient report in Markdown"
  [patient-id patient-db]
  (let [patient (get-in patient-db [:patients :by-id patient-id])]
    (str "# Patient Report: " patient-id "\n\n"
         "**Name:** " (:name patient) "\n"
         "**DOB:** " (:dob patient) "\n"
         "**Blood Type:** " (:blood-type patient) "\n\n"
         "## Allergies\n"
         (if (empty? (:allergies patient))
           "None\n"
           (str/join "\n" (map #(str "- " (name %)) (:allergies patient))))
         "\n\n## Conditions\n"
         (str/join "\n" (map #(str "- " (name %)) (:conditions patient)))
         "\n\n**Last Visit:** " (:last-visit patient))))
```

**Why this matters:**
- Emergency situations need O(1) lookups (seconds matter!)
- Privacy: Store EDN locally, generate Markdown reports on-demand
- Audit: EDN changes tracked in git, Markdown for human review

---

### Finance: Transaction Ledger

**Industry:** Banking, cryptocurrency, payment processing

**Requirements:**
- Append-only (immutability)
- Fast balance queries
- Transaction history
- Fraud detection

**Data Structure:**

```clojure
(def ledger
  {:transactions
   [{:id "tx-001"
     :from "account-123"
     :to "account-456"
     :amount 1000
     :timestamp #inst "2025-01-22T10:00:00Z"
     :type :transfer
     :status :completed}
    {:id "tx-002"
     :from "account-456"
     :to "account-789"
     :amount 500
     :timestamp #inst "2025-01-22T11:00:00Z"
     :type :transfer
     :status :completed}]
   
   :balances {"account-123" 9000
              "account-456" 500
              "account-789" 500}
   
   :by-account {"account-123" #{"tx-001"}
                "account-456" #{"tx-001" "tx-002"}
                "account-789" #{"tx-002"}}})

;; Fast balance query: O(1)
(get-in ledger [:balances "account-123"])
;; => 9000

;; Get account transactions: O(1) + O(k) where k = number of txs
(let [tx-ids (get-in ledger [:by-account "account-456"])]
  (filter #(tx-ids (:id %)) (:transactions ledger)))
```

**Transform to Markdown Statement:**

```clojure
(defn account-statement-to-markdown
  "Generate account statement in Markdown"
  [account-id ledger]
  (let [balance (get-in ledger [:balances account-id])
        tx-ids (get-in ledger [:by-account account-id])
        transactions (filter #(tx-ids (:id %)) (:transactions ledger))]
    (str "# Account Statement: " account-id "\n\n"
         "**Current Balance:** $" balance "\n\n"
         "## Transactions\n\n"
         "| Date | Type | Amount | From/To | Status |\n"
         "|------|------|--------|---------|--------|\n"
         (str/join "\n"
           (for [tx transactions]
             (str "| " (:timestamp tx) " | "
                  (name (:type tx)) " | $"
                  (:amount tx) " | "
                  (if (= (:from tx) account-id)
                    (str "→ " (:to tx))
                    (str "← " (:from tx))) " | "
                  (name (:status tx)) " |"))))))
```

**Why this matters:**
- Banking needs O(1) balance checks (millions of queries/second)
- Immutable ledger (append-only) prevents fraud
- Markdown statements for customer downloads

---

### Retail: Inventory Management

**Industry:** Retail, warehouse, supply chain

**Requirements:**
- Real-time inventory tracking
- Low-stock alerts
- Multi-location support
- Fast restock decisions

**Data Structure:**

```clojure
(def inventory
  {:products
   {:by-sku {"SKU-001" {:name "Grainwriter"
                        :sku "SKU-001"
                        :price 1399
                        :category :electronics}
    "SKU-002" {:name "Grainpack"
               :sku "SKU-002"
               :price 1399
               :category :electronics}}
   
   :stock
   {:by-location {"warehouse-1" {"SKU-001" 50
                                  "SKU-002" 30}
                  "warehouse-2" {"SKU-001" 25
                                  "SKU-002" 15}}
    :total {"SKU-001" 75
            "SKU-002" 45}}
   
   :low-stock-alerts
   {:threshold 10
    :items #{"SKU-002"}}})  ; Items below threshold

;; Fast queries
;; Check total stock: O(1)
(get-in inventory [:stock :total "SKU-001"])
;; => 75

;; Check warehouse stock: O(1)
(get-in inventory [:stock :by-location "warehouse-1" "SKU-001"])
;; => 50

;; Get low-stock items: O(1)
(get-in inventory [:low-stock-alerts :items])
;; => #{"SKU-002"}
```

**Transform to Markdown Inventory Report:**

```clojure
(defn inventory-to-markdown
  "Generate inventory report in Markdown"
  [inventory]
  (str "# Inventory Report\n\n"
       "Generated: " (java.time.LocalDateTime/now) "\n\n"
       "## Low Stock Alerts ⚠️\n\n"
       (str/join "\n"
         (for [sku (get-in inventory [:low-stock-alerts :items])]
           (let [product (get-in inventory [:products :by-sku sku])
                 stock (get-in inventory [:stock :total sku])]
             (str "- **" (:name product) "** (SKU: " sku "): "
                  stock " units remaining"))))
       "\n\n## Stock by Location\n\n"
       (str/join "\n\n"
         (for [[location stocks] (get-in inventory [:stock :by-location])]
           (str "### " location "\n\n"
                "| SKU | Product | Stock |\n"
                "|-----|---------|-------|\n"
                (str/join "\n"
                  (for [[sku qty] stocks]
                    (let [product (get-in inventory [:products :by-sku sku])]
                      (str "| " sku " | " (:name product) " | " qty " |")))))))))
```

**Why this matters:**
- Warehouses need O(1) stock checks (100+ lookups/minute)
- Low-stock alerts prevent stockouts
- Multi-location tracking for distributed warehouses

---

### Use Case 4: Content Management System (CMS)

**Industry:** Publishing, blogging, documentation

**Problem:** Manage articles with tags, categories, and search

**Data Structure:**

```clojure
(def cms-db
  {:articles
   {:by-slug {"getting-started" {:slug "getting-started"
                                  :title "Getting Started with Grain Network"
                                  :author "kae3g"
                                  :published #inst "2025-01-22"
                                  :category :tutorial
                                  :tags #{:beginner :nixos :tutorial}
                                  :content "# Getting Started\n\nWelcome..."}
    "grainweb-release" {:slug "grainweb-release"
                        :title "Grainweb: The Future of Browsing"
                        :author "kae3g"
                        :published #inst "2025-01-22"
                        :category :press-release
                        :tags #{:grainweb :announcement}
                        :content "# Grainweb\n\nToday we..."}}
   
   :by-category {:tutorial #{"getting-started"}
                 :press-release #{"grainweb-release"}}
   
   :by-tag {:beginner #{"getting-started"}
            :nixos #{"getting-started"}
            :grainweb #{"grainweb-release"}
            :announcement #{"grainweb-release"}}
   
   :by-author {"kae3g" #{"getting-started" "grainweb-release"}}}})

;; Efficient queries
;; Get article: O(1)
(get-in cms-db [:articles :by-slug "getting-started"])

;; Get all tutorials: O(1) + O(k)
(let [tutorial-slugs (get-in cms-db [:articles :by-category :tutorial])]
  (map #(get-in cms-db [:articles :by-slug %]) tutorial-slugs))

;; Get articles by tag: O(1) + O(k)
(let [tagged-slugs (get-in cms-db [:articles :by-tag :nixos])]
  (map #(get-in cms-db [:articles :by-slug %]) tagged-slugs))
```

**Transform to Magazine Layout:**

```clojure
(defn cms-to-magazine-markdown
  "Transform CMS articles to magazine-style Markdown"
  [cms-db category]
  (let [article-slugs (get-in cms-db [:articles :by-category category])
        articles (map #(get-in cms-db [:articles :by-slug %]) article-slugs)]
    (str "# " (str/capitalize (name category)) " Articles\n\n"
         (str/join "\n\n---\n\n"
           (for [article articles]
             (str "## " (:title article) "\n\n"
                  "*By " (:author article) " • "
                  (:published article) "*\n\n"
                  "**Tags:** " (str/join ", " (map name (:tags article))) "\n\n"
                  (:content article)))))))
```

**Why this matters:**
- Blog platforms need O(1) article lookups
- Tag-based navigation (common in CMSs)
- Category filtering for magazine layouts
- This is exactly what our Grain magazine website uses!

---

## 🚀 Part 6: Building the Grain Magazine Website

### Real Implementation

**Our actual data flow:**

```
EDN (data/magazine-content.edn)
  ↓ Parse with Clojure
  ↓ Transform to page data
  ↓ Generate Markdown
  ↓ SvelteKit renders
  ↓ HTML/CSS output
  ↓ Deploy to GitHub Pages
```

**Code Example:**

```clojure
(ns grain-magazine.transform
  "Transform EDN data to magazine Markdown"
  (:require [clojure.edn :as edn]
            [clojure.string :as str]))

(defn load-magazine-data
  "Load magazine content from EDN"
  []
  (-> "data/magazine-content.edn"
      slurp
      edn/read-string))

(defn generate-home-page
  "Generate home page Markdown from EDN"
  [data]
  (let [featured (:featured-articles data)
        projects (get-in data [:sections :projects])]
    (str "# " (get-in data [:site :name]) "\n\n"
         "## " (get-in data [:site :tagline]) "\n\n"
         "## Featured Articles\n\n"
         (str/join "\n\n"
           (for [article featured]
             (str "### [" (:title article) "](/" (:slug article) ")\n\n"
                  "*" (:subtitle article) "*\n\n"
                  (:excerpt article))))
         "\n\n## Projects\n\n"
         (str/join "\n\n"
           (for [project projects]
             (str "### " (:name project) "\n\n"
                  (:description project) "\n\n"
                  "**Status:** " (:status project) "\n"
                  "**Tech:** " (str/join ", " (:technologies project))))))))

;; Usage
(def magazine-data (load-magazine-data))
(spit "generated/home.md" (generate-home-page magazine-data))
```

**Performance Analysis:**

- `load-magazine-data`: O(n) where n = file size
- `generate-home-page`: O(m + p) where m = articles, p = projects
- **Total:** O(n + m + p) - Linear scaling, very efficient!

---

## ⚡ Part 7: Performance Optimization Techniques

### Technique 1: Memoization

**Problem:** Expensive transformations called repeatedly

```clojure
;; Without memoization: O(n) every call
(defn expensive-transform [data]
  (Thread/sleep 1000)  ; Simulate expensive operation
  (transform data))

;; Calling 10 times = 10 seconds!
(dotimes [i 10]
  (expensive-transform my-data))

;; With memoization: O(n) first call, O(1) subsequent
(def expensive-transform-memo (memoize expensive-transform))

;; Calling 10 times = 1 second!
(dotimes [i 10]
  (expensive-transform-memo my-data))
```

**Real-world use:**

```clojure
(def edn-to-markdown-memo
  (memoize edn-user-to-markdown))

;; First call: parses and transforms
(edn-to-markdown-memo user-data)  ; ~10ms

;; Subsequent calls: cached
(edn-to-markdown-memo user-data)  ; ~0.01ms
```

### Technique 2: Lazy Sequences

**Problem:** Large datasets that might not be fully used

```clojure
;; Eager: Transforms all 10,000 posts immediately
(def all-posts-markdown
  (map post-to-markdown (range 10000)))
;; Takes 5 seconds

;; Lazy: Only transforms when needed
(def posts-lazy
  (map post-to-markdown (range 10000)))
;; Instant!

;; Only transforms first 10 when displayed
(take 10 posts-lazy)
;; ~50ms
```

### Technique 3: Transducers

**Problem:** Multiple transformation steps create intermediate collections

```clojure
;; Without transducers: Creates 3 intermediate collections
(defn process-posts [posts]
  (->> posts
       (filter :published?)     ; Intermediate collection 1
       (map add-markdown)       ; Intermediate collection 2
       (take 10)))              ; Final collection

;; With transducers: No intermediate collections
(defn process-posts-fast [posts]
  (into []
    (comp
      (filter :published?)
      (map add-markdown)
      (take 10))
    posts))

;; Performance improvement: 30-50% faster for large datasets
```

---

## 🎓 Part 8: Hands-On Exercises

### Exercise 1: Build a Blog Transformer

**Task:** Transform blog post EDN to Markdown

**Input (EDN):**

```clojure
{:post {:slug "my-first-grainversion"
        :title "My First Grainversion"
        :author "student"
        :date "2025-01-22"
        :tags #{:tutorial :nixos}
        :content "Today I learned about grainversioning..."}}
```

**Output (Markdown):**

```markdown
# My First Grainversion

**Author:** student  
**Date:** 2025-01-22  
**Tags:** tutorial, nixos

Today I learned about grainversioning...
```

**Your code (use Cursor AI):**

```clojure
(defn post-to-markdown [post-edn]
  ;; TODO: Implement this
  ;; Hint: Use str/join and get-in
  )
```

### Exercise 2: Build a Product Search

**Task:** Given inventory EDN, implement fast product search by tag

**Performance requirement:** Must be O(1) + O(k) where k = results

```clojure
(defn search-by-tag [inventory tag]
  ;; TODO: Implement efficient search
  ;; Hint: Use the :by-tag index
  )
```

### Exercise 3: Optimize with Memoization

**Task:** Profile transformation performance with and without memoization

```clojure
(require '[criterium.core :as crit])

;; Benchmark without memoization
(crit/bench (expensive-transform data))

;; Benchmark with memoization
(def memo-transform (memoize expensive-transform))
(crit/bench (memo-transform data))

;; Compare results
```

---

## 💡 Part 9: Real-World Grain Network Examples

### Example 1: Grainstore Manifest

**We actually use this!**

```clojure
;; grainstore-manifest.edn (EDN)
{:grainstore-manifest
 {:submodules
  [{:name "clojure-icp"
    :upstream "https://github.com/grainpbc/clojure-icp.git"}]}}

;; Transform to README.md
(defn manifest-to-readme [manifest]
  (str "# Grainstore\n\n"
       "## Submodules\n\n"
       (str/join "\n"
         (for [sub (:submodules manifest)]
           (str "- **" (:name sub) "**: " (:description sub))))))
```

### Example 2: API Documentation

**Our API docs use EDN → Markdown:**

```clojure
;; api-spec.edn
{:api
 {:name "clojure-icp"
  :functions
  [{:name "create-agent"
    :params [{:name "host" :type "string"}]
    :returns "Agent instance"
    :example "(icp/create-agent)"}]}}

;; Generate Markdown docs
(defn api-to-markdown [api-spec]
  ;; Transforms to magazine-style API docs
  )
```

---

## 📊 Part 10: Performance Summary

### When to Use Each Data Structure

```markdown
| Use Case | Data Structure | Lookup | Insert | Search | Why? |
|----------|----------------|--------|--------|--------|------|
| **User by ID** | Hash Map | O(1) | O(1) | O(n) | Fast lookups |
| **Feed of Posts** | Vector + Map | O(1) | O(1) | O(n) | Order + fast access |
| **Tags** | Set | O(1) | O(1) | O(1) | Unique, fast membership |
| **Transactions** | Vector (append) | O(n) | O(1) | O(n) | Immutable history |
| **Index** | Map of Sets | O(1) | O(1) | O(1) | Fast multi-key lookup |
```

### Transformation Performance

```markdown
| Transformation | Time Complexity | Space Complexity | Use When |
|----------------|-----------------|------------------|----------|
| **EDN → Markdown** | O(n) | O(n) | Generating reports |
| **Markdown → EDN** | O(n) | O(n) | Parsing user input |
| **Memoized** | O(1) cached | O(n) cache | Repeated calls |
| **Lazy** | O(1) init | O(1) init | Large datasets |
| **Transducers** | O(n) | O(1) | Pipelines |
```

---

## ✅ Key Takeaways

**Remember:**

1. **Choose the right data structure** - O(1) vs O(n) matters at scale
2. **Use EDN for data** - Type-safe, structured, Clojure-native
3. **Use Markdown for display** - Human-readable, git-friendly
4. **Build indexes** - Trade memory for speed (usually worth it)
5. **Memoize expensive operations** - Cache results when possible
6. **Use lazy sequences** - Don't compute what you don't need
7. **Profile before optimizing** - Measure, don't guess

**In the real world:**
- Social media: 10M+ users need O(1) lookups
- Finance: Milliseconds matter for trading
- Healthcare: Lives depend on fast data access
- E-commerce: Slow search = lost sales

**The Grain Network uses these techniques everywhere:**
- Fast grainstore submodule lookups
- Efficient API documentation generation
- Quick magazine article rendering
- Instant product catalog searches

---

## 🎯 Final Project: Build Your Own CMS

**Task:** Build a simple CMS using these techniques

**Requirements:**
- Store articles in EDN
- Transform to Markdown for display
- Support tags and categories
- O(1) article lookup
- O(1) + O(k) tag search

**Starter code in Cursor:**

```clojure
(ns my-cms.core
  (:require [clojure.edn :as edn]
            [clojure.string :as str]))

(def cms-db (atom {:articles {:by-slug {} :by-tag {} :by-category {}}}))

(defn add-article [article]
  ;; TODO: Add article with indexes
  )

(defn get-article [slug]
  ;; TODO: Get article by slug (must be O(1))
  )

(defn search-by-tag [tag]
  ;; TODO: Search by tag (must be O(1) + O(k))
  )

(defn article-to-markdown [article]
  ;; TODO: Transform article to Markdown
  )
```

**Use Cursor AI:**
1. Open file in Cursor
2. Press Cmd+K on each TODO
3. Prompt: "Implement this function efficiently"
4. Review AI suggestion
5. Understand the algorithm
6. Test performance

---

**Week 14: Data Transformations & Algorithm Performance**  
*Part of Grain Network High School Course*

**Author:** kae3g (Graingalaxy)  
**Organization:** Grain PBC

*"Data structures are the foundation. Algorithms are the art. Performance is the craft."* 🌾


