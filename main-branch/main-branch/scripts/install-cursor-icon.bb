#!/usr/bin/env bb

(require '[babashka.process :refer [shell sh]]
         '[babashka.fs :as fs]
         '[clojure.string :as str])

(def home (System/getenv "HOME"))
(def icon-source "assets/icons/cursor-grain.svg")
(def icon-sizes [512 256 128 64 48 32 16])

(defn svg-to-png
  "Convert SVG to PNG at specified size using ImageMagick or Inkscape"
  [svg-file png-file size]
  (if (fs/which "convert")
    ;; Use ImageMagick
    (sh "convert" "-background" "none" "-resize" (str size "x" size) svg-file png-file)
    ;; Try Inkscape
    (if (fs/which "inkscape")
      (sh "inkscape" "--export-type=png" (str "--export-width=" size) "--export-filename" png-file svg-file)
      (throw (Exception. "Neither ImageMagick nor Inkscape found. Please install one of them.")))))

(defn generate-icons
  "Generate PNG icons at various sizes from SVG source"
  []
  (println "🎨 Generating PNG icons from SVG...")
  (doseq [size icon-sizes]
    (let [output-file (str "assets/icons/cursor-grain-" size ".png")]
      (println (str "  Creating " size "x" size " icon..."))
      (svg-to-png icon-source output-file size)
      (println (str "  ✅ " output-file)))))

(defn install-icon
  "Install icon for current user"
  []
  (println "\n📥 Installing Cursor icon...")
  
  ;; Create icon directory
  (let [icon-dir (str home "/.local/share/icons")]
    (fs/create-dirs icon-dir)
    
    ;; Copy 512px icon as main icon
    (let [source "assets/icons/cursor-grain-512.png"
          dest (str icon-dir "/cursor-grain.png")]
      (fs/copy source dest {:replace-existing true})
      (println (str "  ✅ Installed icon to " dest))))
  
  ;; Create/update desktop entry
  (let [apps-dir (str home "/.local/share/applications")]
    (fs/create-dirs apps-dir)
    
    ;; Check if Cursor desktop file exists
    (let [system-desktop "/usr/share/applications/cursor.desktop"
          user-desktop (str apps-dir "/cursor.desktop")]
      
      (if (fs/exists? system-desktop)
        (do
          (println "\n📝 Creating custom desktop entry...")
          ;; Read system desktop file
          (let [content (slurp system-desktop)
                ;; Replace Icon line
                new-content (str/replace content
                                        #"Icon=.*"
                                        (str "Icon=" home "/.local/share/icons/cursor-grain.png"))]
            (spit user-desktop new-content)
            (println (str "  ✅ Desktop entry: " user-desktop))))
        (do
          (println "\n⚠️  System Cursor desktop file not found at /usr/share/applications/cursor.desktop")
          (println "  Creating custom desktop entry...")
          (let [desktop-content (str "[Desktop Entry]\n"
                                    "Type=Application\n"
                                    "Name=Cursor\n"
                                    "Comment=Code Editor\n"
                                    "Exec=cursor %F\n"
                                    "Icon=" home "/.local/share/icons/cursor-grain.png\n"
                                    "Terminal=false\n"
                                    "Categories=Development;IDE;\n"
                                    "MimeType=text/plain;\n")]
            (spit (str apps-dir "/cursor.desktop") desktop-content)
            (println (str "  ✅ Desktop entry: " apps-dir "/cursor.desktop")))))))
  
  ;; Update desktop database
  (println "\n🔄 Updating desktop database...")
  (sh "update-desktop-database" (str home "/.local/share/applications"))
  (println "  ✅ Desktop database updated")
  
  ;; Update icon cache
  (println "\n🔄 Updating icon cache...")
  (when (fs/exists? (str home "/.local/share/icons"))
    (sh "gtk-update-icon-cache" "-f" "-t" (str home "/.local/share/icons")))
  (println "  ✅ Icon cache updated")
  
  (println "\n✨ Installation complete!")
  (println "\n📌 Next steps:")
  (println "  1. Close all Cursor windows")
  (println "  2. Press Alt+F2, type 'r', press Enter (to reload GNOME)")
  (println "  3. Reopen Cursor")
  (println "\n🌾 Enjoy your Grain Network-themed Cursor icon!"))

(defn check-dependencies
  "Check if required tools are installed"
  []
  (let [has-convert (fs/which "convert")
        has-inkscape (fs/which "inkscape")]
    (when-not (or has-convert has-inkscape)
      (println "❌ Error: Neither ImageMagick nor Inkscape found")
      (println "\nPlease install one of:")
      (println "  ImageMagick: sudo apt install imagemagick")
      (println "  Inkscape: sudo apt install inkscape")
      (System/exit 1))
    (println (str "✅ Found " (if has-convert "ImageMagick" "Inkscape")))))

(defn main [& args]
  (println "🌾 Cursor Icon Installer - Grain Network Edition\n")
  
  (check-dependencies)
  
  (when-not (fs/exists? icon-source)
    (println (str "❌ Error: SVG source not found: " icon-source))
    (System/exit 1))
  
  (let [command (first args)]
    (case command
      "generate" (generate-icons)
      "install" (install-icon)
      "all" (do (generate-icons) (install-icon))
      (do
        (println "Usage: bb scripts/install-cursor-icon.bb [COMMAND]")
        (println)
        (println "Commands:")
        (println "  generate  - Generate PNG icons from SVG")
        (println "  install   - Install icon for current user")
        (println "  all       - Generate and install (default)")
        (println)
        (println "Example:")
        (println "  bb scripts/install-cursor-icon.bb all")
        (when (nil? command)
          ;; Default to "all" if no command specified
          (generate-icons)
          (install-icon))))))

(apply main *command-line-args*)

