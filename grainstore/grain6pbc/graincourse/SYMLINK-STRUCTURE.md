# Graincourse Symlink Structure

## Overview

The graincourse uses immutable grainpath subdirectories that symlink to servable content on both GitHub Pages and Codeberg Pages. This ensures that each course version has a permanent, immutable URL that can be referenced and shared.

## Directory Structure

```
grainstore/graincourse/
├── symlinks/
│   ├── github-pages/
│   │   └── {graintime}/ -> ../personal/{course-dir}/
│   └── codeberg-pages/
│       └── {graintime}/ -> ../personal/{course-dir}/
├── personal/
│   └── {course-dir}/
│       ├── course.edn
│       ├── grainpath.edn
│       ├── lessons/
│       └── public/
└── template/
    └── scripts/
        ├── build-course.bb
        ├── create-course.bb
        ├── deploy-github.bb
        └── deploy-codeberg.bb
```

## URL Structure

### GitHub Pages
- **Base URL**: `https://kae3g.github.io/grainkae3g`
- **Course URL**: `https://kae3g.github.io/grainkae3g/course/kae3g/{course-name}/{graintime}/`
- **Example**: `https://kae3g.github.io/grainkae3g/course/kae3g/grain-network-intro/12025-10-22-2148-PDT-moon-jyeshtha-pada2-asc-gemini22-sun-06thhouse-kae3g/`

### Codeberg Pages
- **Base URL**: `https://kae3g.codeberg.page/grainkae3g`
- **Course URL**: `https://kae3g.codeberg.page/grainkae3g/course/kae3g/{course-name}/{graintime}/`
- **Example**: `https://kae3g.codeberg.page/grainkae3g/course/kae3g/grain-network-intro/12025-10-22-2148-PDT-moon-jyeshtha-pada2-asc-gemini22-sun-06thhouse-kae3g/`

## Immutable Grainpath Benefits

1. **Permanent URLs**: Each course version has a unique, permanent URL that never changes
2. **Version Control**: Easy to reference specific versions of course content
3. **Dual Hosting**: Content is available on both GitHub and Codeberg for redundancy
4. **Symlink Efficiency**: No duplication of content, just symlinks to the actual course directories

## Current Symlinks

### Active Course
- **Graintime**: `12025-10-22-2148-PDT-moon-jyeshtha-pada2-asc-gemini22-sun-06thhouse-kae3g`
- **Course Directory**: `kae3g-grain-network-intro-12025-10-22-2148-PDT-moon-jyeshtha-pada2-asc-gemini22-sun-06thhouse-kae3g`
- **GitHub Symlink**: `symlinks/github-pages/12025-10-22-2148-PDT-moon-jyeshtha-pada2-asc-gemini22-sun-06thhouse-kae3g`
- **Codeberg Symlink**: `symlinks/codeberg-pages/12025-10-22-2148-PDT-moon-jyeshtha-pada2-asc-gemini22-sun-06thhouse-kae3g`

## Management Commands

### Create New Symlinks
```bash
cd grainstore/graincourse/symlinks
ln -sf '../personal/{course-dir}' 'github-pages/{graintime}'
ln -sf '../personal/{course-dir}' 'codeberg-pages/{graintime}'
```

### List Active Symlinks
```bash
ls -la symlinks/github-pages/
ls -la symlinks/codeberg-pages/
```

### Update Symlinks (for new course versions)
```bash
bb symlinks/setup-symlinks.bb
```

## Integration with `gb flow`

The symlink structure is designed to work with the `gb flow` command, which will:

1. Build the course content in the `personal/{course-dir}/` directory
2. Create/update symlinks in `symlinks/github-pages/` and `symlinks/codeberg-pages/`
3. Deploy to both GitHub Pages and Codeberg Pages
4. Ensure the immutable grainpath URLs are accessible

## Future Enhancements

- **Automatic Symlink Creation**: Integrate symlink creation into the course creation process
- **Symlink Validation**: Verify that symlinks point to valid course directories
- **Cleanup Scripts**: Remove symlinks for old course versions
- **URL Testing**: Automatically test that symlinked URLs are accessible

## Notes

- Symlinks are created with relative paths to ensure they work across different systems
- The graintime in the symlink name should match the graintime in the course directory name
- Both GitHub and Codeberg symlinks point to the same source directory for consistency
- The symlink structure supports the Grain Network's immutable grainpath philosophy
