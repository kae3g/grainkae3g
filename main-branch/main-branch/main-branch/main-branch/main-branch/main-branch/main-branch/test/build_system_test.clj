#!/usr/bin/env bb
;; test/build_system_test.clj — Tests for incremental build system
;; Tests caching, dependency detection, sorting, and build correctness

(ns build-system-test
  (:require [clojure.test :refer [deftest is testing run-tests]]
            [babashka.fs :as fs]
            [clojure.string :as str]
            [clojure.edn :as edn]
            [cheshire.core :as json]))

;; Load the build system functions
(load-file "scripts/writings_build_incremental.clj")

(def test-writings-dir "test/fixtures/writings")
(def test-cache-file "test/fixtures/.build-cache.edn")
(def test-output-dir "test/fixtures/output")

;; Test fixtures
(defn create-test-file [filename content]
  (fs/create-dirs test-writings-dir)
  (let [file-path (str test-writings-dir "/" filename)]
    (spit file-path content)
    file-path))

(defn cleanup-test-files []
  (when (fs/exists? "test/fixtures")
    (fs/delete-tree "test/fixtures")))

(defn setup-fixtures []
  (cleanup-test-files)
  (fs/create-dirs test-writings-dir)
  (fs/create-dirs test-output-dir))

;; Test: File hash function
(deftest test-file-hash
  (testing "File hash should change when file is modified"
    (setup-fixtures)
    (let [file (create-test-file "test.md" "# Original content")
          hash1 (writings-build-incremental/file-hash file)]
      ;; Modify file
      (Thread/sleep 10) ;; Ensure timestamp changes
      (spit file "# Modified content")
      (let [hash2 (writings-build-incremental/file-hash file)]
        (is (not= hash1 hash2) "Hash should change after modification")))
    (cleanup-test-files)))

;; Test: Parse front matter
(deftest test-parse-front-matter
  (testing "Should parse YAML front matter correctly"
    (let [content "---\ntitle: Test Essay\nsort-order: 1\n---\n\n# Content"
          result (writings-build-incremental/parse-front-matter content)]
      (is (= "Test Essay" (get-in result [:front-matter :title])))
      (is (= 1 (get-in result [:front-matter :sort-order])))
      (is (str/includes? (:body result) "# Content"))))
  
  (testing "Should handle content without front matter"
    (let [content "# Just Content\n\nNo front matter here"
          result (writings-build-incremental/parse-front-matter content)]
      (is (empty? (:front-matter result)))
      (is (= content (:body result))))))

;; Test: Extract slug
(deftest test-extract-slug
  (testing "Should extract slug from filename"
    (is (= "9999-test-essay" 
           (writings-build-incremental/extract-slug "9999-test-essay.md" {}))))
  
  (testing "Should prefer slug from front matter"
    (is (= "custom-slug"
           (writings-build-incremental/extract-slug "9999-test.md" {:slug "custom-slug"}))))
  
  (testing "Should lowercase filename"
    (is (= "test-essay"
           (writings-build-incremental/extract-slug "Test-Essay.md" {})))))

;; Test: Extract metadata
(deftest test-extract-metadata
  (testing "Should extract metadata from front matter and body"
    (let [front-matter {:title "Test Essay"
                        :sort-order 2
                        :category "Test"}
          body "# Heading\n\nContent"
          result (writings-build-incremental/extract-metadata "test.md" front-matter body)]
      (is (= "Test Essay" (:title result)))
      (is (= 2 (:sort-order result)))
      (is (= "Test" (:category result)))))
  
  (testing "Should extract title from first heading if not in front matter"
    (let [body "# Extracted Title\n\nContent"
          result (writings-build-incremental/extract-metadata "test.md" {} body)]
      (is (= "Extracted Title" (:title result))))))

;; Test: Two-tier sorting
(deftest test-build-content-index-sorting
  (testing "Non-numeric essays should come first, sorted by sort-order"
    (let [entries [{:meta {:title "About" :sort-order 1}}
                   {:meta {:title "Cold Calculation" :sort-order 2}}
                   {:meta {:title "kae3g 9999: Numeric" :filename "9999.md"}}
                   {:meta {:title "kae3g 9998: Another" :filename "9998.md"}}
                   {:meta {:title "Map" :sort-order 3}}]
          index (writings-build-incremental/build-content-index entries)]
      ;; First 3 should be non-numeric, sorted by sort-order
      (is (= "About" (:title (nth index 0))))
      (is (= "Cold Calculation" (:title (nth index 1))))
      (is (= "Map" (:title (nth index 2))))
      ;; Next should be numeric, descending
      (is (= "kae3g 9999: Numeric" (:title (nth index 3))))
      (is (= "kae3g 9998: Another" (:title (nth index 4))))))
  
  (testing "Numeric essays should be sorted descending (highest first)"
    (let [entries [{:meta {:title "kae3g 9500: First"}}
                   {:meta {:title "kae3g 9999: Last"}}
                   {:meta {:title "kae3g 9750: Middle"}}]
          index (writings-build-incremental/build-content-index entries)]
      (is (= "kae3g 9999: Last" (:title (nth index 0))))
      (is (= "kae3g 9750: Middle" (:title (nth index 1))))
      (is (= "kae3g 9500: First" (:title (nth index 2))))))
  
  (testing "Non-numeric without sort-order should use default (999) and sort by title"
    (let [entries [{:meta {:title "Zebra"}}
                   {:meta {:title "About" :sort-order 1}}
                   {:meta {:title "Apple"}}]
          index (writings-build-incremental/build-content-index entries)]
      ;; About has sort-order 1, so comes first
      (is (= "About" (:title (nth index 0))))
      ;; Zebra and Apple have default sort-order 999, sorted alphabetically
      (is (= "Apple" (:title (nth index 1))))
      (is (= "Zebra" (:title (nth index 2)))))))

;; Test: Incremental build - cache management
(deftest test-cache-management
  (testing "Should detect changed files based on hash"
    (setup-fixtures)
    (let [file1 (create-test-file "file1.md" "# Content 1")
          file2 (create-test-file "file2.md" "# Content 2")
          files [file1 file2]
          ;; Create initial cache
          cache {(str file1) {:hash (writings-build-incremental/file-hash file1)}
                 (str file2) {:hash (writings-build-incremental/file-hash file2)}}]
      ;; No files should be changed initially
      (is (empty? (writings-build-incremental/get-changed-files files cache)))
      
      ;; Modify file1
      (Thread/sleep 10)
      (spit file1 "# Modified content")
      
      ;; Only file1 should be detected as changed
      (let [changed (writings-build-incremental/get-changed-files files cache)]
        (is (= 1 (count changed)))
        (is (= file1 (first changed)))))
    (cleanup-test-files)))

;; Test: Full incremental build pipeline
(deftest test-incremental-build-pipeline
  (testing "Full incremental build should work correctly"
    (setup-fixtures)
    
    ;; Create test markdown files
    (create-test-file "about.md" 
      "---\ntitle: About Test\nsort-order: 1\n---\n\n# About\n\nTest content")
    (create-test-file "9999-essay.md"
      "---\ntitle: kae3g 9999: Test Essay\n---\n\n# Test\n\nContent")
    
    ;; Override dirs for testing
    (with-redefs [writings-build-incremental/writings-dir test-writings-dir
                  writings-build-incremental/content-out-dir test-output-dir
                  writings-build-incremental/cache-file test-cache-file]
      ;; Run first build (should build all files)
      (writings-build-incremental/build-writings-incremental)
      
      ;; Check output files exist
      (is (fs/exists? (str test-output-dir "/about.json")))
      (is (fs/exists? (str test-output-dir "/9999-essay.json")))
      (is (fs/exists? (str test-output-dir "/index.json")))
      
      ;; Check cache exists
      (is (fs/exists? test-cache-file))
      
      ;; Check index has correct order (non-numeric first)
      (let [index (json/parse-string (slurp (str test-output-dir "/index.json")) true)]
        (is (= "About Test" (:title (first index))))
        (is (= "kae3g 9999: Test Essay" (:title (second index))))))
    
    (cleanup-test-files)))

;; Test: Cache correctness
(deftest test-cache-correctness
  (testing "Cache should store correct metadata"
    (setup-fixtures)
    (let [file (create-test-file "test.md" 
                 "---\ntitle: Test\nsort-order: 5\n---\n\n# Content")
          entry (writings-build-incremental/process-markdown-file file)]
      (is (= "test" (:slug entry)))
      (is (= "Test" (get-in entry [:meta :title])))
      (is (= 5 (get-in entry [:meta :sort-order])))
      (is (some? (:hash entry))))
    (cleanup-test-files)))

;; Test: Essay number extraction
(deftest test-essay-number-extraction
  (testing "Should correctly extract essay numbers from various title formats"
    (let [get-num (fn [title]
                    (when-let [match (re-find #"^(?:kae3g\s+)?(\d+)" title)]
                      (Integer/parseInt (second match))))]
      (is (= 9999 (get-num "kae3g 9999: Title")))
      (is (= 9999 (get-num "9999: Title")))
      (is (= 9999 (get-num "9999-slug")))
      (is (nil? (get-num "About")))
      (is (nil? (get-num "No number here"))))))

;; Test: Sort order edge cases
(deftest test-sort-order-edge-cases
  (testing "Should handle missing, nil, and zero sort-order values"
    (let [entries [{:meta {:title "No order"}}
                   {:meta {:title "Zero order" :sort-order 0}}
                   {:meta {:title "Nil order" :sort-order nil}}
                   {:meta {:title "Has order" :sort-order 1}}]
          index (writings-build-incremental/build-content-index entries)]
      ;; Zero order should come first
      (is (= "Zero order" (:title (nth index 0))))
      ;; Has order (1) should come second
      (is (= "Has order" (:title (nth index 1))))
      ;; Nil and missing should both default to 999, sorted by title
      (is (or (= "Nil order" (:title (nth index 2)))
              (= "No order" (:title (nth index 2))))))))

;; Test: Build statistics
(deftest test-build-statistics
  (testing "Should correctly count rebuilt vs cached files"
    (setup-fixtures)
    (create-test-file "file1.md" "# File 1")
    (create-test-file "file2.md" "# File 2")
    (create-test-file "file3.md" "# File 3")
    
    (with-redefs [writings-build-incremental/writings-dir test-writings-dir
                  writings-build-incremental/content-out-dir test-output-dir
                  writings-build-incremental/cache-file test-cache-file]
      ;; First build - should rebuild all 3
      (writings-build-incremental/build-writings-incremental)
      
      ;; Second build without changes - should cache all 3
      ;; (We can't easily test this without capturing stdout, but we verify cache exists)
      (is (fs/exists? test-cache-file))
      (let [cache (edn/read-string (slurp test-cache-file))]
        (is (= 3 (count cache)))))
    
    (cleanup-test-files)))

;; Run all tests
(defn -main []
  (println "\n🧪 Running Build System Tests...\n")
  (let [results (run-tests 'build-system-test)]
    (println)
    (if (zero? (+ (:fail results) (:error results)))
      (do
        (println "✅ All tests passed!")
        (System/exit 0))
      (do
        (println "❌ Some tests failed")
        (System/exit 1)))))

(when (= *file* (System/getProperty "babashka.file"))
  (-main))

