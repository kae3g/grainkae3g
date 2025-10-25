import Text "mo:base/Text";

persistent actor GrainThriftSimple {
    public query func getHomePage() : async Text {
        "<!DOCTYPE html>
<html>
<head>
    <title>GrainThrift Demo</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f0f8ff; color: #333; margin: 0; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; }
        .header { background: white; padding: 20px; border-radius: 10px; margin-bottom: 20px; text-align: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .logo { font-size: 2em; font-weight: bold; color: #2c3e50; margin-bottom: 10px; }
        .product { background: white; padding: 20px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .product-image { width: 100%; height: 200px; object-fit: cover; border-radius: 8px; margin-bottom: 15px; }
        .product-title { font-size: 1.3em; font-weight: bold; color: #2c3e50; margin-bottom: 10px; }
        .product-price { font-size: 1.5em; font-weight: bold; color: #27ae60; margin-bottom: 15px; }
        .buy-button { background: #3498db; color: white; border: none; padding: 12px 24px; border-radius: 25px; font-weight: bold; cursor: pointer; font-size: 1.1em; }
        .buy-button:hover { background: #2980b9; }
        .demo-info { background: #e8f5e8; padding: 15px; border-radius: 8px; margin-bottom: 20px; border-left: 4px solid #27ae60; }
        .icp-badge { background: #ff6b6b; color: white; padding: 4px 12px; border-radius: 15px; font-size: 0.8em; margin-left: 8px; }
    </style>
</head>
<body>
    <div class=\"container\">
        <header class=\"header\">
            <div class=\"logo\">🌾 GrainThrift Demo</div>
            <p>Simple Base Case - Ralph Lauren Resale on ICP</p>
        </header>
        
        <div class=\"demo-info\">
            <strong>🎯 Demo Features:</strong><br>
            • Clotoko: Clojure → Motoko compilation<br>
            • ICP Payments: Buy with ICP tokens<br>
            • Unsplash Images: High-quality product photos<br>
            • Internet Identity: Secure authentication<br>
            • Decentralized: Runs on ICP blockchain
        </div>
        
        <div class=\"product\">
            <img src=\"https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400&h=400&fit=crop\" alt=\"Ralph Lauren Polo\" class=\"product-image\">
            <div class=\"product-title\">Ralph Lauren Classic Polo Shirt</div>
            <div class=\"product-price\">15.5 ICP <span class=\"icp-badge\">ICP</span></div>
            <p style=\"color: #666; margin-bottom: 15px;\">Excellent condition • Medium • Navy Blue</p>
            <button class=\"buy-button\" onclick=\"buyProduct('rl-polo-classic')\">🛒 Buy with ICP Tokens</button>
        </div>
        
        <div class=\"product\">
            <img src=\"https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop\" alt=\"Ralph Lauren Rugby\" class=\"product-image\">
            <div class=\"product-title\">Ralph Lauren Rugby Shirt</div>
            <div class=\"product-price\">22.0 ICP <span class=\"icp-badge\">ICP</span></div>
            <p style=\"color: #666; margin-bottom: 15px;\">Good condition • Large • Red & White</p>
            <button class=\"buy-button\" onclick=\"buyProduct('rl-rugby-shirt')\">🛒 Buy with ICP Tokens</button>
        </div>
        
        <div class=\"demo-info\">
            <strong>🚀 How it works:</strong><br>
            1. Click \"Buy with ICP Tokens\" button<br>
            2. Connect your ICP wallet (Internet Identity)<br>
            3. Confirm transaction on ICP blockchain<br>
            4. Product is yours! Decentralized ownership
        </div>
    </div>
    
    <script>
        function buyProduct(productId) {
            alert('🛒 Buying ' + productId + ' with ICP tokens!\\n\\n' +
                  'In a real implementation, this would:\\n' +
                  '• Connect to ICP wallet\\n' +
                  '• Process ICP payment\\n' +
                  '• Transfer ownership\\n' +
                  '• Update blockchain records');
        }
    </script>
</body>
</html>"
    };
    
    public query func getProduct(productId: Text) : async Text {
        switch (productId) {
            case ("rl-polo-classic") { "Ralph Lauren Classic Polo Shirt - 15.5 ICP - Excellent condition - Medium - Navy Blue" };
            case ("rl-rugby-shirt") { "Ralph Lauren Rugby Shirt - 22.0 ICP - Good condition - Large - Red & White" };
            case (_) { "Product not found" };
        }
    };
    
    public query func status() : async Text {
        "GrainThrift Demo - Status: Active - Simple Base Case"
    };
    
    public query func whoami() : async Text {
        "grainthrift-simple"
    };
}
