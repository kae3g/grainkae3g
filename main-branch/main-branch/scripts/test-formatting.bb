#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[babashka.process :refer [shell]]
         '[clojure.string :as str])

(defn create-test-file [content file-path]
  "Create a test file with given content"
  (spit file-path content))

(defn cleanup-test-files []
  "Clean up test files"
  (fs/delete-tree "test-formatting")
  (fs/delete-tree "test-formatting.md"))

(defn test-clojure-formatting []
  "Test Clojure file formatting"
  (println "🧪 Testing Clojure file formatting...")
  
  (fs/create-dirs "test-formatting")
  
  (let [test-content "(defn very-long-function-name-that-exceeds-eighty-characters-and-should-be-wrapped-properly [arg1 arg2 arg3 arg4]
  \"This is a very long docstring that also exceeds eighty characters and should be wrapped properly to maintain readability and consistency\"
  (let [very-long-binding-name-that-exceeds-eighty-characters
        {:key1 \"value1\"
         :key2 \"value2\"
         :key3 \"value3\"
         :key4 \"value4\"}]
    (-> very-long-binding-name-that-exceeds-eighty-characters
        (assoc :new-key \"new-value\")
        (update :existing-key #(str % \"-suffix\"))
        (dissoc :old-key))))"
        
        test-file "test-formatting/test.clj"]
    
    (create-test-file test-content test-file)
    
    (let [before-lines (count (str/split-lines (slurp test-file)))
          before-long-lines (count (filter #(> (count %) 80) (str/split-lines (slurp test-file))))]
      
      (println "  Before formatting:")
      (println "    • Total lines:" before-lines)
      (println "    • Long lines (>80 chars):" before-long-lines)
      
      (shell "bb" "scripts/reformat-clojure.bb" test-file)
      
      (let [after-lines (count (str/split-lines (slurp test-file)))
            after-long-lines (count (filter #(> (count %) 80) (str/split-lines (slurp test-file))))]
        
        (println "  After formatting:")
        (println "    • Total lines:" after-lines)
        (println "    • Long lines (>80 chars):" after-long-lines)
        
        (if (< after-long-lines before-long-lines)
          (println "  ✅ Clojure formatting test passed!")
          (println "  ❌ Clojure formatting test failed!"))))))

(defn test-edn-formatting []
  "Test EDN file formatting"
  (println "🧪 Testing EDN file formatting...")
  
  (let [test-content "{:very-long-key-that-exceeds-eighty-characters-and-should-be-wrapped
  {:nested-key-1 \"very-long-value-that-exceeds-eighty-characters-and-should-be-wrapped\"
   :nested-key-2 \"another-very-long-value-that-exceeds-eighty-characters-and-should-be-wrapped\"
   :nested-key-3 \"yet-another-very-long-value-that-exceeds-eighty-characters-and-should-be-wrapped\"}
 :another-very-long-key-that-exceeds-eighty-characters
 {:key1 \"value1\"
  :key2 \"value2\"
  :key3 \"value3\"}}"
        
        test-file "test-formatting/test.edn"]
    
    (create-test-file test-content test-file)
    
    (let [before-long-lines (count (filter #(> (count %) 80) (str/split-lines (slurp test-file))))]
      
      (println "  Before formatting:")
      (println "    • Long lines (>80 chars):" before-long-lines)
      
      (shell "bb" "scripts/reformat-clojure.bb" test-file)
      
      (let [after-long-lines (count (filter #(> (count %) 80) (str/split-lines (slurp test-file))))]
        
        (println "  After formatting:")
        (println "    • Long lines (>80 chars):" after-long-lines)
        
        (if (< after-long-lines before-long-lines)
          (println "  ✅ EDN formatting test passed!")
          (println "  ❌ EDN formatting test failed!"))))))

(defn test-markdown-code-blocks []
  "Test markdown code block formatting"
  (println "🧪 Testing markdown code block formatting...")
  
  (let [test-content "# Test Document

This is a test document with Clojure code blocks.

```clojure
(defn very-long-function-name-that-exceeds-eighty-characters-and-should-be-wrapped-properly [arg1 arg2 arg3 arg4]
  \"This is a very long docstring that also exceeds eighty characters and should be wrapped properly to maintain readability and consistency\"
  (let [very-long-binding-name-that-exceeds-eighty-characters
        {:key1 \"value1\"
         :key2 \"value2\"
         :key3 \"value3\"
         :key4 \"value4\"}]
    (-> very-long-binding-name-that-exceeds-eighty-characters
        (assoc :new-key \"new-value\")
        (update :existing-key #(str % \"-suffix\"))
        (dissoc :old-key))))
```

```edn
{:very-long-key-that-exceeds-eighty-characters-and-should-be-wrapped
 {:nested-key-1 \"very-long-value-that-exceeds-eighty-characters-and-should-be-wrapped\"
  :nested-key-2 \"another-very-long-value-that-exceeds-eighty-characters-and-should-be-wrapped\"}}
```

```bb
(defn very-long-function-name-that-exceeds-eighty-characters-and-should-be-wrapped-properly [arg1 arg2 arg3 arg4]
  \"This is a very long docstring that also exceeds eighty characters and should be wrapped properly to maintain readability and consistency\"
  (let [very-long-binding-name-that-exceeds-eighty-characters
        {:key1 \"value1\"
         :key2 \"value2\"
         :key3 \"value3\"
         :key4 \"value4\"}]
    (-> very-long-binding-name-that-exceeds-eighty-characters
        (assoc :new-key \"new-value\")
        (update :existing-key #(str % \"-suffix\"))
        (dissoc :old-key))))
```

End of document."
        
        test-file "test-formatting.md"]
    
    (create-test-file test-content test-file)
    
    (let [before-long-lines (count (filter #(> (count %) 80) (str/split-lines (slurp test-file))))]
      
      (println "  Before formatting:")
      (println "    • Long lines (>80 chars):" before-long-lines)
      
      (shell "bb" "scripts/reformat-clojure.bb" test-file)
      
      (let [after-long-lines (count (filter #(> (count %) 80) (str/split-lines (slurp test-file))))]
        
        (println "  After formatting:")
        (println "    • Long lines (>80 chars):" after-long-lines)
        
        (if (< after-long-lines before-long-lines)
          (println "  ✅ Markdown code block formatting test passed!")
          (println "  ❌ Markdown code block formatting test failed!"))))))

(defn run-all-tests []
  "Run all formatting tests"
  (println "🧪 Running Clojure formatting tests...")
  (println)
  
  (try
    (test-clojure-formatting)
    (println)
    (test-edn-formatting)
    (println)
    (test-markdown-code-blocks)
    (println)
    (println "✨ All formatting tests completed!")
    (finally
      (cleanup-test-files))))

(defn main []
  (if (empty? *command-line-args*)
    (run-all-tests)
    (let [test-type (first *command-line-args*)]
      (case test-type
        "clojure" (test-clojure-formatting)
        "edn" (test-edn-formatting)
        "markdown" (test-markdown-code-blocks)
        "all" (run-all-tests)
        (do
          (println "❌ Unknown test type:" test-type)
          (println "Available tests: clojure, edn, markdown, all"))))))

(when (= *file* (System/getProperty "babashka.file"))
  (main))
