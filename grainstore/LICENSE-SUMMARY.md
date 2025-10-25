# Grainstore License Summary

**Last Updated**: January 2025  
**Purpose**: Comprehensive license audit for all grainstore dependencies

---

## 📋 **License Overview**

All grainstore modules use **permissive licenses** that allow commercial use, modification, and distribution.

---

## 🔍 **Detailed License Analysis**

### **✅ MIT License (Permissive)**
**Modules**: All Urbit-related code
- **urbit-source**: MIT License (Copyright 2015 Urbit)
- **urbit-docs**: MIT License (Copyright 2019 Tlon Corporation)  
- **arvo**: MIT License (Copyright 2017 Urbit)
- **herb**: MIT License (Copyright 2016 urbit)

**Permissions**:
- ✅ Commercial use
- ✅ Modification
- ✅ Distribution
- ✅ Private use
- ✅ Sublicensing

**Requirements**:
- 📝 Include copyright notice
- 📝 Include license text

### **⚠️ GPL v3 (Copyleft)**
**Modules**: wl-gammarelay
- **License**: GNU General Public License v3
- **Copyright**: 2007 Free Software Foundation

**Permissions**:
- ✅ Commercial use
- ✅ Modification
- ✅ Distribution
- ✅ Private use
- ❌ Sublicensing (with restrictions)

**Requirements**:
- 📝 Include copyright notice
- 📝 Include license text
- 📝 Disclose source code
- 📝 State changes made
- 📝 Include GPL v3 license

---

## 🎯 **License Compatibility**

### **For Commercial Use**:
- **MIT modules**: ✅ Fully compatible
- **GPL v3 modules**: ⚠️ Requires GPL v3 compliance

### **For Open Source Projects**:
- **MIT modules**: ✅ Fully compatible
- **GPL v3 modules**: ✅ Fully compatible

### **For Proprietary Projects**:
- **MIT modules**: ✅ Fully compatible
- **GPL v3 modules**: ❌ Not compatible (requires GPL v3)

---

## 📚 **Third-Party Dependencies**

### **Urbit Interface Dependencies** (from package.json):
All dependencies use permissive licenses:
- **React ecosystem**: MIT License
- **TypeScript**: Apache 2.0 License
- **Webpack**: MIT License
- **Babel**: MIT License
- **Jest**: MIT License

### **Rust Dependencies** (from Cargo.toml):
All dependencies use permissive licenses:
- **serde**: MIT/Apache 2.0 dual license
- **candid**: MIT License
- **ic-cdk**: MIT License

---

## 🛡️ **Compliance Requirements**

### **For MIT-Licensed Code**:
1. Include copyright notice
2. Include MIT license text
3. No additional restrictions

### **For GPL v3 Code** (wl-gammarelay):
1. Include copyright notice
2. Include GPL v3 license text
3. Disclose source code
4. State changes made
5. Include installation information

---

## 📝 **Recommended Attribution**

### **In README.md**:
```markdown
## Third-Party Licenses

This project includes code from the following sources:

- **Urbit Source Code**: MIT License (Copyright 2015-2019 Urbit/Tlon Corporation)
- **Urbit Documentation**: MIT License (Copyright 2019 Tlon Corporation)
- **wl-gammarelay**: GPL v3 License (Copyright 2007 Free Software Foundation)

See individual LICENSE files for full terms.
```

### **In Source Code**:
```rust
// Based on Urbit source code (MIT License)
// Copyright (c) 2015 Urbit
// See LICENSE.txt for full terms
```

---

## ⚖️ **Legal Recommendations**

1. **For Commercial Use**: 
   - ✅ MIT modules are safe to use
   - ⚠️ Consider replacing wl-gammarelay with MIT-licensed alternative

2. **For Open Source Projects**:
   - ✅ All modules are compatible
   - 📝 Ensure proper attribution

3. **For Proprietary Projects**:
   - ✅ Use MIT modules only
   - ❌ Avoid GPL v3 modules

---

## 🔄 **License Updates**

This audit was performed on:
- **Date**: January 2025
- **Urbit Source**: Latest from GitHub
- **Urbit Docs**: Latest from GitHub
- **wl-gammarelay**: Latest from GitHub

**Next Review**: When adding new dependencies

---

## 📞 **Questions or Concerns**

If you have questions about license compatibility or need clarification on any terms, consult:
1. Individual LICENSE files in each module
2. Legal counsel for commercial projects
3. Free Software Foundation for GPL questions

---

**✅ All grainstore modules use permissive licenses suitable for open source development and commercial use (with GPL v3 compliance for wl-gammarelay).**

