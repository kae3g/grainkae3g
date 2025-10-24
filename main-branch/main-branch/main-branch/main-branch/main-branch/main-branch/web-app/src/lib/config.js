// Auto-generated from .config.edn - DO NOT EDIT MANUALLY
// Run: bb scripts/generate-site-config.bb

export const siteConfig = {
  "site" : {
    "title" : "kae3g",
    "subtitle" : "Experimental aspiringly helpful generative AI writings",
    "url" : "https://kae3g.codeberg.page/12025-10/",
    "github-repo" : "https://codeberg.org/kae3g/12025-10/",
    "base-path" : "/12025-10"
  },
  "author" : {
    "name" : "kae3g",
    "copyright-year" : "2025",
    "email" : null,
    "github-username" : "kae3g"
  },
  "license" : {
    "type" : "dual",
    "primary" : "Apache-2.0",
    "secondary" : "MIT",
    "apache-url" : "https://www.apache.org/licenses/LICENSE-2.0",
    "mit-url" : "https://opensource.org/licenses/MIT",
    "essays-license" : "CC BY 4.0",
    "essays-url" : "https://creativecommons.org/licenses/by/4.0/"
  },
  "footer" : {
    "tagline" : "Contemplative technology in service of clarity and beauty",
    "emoji" : "🌱",
    "show-on-index" : true,
    "show-on-essays" : true
  },
  "theme" : {
    "primary-color" : "#8B745E",
    "background" : "#1A1410",
    "text" : "#E8DCC8",
    "link" : "#8B745E",
    "code-bg" : "#2A2420",
    "code-fg" : "#D4C5B0"
  },
  "features" : {
    "infinite-scroll" : true,
    "items-per-page" : 10,
    "show-timestamps" : true,
    "show-categories" : true,
    "show-reading-time" : true
  },
  "build" : {
    "content-dir" : "writings",
    "output-dir" : "web-app/static/content",
    "validate-links" : true,
    "generate-sitemap" : true
  }
};
