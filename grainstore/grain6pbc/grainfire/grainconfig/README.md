# grainconfig

**Script-based configuration management for grain6pbc template modules**

Grainconfig provides a unified configuration system that allows template modules to be customized via scripts, enabling personal defaults while maintaining template consistency.

## Features

- **Template/Personal Split**: Template defaults with personal overrides
- **Script-Based Updates**: Interactive configuration via command-line scripts
- **Module-Specific Configs**: Individual configuration for each grain6pbc module
- **Validation**: Configuration structure validation and error reporting
- **Cross-Module Consistency**: Shared configuration values across modules

## Architecture

```
grain6pbc/grainconfig/
├── src/grainconfig/core.clj     # Core configuration management
├── scripts/
│   ├── grainconfig-grainsync.bb # Grainsync configuration script
│   └── grainconfig-graintime.bb # Graintime configuration script
└── README.md

~/.grainconfig.edn               # Personal configuration overrides
```

## Usage

### Show Current Configuration

```bash
# Show all module configurations
bb config:show

# Show specific module configuration
bb grainsync:config:update
```

### Update Module Configuration

```bash
# Update grainsync defaults
bb grainsync:config:update

# Update graintime defaults  
bb graintime:config:update

# Interactive configuration update
bb config:update
```

### Reset Configuration

```bash
# Reset to template defaults
bb config:reset

# Validate current configuration
bb config:validate
```

## Configuration Structure

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

## Module Configuration

### Grainsync Configuration

Controls default values for course creation:

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

## Integration with Template Modules

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

## Personal Configuration

Personal configuration overrides are stored in `~/.grainconfig.edn`:

```clojure
{:grainconfig
 {:modules
  {:grainsync
   {:defaults
    {:course-title "my-custom-title"
     :github-owner "myusername"}}
   
   :graintime
   {:defaults
    {:location "My City, Country"
     :latitude 40.7128
     :longitude -74.0060}}}}}
```

## Script Development

To add configuration for a new module:

1. **Create configuration script**:
   ```bash
   # Create script: scripts/grainconfig-{module}.bb
   # Follow pattern from existing scripts
   ```

2. **Add to bb.edn**:
   ```clojure
   {module:config:update
    {:doc "Update {module} configuration"
     :task (shell "bb" "scripts/grainconfig-{module}.bb")}}
   ```

3. **Update default-config**:
   ```clojure
   {:modules
    {:new-module
     {:defaults
      {:key1 "value1"
       :key2 "value2"}}}}
   ```

## Examples

### Setting Up Personal Configuration

```bash
# Configure grainsync defaults
$ bb grainsync:config:update

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

### Viewing Configuration

```bash
$ bb config:show

🌾 Grainpbc Configuration
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Version: 0.1.0
Timestamp: 2025-10-23T02:15:00Z

📦 grainsync:
   course-title: My Custom Course
   course-description: A personalized course
   github-owner: myusername
   github-repo: myrepo
   codeberg-owner: myusername
   codeberg-repo: myrepo

📦 graintime:
   location: San Rafael, CA, USA
   latitude: 37.9735
   longitude: -122.5311
   timezone: America/Los_Angeles
   username: kae3g
```

## Benefits

1. **Template Consistency**: All modules use the same configuration system
2. **Personal Customization**: Easy personalization without template modification
3. **Script Automation**: Configuration updates via command-line scripts
4. **Validation**: Ensures configuration integrity
5. **Cross-Module Sharing**: Shared values across related modules
6. **Version Control**: Personal config separate from template config

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
