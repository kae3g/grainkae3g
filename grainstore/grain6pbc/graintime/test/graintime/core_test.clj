(ns graintime.core-test
  "Tests for graintime.core - character limits, validation, and grainpath generation"
  (:require [clojure.test :refer [deftest is testing]]))

;; Load dependencies for testing
(load-file "src/graintime/location_dialog.clj")
(load-file "src/graintime/sunset.clj")
(load-file "src/graintime/solar_houses.clj")
(load-file "src/graintime/astromitra.clj")
(load-file "src/graintime/core.clj")

;; Alias for convenience
(def gt (resolve 'graintime.core))

(deftest test-graintime-character-limit
  (testing "Graintime should enforce 80-character maximum"
    (let [gen-fn (resolve 'graintime.core/generate-graintime-with-validation)
          validate-fn (resolve 'graintime.core/validate-graintime-length)
          graintime (gen-fn "system")
          validation (validate-fn graintime)]
      (is (:valid validation) "Graintime should be valid")
      (is (<= (:length validation) 80) "Graintime should be <= 80 chars")
      (is (string? graintime) "Graintime should be a string"))))

(deftest test-grainpath-character-limit
  (testing "Grainpath should enforce 100-character maximum"
    (let [grainpath-fn (resolve 'graintime.core/generate-grainpath-with-username)
          validate-fn (resolve 'graintime.core/validate-grainpath-length)
          grainpath (grainpath-fn "course" "kae3g" "test")
          validation (validate-fn grainpath)]
      (is (:valid validation) "Grainpath should be valid")
      (is (<= (:length validation) 100) "Grainpath should be <= 100 chars"))))

(deftest test-grainpath-with-long-name
  (testing "Grainpath should auto-shorten long course names"
    (let [long-name "very-long-course-name-that-exceeds-reasonable-limits-for-url-paths"
          grainpath-fn (resolve 'graintime.core/generate-grainpath-with-username)
          validate-fn (resolve 'graintime.core/validate-grainpath-length)
          grainpath (grainpath-fn "course" "kae3g" long-name)
          validation (validate-fn grainpath)]
      (is (:valid validation) "Even with long names, grainpath should be valid")
      (is (<= (:length validation) 100) "Long name should be auto-shortened"))))

(deftest test-username-decoupling
  (testing "Username should be decoupled from graintime"
    (let [grainpath-1 (gt/generate-grainpath-with-username "course" "kae3g" "test")
          grainpath-2 (gt/generate-grainpath-with-username "course" "jen3g" "test")]
      ;; Both should have the same structure but different usernames
      (is (clojure.string/includes? grainpath-1 "kae3g") "Should include kae3g")
      (is (clojure.string/includes? grainpath-2 "jen3g") "Should include jen3g")
      ;; Username should be a suffix in the path
      (is (clojure.string/starts-with? grainpath-1 "/course/kae3g/") "Username in path")
      (is (clojure.string/starts-with? grainpath-2 "/course/jen3g/") "Username in path"))))

(deftest test-graintime-format
  (testing "Graintime should follow expected format"
    (let [graintime (gt/generate-graintime-with-validation "system")]
      ;; Should start with Holocene year (12025)
      (is (clojure.string/starts-with? graintime "12025-") "Should start with 12025-")
      ;; Should contain moon nakshatra
      (is (re-find #"moon-\w+" graintime) "Should contain moon-nakshatra")
      ;; Should contain ascendant
      (is (re-find #"asc-\w+\d{3}" graintime) "Should contain asc with 3-digit degree")
      ;; Should contain solar house
      (is (re-find #"sun-\d{2}thhouse" graintime) "Should contain solar house"))))

(deftest test-grainpath-structure
  (testing "Grainpath should follow /{type}/{username}/{name}/{graintime}/ format"
    (let [grainpath (gt/generate-grainpath-with-username "course" "kae3g" "fundamentals")]
      (is (clojure.string/starts-with? grainpath "/") "Should start with /")
      (is (clojure.string/ends-with? grainpath "/") "Should end with /")
      (is (= 5 (count (clojure.string/split grainpath #"/"))) "Should have 4 path segments + empty"))))

(deftest test-validation-messages
  (testing "Validation should provide helpful messages"
    (let [valid-graintime (gt/generate-graintime-with-validation "system")
          validation (gt/validate-graintime-length valid-graintime)]
      (is (= "OK" (:message validation)) "Valid graintime should say OK"))))

(deftest test-short-graintime-generation
  (testing "Short graintime should be generated when needed"
    (let [short-gt (gt/generate-short-graintime "system")]
      (is (string? short-gt) "Short graintime should be a string")
      (is (< (count short-gt) 80) "Short graintime should be under 80 chars"))))

(deftest test-parse-graintime
  (testing "Should be able to parse graintime components"
    (let [graintime "12025-10-23--2310--PDT--moon-vishakha--asc-gemini022--sun-05thhouse--system"
          parsed (gt/parse-graintime graintime)]
      (is (map? parsed) "Should return a map")
      (is (= "12025" (:year parsed)) "Should extract year")
      (is (= "vishakha" (:nakshatra parsed)) "Should extract nakshatra")
      (is (= "gemini" (:asc-sign parsed)) "Should extract ascendant sign"))))

(deftest test-multiple-path-types
  (testing "Should support different path types"
    (let [course-path (gt/generate-grainpath-with-username "course" "kae3g" "test")
          module-path (gt/generate-grainpath-with-username "module" "kae3g" "test")
          project-path (gt/generate-grainpath-with-username "project" "kae3g" "test")]
      (is (clojure.string/starts-with? course-path "/course/") "Course path")
      (is (clojure.string/starts-with? module-path "/module/") "Module path")
      (is (clojure.string/starts-with? project-path "/project/") "Project path"))))

(deftest test-immutable-grainpath
  (testing "Graintime component makes grainpath unique and immutable"
    (let [path1 (gt/generate-grainpath-with-username "course" "kae3g" "test")
          ;; Wait a moment
          _ (Thread/sleep 100)
          path2 (gt/generate-grainpath-with-username "course" "kae3g" "test")]
      ;; Paths should be different (different timestamps)
      (is (not= path1 path2) "Different timestamps create unique paths")
      ;; But both should be valid
      (is (:valid (gt/validate-grainpath-length path1)) "Path 1 valid")
      (is (:valid (gt/validate-grainpath-length path2)) "Path 2 valid"))))

(deftest test-graindevname-convention
  (testing "Should work with 5-character graindevname convention"
    (let [usernames ["kae3g" "jen3g" "sam1x" "alex7" "zo9rd"]
          paths (map #(gt/generate-grainpath-with-username "course" % "test") usernames)]
      (doseq [path paths]
        (is (:valid (gt/validate-grainpath-length path)) "All paths should be valid"))
      ;; Each should contain its username
      (doseq [[username path] (map vector usernames paths)]
        (is (clojure.string/includes? path username) (str "Should include " username))))))

;; Run all tests
(defn run-tests []
  (clojure.test/run-tests 'graintime.core-test))

(comment
  ;; Run tests manually
  (run-tests)
  
  ;; Test individual functions
  (test-graintime-character-limit)
  (test-grainpath-character-limit)
  (test-username-decoupling)
  )
