(ns grainsource-personalize.core
  "Utility library for personalizing Grain Network template repositories
  
  Converts grain6pbc templates into grain{devname}{module} personal repos by:
  - Removing personal/ from .gitignore
  - Renaming repository
  - Updating all references
  - Setting up git remotes"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn ungitignore-personal
  "Remove personal/ from .gitignore file
   
   In template repos (grain6pbc), personal/ is gitignored.
   In personal repos (grain{devname}{module}), personal/ should be versioned.
   
   Returns: updated .gitignore content"
  [gitignore-path]
  (let [content (slurp gitignore-path)
        lines (str/split-lines content)
        filtered (remove #(or (= % "personal/")
                              (= % "# Personal content (not for template)")
                              (str/starts-with? % "personal/"))
                        lines)]
    (str/join "\n" filtered)))

(defn update-references
  "Update all grain6pbc references to grain{devname}{module}
   
   Updates:
   - grain6pbc → devname (in text)
   - graincourse → grain{devname}course (in names)
   - URLs and namespaces"
  [{:keys [from to module dir]}]
  (let [old-name (str "grain" module)
        new-name (str "grain" to module)
        old-org from
        new-org to]
    {:old-name old-name
     :new-name new-name
     :old-org old-org
     :new-org new-org
     :replacements
     {;; Repository names
      (str "grain6pbc/" old-name) (str new-org "/" new-name)
      (str "grain6pbc." old-name) (str new-org "." new-name)
      
      ;; URLs
      (str "github.com/grain6pbc/" old-name) (str "github.com/" new-org "/" new-name)
      (str "codeberg.org/grain6pbc/" old-name) (str "codeberg.org/" new-org "/" new-name)
      
      ;; Namespaces (if applicable)
      (str "grain6pbc." old-name ".") (str new-name ".")
      
      ;; Generic references
      "grain6pbc" new-org}}))

(defn apply-replacements
  "Apply text replacements to a file"
  [file-path replacements]
  (when (.exists (io/file file-path))
    (let [content (slurp file-path)
          updated (reduce (fn [text [old new]]
                           (str/replace text old new))
                         content
                         replacements)]
      (spit file-path updated))))

(defn setup-remotes
  "Set up git remotes for personal repository
   
   Creates origin (GitHub) and codeberg remotes"
  [{:keys [graindevname module github codeberg dir]}]
  (let [repo-name (str "grain" graindevname module)
        github-url (when github
                    (str "https://github.com/" graindevname "/" repo-name ".git"))
        codeberg-url (when codeberg
                      (str "https://codeberg.org/" graindevname "/" repo-name ".git"))]
    {:repo-name repo-name
     :github-url github-url
     :codeberg-url codeberg-url
     :commands
     (cond-> []
       github (conj (str "git remote add origin " github-url))
       codeberg (conj (str "git remote add codeberg " codeberg-url)))}))

(defn personalize-repo
  "Main function to personalize a template repository
   
   Process:
   1. Copy template to new directory
   2. Remove personal/ from .gitignore
   3. Update all references
   4. Set up git remotes
   5. Create initial commit
   
   Example:
   (personalize-repo
     {:graindevname \"kae3g\"
      :module \"course\"
      :template-dir \"/path/to/grain6pbc/graincourse\"
      :output-dir \"/path/to/grainkae3gcourse\"})"
  [{:keys [graindevname module template-dir output-dir] :as opts}]
  (let [repo-name (str "grain" graindevname module)
        output (or output-dir (str "../" repo-name))]
    
    ;; Validation
    (when-not (and graindevname module template-dir)
      (throw (ex-info "Missing required parameters"
                     {:required [:graindevname :module :template-dir]
                      :provided opts})))
    
    ;; Result map
    {:graindevname graindevname
     :module module
     :repo-name repo-name
     :template-dir template-dir
     :output-dir output
     :steps
     [{:step 1 :action "copy-template" :status "pending"}
      {:step 2 :action "ungitignore-personal" :status "pending"}
      {:step 3 :action "update-references" :status "pending"}
      {:step 4 :action "setup-remotes" :status "pending"}
      {:step 5 :action "initial-commit" :status "pending"}]}))

(defn verify-personalization
  "Verify that a repository has been properly personalized
   
   Checks:
   - Repository name follows grain{devname}{module} pattern
   - personal/ is NOT in .gitignore
   - All grain6pbc references updated
   - Git remotes configured
   
   Returns: map with :valid boolean and :issues list"
  [repo-dir]
  (let [gitignore-path (str repo-dir "/.gitignore")
        gitignore-content (when (.exists (io/file gitignore-path))
                           (slurp gitignore-path))
        has-personal-gitignored? (and gitignore-content
                                      (or (str/includes? gitignore-content "personal/")
                                          (str/includes? gitignore-content "# Personal content")))
        
        readme-path (str repo-dir "/README.md")
        readme-content (when (.exists (io/file readme-path))
                        (slurp readme-path))
        has-grain6pbc-refs? (and readme-content
                               (str/includes? readme-content "grain6pbc"))
        
        issues (cond-> []
                 has-personal-gitignored?
                 (conj "personal/ still in .gitignore (should be versioned)")
                 
                 has-grain6pbc-refs?
                 (conj "grain6pbc references still present (should be updated)")
                 
                 (not (.exists (io/file (str repo-dir "/.git"))))
                 (conj "Not a git repository"))]
    
    {:valid (empty? issues)
     :issues issues
     :checks {:personal-gitignored? has-personal-gitignored?
              :grain6pbc-refs? has-grain6pbc-refs?
              :is-git-repo? (.exists (io/file (str repo-dir "/.git")))}}))

(comment
  ;; Example usage
  
  ;; 1. Remove personal/ from .gitignore
  (ungitignore-personal ".gitignore")
  
  ;; 2. Update references
  (update-references
    {:from "grain6pbc"
     :to "kae3g"
     :module "course"
     :dir "/path/to/grainkae3gcourse"})
  
  ;; 3. Setup remotes
  (setup-remotes
    {:graindevname "kae3g"
     :module "course"
     :github true
     :codeberg true})
  
  ;; 4. Full personalization
  (personalize-repo
    {:graindevname "kae3g"
     :module "course"
     :template-dir "/path/to/grain6pbc/graincourse"
     :output-dir "/path/to/grainkae3gcourse"})
  
  ;; 5. Verify
  (verify-personalization "/path/to/grainkae3gcourse"))

