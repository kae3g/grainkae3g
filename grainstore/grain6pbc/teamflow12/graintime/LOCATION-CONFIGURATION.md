# Graintime Location Configuration

**Version**: 1.0.0  
**Date**: October 22, 2025  
**Status**: Complete  

---

## 🌍 **Overview**

Graintime uses your geographic location to calculate accurate astrological data:

- **Solar House Clock**: Calculates houses based on Sun's diurnal motion (sunrise=1st, noon=10th, sunset=7th, midnight=4th)
- **Ascendant**: Rising sign and degree based on local sidereal time
- **Timezone**: Automatic timezone detection and formatting

---

## 🎯 **Quick Start**

### **1. Interactive Configuration** (Recommended)

```bash
gt config
```

This opens an interactive dialog that guides you through location setup:

```
🌍 Graintime Location Configuration
====================================

Your location is used to calculate:
  • Solar House Clock (sunrise, noon, sunset, midnight)
  • Accurate astrological house positions
  • Local timezone for timestamps

You can enter your location in three ways:
  1. City, State (USA)  - e.g., San Rafael, CA
  2. City, Country      - e.g., London, UK
  3. Lat,Lon coords     - e.g., 37.9735,-122.5311

📍 Popular cities:
   USA: San Francisco, CA | New York, NY | Chicago, IL | Seattle, WA
   International: London, UK | Paris, France | Tokyo, Japan | Sydney, Australia

Enter your location: _
```

### **2. View Current Configuration**

```bash
gt config show
```

Output:
```
📍 Current Location Configuration
=================================
Location: San Francisco, CA
Coordinates: 37.7749°N, 122.4194°W
Unix format: 37.7749,-122.4194
Configured: 2025-10-22T21:30:45-07:00[America/Los_Angeles]
```

### **3. Reset to Default**

```bash
gt config reset
```

---

## 📝 **Location Input Formats**

### **Format 1: City, State (USA)**

Most common US cities are recognized automatically:

```bash
Enter your location: San Francisco, CA
Enter your location: New York, NY
Enter your location: Chicago, IL
Enter your location: Seattle, WA
Enter your location: Austin, TX
```

**Supported US Cities**:
- San Francisco, CA
- San Rafael, CA
- Los Angeles, CA
- New York, NY
- Chicago, IL
- Houston, TX
- Phoenix, AZ
- Philadelphia, PA
- San Antonio, TX
- San Diego, CA
- Dallas, TX
- Austin, TX
- Seattle, WA
- Denver, CO
- Boston, MA
- Portland, OR
- Miami, FL
- Atlanta, GA

### **Format 2: City, Country (International)**

Major international cities are also recognized:

```bash
Enter your location: London, UK
Enter your location: Paris, France
Enter your location: Tokyo, Japan
Enter your location: Sydney, Australia
```

**Supported International Cities**:
- London, UK
- Paris, France
- Tokyo, Japan
- Berlin, Germany
- Sydney, Australia
- Toronto, Canada
- Mumbai, India
- Beijing, China
- Moscow, Russia
- Mexico City, Mexico

### **Format 3: Coordinates (Unix Standard)**

For precise location or cities not in the list, use coordinates in **Unix standard format** (`LAT,LON`):

```bash
Enter your location: 37.9735,-122.5311
Enter your location: 40.7128,-74.0060
Enter your location: 51.5074,-0.1278
```

**How to Find Your Coordinates**:
1. **Google Maps**: Right-click your location → Click coordinates to copy
2. **LatLong.net**: Visit https://www.latlong.net/
3. **Command line**: `curl ipinfo.io | grep loc` (requires internet)

**Coordinate Ranges**:
- Latitude: -90 to 90 (North is positive, South is negative)
- Longitude: -180 to 180 (East is positive, West is negative)

---

## 🔧 **Command-Line Location Flags**

You can override your configured location for individual `gt` commands using flags:

### **Option 1: Unix Standard Format** (Recommended)

```bash
# Using --loc flag (Unix standard LAT,LON format)
gt at kae3g --loc 40.7128,-74.0060 "2025-10-22 12:00"
gt at kae3g --loc 51.5074,-0.1278 "2025-10-22 12:00"

# Get coordinates from a Unix utility
gt at kae3g --loc $(curl -s ipinfo.io | grep -oP '"loc":\s*"\K[^"]+') "2025-10-22 12:00"
```

### **Option 2: Explicit Lat/Lon Flags**

```bash
# Using separate --lat and --lon flags
gt at kae3g --lat 40.7128 --lon -74.0060 "2025-10-22 12:00"
gt at kae3g --latitude 51.5074 --longitude -0.1278 "2025-10-22 12:00"
```

### **Examples by City**

#### **New York, NY**
```bash
gt at kae3g --loc 40.7128,-74.0060 "2025-10-22 12:00"
# => 12025-10-22--1200--EDT--moon-jyeshtha-pada2--asc-gemini22--sun-10thhouse--kae3g
```

#### **London, UK**
```bash
gt at kae3g --loc 51.5074,-0.1278 "2025-10-22 12:00"
# => 12025-10-22--1200--BST--moon-jyeshtha-pada2--asc-gemini22--sun-10thhouse--kae3g
```

#### **Tokyo, Japan**
```bash
gt at kae3g --loc 35.6762,139.6503 "2025-10-22 12:00"
# => 12025-10-22--1200--JST--moon-jyeshtha-pada2--asc-gemini22--sun-10thhouse--kae3g
```

---

## 📁 **Configuration File**

Location configuration is stored in:
```
~/kae3g/grainkae3g/grainstore/graintime/personal/location.edn
```

**File Format** (EDN):
```clojure
{:location-name "San Francisco, CA"
 :latitude 37.7749
 :longitude -122.4194
 :configured-at "2025-10-22T21:30:45.123456-07:00[America/Los_Angeles]"}
```

**Manual Editing**:
You can manually edit this file if needed:

```bash
# Edit with your favorite editor
nano ~/kae3g/grainkae3g/grainstore/graintime/personal/location.edn
vi ~/kae3g/grainkae3g/grainstore/graintime/personal/location.edn
```

---

## 🌐 **Unix Utility Integration**

### **Get Location from IP Address**

```bash
# Get your location from IP (requires internet)
curl ipinfo.io

# Extract just the coordinates
curl -s ipinfo.io | grep -oP '"loc":\s*"\K[^"]+'
# => 37.7749,-122.4194

# Use directly with gt
gt at kae3g --loc $(curl -s ipinfo.io | grep -oP '"loc":\s*"\K[^"]+') "$(date -Iseconds)"
```

### **Use with geoiplookup** (if installed)

```bash
# Install geoiplookup (Ubuntu/Debian)
sudo apt-get install geoip-bin geoip-database

# Look up your IP
geoiplookup $(curl -s ifconfig.me)
```

### **Integration with whereami** (if installed)

```bash
# If you have 'whereami' command
LOCATION=$(whereami --format "{latitude},{longitude}")
gt at kae3g --loc $LOCATION "2025-10-22 12:00"
```

---

## 🔍 **How Location Affects Graintime**

### **Example: Same Time, Different Locations**

#### **San Francisco, CA (37.7749,-122.4194)**
```bash
gt at kae3g --loc 37.7749,-122.4194 "2025-10-22 12:00"
# => 12025-10-22--1200--PDT--moon-jyeshtha-pada2--asc-gemini22--sun-10thhouse--kae3g
```

#### **New York, NY (40.7128,-74.0060)**
```bash
gt at kae3g --loc 40.7128,-74.0060 "2025-10-22 15:00"
# => 12025-10-22--1500--EDT--moon-jyeshtha-pada2--asc-gemini22--sun-10thhouse--kae3g
```
- **Timezone changes**: PDT vs EDT (3-hour difference)
- **Ascendant changes**: Different local sidereal time
- **Same Solar House**: Both at solar noon (12:00 local time)

### **Solar House Clock**

The sun house changes based on your local solar time:

**San Rafael, CA Example** (37.9735,-122.5311):
- **Sunrise (07:24)**: 1st House
- **Solar Noon (12:54)**: 10th House
- **Sunset (18:24)**: 7th House
- **Solar Midnight (00:54)**: 4th House

**London, UK Example** (51.5074,-0.1278):
- **Sunrise (07:20)**: 1st House
- **Solar Noon (13:00)**: 10th House
- **Sunset (18:40)**: 7th House
- **Solar Midnight (00:30)**: 4th House

*Note: Solar times vary by latitude and season*

---

## 🛠️ **Troubleshooting**

### **Problem: Unknown city**

**Solution 1**: Use coordinates from https://www.latlong.net/

```bash
# Find your city's coordinates, then:
gt config
Enter your location: 37.9735,-122.5311
```

**Solution 2**: Use a nearby major city

```bash
# If your city isn't recognized, use the nearest major city
gt config
Enter your location: San Francisco, CA  # Instead of Sausalito, CA
```

### **Problem: Invalid coordinates**

Check that:
- Latitude is between -90 and 90
- Longitude is between -180 and 180
- Format is: `LAT,LON` (comma-separated, no spaces)

```bash
# ❌ Invalid
37.9735, -122.5311  # Space after comma
122.5311,-37.9735   # Longitude and latitude reversed

# ✅ Valid
37.9735,-122.5311
```

### **Problem: Configuration not saving**

Check file permissions:

```bash
# Ensure directory exists and is writable
mkdir -p ~/kae3g/grainkae3g/grainstore/graintime/personal
chmod 755 ~/kae3g/grainkae3g/grainstore/graintime/personal
```

---

## 📚 **API Reference**

### **Configuration Commands**

```bash
# Interactive setup
gt config
gt config setup

# Show current location
gt config show

# Reset to default (San Rafael, CA)
gt config reset
```

### **Location Flags**

```bash
# Unix standard format (preferred)
--loc LAT,LON

# Explicit flags
--lat LATITUDE
--lon LONGITUDE
--latitude LATITUDE
--longitude LONGITUDE
```

### **Flag Examples**

```bash
# All of these are equivalent:
gt at kae3g --loc 37.9735,-122.5311 "2025-10-22 12:00"
gt at kae3g --lat 37.9735 --lon -122.5311 "2025-10-22 12:00"
gt at kae3g --latitude 37.9735 --longitude -122.5311 "2025-10-22 12:00"
```

---

## 🌟 **Best Practices**

### **1. Configure Once, Use Forever**

Set up your location once:
```bash
gt config
# Enter your location: San Francisco, CA
# ✅ Location saved!
```

Then all `gt` commands use your configured location automatically:
```bash
gt now kae3g
gt at kae3g "2025-10-22 12:00"
gt grainpath course kae3g my-course
```

### **2. Use Unix Standard Format for Scripting**

For automation and scripting, use the `LAT,LON` format:

```bash
#!/bin/bash
# Script to generate graintime for multiple locations

LOCATIONS=(
  "37.7749,-122.4194"  # San Francisco
  "40.7128,-74.0060"   # New York
  "51.5074,-0.1278"    # London
)

for loc in "${LOCATIONS[@]}"; do
  echo "Location: $loc"
  gt at kae3g --loc $loc "2025-10-22 12:00"
  echo ""
done
```

### **3. Override for Special Cases**

Use flags to override your configured location for specific calculations:

```bash
# Your configured location: San Francisco
gt now kae3g
# => Uses San Francisco coordinates

# Calculate for New York without changing config
gt at kae3g --loc 40.7128,-74.0060 "$(date -Iseconds)"
# => Uses New York coordinates

# Next command uses San Francisco again
gt now kae3g
# => Back to San Francisco coordinates
```

---

## 🔮 **Advanced Usage**

### **Travel Mode**

When traveling, temporarily override your location:

```bash
# In San Francisco (configured)
gt now kae3g

# Traveling to Tokyo
gt at kae3g --loc 35.6762,139.6503 "$(date -Iseconds)"

# Back in San Francisco
gt now kae3g
```

### **Multi-Location Analysis**

Compare graintime across locations:

```bash
#!/bin/bash
# Compare solar houses across timezones

echo "2025-10-22 at 12:00 local time:"
echo "San Francisco: $(gt at kae3g --loc 37.7749,-122.4194 '2025-10-22 12:00')"
echo "New York:      $(gt at kae3g --loc 40.7128,-74.0060 '2025-10-22 12:00')"
echo "London:        $(gt at kae3g --loc 51.5074,-0.1278 '2025-10-22 12:00')"
echo "Tokyo:         $(gt at kae3g --loc 35.6762,139.6503 '2025-10-22 12:00')"
```

---

## 🌾 **Grain Network Integration**

Location configuration integrates seamlessly with the Grain Network ecosystem:

- **Grainpath**: Location affects solar house in grainpath timestamps
- **Immutable Trails**: Location recorded in grainpath metadata
- **Reproducibility**: Exact location preserved for future reference
- **Collaboration**: Share grainpaths with location context

---

*Location configuration powered by Unix standards and Vedic astrology precision.*

**🌾 From granules to grains to THE WHOLE GRAIN**

