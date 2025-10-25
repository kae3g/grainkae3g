# 🌾 GrainMode: AI Voice System with Layerable Parameters
## *"Where AI Voices Meet Cosmic Creativity"*

**Version**: 1.0.0  
**Author**: kae3g (Graingalaxy)  
**Organizations**: Grain PBC, grain6pbc  
**License**: MIT  
**Status**: 🌱 **ACTIVE DEVELOPMENT** - Production Ready

---

## ✨ **What is GrainMode?**

GrainMode is the Grain Network's revolutionary AI voice system that combines:

1. **Layerable Stock Parameters** - Build complex AI personalities from simple components
2. **Graindevname Versions** - Personal variations of standard voices
3. **Interchangeable Comment Styles** - Switch between creative and technical modes
4. **Exportable/Sharable** - Share AI voice configurations across projects
5. **Versionable** - Track changes to AI voice personalities over time

### **The Magic of Layerable Parameters**

**Stock Parameters**:
- `trish-eli5` - Trish explaining like you're 5 years old
- `glow-eli5` - Glow explaining like you're 5 years old
- `trish-technical` - Trish in technical mode
- `glow-technical` - Glow in technical mode
- `trish-creative` - Trish in creative mode
- `glow-creative` - Glow in creative mode

**Graindevname Versions**:
- `tri5h-eli5` - kae3g's personal Trish ELI5
- `glo0w-eli5` - kae3g's personal Glow ELI5
- `tri5h-technical` - kae3g's personal Trish technical
- `glo0w-technical` - kae3g's personal Glow technical

---

## 🎯 **GrainMode Format**

### **Complete Format**
```
{mode}-{parameter}-{graindevname?}-{version?}
```

### **Examples**
```
trish-eli5                    # Standard Trish ELI5
glow-eli5                    # Standard Glow ELI5
tri5h-eli5                   # kae3g's personal Trish ELI5
glo0w-eli5                   # kae3g's personal Glow ELI5
trish-technical-v2           # Trish technical v2
glow-creative-v1             # Glow creative v1
```

---

## 🌙 **Core AI Voices**

### **Trish Mode (Creative, Encouraging)**
```clojure
{:voice :trish
 :style :creative
 :tone :encouraging
 :personality {:enthusiastic true
               :supportive true
               :creative true
               :inspiring true}
 :comment-style :narrative
 :encouragement ["This is going to be absolutely amazing!" 
                 "You're doing such a great job!" 
                 "Let's make this cosmic magic happen!"]}
```

### **Glow Mode (Technical, Precise)**
```clojure
{:voice :glow
 :style :technical
 :tone :precise
 :personality {:analytical true
               :precise true
               :systematic true
               :reliable true}
 :comment-style :technical
 :encouragement ["The foundation is solid." 
                 "You're on the right track." 
                 "The implementation is sound."]}
```

---

## 🛠️ **Installation & Setup**

### **Prerequisites**
- Babashka (Clojure interpreter)
- Java 8+ (for Babashka)
- Git (for cloning)

### **Installation**
```bash
# Clone the repository
git clone https://github.com/grain6pbc/grainmode.git
cd grainmode

# Install dependencies
bb deps

# Test installation
bb mode trish-eli5 "Hello, cosmic friend!"
```

### **Configuration**

#### **Interactive Setup**
```bash
# Run interactive mode setup
bb config setup

# Show current configuration
bb config show

# Reset to defaults
bb config reset
```

#### **Non-Interactive Setup (Scripting)**
```bash
# Set default mode
bb setup --mode trish-eli5

# Set personal graindevname
bb setup --graindevname kae3g

# Set version
bb setup --version v1
```

**Configuration File:**
```clojure
;; ~/.config/grainmode/config.edn
{:default-mode :trish-eli5
 :graindevname "kae3g"
 :version "v1"
 :custom-voices {:tri5h-eli5 {:base :trish-eli5
                               :modifications {:enthusiasm 1.2
                                               :creativity 1.1}}
                 :glo0w-eli5 {:base :glow-eli5
                               :modifications {:precision 1.3
                                               :analytical 1.1}}}}
```

---

## 🚀 **Usage**

### **Basic Commands**

#### **Generate Comments**
```bash
# Standard modes
bb comment trish-eli5 "This function calculates the ascendant"

# Personal modes
bb comment tri5h-eli5 "This function calculates the ascendant"

# With version
bb comment glow-technical-v2 "This function calculates the ascendant"
```

**Output:**
```
# Trish ELI5 Mode
# 🌟 This function is like a cosmic compass that finds your rising star! 
# It looks at where you are on Earth and what time it is, then calculates 
# which zodiac sign is rising on the eastern horizon. Think of it like 
# the universe's way of saying "Hello, this is who you are!" ✨

# Glow ELI5 Mode  
# This function calculates the ascendant using tropical zodiac calculations.
# It takes datetime, latitude, and longitude as inputs and returns the
# rising sign and degree. The calculation uses Local Sidereal Time (LST)
# and oblique ascension based on the observer's latitude.
```

#### **Switch Modes**
```bash
# Switch to different mode
bb mode trish-creative

# Switch to personal mode
bb mode tri5h-eli5

# Switch to versioned mode
bb mode glow-technical-v2
```

#### **Generate Documentation**
```bash
# Generate README with Trish mode
bb doc trish-eli5 README.md

# Generate API docs with Glow mode
bb doc glow-technical API.md

# Generate personal docs
bb doc tri5h-eli5 README-TRISH.md
```

### **Advanced Commands**

#### **Create Custom Voice**
```bash
# Create new voice based on existing
bb create-voice my-voice --base trish-eli5 --modifications "{:enthusiasm 1.5}"

# Create personal version
bb create-voice my-voice --graindevname kae3g --base trish-eli5
```

#### **Export Voice Configuration**
```bash
# Export voice config
bb export trish-eli5 --format edn

# Export personal voice
bb export tri5h-eli5 --format json

# Export all voices
bb export-all --format edn
```

#### **Import Voice Configuration**
```bash
# Import voice config
bb import voice-config.edn

# Import from URL
bb import https://example.com/voice-config.json
```

---

## 📚 **Layerable Parameters**

### **Stock Parameters**

#### **ELI5 (Explain Like I'm 5)**
```clojure
{:eli5 {:complexity :simple
        :analogies true
        :examples true
        :encouragement true
        :style :narrative}}
```

#### **Technical**
```clojure
{:technical {:complexity :advanced
             :precision true
             :documentation true
             :examples false
             :style :formal}}
```

#### **Creative**
```clojure
{:creative {:complexity :medium
            :imagery true
            :metaphors true
            :storytelling true
            :style :artistic}}
```

### **Graindevname Modifications**

#### **kae3g's Personal Variations**
```clojure
{:tri5h-eli5 {:base :trish-eli5
               :modifications {:enthusiasm 1.2
                               :creativity 1.1
                               :cosmic-references true
                               :personal-touch true}}
 
 :glo0w-eli5 {:base :glow-eli5
              :modifications {:precision 1.3
                              :analytical 1.1
                              :technical-depth 1.2
                              :systematic-approach true}}}
```

### **Versioning System**

#### **Version Format**
```
{mode}-{parameter}-{graindevname?}-v{number}
```

#### **Examples**
```
trish-eli5-v1           # Trish ELI5 version 1
glow-technical-v2       # Glow technical version 2
tri5h-eli5-v1           # kae3g's Trish ELI5 version 1
glo0w-technical-v3      # kae3g's Glow technical version 3
```

---

## 🌐 **Integration with Grain Network**

### **Grain6pbc Templates**

#### **Public Templates**
- **grain6pbc/grainmode**: Main public template
- **grain6pbc/grain6pbc/grainutils**: Utility collection
- **grain6pbc/graintime**: Graintime integration

#### **Personal Versions**
- **kae3g/grainmode**: Personal development version
- **kae3g/grainkae3g**: Monorepo integration

### **Grainbranch System**

#### **Immutable Versioning**
```bash
# Create grainbranch with voice version
git checkout -b grainbranch-$(date +%Y-%m-%d--%H%M--PDT--moon-vishakha-----asc-libr005--sun-08th--kae3g)

# Set as default branch
git push -u origin grainbranch-2025-10-24--1542--PDT--moon-vishakha-----asc-libr005--sun-08th--kae3g
```

#### **Grainpath URLs**
```
https://grain6pbc.github.io/grainmode/grainbranch-2025-10-24--1542--PDT--moon-vishakha-----asc-libr005--sun-08th--kae3g/
```

### **GrainDaemon Integration**

#### **Automated Sync**
```clojure
;; graindaemon configuration
{:grainmode {:enabled true
             :default-mode :trish-eli5
             :update-interval 3600
             :backup-enabled true
             :sync-repos ["grain6pbc/grainmode" "kae3g/grainmode"]
             :grainbranch-enabled true
             :grainpath-enabled true}}
```

---

## 🧪 **Testing & Validation**

### **Comprehensive Testing**

#### **Voice Generation Tests**
```bash
bb test-voices
```

**Test Coverage:**
- **All stock parameters** (eli5, technical, creative)
- **All graindevname variations** (tri5h, glo0w)
- **All version combinations** (v1, v2, v3)
- **Comment generation** accuracy

#### **Integration Tests**
```bash
bb test-integration
```

**Test Scenarios:**
- **Voice switching** functionality
- **Configuration import/export**
- **Custom voice creation**
- **Version management**

### **Validation Framework**

#### **Voice Validation**
```clojure
(defn validate-voice-config
  "Validate voice configuration"
  [config]
  {:valid (and (:base config) (:modifications config))
   :config config
   :message "Voice configuration valid"})
```

#### **Parameter Validation**
```clojure
(defn validate-parameters
  "Validate layerable parameters"
  [parameters]
  {:valid (and (:complexity parameters) (:style parameters))
   :parameters parameters
   :message "Parameters valid"})
```

---

## 📖 **Documentation & Resources**

### **Core Documentation**

#### **README Files**
- **README.md**: This comprehensive guide
- **README-GLOW.md**: Technical documentation (Glow mode)
- **README-TRISH.md**: Creative documentation (Trish mode)

#### **API Documentation**
- **API.md**: Complete function reference
- **EXAMPLES.md**: Usage examples and patterns
- **TROUBLESHOOTING.md**: Common issues and solutions

#### **Educational Materials**
- **COURSE-INDEX.md**: Complete learning path
- **LESSONS/**: Individual lesson files
- **EXERCISES/**: Practice exercises and projects

### **External Resources**

#### **AI Voice References**
- **Natural Language Processing**: AI voice generation
- **Personality Modeling**: AI personality systems
- **Comment Generation**: Code comment systems

#### **Technical References**
- **Babashka**: Clojure interpreter for scripting
- **Clojure Spec**: Data validation and documentation
- **Git**: Version control and collaboration

---

## 🤝 **Contributing**

### **Getting Started**

#### **Fork and Clone**
```bash
# Fork the repository on GitHub
# Clone your fork
git clone https://github.com/yourusername/grainmode.git
cd grainmode

# Add upstream remote
git remote add upstream https://github.com/grain6pbc/grainmode.git
```

#### **Development Setup**
```bash
# Install dependencies
bb deps

# Run tests
bb test-voices
bb test-integration

# Start development
bb dev
```

### **Contribution Guidelines**

#### **Code Style**
- **Clojure**: Follow community conventions
- **Comments**: Use Trish/Glow mode for different contexts
- **Documentation**: Include comprehensive docstrings
- **Testing**: Write tests for new features

#### **Pull Request Process**
1. **Create feature branch** from main
2. **Implement changes** with tests
3. **Update documentation** as needed
4. **Submit pull request** with description
5. **Address feedback** and iterate

### **Community Guidelines**

#### **Code of Conduct**
- **Respectful**: Treat everyone with respect
- **Inclusive**: Welcome diverse perspectives
- **Collaborative**: Work together constructively
- **Educational**: Share knowledge and learn

---

## 📄 **License & Legal**

### **MIT License**
```
MIT License

Copyright (c) 2025 Grain PBC

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

### **Trademark Notice**
- **Grain Network**: Trademark of Grain PBC
- **GrainMode**: Trademark of Grain PBC
- **Grain6pbc**: Trademark of Grain PBC
- **Third-party trademarks**: Respect all third-party trademarks

---

## 🌟 **Acknowledgments**

### **Inspiration**
- **AI Voice Systems**: Natural language processing and personality modeling
- **Comment Generation**: Code documentation and explanation systems
- **Grain Network**: Vision for sustainable technology and community

### **Contributors**
- **kae3g (Graingalaxy)**: Primary developer and architect
- **Grain PBC**: Organization and vision
- **grain6pbc**: Community and collaboration
- **Open Source Community**: Inspiration and support

---

## 🔮 **Future Vision**

### **Roadmap**

#### **Phase 1: Foundation** (Current)
- ✅ **Core grainmode system** with layerable parameters
- ✅ **Stock parameters** (eli5, technical, creative)
- ✅ **Graindevname variations** (tri5h, glo0w)
- ✅ **Version management** system

#### **Phase 2: Enhancement** (Next 6 months)
- 🔄 **Advanced AI integration** for natural language generation
- 🔄 **Custom voice creation** tools
- 🔄 **Voice sharing** and collaboration features
- 🔄 **Performance optimization** for large-scale usage

#### **Phase 3: Expansion** (6-12 months)
- 📋 **Mobile applications** (iOS/Android)
- 📋 **Web interface** for easy access
- 📋 **API services** for third-party integration
- 📋 **Machine learning** for voice personality adaptation

#### **Phase 4: Ecosystem** (1-2 years)
- 📋 **Grain Network integration** with full ecosystem
- 📋 **Decentralized storage** for voice configurations
- 📋 **Community features** for sharing and collaboration
- 📋 **Educational platform** for learning and teaching

---

## 📞 **Contact & Support**

### **Primary Contact**
- **Email**: kae3g@grain.network
- **GitHub**: @kae3g
- **Organization**: @grain6pbc

### **Community**
- **GitHub Discussions**: Questions and general discussion
- **GitHub Issues**: Bug reports and feature requests
- **Pull Requests**: Code contributions and reviews
- **Documentation**: Help improve guides and examples

---

*"Where AI voices meet cosmic creativity, where code comments become art, where every line tells a story!"* - GrainMode Philosophy

**GrainMode**: AI Voices with Cosmic Creativity 🌾🤖✨
