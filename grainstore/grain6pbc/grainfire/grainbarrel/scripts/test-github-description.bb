#!/usr/bin/env bb

(require '[clojure.string :as str]
         '[clojure.java.shell :as shell])

(defn test-github-description-update
  "Test updating GitHub repository description with existing course"
  []
  (println "🧪 Testing GitHub Description Update")
  (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
  (println "")
  
  (try
    (load-file "/home/xy/kae3g/grainkae3g/grainstore/grainpbc/grainsync/src/grainsync/github.clj")
    (require '[grainsync.github :as github])
    
    (let [grainpath "12025-10-23--0222--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/test-course"
          course-title "Test Course"
          pages-url "https://kae3g.github.io/grainkae3g/12025-10-23--0222--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/test-course/"]
      
      (println "📝 Updating repository description...")
      (println "   Course: " course-title)
      (println "   Grainpath: " grainpath)
      (println "   Pages URL: " pages-url)
      (println "")
      
      (if (github/update-repo-description! grainpath course-title pages-url)
        (println "✅ Repository description updated successfully!")
        (println "❌ Failed to update repository description")))
    
    (catch Exception e
      (println "❌ Error testing GitHub description update:")
      (println "   " (.getMessage e))
      (println "")
      (println "💡 Make sure you have a GitHub token configured:")
      (println "   bb github:setup-token"))))

;; Run the test
(test-github-description-update)
