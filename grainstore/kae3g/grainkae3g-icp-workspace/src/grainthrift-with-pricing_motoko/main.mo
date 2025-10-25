import Text "mo:base/Text";
import Float "mo:base/Float";
import Time "mo:base/Time";
import Buffer "mo:base/Buffer";
import Array "mo:base/Array";

persistent actor GrainThriftWithPricing {
    // ═══════════════════════════════════════════════════════════
    // ICP PRICE INDEXING
    // ═══════════════════════════════════════════════════════════
    
    private var currentICPPrice: Float = 12.50; // Default price
    private var lastPriceUpdate: Time.Time = 0;
    
    // Price update interval (30 seconds in nanoseconds)
    private let PRICE_UPDATE_INTERVAL: Time.Time = 30_000_000_000;
    
    public func updateICPPrice(newPrice: Float): async () {
        currentICPPrice := newPrice;
        lastPriceUpdate := Time.now();
    };
    
    public query func getICPPrice(): async Float {
        currentICPPrice
    };
    
    public query func getLastPriceUpdate(): async Time.Time {
        lastPriceUpdate
    };
    
    // ═══════════════════════════════════════════════════════════
    // PRODUCT CATALOG WITH USD PRICING
    // ═══════════════════════════════════════════════════════════
    
    private let products = [
        {
            id = "rl-polo-classic";
            name = "Ralph Lauren Classic Polo Shirt";
            priceICP = 15.5;
            condition = "Excellent";
            size = "Medium";
            color = "Navy Blue";
            imageUrl = "https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400&h=400&fit=crop";
            sellerId = "user-001";
            listedDate = "2025-10-24";
            status = "available";
        },
        {
            id = "rl-rugby-shirt";
            name = "Ralph Lauren Rugby Shirt";
            priceICP = 22.0;
            condition = "Good";
            size = "Large";
            color = "Red & White";
            imageUrl = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop";
            sellerId = "user-002";
            listedDate = "2025-10-23";
            status = "available";
        },
        {
            id = "rl-chinos";
            name = "Ralph Lauren Chino Pants";
            priceICP = 28.5;
            condition = "Like New";
            size = "32x32";
            color = "Khaki";
            imageUrl = "https://images.unsplash.com/photo-1591195853828-11db59a44f6b?w=400&h=400&fit=crop";
            sellerId = "user-003";
            listedDate = "2025-10-22";
            status = "available";
        }
    ];
    
    public query func getProducts(): async [Text] {
        Array.map<{id: Text; name: Text; priceICP: Float; condition: Text; size: Text; color: Text; imageUrl: Text; sellerId: Text; listedDate: Text; status: Text}, Text>(products, func(product) {
            let usdPrice = product.priceICP * currentICPPrice;
            "\"" # product.id # "\": {\"name\": \"" # product.name # "\", \"priceICP\": " # Float.toText(product.priceICP) # ", \"priceUSD\": " # Float.toText(usdPrice) # ", \"condition\": \"" # product.condition # "\", \"size\": \"" # product.size # "\", \"color\": \"" # product.color # "\", \"imageUrl\": \"" # product.imageUrl # "\", \"status\": \"" # product.status # "\"}"
        })
    };
    
    public query func getProduct(productId: Text): async Text {
        switch (Array.find<{id: Text; name: Text; priceICP: Float; condition: Text; size: Text; color: Text; imageUrl: Text; sellerId: Text; listedDate: Text; status: Text}>(products, func(product) { product.id == productId })) {
            case (?product) {
                let usdPrice = product.priceICP * currentICPPrice;
                "{\"id\": \"" # product.id # "\", \"name\": \"" # product.name # "\", \"priceICP\": " # Float.toText(product.priceICP) # ", \"priceUSD\": " # Float.toText(usdPrice) # ", \"condition\": \"" # product.condition # "\", \"size\": \"" # product.size # "\", \"color\": \"" # product.color # "\", \"imageUrl\": \"" # product.imageUrl # "\", \"status\": \"" # product.status # "\"}"
            };
            case null { "Product not found" };
        }
    };
    
    // ═══════════════════════════════════════════════════════════
    // WEBSITE PAGES WITH THEME SUPPORT
    // ═══════════════════════════════════════════════════════════
    
    public query func getHomePage(): async Text {
        "<!DOCTYPE html>
<html lang=\"en\">
<head>
    <meta charset=\"UTF-8\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>GrainThrift - Ralph Lauren Resale Marketplace</title>
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
        
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            background: var(--bg-primary); 
            color: var(--text-primary); 
            transition: all 0.3s ease;
        }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .header { 
            background: var(--bg-secondary); 
            padding: 20px; 
            border-radius: 15px; 
            margin-bottom: 30px; 
            box-shadow: 0 4px 20px var(--shadow);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .logo { font-size: 2.5em; font-weight: bold; color: var(--accent); }
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
        .theme-toggle:hover { transform: scale(1.05); }
        .icp-price { 
            background: var(--success); 
            color: white; 
            padding: 8px 16px; 
            border-radius: 20px; 
            font-weight: bold;
        }
        .products-grid { 
            display: grid; 
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); 
            gap: 30px; 
            margin-bottom: 30px; 
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
        .product-image { width: 100%; height: 250px; object-fit: cover; }
        .product-info { padding: 20px; }
        .product-title { font-size: 1.2em; font-weight: bold; margin-bottom: 10px; color: var(--text-primary); }
        .price-container { margin-bottom: 15px; }
        .icp-price { font-size: 1.5em; font-weight: bold; color: var(--success); margin-bottom: 5px; }
        .usd-price { font-size: 1.1em; color: var(--text-secondary); }
        .product-details { color: var(--text-secondary); font-size: 0.9em; margin-bottom: 15px; }
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
        .live-price-banner {
            background: linear-gradient(45deg, var(--accent), #2980b9);
            color: white;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            text-align: center;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class=\"container\">
        <header class=\"header\">
            <div class=\"logo\">🌾 GrainThrift</div>
            <div style=\"display: flex; gap: 15px; align-items: center;\">
                <div class=\"icp-price\" id=\"icp-price\">ICP: Loading...</div>
                <button class=\"theme-toggle\" onclick=\"toggleTheme()\">🌙</button>
            </div>
        </header>
        
        <div class=\"live-price-banner\">
            💰 Live ICP Price Indexing • Real-time USD Conversion • Decentralized Marketplace
        </div>
        
        <div class=\"products-grid\" id=\"products-grid\">
            <!-- Products will be loaded here -->
        </div>
    </div>
    
    <script>
        let currentTheme = 'light';
        let icpPrice = 0;
        
        function toggleTheme() {
            currentTheme = currentTheme === 'light' ? 'dark' : 'light';
            document.documentElement.setAttribute('data-theme', currentTheme);
            document.querySelector('.theme-toggle').textContent = currentTheme === 'light' ? '🌙' : '☀️';
            localStorage.setItem('theme', currentTheme);
        }
        
        async function loadICPPrice() {
            try {
                // In a real implementation, this would call the ICP canister
                // For demo, we'll simulate a price update
                icpPrice = 12.50 + (Math.random() - 0.5) * 2; // Simulate price fluctuation
                document.getElementById('icp-price').textContent = `ICP: $${icpPrice.toFixed(2)} USD`;
                loadProducts();
            } catch (error) {
                console.error('Error loading ICP price:', error);
            }
        }
        
        async function loadProducts() {
            const products = [
                {
                    id: 'rl-polo-classic',
                    name: 'Ralph Lauren Classic Polo Shirt',
                    priceICP: 15.5,
                    condition: 'Excellent',
                    size: 'Medium',
                    color: 'Navy Blue',
                    imageUrl: 'https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400&h=400&fit=crop',
                    status: 'available'
                },
                {
                    id: 'rl-rugby-shirt',
                    name: 'Ralph Lauren Rugby Shirt',
                    priceICP: 22.0,
                    condition: 'Good',
                    size: 'Large',
                    color: 'Red & White',
                    imageUrl: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop',
                    status: 'available'
                },
                {
                    id: 'rl-chinos',
                    name: 'Ralph Lauren Chino Pants',
                    priceICP: 28.5,
                    condition: 'Like New',
                    size: '32x32',
                    color: 'Khaki',
                    imageUrl: 'https://images.unsplash.com/photo-1591195853828-11db59a44f6b?w=400&h=400&fit=crop',
                    status: 'available'
                }
            ];
            
            const productsGrid = document.getElementById('products-grid');
            productsGrid.innerHTML = products.map(product => {
                const usdPrice = product.priceICP * icpPrice;
                return `
                    <div class=\"product-card\">
                        <img src=\"${product.imageUrl}\" alt=\"${product.name}\" class=\"product-image\">
                        <div class=\"product-info\">
                            <div class=\"product-title\">${product.name}</div>
                            <div class=\"price-container\">
                                <div class=\"icp-price\">${product.priceICP} ICP</div>
                                <div class=\"usd-price\">$${usdPrice.toFixed(2)} USD</div>
                            </div>
                            <div class=\"product-details\">${product.condition} • ${product.size} • ${product.color}</div>
                            <button class=\"buy-button\" onclick=\"buyProduct('${product.id}')\">🛒 Buy with ICP Tokens</button>
                        </div>
                    </div>
                `;
            }).join('');
        }
        
        function buyProduct(productId) {
            alert(`🛒 Buying ${productId} with ICP tokens!\\n\\n` +
                  `In a real implementation, this would:\\n` +
                  `• Connect to ICP wallet\\n` +
                  `• Process ICP payment\\n` +
                  `• Transfer ownership\\n` +
                  `• Update blockchain records\\n\\n` +
                  `Current ICP Price: $${icpPrice.toFixed(2)} USD`);
        }
        
        // Initialize
        document.addEventListener('DOMContentLoaded', function() {
            const savedTheme = localStorage.getItem('theme') || 'light';
            currentTheme = savedTheme;
            document.documentElement.setAttribute('data-theme', currentTheme);
            document.querySelector('.theme-toggle').textContent = currentTheme === 'light' ? '🌙' : '☀️';
            
            loadICPPrice();
            
            // Update price every 30 seconds
            setInterval(loadICPPrice, 30000);
        });
    </script>
</body>
</html>"
    };
    
    // ═══════════════════════════════════════════════════════════
    // CANISTER STATUS
    // ═══════════════════════════════════════════════════════════
    
    public query func status(): async Text {
        "GrainThrift with Live ICP Pricing - Status: Active - Version: 0.2.0 (Clelte + Clotoko)"
    };
    
    public query func whoami(): async Text {
        "grainthrift-with-pricing"
    };
}


