import Text "mo:base/Text";

persistent actor GrainThriftClojure {
    public query func getHomePage() : async Text {
        "<!DOCTYPE html>
<html>
<head>
    <title>GrainThrift - Ralph Lauren Resale</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f5f7fa; color: #333; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .header { background: white; padding: 20px; border-radius: 15px; margin-bottom: 30px; }
        .logo { font-size: 2.5em; font-weight: bold; color: #2c3e50; text-align: center; }
        .hero { text-align: center; padding: 60px 20px; background: white; border-radius: 15px; margin-bottom: 30px; }
        .products-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 30px; }
        .product-card { background: white; border-radius: 15px; overflow: hidden; }
        .product-image { width: 100%; height: 250px; object-fit: cover; }
        .product-info { padding: 20px; }
        .product-title { font-size: 1.2em; font-weight: bold; margin-bottom: 10px; color: #2c3e50; }
        .product-price { font-size: 1.5em; font-weight: bold; color: #27ae60; margin-bottom: 10px; }
        .buy-button { width: 100%; background: #27ae60; color: white; border: none; padding: 12px; border-radius: 25px; font-weight: bold; cursor: pointer; }
        .icp-badge { background: #ff6b6b; color: white; padding: 5px 15px; border-radius: 20px; font-size: 0.8em; margin-left: 10px; }
    </style>
</head>
<body>
    <div class=\"container\">
        <header class=\"header\">
            <div class=\"logo\">🌾 GrainThrift</div>
            <p>Ralph Lauren Resale Marketplace on ICP</p>
        </header>
        
        <section class=\"hero\">
            <h1>Buy & Sell Ralph Lauren with ICP Tokens</h1>
            <p>Join the decentralized fashion marketplace powered by Internet Computer Protocol</p>
        </section>
        
        <section class=\"products-grid\">
            <div class=\"product-card\">
                <img src=\"https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400&h=400&fit=crop\" alt=\"Ralph Lauren Polo\" class=\"product-image\">
                <div class=\"product-info\">
                    <div class=\"product-title\">Ralph Lauren Classic Polo Shirt</div>
                    <div class=\"product-price\">15.5 ICP <span class=\"icp-badge\">ICP</span></div>
                    <button class=\"buy-button\" onclick=\"buyProduct('rl-polo-classic')\">Buy Now</button>
                </div>
            </div>
            <div class=\"product-card\">
                <img src=\"https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop\" alt=\"Ralph Lauren Rugby\" class=\"product-image\">
                <div class=\"product-info\">
                    <div class=\"product-title\">Ralph Lauren Rugby Shirt</div>
                    <div class=\"product-price\">22.0 ICP <span class=\"icp-badge\">ICP</span></div>
                    <button class=\"buy-button\" onclick=\"buyProduct('rl-rugby-shirt')\">Buy Now</button>
                </div>
            </div>
            <div class=\"product-card\">
                <img src=\"https://images.unsplash.com/photo-1591195853828-11db59a44f6b?w=400&h=400&fit=crop\" alt=\"Ralph Lauren Chinos\" class=\"product-image\">
                <div class=\"product-info\">
                    <div class=\"product-title\">Ralph Lauren Chino Pants</div>
                    <div class=\"product-price\">28.5 ICP <span class=\"icp-badge\">ICP</span></div>
                    <button class=\"buy-button\" onclick=\"buyProduct('rl-chinos')\">Buy Now</button>
                </div>
            </div>
        </section>
    </div>
    
    <script>
        function buyProduct(productId) {
            alert('Buying product ' + productId + ' with ICP tokens!');
        }
    </script>
</body>
</html>"
    };
    
    public query func status() : async Text {
        "GrainThrift ICP Canister - Status: Active - Version: 0.1.0 (Clotoko)"
    };
    
    public query func whoami() : async Text {
        "grainthrift-clojure"
    };
}