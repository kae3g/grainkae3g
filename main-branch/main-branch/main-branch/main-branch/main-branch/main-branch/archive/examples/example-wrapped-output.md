# Documentation Pipeline Example Output

This file demonstrates the 57-character line
wrapping that the pipeline applies to all
markdown documents. Notice how each paragraph
flows naturally while maintaining consistent
line length for optimal readability.

## Why 57 Characters?

The 57-character line limit provides several
important benefits for technical documentation:

- **Terminal Friendly** - Fits standard widths
- **Easy Reading** - Optimal line length
- **Git Friendly** - Clear diffs and changes
- **Professional** - Consistent appearance
- **Accessible** - Works in all editors

## Example Code Block

Code blocks are preserved exactly as written,
without wrapping, to maintain proper syntax:

```javascript
// This code block maintains its formatting
function buildDocumentation(source) {
  const wrapped = wrapText(source, 57);
  const parsed = parseMarkdown(wrapped);
  const validated = validateStructure(parsed);
  return generateSite(validated);
}
```

## Automated Pipeline

The documentation pipeline automatically
processes your markdown files through multiple
stages, each one adding value and ensuring
quality:

1. **Wrapping** - Apply 57-char line limit
2. **Parsing** - Extract document structure
3. **Validation** - Verify correct format
4. **Generation** - Create web components
5. **Building** - Compile to static HTML

## Lists and Formatting

Both ordered and unordered lists work great:

- First item in the list
- Second item with more content
- Third item with even more text to show wrap
- Fourth item demonstrates consistency

And numbered lists too:

1. First numbered item
2. Second numbered item with longer text
3. Third item showing how wrapping handles it
4. Fourth item for completeness

## Headers and Structure

Headers at all levels are preserved:

### Third Level Header
#### Fourth Level Header
##### Fifth Level Header

## Quotes and Emphasis

> This is a blockquote that demonstrates how
> quoted text is handled by the pipeline with
> proper formatting and line wrapping applied.

**Bold text** and *italic text* both work
perfectly within the 57-character constraint.

## Real-World Usage

When you write documentation using this
pipeline, you can focus on content without
worrying about formatting. The system handles
all the details automatically, ensuring your
output is always professional and consistent.

Simply write your markdown naturally, run the
build command, and your documentation becomes
a beautiful static website ready to deploy.

## Conclusion

This example shows the complete formatting
capabilities of the documentation pipeline.
Try creating your own markdown files and watch
them transform into professional documentation
with just one command: `bb build:pipeline`

