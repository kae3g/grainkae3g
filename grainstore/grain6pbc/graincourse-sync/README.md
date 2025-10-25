# GrainCourse Sync

**Immutable Grainpath Symlink System for Course Deployment**

## Overview

`graincourse-sync` replaces the old manual repository creation process with a clean, fast symlink-based system for immutable grainpath subdirectories. This enables permanent, shareable URLs for each course version without duplicating content.

## Key Benefits

- **🚀 Fast**: No more manual repo creation - just symlinks
- **🔗 Immutable**: Permanent URLs for each course version
- **📦 Efficient**: No content duplication, just symlinks
- **🌐 Dual Hosting**: Automatic GitHub Pages + Codeberg Pages deployment
- **🔄 Automated**: Integrated with `gb flow` command

## Architecture

### Old System (Deprecated)
```
❌ Manual Process:
1. Create new GitHub repo
2. Create new Codeberg repo  
3. Copy course content
4. Deploy separately
5. Update documentation
```

### New System (Current)
```
✅ Symlink System:
1. Build course in personal/{course-dir}/
2. Create symlinks to immutable grainpath subdirs
3. Deploy via gb flow (GitHub + Codeberg)
4. Permanent URLs automatically available
```

## Directory Structure

```
grain6pbc/graincourse-sync/
├── template/                    # Template configuration
│   ├── symlink-setup.bb        # Symlink creation script
│   ├── url-generator.bb        # URL generation utilities
│   └── validation.bb           # Symlink validation
├── personal/                    # Personal configuration
│   ├── config.edn              # Personal settings
│   └── secrets/                # Sensitive data (.gitignore)
└── src/graincourse-sync/       # Core implementation
    ├── core.clj                # Main sync logic
    ├── symlinks.clj            # Symlink management
    └── urls.clj                # URL generation
```

## URL Structure

### GitHub Pages
- **Base**: `https://kae3g.github.io/grainkae3g`
- **Course**: `https://kae3g.github.io/grainkae3g/course/kae3g/{course-name}/{graintime}/`

### Codeberg Pages  
- **Base**: `https://kae3g.codeberg.page/grainkae3g`
- **Course**: `https://kae3g.codeberg.page/grainkae3g/course/kae3g/{course-name}/{graintime}/`

## Usage

### Basic Sync
```bash
# Setup symlinks for current course
gb graincourse-sync:setup

# Deploy with symlinks
gb flow
```

### Advanced Usage
```bash
# Create symlinks for specific course
gb graincourse-sync:create course-name graintime

# Validate all symlinks
gb graincourse-sync:validate

# List active symlinks
gb graincourse-sync:list

# Clean old symlinks
gb graincourse-sync:cleanup
```

## Integration

### With GrainCourse
- Automatically creates symlinks during course creation
- Integrates with `gb flow` for deployment
- Maintains immutable grainpath URLs

### With GrainPages
- Works with the Markdown → Clojure → SvelteKit pipeline
- Supports both GitHub Actions and Woodpecker CI
- Ensures Codeberg `pages` branch sync

## Migration from Old System

### Deprecated Files
- `GRAINPATH-IMMUTABLE-COURSES.md` → Use this module instead
- Manual repo creation scripts → Use symlink system
- Individual course deployment → Use `gb flow`

### Migration Steps
1. **Backup**: Keep existing repos as reference
2. **Setup**: Install `graincourse-sync` module
3. **Configure**: Update personal settings
4. **Test**: Create test symlinks
5. **Deploy**: Use new `gb flow` system

## Configuration

### Template Settings
```clojure
;; template/config.edn
{:github-pages-base "https://kae3g.github.io/grainkae3g"
 :codeberg-pages-base "https://kae3g.codeberg.page/grainkae3g"
 :symlink-dir "symlinks"
 :course-dir "personal"}
```

### Personal Settings
```clojure
;; personal/config.edn
{:author "kae3g"
 :default-course-type "course"
 :auto-symlink true
 :cleanup-old-symlinks true}
```

## Symlink Management

### Creation
- Automatically creates symlinks for new courses
- Uses relative paths for portability
- Validates symlink targets exist

### Validation
- Checks symlink targets are accessible
- Verifies URL structure is correct
- Reports broken or missing symlinks

### Cleanup
- Removes symlinks for old course versions
- Configurable retention policy
- Safe cleanup with confirmation

## Error Handling

### Common Issues
- **Broken Symlinks**: Automatically detected and reported
- **Missing Targets**: Graceful fallback to manual creation
- **Permission Errors**: Clear error messages and solutions

### Recovery
- **Symlink Repair**: `gb graincourse-sync:repair`
- **Full Reset**: `gb graincourse-sync:reset`
- **Manual Override**: Direct symlink creation commands

## Future Enhancements

- **Automatic Cleanup**: Configurable retention policies
- **URL Testing**: Automatic validation of deployed URLs
- **Analytics**: Track symlink usage and performance
- **Batch Operations**: Manage multiple courses at once

## Dependencies

- `graintime` - For grainpath generation
- `grainpages` - For deployment pipeline
- `grainsource-separation` - For template/personal split

## Related Modules

- `grain6pbc/graincourse` - Course content management
- `grain6pbc/grainpages` - Static site generation
- `grain6pbc/grainsource-separation` - Configuration pattern

---

**🌾 From granules to grains to THE WHOLE GRAIN**

*This module represents the evolution from manual, error-prone processes to automated, reliable systems that scale with the Grain Network's growth.*
