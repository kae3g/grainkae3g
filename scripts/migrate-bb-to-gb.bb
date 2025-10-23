#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[clojure.string :as str]
         '[babashka.process :refer [shell]])

(def dry-run (atom true))

(defn find-bb-files []
  "Find all .bb files in the repository"
  (filter #(str/ends-with? (str %) ".bb")
          (fs/glob "." "**/*.bb")))

(defn rename-bb-to-gb [bb-file]
  "Rename a .bb file to .gb"
  (let [bb-path (str bb-file)
        gb-path (str/replace bb-path #"\.bb$" ".gb")]
    (if @dry-run
      (println (str "  Would rename: " bb-path " → " gb-path))
      (do
        (fs/move bb-file gb-path)
        (println (str "  ✅ Renamed: " bb-path " → " gb-path))))
    {:old bb-path :new gb-path}))

(defn find-files-referencing-bb []
  "Find files that reference .bb files"
  (concat
   ;; Find all bb.edn files (might reference .bb scripts)
   (fs/glob "." "**/bb.edn")
   
   ;; Find all markdown files (might have .bb in examples)
   (fs/glob "." "**/*.md")
   
   ;; Find shell scripts
   (fs/glob "." "**/*.sh")))

(defn update-file-references [file-path renames]
  "Update .bb references to .gb in a file"
  (let [content (slurp (str file-path))
        updated (reduce (fn [text {:keys [old new]}]
                         (let [old-name (fs/file-name old)
                               new-name (fs/file-name new)]
                           (-> text
                               (str/replace old-name new-name)
                               (str/replace (str "\"" old "\"") (str "\"" new "\""))
                               (str/replace (str "'" old "'") (str "'" new "'")))))
                       content
                       renames)]
    
    (when-not (= content updated)
      (if @dry-run
        (println (str "  Would update references in: " file-path))
        (do
          (spit (str file-path) updated)
          (println (str "  ✅ Updated references in: " file-path)))))))

(defn rename-bb-edn-files []
  "Rename bb.edn files to gb.edn"
  (let [bb-edn-files (fs/glob "." "**/bb.edn")]
    (doseq [bb-edn bb-edn-files]
      (let [parent-dir (fs/parent bb-edn)
            gb-edn (fs/path parent-dir "gb.edn")]
        (if @dry-run
          (println (str "  Would rename: " bb-edn " → " gb-edn))
          (do
            (fs/move bb-edn gb-edn)
            (println (str "  ✅ Renamed: " bb-edn " → " gb-edn))))))))

(defn update-shebang-lines [gb-files]
  "Update shebang lines from #!/usr/bin/env bb to #!/usr/bin/env gb"
  (doseq [gb-file gb-files]
    (let [content (slurp (str gb-file))
          updated (str/replace content #"#!/usr/bin/env bb\b" "#!/usr/bin/env gb")]
      (when-not (= content updated)
        (if @dry-run
          (println (str "  Would update shebang in: " gb-file))
          (do
            (spit (str gb-file) updated)
            (println (str "  ✅ Updated shebang in: " gb-file))))))))

(defn main [& args]
  (let [mode (first args)]
    
    (println "🌾 Grainbarrel Migration: .bb → .gb\n")
    
    (when (= mode "--execute")
      (reset! dry-run false)
      (println "⚠️  EXECUTING MIGRATION (not a dry run!)\n"))
    
    (when @dry-run
      (println "🔍 DRY RUN MODE (use --execute to actually migrate)\n"))
    
    ;; Step 1: Find all .bb files
    (let [bb-files (find-bb-files)]
      (println (str "📋 Found " (count bb-files) " .bb files:\n"))
      
      ;; Step 2: Rename .bb → .gb
      (println "🔄 Step 1: Renaming .bb files to .gb\n")
      (let [renames (mapv rename-bb-to-gb bb-files)]
        (println)
        
        ;; Step 3: Update references in other files
        (println "🔄 Step 2: Updating references in other files\n")
        (let [ref-files (find-files-referencing-bb)]
          (println (str "  Checking " (count ref-files) " files for references..."))
          (doseq [file ref-files]
            (update-file-references file renames)))
        (println)
        
        ;; Step 4: Rename bb.edn → gb.edn
        (println "🔄 Step 3: Renaming bb.edn files to gb.edn\n")
        (rename-bb-edn-files)
        (println)
        
        ;; Step 5: Update shebangs
        (println "🔄 Step 4: Updating shebang lines\n")
        (let [gb-files (map (fn [{:keys [new]}] new) renames)]
          (update-shebang-lines (map io/file gb-files)))
        (println)))
    
    ;; Summary
    (println "\n" (str (if @dry-run "📋" "✅") " Migration " (if @dry-run "preview" "complete") "!"))
    
    (when @dry-run
      (println "\n💡 To execute the migration, run:")
      (println "   bb scripts/migrate-bb-to-gb.bb --execute")
      (println "\n⚠️  This will:")
      (println "   1. Rename all .bb files to .gb")
      (println "   2. Rename all bb.edn files to gb.edn")
      (println "   3. Update all references in markdown and config files")
      (println "   4. Update shebang lines (#!/usr/bin/env bb → #!/usr/bin/env gb)")
      (println "\n📸 Recommended: Commit your work before running!"))))

(apply main *command-line-args*)

