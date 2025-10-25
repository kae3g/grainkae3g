# Grainpbc Template Configuration System

**Script-based configuration management for template modules**

The grain6pbc template system now supports script-based configuration updates, allowing personal customization while maintaining template consistency.

## Overview

The configuration system provides:

- **Template Defaults**: Base configuration in template modules
- **Personal Overrides**: User-specific configuration in `~/.grainconfig.edn`
- **Script-Based Updates**: Interactive configuration via command-line scripts
- **Module-Specific Configs**: Individual configuration for each grain6pbc module
- **Cross-Module Sharing**: Shared values across related modules

## Architecture

```
grain6pbc/
├── grainconfig/           # Configuration management module
│   ├── src/grainconfig/core.clj
│   ├── scripts/
│   │   ├── grainconfig-grainsync.bb
│   │   └── grainconfig-graintime.bb
│   └── README.md
├── grainsync/             # Course creation module
├── graintime/             # Time generation module
└── ...

~/.grainconfig.edn         # Personal configuration overrides
```

## Usage

### Configuration Commands

```bash
# Show all module configurations
gb config:show

# Update specific module configuration
gb grainsync:config:update
gb graintime:config:update

# Interactive configuration update
gb config:update

# Reset to template defaults
gb config:reset

# Validate configuration
gb config:validate
```

### Module Integration

Template modules can access configuration via:

```clojure
;; In template module
(require '[grainconfig.core :as config])

;; Read merged configuration
(let [merged-config (config/merge-configs 
                     (config/read-config template-config-path)
                     (config/read-config personal-config-path))
      module-config (config/get-module-config merged-config :grainsync)]
  (get module-config :course-title "default"))
```

## Configuration Structure

### Template Configuration

Stored in `grain6pbc/grainconfig/config.edn`:

```clojure
{:grainconfig
 {:version "0.1.0"
  :timestamp "2025-10-23T02:15:00Z"
  :description "Grainpbc template configuration"
  
  :modules
  {:grainsync
   {:defaults
    {:course-title "kae3g"
     :course-description "kae3g"
     :github-owner "kae3g"
     :github-repo "grainkae3g"
     :codeberg-owner "kae3g"
     :codeberg-repo "grainkae3g"}}
   
   :graintime
   {:defaults
    {:location "San Rafael, CA, USA"
     :latitude 37.9735
     :longitude -122.5311
     :timezone "America/Los_Angeles"
     :username "kae3g"}}
   
   :graincourse
   {:defaults
    {:author "kae3g"
     :template "default"
     :style "monospace"}}}}}
```

### Personal Configuration

Stored in `~/.grainconfig.edn`:

```clojure
{:grainconfig
 {:modules
  {:grainsync
   {:defaults
    {:course-title "My Custom Course"
     :github-owner "myusername"}}
   
   :graintime
   {:defaults
    {:location "My City, Country"
     :latitude 40.7128
     :longitude -74.0060}}}}}
```

## Module Configurations

### Grainsync Configuration

Controls course creation defaults:

- `course-title`: Default course title when empty
- `course-description`: Default course description when empty
- `github-owner`: GitHub organization/username
- `github-repo`: GitHub repository name
- `codeberg-owner`: Codeberg organization/username
- `codeberg-repo`: Codeberg repository name

### Graintime Configuration

Controls astronomical time generation:

- `location`: Default location name
- `latitude`: Default latitude (decimal degrees)
- `longitude`: Default longitude (decimal degrees)
- `timezone`: Default timezone identifier
- `username`: Default username for graintime

### Graincourse Configuration

Controls course generation:

- `author`: Default course author
- `template`: Default course template
- `style`: Default course styling

## Script Development

To add configuration for a new module:

### 1. Create Configuration Script

```bash
# Create: grain6pbc/grainconfig/scripts/grainconfig-{module}.bb
```

Follow the pattern from existing scripts:

```clojure
#!/usr/bin/env bb

(require '[clojure.edn :as edn]
         '[clojure.string :as str]
         '[babashka.fs :as fs])

;; Configuration paths
(def personal-config-file (str (System/getenv "HOME") "/.grainconfig.edn"))

;; Default configuration
(def module-defaults
  {:key1 "value1"
   :key2 "value2"})

;; Functions
(defn read-personal-config [] ...)
(defn write-personal-config [config] ...)
(defn update-module-config [] ...)

;; Run the update
(update-module-config)
```

### 2. Add to bb.edn

```clojure
{module:config:update
 {:doc "Update {module} configuration"
  :task (shell "bb" "scripts/grainconfig-{module}.bb")}}
```

### 3. Update Default Configuration

```clojure
{:modules
 {:new-module
  {:defaults
   {:key1 "value1"
    :key2 "value2"}}}}
```

## Benefits

1. **Template Consistency**: All modules use the same configuration system
2. **Personal Customization**: Easy personalization without template modification
3. **Script Automation**: Configuration updates via command-line scripts
4. **Validation**: Ensures configuration integrity
5. **Cross-Module Sharing**: Shared values across related modules
6. **Version Control**: Personal config separate from template config

## Examples

### Setting Up Personal Configuration

```bash
# Configure grainsync defaults
$ gb grainsync:config:update

🌾 Update Grainsync Configuration
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Current configuration:
  course-title: kae3g
  course-description: kae3g
  github-owner: kae3g
  github-repo: grainkae3g
  codeberg-owner: kae3g
  codeberg-repo: grainkae3g

course-title [kae3g]: My Custom Course
course-description [kae3g]: A personalized course
github-owner [kae3g]: myusername
github-repo [grainkae3g]: myrepo
codeberg-owner [kae3g]: myusername
codeberg-repo [grainkae3g]: myrepo

✅ Grainsync configuration updated!
```

### Using Configuration in Scripts

```bash
# Create course with personal defaults
$ gb grainsync:course:new

🌾 Create New Graincourse
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Course Title [My Custom Course]: 
Description [A personalized course]: 
```

## Integration with Existing Modules

### Grainsync Integration

The grainsync module now uses configuration for:

- Default course title and description
- GitHub and Codeberg repository URLs
- Personal customization without code changes

### Graintime Integration

The graintime module can use configuration for:

- Default location settings
- Username for graintime generation
- Timezone preferences

### Future Module Integration

New modules can easily integrate by:

1. Reading configuration via `grainconfig.core`
2. Using personal defaults when available
3. Falling back to template defaults
4. Providing configuration scripts for easy updates

## Migration Guide

### For Existing Modules

1. **Add Configuration Support**:
   ```clojure
   (require '[grainconfig.core :as config])
   
   (defn get-module-config []
     (let [merged-config (config/merge-configs 
                          (config/read-config template-config-path)
                          (config/read-config personal-config-path))]
       (config/get-module-config merged-config :module-key)))
   ```

2. **Use Configuration Values**:
   ```clojure
   (let [config (get-module-config)
         default-value (get config :key "fallback")]
     ;; Use default-value
     )
   ```

3. **Create Configuration Script**:
   - Follow the pattern in `grainconfig/scripts/`
   - Add to grainbarrel bb.edn
   - Update documentation

### For New Modules

1. **Design Configuration Schema**:
   - Identify configurable values
   - Define sensible defaults
   - Consider cross-module sharing

2. **Implement Configuration Reading**:
   - Use `grainconfig.core` functions
   - Handle missing configuration gracefully
   - Provide fallback values

3. **Create Configuration Script**:
   - Interactive configuration updates
   - Validation of input values
   - Clear user feedback

## Best Practices

1. **Sensible Defaults**: Provide useful default values
2. **Validation**: Validate configuration values
3. **Documentation**: Document all configuration options
4. **Backwards Compatibility**: Maintain compatibility with existing configs
5. **Error Handling**: Graceful handling of configuration errors
6. **User Feedback**: Clear feedback during configuration updates

## Troubleshooting

### Configuration Not Found

```bash
# Reset to defaults
gb config:reset

# Validate configuration
gb config:validate
```

### Module Not Using Configuration

1. Check module integration
2. Verify configuration reading
3. Test with configuration script
4. Check error messages

### Configuration Script Issues

1. Check script syntax
2. Verify file paths
3. Test script independently
4. Check permissions

## Future Enhancements

1. **Configuration Templates**: Pre-defined configuration sets
2. **Configuration Import/Export**: Share configurations
3. **Configuration Validation**: Schema-based validation
4. **Configuration Migration**: Automatic migration between versions
5. **Configuration UI**: Web-based configuration interface

## License

MIT License - see [LICENSE](LICENSE) file.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Add configuration for your module
4. Add corresponding script
5. Update documentation
6. Submit a pull request

## Support

For issues and questions:
- GitHub Issues: [grain6pbc/grainconfig/issues](https://github.com/grain6pbc/grainconfig/issues)
- Codeberg Issues: [grain6pbc/grainconfig/issues](https://codeberg.org/grain6pbc/grainconfig/issues)
