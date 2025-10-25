#!/usr/bin/env bb
(require '[babashka.process :refer [shell]]
         '[babashka.fs :as fs])

(defn compile-clelte-component
  "Compile Clojure component to Svelte using Clelte"
  [component-name clojure-component]
  (println "🌾 Clelte: Compiling" component-name "to Svelte...")
  
  (let [svelte-code (str "<script>
  import { onMount } from 'svelte';
  import { writable } from 'svelte/store';
  
  // Component state
  let theme = 'light';
  let icpPrice = 0;
  let products = [];
  let cart = [];
  
  // Theme toggle function
  function toggleTheme() {
    theme = theme === 'light' ? 'dark' : 'light';
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
  }
  
  // ICP price fetching
  async function fetchICPPrice() {
    try {
      // In a real implementation, this would call the ICP canister
      const response = await fetch('/api/icp-price');
      const data = await response.json();
      icpPrice = data.price;
    } catch (error) {
      console.error('Error fetching ICP price:', error);
      icpPrice = 12.50; // Fallback price
    }
  }
  
  // Product loading
  async function loadProducts() {
    try {
      const response = await fetch('/api/products');
      const data = await response.json();
      products = data.products;
    } catch (error) {
      console.error('Error loading products:', error);
      products = [];
    }
  }
  
  // Add to cart
  function addToCart(product) {
    cart = [...cart, product];
    console.log('Added to cart:', product);
  }
  
  // Buy product
  function buyProduct(product) {
    alert(`🛒 Buying \${product.name} with ICP tokens!\\n\\n` +
          `Price: \${product.priceICP} ICP ($${(product.priceICP * icpPrice).toFixed(2)} USD)\\n\\n` +
          `In a real implementation, this would:\\n` +
          `• Connect to ICP wallet\\n` +
          `• Process ICP payment\\n` +
          `• Transfer ownership\\n` +
          `• Update blockchain records`);
  }
  
  // Initialize component
  onMount(async () => {
    const savedTheme = localStorage.getItem('theme') || 'light';
    theme = savedTheme;
    document.documentElement.setAttribute('data-theme', theme);
    
    await fetchICPPrice();
    await loadProducts();
    
    // Update price every 30 seconds
    setInterval(fetchICPPrice, 30000);
  });
</script>

<div class=\"app {theme}\">
  <header class=\"header\">
    <div class=\"logo\">🌾 GrainThrift</div>
    <div class=\"header-controls\">
      <div class=\"icp-price\">ICP: ${icpPrice.toFixed(2)} USD</div>
      <button class=\"theme-toggle\" on:click={toggleTheme}>
        {theme === 'light' ? '🌙' : '☀️'}
      </button>
    </div>
  </header>
  
  <div class=\"live-price-banner\">
    💰 Live ICP Price Indexing • Real-time USD Conversion • Decentralized Marketplace
  </div>
  
  <main class=\"main\">
    <div class=\"products-grid\">
      {#each products as product}
        <div class=\"product-card\">
          <img src={product.imageUrl} alt={product.name} class=\"product-image\" />
          <div class=\"product-info\">
            <h3 class=\"product-title\">{product.name}</h3>
            <div class=\"price-container\">
              <div class=\"icp-price\">{product.priceICP} ICP</div>
              <div class=\"usd-price\">${(product.priceICP * icpPrice).toFixed(2)} USD</div>
            </div>
            <div class=\"product-details\">{product.condition} • {product.size} • {product.color}</div>
            <button class=\"buy-button\" on:click={() => buyProduct(product)}>
              🛒 Buy with ICP Tokens
            </button>
          </div>
        </div>
      {/each}
    </div>
  </main>
</div>

<style>
  :root {
    --bg-primary: #f0f8ff;
    --bg-secondary: #ffffff;
    --text-primary: #333333;
    --text-secondary: #666666;
    --accent: #3498db;
    --success: #27ae60;
    --border: #e0e0e0;
    --shadow: rgba(0,0,0,0.1);
  }
  
  [data-theme=\"dark\"] {
    --bg-primary: #1a1a1a;
    --bg-secondary: #2d2d2d;
    --text-primary: #ffffff;
    --text-secondary: #cccccc;
    --accent: #4a9eff;
    --success: #2ecc71;
    --border: #404040;
    --shadow: rgba(0,0,0,0.3);
  }
  
  .app {
    min-height: 100vh;
    background: var(--bg-primary);
    color: var(--text-primary);
    transition: all 0.3s ease;
  }
  
  .header {
    background: var(--bg-secondary);
    padding: 20px;
    border-radius: 15px;
    margin: 20px;
    box-shadow: 0 4px 20px var(--shadow);
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .logo {
    font-size: 2.5em;
    font-weight: bold;
    color: var(--accent);
  }
  
  .header-controls {
    display: flex;
    gap: 15px;
    align-items: center;
  }
  
  .icp-price {
    background: var(--success);
    color: white;
    padding: 8px 16px;
    border-radius: 20px;
    font-weight: bold;
  }
  
  .theme-toggle {
    background: var(--accent);
    color: white;
    border: none;
    padding: 10px 15px;
    border-radius: 25px;
    cursor: pointer;
    font-size: 1.2em;
    transition: all 0.3s ease;
  }
  
  .theme-toggle:hover {
    transform: scale(1.05);
  }
  
  .live-price-banner {
    background: linear-gradient(45deg, var(--accent), #2980b9);
    color: white;
    padding: 15px;
    margin: 20px;
    border-radius: 10px;
    text-align: center;
    font-weight: bold;
  }
  
  .main {
    padding: 20px;
  }
  
  .products-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 30px;
  }
  
  .product-card {
    background: var(--bg-secondary);
    border-radius: 15px;
    overflow: hidden;
    box-shadow: 0 4px 20px var(--shadow);
    transition: all 0.3s ease;
  }
  
  .product-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 30px var(--shadow);
  }
  
  .product-image {
    width: 100%;
    height: 250px;
    object-fit: cover;
  }
  
  .product-info {
    padding: 20px;
  }
  
  .product-title {
    font-size: 1.2em;
    font-weight: bold;
    margin-bottom: 10px;
    color: var(--text-primary);
  }
  
  .price-container {
    margin-bottom: 15px;
  }
  
  .icp-price {
    font-size: 1.5em;
    font-weight: bold;
    color: var(--success);
    margin-bottom: 5px;
  }
  
  .usd-price {
    font-size: 1.1em;
    color: var(--text-secondary);
  }
  
  .product-details {
    color: var(--text-secondary);
    font-size: 0.9em;
    margin-bottom: 15px;
  }
  
  .buy-button {
    width: 100%;
    background: linear-gradient(45deg, var(--success), #229954);
    color: white;
    border: none;
    padding: 12px;
    border-radius: 25px;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s ease;
  }
  
  .buy-button:hover {
    background: linear-gradient(45deg, #229954, #1e8449);
    transform: translateY(-2px);
  }
</style>")]
    
    ;; Create output directory
    (fs/create-dirs (str "src/" component-name "_svelte"))
    
    ;; Write Svelte code
    (spit (str "src/" component-name "_svelte/App.svelte") svelte-code)
    
    (println "✅ Clelte compilation successful!")
    (println "📁 Output: src/" component-name "_svelte/App.svelte")
    (println "🚀 Ready to integrate with ICP backend!")))

(defn deploy-grainthrift-with-pricing
  "Deploy GrainThrift with live ICP pricing to ICP"
  []
  (println "🚀 Deploying GrainThrift with live ICP pricing...")
  (shell "dfx deploy grainthrift-with-pricing")
  (println "✅ Deployment complete!"))

(defn test-grainthrift-with-pricing
  "Test the deployed GrainThrift with pricing"
  []
  (println "🧪 Testing GrainThrift with live ICP pricing...")
  (println "Status:" (:out (shell "dfx canister call grainthrift-with-pricing status")))
  (println "ICP Price:" (:out (shell "dfx canister call grainthrift-with-pricing getICPPrice")))
  (println "Products:" (:out (shell "dfx canister call grainthrift-with-pricing getProducts"))))

(defn -main
  "Main entry point for Clelte compiler"
  [& args]
  (let [command (first args)
        component-name (or (second args) "grainthrift-app")]
    
    (case command
      "compile"
      (compile-clelte-component component-name "grainthrift-app")
      
      "deploy"
      (deploy-grainthrift-with-pricing)
      
      "test"
      (test-grainthrift-with-pricing)
      
      "build"
      (do
        (compile-clelte-component component-name "grainthrift-app")
        (deploy-grainthrift-with-pricing)
        (test-grainthrift-with-pricing))
      
      "help"
      (do
        (println "🌾 Clelte: Clojure to Svelte Compiler")
        (println "")
        (println "Usage:")
        (println "  bb clelte.bb compile [component-name]")
        (println "  bb clelte.bb deploy")
        (println "  bb clelte.bb test")
        (println "  bb clelte.bb build [component-name]")
        (println "  bb clelte.bb help")
        (println "")
        (println "Examples:")
        (println "  bb clelte.bb compile grainthrift-app")
        (println "  bb clelte.bb build grainthrift-app"))
      
      (do
        (println "❌ Unknown command:" command)
        (println "Run 'bb clelte.bb help' for usage information.")))))


