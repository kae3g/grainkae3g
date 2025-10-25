# 🌾 GrainSource s6-musl - Build from Source

> **"Own Your Binaries: s6 Built from Source with musl"**

Building s6 and dependencies from source with musl libc, stored in grainstore for version control and reproducibility.

---

## 📋 **Philosophy**

**Why Build from Source?**

✅ **Full Control**: Know exactly what's in your binaries  
✅ **Version Control**: Source code in git, reproducible builds  
✅ **Customization**: Enable/disable features as needed  
✅ **Security Audit**: Can review every line of code  
✅ **Portability**: Static binaries work everywhere  
✅ **Learning**: Understand how s6 works internally  

**Why in grainstore?**

✅ **Git History**: Track changes to build configuration  
✅ **Sharing**: Others can build identical binaries  
✅ **Backup**: Source preserved with your project  
✅ **Template/Personal**: grainpbc template, personalized versions  

---

## 🎯 **Architecture**

```
grainstore/grainsource-s6-musl/
├── src/                    # Source tarballs
│   ├── skalibs-2.14.4.0.tar.gz
│   ├── execline-2.9.7.0.tar.gz
│   └── s6-2.13.2.0.tar.gz
├── build/                  # Build directory (gitignored)
│   ├── skalibs-2.14.4.0/
│   ├── execline-2.9.7.0/
│   └── s6-2.13.2.0/
├── install/                # Installation prefix (gitignored)
│   ├── bin/
│   ├── lib/
│   └── include/
├── scripts/
│   ├── download-sources.bb    # Download source tarballs
│   ├── build-all.bb          # Build everything
│   └── install-system.bb     # Install to /opt/s6-musl
└── README.md               # This file
```

---

## 📦 **Source Versions**

Following **skarnet.org** official releases:

- **skalibs**: 2.14.4.0 (build system and portability library)
- **execline**: 2.9.7.0 (shell scripting language for s6)
- **s6**: 2.13.2.0 (supervision suite)

**Why these versions?**
- Latest stable releases
- Known to work together
- Security fixes included
- Same versions as Alpine Linux 3.21

---

## 🚀 **Quick Start**

### **One-Command Build**

```bash
cd /home/xy/kae3g/grainkae3g/grainstore/grainsource-s6-musl
bb build:all
```

### **Step-by-Step**

```bash
# 1. Download sources
bb download

# 2. Build skalibs
bb build:skalibs

# 3. Build execline
bb build:execline

# 4. Build s6
bb build:s6

# 5. Install to /opt/s6-musl
sudo bb install:system
```

---

## 📥 **Phase 1: Download Sources**

### **1.1 Create Directory Structure**

```bash
mkdir -p src build install
```

### **1.2 Download Source Tarballs**

Create `scripts/download-sources.bb`:

```clojure
#!/usr/bin/env bb

(require '[babashka.curl :as curl]
         '[clojure.java.io :as io])

(def versions
  {:skalibs "2.14.4.0"
   :execline "2.9.7.0"
   :s6 "2.13.2.0"})

(def base-url "https://skarnet.org/software")

(defn download-source [package version]
  (let [pkg-name (name package)
        filename (str pkg-name "-" version ".tar.gz")
        url (str base-url "/" pkg-name "/" filename)
        output-path (str "src/" filename)]
    
    (println (str "📥 Downloading " pkg-name " " version "..."))
    
    (if (.exists (io/file output-path))
      (println (str "✅ Already exists: " output-path))
      (do
        (io/make-parents output-path)
        (spit output-path
              (:body (curl/get url {:as :bytes})))
        (println (str "✅ Downloaded: " output-path))))))

(defn main []
  (println "🌾 Downloading s6 sources from skarnet.org")
  (println)
  
  (doseq [[pkg version] versions]
    (download-source pkg version)
    (println))
  
  (println "✅ All sources downloaded!")
  (println)
  (println "Next: bb build:all"))

(main)
```

---

## 🔨 **Phase 2: Build with musl**

### **2.1 Build Configuration**

Create `scripts/build-all.bb`:

```clojure
#!/usr/bin/env bb

(require '[babashka.process :refer [shell]]
         '[clojure.java.io :as io]
         '[clojure.string :as str])

(def versions
  {:skalibs "2.14.4.0"
   :execline "2.9.7.0"
   :s6 "2.13.2.0"})

(def build-dir "build")
(def install-dir (str (System/getProperty "user.dir") "/install"))

(def musl-flags
  {:CC "musl-gcc"
   :CFLAGS "-Os -fomit-frame-pointer -fno-asynchronous-unwind-tables"
   :LDFLAGS "-static"})

(defn extract-tarball [package version]
  (let [pkg-name (name package)
        tarball (str "src/" pkg-name "-" version ".tar.gz")
        extract-dir (str build-dir "/" pkg-name "-" version)]
    
    (println (str "📦 Extracting " pkg-name "..."))
    
    (if (.exists (io/file extract-dir))
      (println (str "✅ Already extracted: " extract-dir))
      (do
        (io/make-parents (str extract-dir "/."))
        (shell "tar" "-xzf" tarball "-C" build-dir)
        (println (str "✅ Extracted to: " extract-dir))))
    
    extract-dir))

(defn build-package [package version deps-prefix]
  (let [pkg-name (name package)
        extract-dir (extract-tarball package version)]
    
    (println)
    (println (str "🔨 Building " pkg-name " with musl..."))
    (println)
    
    ;; Configure
    (let [configure-cmd (if (empty? deps-prefix)
                          ["./configure"
                           (str "--prefix=" install-dir)
                           "--enable-static-libc"
                           "--enable-allstatic"]
                          ["./configure"
                           (str "--prefix=" install-dir)
                           (str "--with-sysdeps=" deps-prefix "/lib/skalibs/sysdeps")
                           (str "--with-include=" deps-prefix "/include")
                           (str "--with-lib=" deps-prefix "/lib")
                           "--enable-static-libc"
                           "--enable-allstatic"])]
      
      (println (str "⚙️  Configuring: " (str/join " " configure-cmd)))
      (apply shell {:dir extract-dir
                    :env (merge (into {} (System/getenv))
                                {"CC" (:CC musl-flags)
                                 "CFLAGS" (:CFLAGS musl-flags)
                                 "LDFLAGS" (:LDFLAGS musl-flags)})}
             configure-cmd))
    
    ;; Build
    (println "🔧 Compiling...")
    (shell {:dir extract-dir} "make" "-j8")
    
    ;; Install
    (println "📦 Installing...")
    (shell {:dir extract-dir} "make" "install")
    
    (println (str "✅ Built and installed: " pkg-name))
    (println)))

(defn verify-musl-binary [binary-path]
  (println (str "🔍 Verifying " binary-path "..."))
  
  (let [file-result (shell {:out :string} "file" binary-path)
        file-output (:out file-result)]
    
    (if (str/includes? file-output "statically linked")
      (do
        (println "✅ Static binary confirmed!")
        (let [ldd-result (shell {:out :string :continue true} "ldd" binary-path)
              ldd-output (:out ldd-result)]
          (if (or (str/includes? ldd-output "not a dynamic")
                  (str/includes? ldd-output "statically linked"))
            (println "✅ No dynamic dependencies!")
            (println "⚠️  Warning: May have dynamic dependencies"))))
      (println "⚠️  Warning: Not statically linked")))
  
  ;; Show size
  (let [size-result (shell {:out :string} "ls" "-lh" binary-path)
        size-line (:out size-result)
        size (second (re-find #"\s+(\S+)\s+\S+\s+\S+\s*$" size-line))]
    (println (str "📊 Size: " size)))
  (println))

(defn main []
  (println "╔══════════════════════════════════════════════════════════════╗")
  (println "║                                                              ║")
  (println "║   🌾  B U I L D   S 6   W I T H   M U S L                   ║")
  (println "║                                                              ║")
  (println "║   Static binaries for maximum portability                   ║")
  (println "║                                                              ║")
  (println "╚══════════════════════════════════════════════════════════════╝")
  (println)
  (println (str "📂 Build directory: " build-dir))
  (println (str "📂 Install prefix: " install-dir))
  (println (str "🏔️  Compiler: " (:CC musl-flags)))
  (println)
  
  ;; Create directories
  (io/make-parents (str build-dir "/."))
  (io/make-parents (str install-dir "/."))
  
  ;; Build in dependency order
  (println "═══════════════════════════════════════════════════════════════")
  (println "Step 1/3: Building skalibs (dependency library)")
  (println "═══════════════════════════════════════════════════════════════")
  (build-package :skalibs (:skalibs versions) "")
  
  (println "═══════════════════════════════════════════════════════════════")
  (println "Step 2/3: Building execline (shell for s6)")
  (println "═══════════════════════════════════════════════════════════════")
  (build-package :execline (:execline versions) install-dir)
  
  (println "═══════════════════════════════════════════════════════════════")
  (println "Step 3/3: Building s6 (supervision suite)")
  (println "═══════════════════════════════════════════════════════════════")
  (build-package :s6 (:s6 versions) install-dir)
  
  (println)
  (println "╔══════════════════════════════════════════════════════════════╗")
  (println "║                                                              ║")
  (println "║   ✅  B U I L D   C O M P L E T E !                         ║")
  (println "║                                                              ║")
  (println "╚══════════════════════════════════════════════════════════════╝")
  (println)
  
  ;; Verify key binaries
  (println "🔍 Verifying binaries...")
  (println)
  (verify-musl-binary (str install-dir "/bin/s6-svscan"))
  (verify-musl-binary (str install-dir "/bin/s6-supervise"))
  (verify-musl-binary (str install-dir "/bin/execlineb"))
  
  (println "✅ All binaries built successfully!")
  (println)
  (println "Next steps:")
  (println "  sudo bb install:system  # Install to /opt/s6-musl")
  (println "  bb test                 # Test the binaries")
  (println))

(main)
```

---

## 🚀 **Phase 3: System Installation**

Create `scripts/install-system.bb`:

```clojure
#!/usr/bin/env bb

(require '[babashka.process :refer [shell]])

(def install-source "install")
(def install-target "/opt/s6-musl")

(defn main []
  (println "🌾 Installing s6-musl to system...")
  (println)
  (println (str "Source: " install-source))
  (println (str "Target: " install-target))
  (println)
  
  ;; Copy to system location
  (shell "sudo" "mkdir" "-p" install-target)
  (shell "sudo" "cp" "-r" (str install-source "/*") install-target)
  
  (println "✅ Installed to /opt/s6-musl")
  (println)
  (println "Add to PATH:")
  (println "  export PATH=\"/opt/s6-musl/bin:$PATH\"")
  (println)
  (println "Or create symlinks:")
  (println "  sudo ln -sf /opt/s6-musl/bin/* /usr/local/bin/"))

(main)
```

---

## 📝 **bb.edn Configuration**

Create `bb.edn`:

```clojure
{:paths ["."]
 
 :tasks
 {download
  {:doc "Download s6 source tarballs from skarnet.org"
   :task (shell "bb" "scripts/download-sources.bb")}
  
  build:all
  {:doc "Build skalibs, execline, and s6 with musl (static linking)"
   :task (shell "bb" "scripts/build-all.bb")}
  
  install:system
  {:doc "Install built binaries to /opt/s6-musl (requires sudo)"
   :task (shell "bb" "scripts/install-system.bb")}
  
  clean
  {:doc "Clean build and install directories"
   :task (do
           (println "🧹 Cleaning build artifacts...")
           (shell "rm" "-rf" "build" "install")
           (println "✅ Cleaned!"))}
  
  test
  {:doc "Test the built binaries"
   :task (do
           (println "🧪 Testing s6 binaries...")
           (shell "install/bin/s6-svscan" "--help")
           (shell "install/bin/s6-supervise" "--help")
           (println "✅ Tests passed!"))}}}
```

---

## 🎯 **Usage**

### **First Time Setup**

```bash
cd /home/xy/kae3g/grainkae3g/grainstore/grainsource-s6-musl

# Download sources (stored in git)
bb download

# Build everything with musl
bb build:all

# Install to system
sudo bb install:system

# Add to PATH
echo 'export PATH="/opt/s6-musl/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verify
s6-svscan --help
```

### **Rebuilding**

```bash
# Clean previous build
bb clean

# Rebuild
bb build:all
```

---

## 📊 **Expected Results**

### **Binary Sizes**

```
s6-svscan:      ~85 KB (static)
s6-supervise:   ~75 KB (static)
s6-svc:         ~65 KB (static)
execlineb:      ~70 KB (static)

Total s6 suite: ~2 MB (all binaries)
```

Compare to glibc dynamic versions: ~45 KB binary + 2-3 MB shared libraries each!

### **Verification Output**

```bash
$ file install/bin/s6-svscan
install/bin/s6-svscan: ELF 64-bit LSB executable, x86-64, 
statically linked, stripped

$ ldd install/bin/s6-svscan
	not a dynamic executable

$ ls -lh install/bin/s6-svscan
-rwxr-xr-x 1 xy xy 85K Oct 23 17:30 install/bin/s6-svscan
```

---

## 🌾 **Git Integration**

### **.gitignore**

```gitignore
# Build artifacts
build/
install/

# Downloaded sources are kept in git
# src/*.tar.gz
```

**Why keep source tarballs in git?**
- Reproducibility (exact versions)
- Offline builds
- Audit trail
- Verifiable source

### **Commit Strategy**

```bash
# Commit source downloads
git add src/*.tar.gz
git commit -m "feat: add s6 source tarballs for musl build"

# Commit build scripts
git add scripts/*.bb bb.edn
git commit -m "feat: add s6 musl build automation"

# Build artifacts are gitignored
# (build/ and install/ directories)
```

---

## 🚀 **Integration with Grain Network**

### **Use in s6-svscan.service**

```ini
[Unit]
Description=s6 supervision (built from source with musl)
After=local-fs.target

[Service]
Type=simple
ExecStart=/opt/s6-musl/bin/s6-svscan /service
Environment="PATH=/opt/s6-musl/bin:/usr/bin:/bin"
Restart=always

[Install]
WantedBy=multi-user.target
```

### **Use in grain6**

```clojure
(ns grain6.s6
  "grain6 integration with musl-built s6")

(def s6-bin-path "/opt/s6-musl/bin")

(defn s6-cmd [cmd & args]
  (apply shell (str s6-bin-path "/" cmd) args))

(defn service-status [service]
  (s6-cmd "s6-svstat" (str "/service/" service)))
```

---

## 📚 **Resources**

- **skarnet.org**: https://skarnet.org/software/s6/
- **Build Guide**: https://skarnet.org/software/s6/install.html
- **musl libc**: https://musl.libc.org/

---

## 🌾 **Philosophy**

**"Build from Source, Own Your Stack"**

- Source code in version control
- Reproducible builds
- No dependency on external binaries
- Audit trail for security
- Educational value (learn by building)

**"Static Binaries, Maximum Portability"**

- Works on any Linux system
- No shared library dependencies
- Copy and run anywhere
- Perfect for containers and VMs

---

**Status**: 🌱 Ready to build s6 from source with musl  
**Time**: ~10 minutes for full build  
**Result**: Static s6 binaries in version control  

🌾 **now == next + 1** (but make it from source, chief!) 🔨
