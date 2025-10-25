import Text "mo:base/Text";
import Time "mo:base/Time";
import Principal "mo:base/Principal";
import Buffer "mo:base/Buffer";
import Array "mo:base/Array";

persistent actor Grain6Website {
    // ═══════════════════════════════════════════════════════════
    // WEBSITE CONTENT AND PAGES
    // ═══════════════════════════════════════════════════════════
    
    public query func getHomePage() : Text {
        let graintime = getCurrentGraintime();
        let grainpath = getCurrentGrainpath();
        
        return "
<!DOCTYPE html>
<html lang=\"en\">
<head>
    <meta charset=\"UTF-8\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>Grain6 Get Started - Your Journey Begins Here</title>
    <meta name=\"description\" content=\"Welcome to Grain6 Get Started - your gateway to the Grain Network ecosystem\" />
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; min-height: 100vh; }
        .container { max-width: 800px; margin: 0 auto; padding: 20px; }
        .header { text-align: center; padding: 40px 0; background: rgba(255, 255, 255, 0.1); border-radius: 15px; margin-bottom: 30px; backdrop-filter: blur(10px); }
        .card { background: rgba(255, 255, 255, 0.1); padding: 30px; border-radius: 15px; margin-bottom: 20px; backdrop-filter: blur(10px); box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); }
        .btn { display: inline-block; background: linear-gradient(45deg, #ff6b6b, #ee5a24); color: white; text-decoration: none; padding: 12px 24px; border-radius: 25px; margin: 10px; transition: all 0.3s ease; }
        .btn:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3); }
        .graintime, .grainpath { background: rgba(0, 0, 0, 0.3); padding: 15px; border-radius: 10px; margin: 10px 0; font-family: 'Courier New', monospace; }
        .voice-indicator { background: rgba(255, 182, 193, 0.3); padding: 10px; border-radius: 10px; margin-bottom: 20px; text-align: center; font-weight: bold; }
        h1 { text-align: center; margin-bottom: 30px; font-size: 2.5em; text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); }
        h3 { color: #ffd700; margin-bottom: 15px; }
        ol, ul { margin-left: 20px; }
        li { margin-bottom: 10px; }
        .footer { text-align: center; padding: 30px 0; margin-top: 40px; border-top: 1px solid rgba(255, 255, 255, 0.3); }
    </style>
</head>
<body>
    <div class=\"container\">
        <header class=\"header\">
            <h1>🌾 Grain6 Get Started</h1>
            <p>Your Journey to Personal Sovereignty Begins Here</p>
        </header>
        
        <div class=\"voice-indicator\">🌸 Trish's Voice</div>
        
        <div class=\"card\">
            <h1>Welcome to Grain6 Get Started! ✨</h1>
            <p><strong>Ooh, that's such a cute idea! Let me help you make it even more sparkly! ✨</strong></p>
            <p>I love how you're thinking about this! Maybe we could add some more personality to it? This is going to be absolutely amazing! I'm so excited to work on this with you!</p>
        </div>
        
        <div class=\"card\">
            <h3>🌾 What is Grain6?</h3>
            <p>Grain6 is your personal gateway to the <strong>Grain Network</strong> - a world where technology meets wisdom, where every line of code tells a story, and where you're always exactly where you need to be.</p>
            
            <div class=\"grainpath\">
                <strong>Current Grainpath:</strong><br>
                <code>" # grainpath # "</code>
            </div>
            
            <div class=\"graintime\">
                <strong>Current Graintime:</strong><br>
                <code>" # graintime # "</code>
            </div>
        </div>
        
        <div class=\"card\">
            <h3>🚀 Your First Steps</h3>
            <p>Don't worry if this feels overwhelming at first. We'll walk through each step together, and soon you'll be creating beautiful contributions that make the whole network stronger!</p>
            
            <ol>
                <li><strong>Explore the Grain Network</strong> - Take time to understand what we're building and why it matters to you</li>
                <li><strong>Set up your environment</strong> - We'll help you get everything working beautifully</li>
                <li><strong>Make your first contribution</strong> - Start small and build confidence gradually</li>
                <li><strong>Join our community</strong> - Connect with other beautiful souls on this journey</li>
            </ol>
        </div>
        
        <div class=\"card\">
            <h3>💫 The Magic Behind the Scenes</h3>
            <ul>
                <li><strong>Your Personal Sheaf</strong> - You're part of something bigger - the 88-sheaf network where each person contributes their unique sparkle</li>
                <li><strong>Multi-Chain Wonder</strong> - We work with ICP, Hedera, and Solana - don't worry, we'll explain everything gently</li>
                <li><strong>Learning That Sticks</strong> - Our courses are designed like beautiful stories that you'll remember forever</li>
                <li><strong>Your Sovereign Space</strong> - Complete control over your development environment - no more feeling lost in someone else's system</li>
                <li><strong>Timeless Knowledge</strong> - Everything we build becomes part of an eternal, growing library of wisdom</li>
            </ul>
        </div>
        
        <div style=\"text-align: center; margin-top: 30px;\">
            <a href=\"/about\" class=\"btn\">Learn More About Us</a>
            <a href=\"/contact\" class=\"btn btn-secondary\">Get in Touch</a>
        </div>
        
        <footer class=\"footer\">
            <p>Built with ❤️ for personal sovereignty, append-only truth, and infinite recursive beauty</p>
            <p><em>\"Chaos flowing out calmly, solidity watching from within, feeling like a leaf but rooted like a rock\"</em></p>
            <p>🌾 From granules to grains to THE WHOLE GRAIN 🌾</p>
        </footer>
    </div>
</body>
</html>";
    };
    
    public query func getAboutPage() : async Text {
        return "
<!DOCTYPE html>
<html lang=\"en\">
<head>
    <meta charset=\"UTF-8\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>About Grain6 - The Grain Network</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; min-height: 100vh; }
        .container { max-width: 800px; margin: 0 auto; padding: 20px; }
        .header { text-align: center; padding: 40px 0; background: rgba(255, 255, 255, 0.1); border-radius: 15px; margin-bottom: 30px; backdrop-filter: blur(10px); }
        .card { background: rgba(255, 255, 255, 0.1); padding: 30px; border-radius: 15px; margin-bottom: 20px; backdrop-filter: blur(10px); box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); }
        .btn { display: inline-block; background: linear-gradient(45deg, #ff6b6b, #ee5a24); color: white; text-decoration: none; padding: 12px 24px; border-radius: 25px; margin: 10px; transition: all 0.3s ease; }
        h1 { text-align: center; margin-bottom: 30px; font-size: 2.5em; text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); }
        h3 { color: #ffd700; margin-bottom: 15px; }
    </style>
</head>
<body>
    <div class=\"container\">
        <header class=\"header\">
            <h1>🌾 About Grain6</h1>
            <p>Building the Future of Personal Sovereignty</p>
        </header>
        
        <div class=\"card\">
            <h3>🌟 Our Vision</h3>
            <p>Grain6 is more than just a platform - it's a movement toward personal sovereignty, append-only truth, and infinite recursive beauty. We believe technology should serve humanity, not the other way around.</p>
        </div>
        
        <div class=\"card\">
            <h3>🔧 Our Technology</h3>
            <p>Built on the Internet Computer Protocol (ICP), Grain6 leverages decentralized infrastructure to create truly sovereign digital spaces. No more dependence on centralized platforms or corporate control.</p>
        </div>
        
        <div class=\"card\">
            <h3>🌱 Our Philosophy</h3>
            <p>From granules to grains to THE WHOLE GRAIN - every small contribution matters, every individual is valued, and together we build something greater than the sum of its parts.</p>
        </div>
        
        <div style=\"text-align: center; margin-top: 30px;\">
            <a href=\"/\" class=\"btn\">Back to Home</a>
            <a href=\"/contact\" class=\"btn\">Get in Touch</a>
        </div>
    </div>
</body>
</html>";
    };
    
    public query func getContactPage() : async Text {
        return "
<!DOCTYPE html>
<html lang=\"en\">
<head>
    <meta charset=\"UTF-8\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>Contact Grain6 - Get in Touch</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; min-height: 100vh; }
        .container { max-width: 800px; margin: 0 auto; padding: 20px; }
        .header { text-align: center; padding: 40px 0; background: rgba(255, 255, 255, 0.1); border-radius: 15px; margin-bottom: 30px; backdrop-filter: blur(10px); }
        .card { background: rgba(255, 255, 255, 0.1); padding: 30px; border-radius: 15px; margin-bottom: 20px; backdrop-filter: blur(10px); box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); }
        .btn { display: inline-block; background: linear-gradient(45deg, #ff6b6b, #ee5a24); color: white; text-decoration: none; padding: 12px 24px; border-radius: 25px; margin: 10px; transition: all 0.3s ease; }
        h1 { text-align: center; margin-bottom: 30px; font-size: 2.5em; text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); }
        h3 { color: #ffd700; margin-bottom: 15px; }
    </style>
</head>
<body>
    <div class=\"container\">
        <header class=\"header\">
            <h1>🌾 Contact Grain6</h1>
            <p>Let's Build Something Beautiful Together</p>
        </header>
        
        <div class=\"card\">
            <h3>📧 Get in Touch</h3>
            <p>Ready to join the Grain Network? Have questions about personal sovereignty? Want to contribute to our mission?</p>
            <p><strong>Email:</strong> kae3g@grain6pbc.org</p>
            <p><strong>GitHub:</strong> github.com/grain6pbc</p>
            <p><strong>Website:</strong> grain6pbc.com</p>
        </div>
        
        <div class=\"card\">
            <h3>🤝 Join Our Community</h3>
            <p>We're building a community of developers, creators, and visionaries who believe in personal sovereignty and decentralized technology.</p>
            <p>Whether you're a seasoned developer or just starting your journey, there's a place for you in the Grain Network.</p>
        </div>
        
        <div style=\"text-align: center; margin-top: 30px;\">
            <a href=\"/\" class=\"btn\">Back to Home</a>
            <a href=\"/about\" class=\"btn\">Learn More</a>
        </div>
    </div>
</body>
</html>";
    };
    
    // ═══════════════════════════════════════════════════════════
    // GRAINTIME AND GRAINPATH FUNCTIONS
    // ═══════════════════════════════════════════════════════════
    
    private func getCurrentGraintime() : Text {
        let timestamp = Time.now();
        "12025-10-24--1033--PDT--moon-vishakha--asc-gem000--sun-11th--kae3g"
    };
    
    private func getCurrentGrainpath() : Text {
        "/course/kae3g/grain6-get/12025-10-24--1033--PDT--moon-vishakha------asc-gem000--sun-11th--kae3g/"
    };
    
    // ═══════════════════════════════════════════════════════════
    // WEBSITE ROUTING
    // ═══════════════════════════════════════════════════════════
    
    public query func getPage(path: Text) : async Text {
        switch (path) {
            case ("/") { getHomePage() };
            case ("/about") { getAboutPage() };
            case ("/contact") { getContactPage() };
            case (_) { getHomePage() };
        }
    };
    
    // ═══════════════════════════════════════════════════════════
    // CANISTER STATUS
    // ═══════════════════════════════════════════════════════════
    
    public query func status() : async Text {
        "Grain6 Website ICP Canister - Status: Active - Version: 0.1.0"
    };
    
    public query func whoami() : async Principal {
        Principal.fromActor(Grain6Website)
    };
}
