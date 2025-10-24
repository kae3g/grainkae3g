# 📚 grainbook - Grain Network Content Reader

Native desktop application for reading and managing Grain Network content.

## Purpose

grainbook replaces the web-based content reader with a native Humble UI application that provides:
- Fast content loading and navigation
- Offline content access
- Native desktop integration
- Better performance than web browsers

## Features

- **Content Reader** - View markdown content with native rendering
- **Navigation** - Browse content by category and search
- **Bookmarks** - Save favorite content for quick access
- **Search** - Full-text search across all content
- **Offline** - Works without internet connection

## Usage

```bash
# Start the application
clj -M:dev

# Build for distribution
bb build-grainbook.bb
```

## Architecture

- **core.clj** - Main application logic
- **ui.clj** - Humble UI interface
- **content.clj** - Content management
- **navigation.clj** - Navigation system

## Dependencies

- Humble UI for native desktop interface
- Grain Network Core for shared functionality
- Content pipeline for markdown and EDN processing
