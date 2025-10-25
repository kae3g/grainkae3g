# grainkae3gtime

**kae3g's personal graintime configuration**

Personal location and timezone settings for accurate astronomical calculations.

---

## Configuration

**Template**: `grainpbc/graintime`  
**Personal**: This module

### Location
- **City**: San Rafael, CA, USA
- **Coordinates**: 37.9735°N, 122.5311°W
- **Timezone**: America/Los_Angeles (PDT/PST)

### File: `personal/location.edn`
```edn
{:latitude 37.9735
 :longitude -122.5311
 :location-name "San Rafael, CA, USA"
 :timezone "America/Los_Angeles"
 :timezone-abbr "PDT/PST"}
```

---

## Usage

The `gt` command automatically uses this personal configuration:

```bash
$ gt
🌾 Generating Graintime...

🌅 Solar House Calculation:
   Location: San Rafael, CA, USA (37.9735°N, -122.5311°W)
   ...
```

---

## Updating Location

To change your default location:

```bash
gt config setup
```

Or edit `personal/location.edn` directly.

---

**Created**: October 23, 2025  
**Graintime**: `12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g`

