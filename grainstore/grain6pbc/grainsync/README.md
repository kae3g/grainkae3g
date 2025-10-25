# grainsync

**Automated graincourse creation and synchronization workflow**

Grainsync provides a streamlined command-line interface for creating, managing, and deploying graincourses with automatic graintime generation, grainpath creation, and multi-platform synchronization.

## Features

- **Automated Course Creation**: Generate graintime and grainpath automatically
- **Multi-Platform Sync**: Deploy to both GitHub Pages and Codeberg Pages
- **GitHub Integration**: Auto-update repository descriptions with course URLs
- **Interactive Workflow**: User-friendly prompts and confirmations
- **Template System**: Consistent course structure and styling

## Usage

### Create New Course

```bash
# Interactive course creation
gb grainsync course new

# This will prompt for:
# - Course title
# - Course description
# - Confirmation before creation
```

### Sync Existing Course

```bash
# Sync course to grainkae3g pages
gb grainsync course sync
```

### List Courses

```bash
# List all available graincourses
gb grainsync course list
```

### Update GitHub Description

```bash
# Update GitHub repository description with Pages URL
gb grainsync github update-description
```

## Workflow

1. **Generate Graintime**: Uses `gt` command to get current astronomical time
2. **Create Grainpath**: Generates unique path using graincourse module
3. **Build Structure**: Creates course directory with HTML and README
4. **Sync to Pages**: Deploys to both GitHub and Codeberg Pages
5. **Update GitHub**: Updates repository description with course URL

## Generated Structure

```
/{grainpath}/
├── index.html          # Course landing page
└── README.md           # Course documentation
```

## Dependencies

- `graintime` - Astronomical time generation
- `graincourse` - Course path generation
- `graincourse-sync` - Multi-platform synchronization
- GitHub API - Repository metadata updates

## Configuration

Set your GitHub token for repository updates:

```bash
# Option 1: Environment variable
export GITHUB_TOKEN=your_token_here

# Option 2: Git config
git config --global github.token your_token_here
```

## Examples

### Creating a Course

```bash
$ gb grainsync course new

🌾 Create New Graincourse
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Course Title: Advanced Clojure Development
Description: Learn advanced Clojure techniques for distributed systems

🕐 Generating graintime...
   12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g

🛤️  Generating grainpath...
   12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/advanced-clojure-development

📋 Course Details:
  Title: Advanced Clojure Development
  Description: Learn advanced Clojure techniques for distributed systems
  Grainpath: 12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/advanced-clojure-development
  GitHub Pages: https://kae3g.github.io/grainkae3g/12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/advanced-clojure-development/
  Codeberg Pages: https://kae3g.codeberg.page/grainkae3g/12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/advanced-clojure-development/

Create this course? (y/N): y

🏗️  Creating course structure...
   ✅ Course directory created

🔄 Syncing to grainkae3g...
   ✅ Synced to grainkae3g

📝 Updating GitHub repository description...
   ✅ Repository description updated

🎉 Course created successfully!

🔗 Access your course:
   GitHub Pages: https://kae3g.github.io/grainkae3g/12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/advanced-clojure-development/
   Codeberg Pages: https://kae3g.codeberg.page/grainkae3g/12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/advanced-clojure-development/

📁 Local path: /home/xy/kae3g/grainkae3g/12025-10-23--0145--PDT--moon-vishakha------asc-gem000--sun-04th--kae3g/advanced-clojure-development
```

## Integration

Grainsync integrates with the broader Grain ecosystem:

- **Graintime**: Astronomical time generation
- **Grainpath**: Unique course identifiers
- **Grainstore**: Module management
- **Grainbarrel**: Command-line interface
- **Grainpages**: Multi-platform deployment

## License

MIT License - see [LICENSE](LICENSE) file.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## Support

For issues and questions:
- GitHub Issues: [grainkae3g/issues](https://github.com/kae3g/grainkae3g/issues)
- Codeberg Issues: [grainkae3g/issues](https://codeberg.org/kae3g/grainkae3g/issues)
