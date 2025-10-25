#!/usr/bin/env bb
(ns build-appimage
  "Build AppImage for GrainDisplay desktop application"
  (:require [clojure.java.shell :as shell]
            [clojure.string :as str]
            [babashka.fs :as fs]))

(def app-name "GrainDisplay")
(def app-id "org.grainpbc.graindisplay")
(def version "1.0.0")
(def arch (str/trim (:out (shell/sh "uname" "-m"))))

(defn run-command [cmd & {:keys [sh]}]
  "Run command with optional shell execution"
  (let [result (if sh
                 (shell/sh "bash" "-c" cmd)
                 (shell/sh cmd))]
    (if (= (:exit result) 0)
      (str/trim (:out result))
      (do
        (println "Command failed:" cmd)
        (println "Error:" (:err result))
        nil))))

(defn create-appdir []
  "Create AppDir structure for AppImage"
  (let [appdir (str "GrainDisplay-" arch ".AppDir")
        bin-dir (str appdir "/usr/bin")
        lib-dir (str appdir "/usr/lib")
        share-dir (str appdir "/usr/share/graindisplay")
        icons-dir (str appdir "/usr/share/icons/hicolor/256x256/apps")
        desktop-dir (str appdir "/usr/share/applications")]
    
    ;; Create directories
    (doseq [dir [appdir bin-dir lib-dir share-dir icons-dir desktop-dir]]
      (fs/create-dirs dir))
    
    ;; Create AppRun script
    (spit (str appdir "/AppRun")
          (str/join "\n"
            ["#!/bin/bash"
             "cd \"$(dirname \"$0\")\""
             "exec ./usr/bin/graindisplay \"$@\""]))
    (run-command (str "chmod +x " appdir "/AppRun") :sh true)
    
    ;; Create desktop file
    (spit (str desktop-dir "/" app-id ".desktop")
          (str/join "\n"
            ["[Desktop Entry]"
             "Type=Application"
             "Name=" app-name
             "Comment=Bedtime Warm Theme for GNOME"
             "Exec=graindisplay"
             "Icon=" app-id
             "Categories=Utility;Settings;"
             "StartupNotify=true"
             "Terminal=false"]))
    
    ;; Create icon (placeholder)
    (run-command (str "convert -size 256x256 xc:'#D2691E' " icons-dir "/" app-id ".png") :sh true)
    
    appdir))

(defn build-native-image []
  "Build native image using GraalVM"
  (println "🔨 Building native image...")
  
  (let [native-image-cmd
        (str/join " "
          ["native-image"
           "--no-fallback"
           "--install-exit-handlers"
           "--enable-http"
           "--enable-https"
           "--enable-url-protocols=http,https"
           "-H:+ReportExceptionStackTraces"
           "-H:+AddAllCharsets"
           "-H:IncludeResources=.*\\.(edn|json|xml|properties)$"
           "-H:IncludeResourceBundles=.*"
           "-H:+AllowVMInspection"
           "-H:+ReportUnsupportedElementsAtRuntime"
           "-J-Xmx4g"
           "-J-Xms1g"
           "--initialize-at-build-time"
           "-cp" (str/join ":" ["src" "resources" "classes"])
           "graindisplay.core"
           "graindisplay"])]
    
    (run-command native-image-cmd :sh true)))

(defn create-appimage []
  "Create AppImage using appimagetool"
  (println "📦 Creating AppImage...")
  
  (let [appdir (create-appdir)
        appimage-name (str app-name "-" version "-" arch ".AppImage")]
    
    ;; Copy native binary
    (run-command (str "cp graindisplay " appdir "/usr/bin/") :sh true)
    (run-command (str "chmod +x " appdir "/usr/bin/graindisplay") :sh true)
    
    ;; Copy resources
    (run-command (str "cp -r resources/* " appdir "/usr/share/graindisplay/") :sh true)
    
    ;; Create AppImage
    (run-command (str "appimagetool " appdir " " appimage-name) :sh true)
    
    (println "✅ AppImage created:" appimage-name)
    appimage-name))

(defn install-dependencies []
  "Install build dependencies"
  (println "📦 Installing dependencies...")
  
  ;; Install GraalVM
  (when-not (fs/exists? "/usr/bin/native-image")
    (println "Installing GraalVM...")
    (run-command "curl -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-22.3.0/graalvm-ce-java17-linux-amd64-22.3.0.tar.gz -o graalvm.tar.gz" :sh true)
    (run-command "sudo tar -xzf graalvm.tar.gz -C /opt/" :sh true)
    (run-command "sudo ln -sf /opt/graalvm-ce-java17-22.3.0/bin/* /usr/bin/" :sh true))
  
  ;; Install AppImageTool
  (when-not (fs/exists? "/usr/bin/appimagetool")
    (println "Installing AppImageTool...")
    (run-command "curl -L https://github.com/AppImage/AppImageKit/releases/download/continuous/appimagetool-x86_64.AppImage -o appimagetool" :sh true)
    (run-command "chmod +x appimagetool" :sh true)
    (run-command "sudo mv appimagetool /usr/bin/" :sh true))
  
  ;; Install ImageMagick for icon generation
  (when-not (fs/exists? "/usr/bin/convert")
    (run-command "sudo apt-get update && sudo apt-get install -y imagemagick" :sh true)))

(defn main []
  "Main build process"
  (let [args *command-line-args*
        command (first args)]
    (case command
      "deps" (install-dependencies)
      "native" (build-native-image)
      "appimage" (create-appimage)
      "all" (do
              (install-dependencies)
              (build-native-image)
              (create-appimage))
      "clean" (do
                (run-command "rm -rf *.AppDir graindisplay *.AppImage" :sh true)
                (println "✅ Cleaned build artifacts"))
      (do
        (println "GrainDisplay AppImage Builder")
        (println "")
        (println "USAGE:")
        (println "  bb build-appimage.bb deps      Install build dependencies")
        (println "  bb build-appimage.bb native    Build native image")
        (println "  bb build-appimage.bb appimage  Create AppImage")
        (println "  bb build-appimage.bb all       Full build process")
        (println "  bb build-appimage.bb clean     Clean build artifacts")))))

(main)
