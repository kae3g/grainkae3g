(ns clotoko.core
  "Clotoko - Clojure to Motoko transpiler for ICP canister development.
   Main transpiler engine that converts Clojure syntax to Motoko."
  (:require
   [clojure.tools.analyzer.jvm :as ana]
   [clojure.tools.reader :as reader]
   [clojure.tools.reader.reader-types :as reader-types]
   [clojure.string :as str]
   [clojure.walk :as walk]
   [clojure.spec.alpha :as s]
   [clotoko.types :as types]
   [clotoko.motoko :as motoko]
   [clotoko.icp :as icp]))

;; =============================================================================
;; Core Transpiler State
;; =============================================================================

(defonce transpiler-state
  (atom
   {:canisters {}
    :current-canister nil
    :type-context {}
    :imports #{}
    :exports #{}}))

;; =============================================================================
;; Main Transpiler Functions
;; =============================================================================

(defn compile-file
  "Compile a Clotoko file to Motoko."
  [input-file output-file & {:keys [canister-name]}]
  (let [source (slurp input-file)
        ast (parse-source source)
        motoko-code (generate-motoko ast canister-name)]
    (spit output-file motoko-code)
    (log/info "Compiled" input-file "to" output-file)))

(defn compile-string
  "Compile a Clotoko string to Motoko."
  [source & {:keys [canister-name]}]
  (let [ast (parse-source source)
        motoko-code (generate-motoko ast canister-name)]
    motoko-code))

(defn generate-canister
  "Generate a complete ICP canister from Clotoko source."
  [canister-name source & {:keys [output-dir candid-file]}]
  (let [motoko-code (compile-string source :canister-name canister-name)
        candid-spec (generate-candid canister-name source)
        output-dir (or output-dir (str "canisters/" canister-name))]
    
    ;; Create output directory
    (io/make-parents (str output-dir "/src/Main.mo"))
    
    ;; Write Motoko source
    (spit (str output-dir "/src/Main.mo") motoko-code)
    
    ;; Write Candid interface
    (when candid-file
      (spit (str output-dir "/" candid-file) candid-spec))
    
    ;; Write dfx.json
    (spit (str output-dir "/dfx.json") (generate-dfx-json canister-name))
    
    (log/info "Generated canister" canister-name "in" output-dir)))

;; =============================================================================
;; Source Parsing
;; =============================================================================

(defn parse-source
  "Parse Clotoko source code to AST."
  [source]
  (let [reader (reader-types/string-push-back-reader source)]
    (loop [forms []
           form (reader/read reader)]
      (if (= form ::reader/eof)
        forms
        (recur (conj forms form) (reader/read reader))))))

(defn analyze-form
  "Analyze a Clojure form for type inference and transformation."
  [form]
  (try
    (ana/analyze form)
    (catch Exception e
      (log/warn "Failed to analyze form:" form e)
      form)))

;; =============================================================================
;; Motoko Code Generation
;; =============================================================================

(defn generate-motoko
  "Generate Motoko code from Clojure AST."
  [ast canister-name]
  (reset! transpiler-state
         {:canisters {}
          :current-canister canister-name
          :type-context {}
          :imports #{}
          :exports #{}})
  
  (let [motoko-forms (map transform-form ast)
        imports (generate-imports)
        exports (generate-exports)
        canister-code (generate-canister-code motoko-forms)]
    
    (str/join "\n\n" (concat imports [canister-code exports]))))

(defn transform-form
  "Transform a Clojure form to Motoko."
  [form]
  (cond
    ;; Canister definition
    (and (list? form) (= (first form) 'defcanister))
    (transform-canister-definition form)
    
    ;; Function definition
    (and (list? form) (= (first form) 'defn))
    (transform-function-definition form)
    
    ;; Variable definition
    (and (list? form) (= (first form) 'def))
    (transform-variable-definition form)
    
    ;; Let binding
    (and (list? form) (= (first form) 'let))
    (transform-let-binding form)
    
    ;; Function call
    (and (list? form) (symbol? (first form)))
    (transform-function-call form)
    
    ;; Literal values
    :else
    (transform-literal form)))

(defn transform-canister-definition
  "Transform a canister definition to Motoko."
  [[_ canister-name & body]]
  (swap! transpiler-state assoc :current-canister canister-name)
  
  (let [canister-forms (map transform-form body)
        canister-code (str/join "\n\n" canister-forms)]
    (str "// Canister: " canister-name "\n" canister-code)))

(defn transform-function-definition
  "Transform a function definition to Motoko."
  [[_ fn-name params & body]]
  (let [motoko-name (clojure->motoko-name fn-name)
        motoko-params (map transform-parameter params)
        motoko-body (map transform-form body)
        return-type (infer-return-type body)
        visibility (if (str/starts-with? (str fn-name) "public-") "public " "")]
    
    (str visibility "func " motoko-name "(" 
         (str/join ", " motoko-params) ") : " return-type " {\n"
         (str/join "\n" (map #(str "  " %) motoko-body))
         "\n}")))

(defn transform-variable-definition
  "Transform a variable definition to Motoko."
  [[_ var-name value]]
  (let [motoko-name (clojure->motoko-name var-name)
        motoko-value (transform-form value)
        var-type (infer-type value)]
    
    (str "let " motoko-name " : " var-type " = " motoko-value ";")))

(defn transform-let-binding
  "Transform a let binding to Motoko."
  [[_ bindings & body]]
  (let [motoko-bindings (map transform-binding bindings)
        motoko-body (map transform-form body)]
    
    (str "{\n"
         (str/join "\n" (map #(str "  " %) motoko-bindings))
         "\n  " (str/join "\n  " (map #(str % ";") motoko-body))
         "\n}")))

(defn transform-binding
  "Transform a let binding pair to Motoko."
  [[var-name value]]
  (let [motoko-name (clojure->motoko-name var-name)
        motoko-value (transform-form value)
        var-type (infer-type value)]
    
    (str "let " motoko-name " : " var-type " = " motoko-value ";")))

(defn transform-function-call
  "Transform a function call to Motoko."
  [[fn-name & args]]
  (let [motoko-name (clojure->motoko-name fn-name)
        motoko-args (map transform-form args)]
    
    (str motoko-name "(" (str/join ", " motoko-args) ")")))

(defn transform-literal
  "Transform a literal value to Motoko."
  [form]
  (cond
    (string? form) (str "\"" form "\"")
    (number? form) (str form)
    (boolean? form) (if form "true" "false")
    (keyword? form) (str "\"" (name form) "\"")
    (vector? form) (transform-vector form)
    (map? form) (transform-map form)
    (set? form) (transform-set form)
    (nil? form) "null"
    (symbol? form) (clojure->motoko-name form)
    :else (str form)))

(defn transform-vector
  "Transform a vector to Motoko array."
  [vec]
  (let [motoko-elements (map transform-form vec)
        element-type (infer-array-type vec)]
    
    (str "[" (str/join ", " motoko-elements) "] : [" element-type "]")))

(defn transform-map
  "Transform a map to Motoko record."
  [map]
  (let [motoko-pairs (map transform-map-pair map)]
    
    (str "{\n  " (str/join ";\n  " motoko-pairs) "\n}")))

(defn transform-map-pair
  "Transform a map key-value pair to Motoko."
  [[key value]]
  (let [motoko-key (clojure->motoko-name key)
        motoko-value (transform-form value)]
    
    (str motoko-key " = " motoko-value)))

(defn transform-set
  "Transform a set to Motoko array."
  [set]
  (let [motoko-elements (map transform-form set)
        element-type (infer-set-type set)]
    
    (str "[" (str/join ", " motoko-elements) "] : [" element-type "]")))

;; =============================================================================
;; Type System
;; =============================================================================

(defn infer-type
  "Infer Motoko type from Clojure form."
  [form]
  (cond
    (string? form) "Text"
    (number? form) (if (integer? form) "Int" "Float")
    (boolean? form) "Bool"
    (keyword? form) "Text"
    (vector? form) (str "[" (infer-array-type form) "]")
    (map? form) "Record"
    (set? form) (str "[" (infer-set-type form) "]")
    (nil? form) "Null"
    (symbol? form) "Any"
    :else "Any"))

(defn infer-array-type
  "Infer array element type."
  [vec]
  (if (empty? vec)
    "Any"
    (let [element-types (map infer-type vec)
          common-type (first element-types)]
      (if (every? #(= % common-type) element-types)
        common-type
        "Any"))))

(defn infer-set-type
  "Infer set element type."
  [set]
  (if (empty? set)
    "Any"
    (let [element-types (map infer-type set)
          common-type (first element-types)]
      (if (every? #(= % common-type) element-types)
        common-type
        "Any"))))

(defn infer-return-type
  "Infer function return type from body."
  [body]
  (if (empty? body)
    "()"
    (let [last-form (last body)]
      (infer-type last-form))))

(defn infer-parameter-type
  "Infer parameter type from usage."
  [param-name body]
  (let [usages (find-parameter-usages param-name body)]
    (if (empty? usages)
      "Any"
      (let [types (map infer-type usages)
            common-type (first types)]
        (if (every? #(= % common-type) types)
          common-type
          "Any")))))

;; =============================================================================
;; Name Conversion
;; =============================================================================

(defn clojure->motoko-name
  "Convert Clojure name to Motoko name."
  [name]
  (let [name-str (str name)]
    (cond
      (str/starts-with? name-str "public-")
      (str "public" (str/capitalize (subs name-str 7)))
      
      (str/starts-with? name-str "private-")
      (str "private" (str/capitalize (subs name-str 8)))
      
      :else
      (-> name-str
          (str/replace #"-" "")
          (str/capitalize)))))

(defn transform-parameter
  "Transform a function parameter to Motoko."
  [param]
  (if (vector? param)
    (let [[param-name param-type] param]
      (str (clojure->motoko-name param-name) " : " param-type))
    (str (clojure->motoko-name param) " : Any")))

;; =============================================================================
;; ICP Integration
;; =============================================================================

(defn generate-candid
  "Generate Candid interface from canister source."
  [canister-name source]
  (let [ast (parse-source source)
        functions (extract-canister-functions ast)]
    
    (str "service " canister-name " : {\n"
         (str/join "\n" (map generate-candid-function functions))
         "\n}")))

(defn generate-candid-function
  "Generate Candid function signature."
  [function]
  (let [fn-name (:name function)
        params (:params function)
        return-type (:return-type function)
        candid-name (clojure->motoko-name fn-name)
        candid-params (map generate-candid-parameter params)]
    
    (str "  " candid-name " : (" (str/join ", " candid-params) ") -> " return-type)))

(defn generate-candid-parameter
  "Generate Candid parameter."
  [param]
  (if (vector? param)
    (let [[name type] param]
      (str type))
    "text"))

(defn generate-dfx-json
  "Generate dfx.json configuration."
  [canister-name]
  (json/write-str
   {:canisters {canister-name {:main "src/Main.mo"
                               :type "motoko"}}}))

(defn extract-canister-functions
  "Extract functions from canister AST."
  [ast]
  (filter #(= (:op %) :def) ast))

;; =============================================================================
;; Utility Functions
;; =============================================================================

(defn find-parameter-usages
  "Find all usages of a parameter in function body."
  [param-name body]
  (filter #(= % param-name) (tree-seq coll? seq body)))

(defn generate-imports
  "Generate Motoko imports."
  []
  (let [imports (:imports @transpiler-state)]
    (map #(str "import " % ";") imports)))

(defn generate-exports
  "Generate Motoko exports."
  []
  (let [exports (:exports @transpiler-state)]
    (map #(str "export " % ";") exports)))

(defn generate-canister-code
  "Generate main canister code."
  [forms]
  (str/join "\n\n" forms))

;; =============================================================================
;; Main Entry Point
;; =============================================================================

(defn -main
  "Main entry point for Clotoko compiler."
  [& args]
  (let [input-file (first args)
        output-file (second args)]
    (if (and input-file output-file)
      (compile-file input-file output-file)
      (println "Usage: clotoko <input-file> <output-file>"))))

