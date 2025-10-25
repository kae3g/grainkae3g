# Homebrew Formula for Grainspace
# Install with: brew install grainspace
# Or via tap: brew tap grainpbc/grainspace && brew install grainspace

class Grainspace < Formula
  desc "Unified decentralized platform for the Grain Network"
  homepage "https://grain.network"
  url "https://github.com/grainpbc/grainspace/archive/refs/tags/v0.1.0.tar.gz"
  sha256 "YOUR_SHA256_HASH_HERE" # Update this when releasing
  license "MIT"
  
  # Supported platforms
  depends_on :macos => :catalina # Minimum macOS 10.15
  # Linux support via Linuxbrew
  
  # Dependencies
  depends_on "openjdk@17" # For Clojure
  depends_on "clojure/tools/clojure" # Clojure CLI
  depends_on "borkdude/brew/babashka" # Babashka for scripts
  depends_on "node" # For web interface
  depends_on "git" # For grainsource integration
  
  # Optional dependencies
  depends_on "dfx" => :optional # ICP SDK (if available)
  
  def install
    # Set up Java environment
    ENV["JAVA_HOME"] = Formula["openjdk@17"].opt_prefix
    
    # Install Clojure dependencies
    system "clojure", "-P" # Download dependencies
    
    # Build the application
    system "clojure", "-M:build"
    
    # Install binaries
    bin.install "target/grainspace" => "grainspace"
    
    # Install libraries
    lib.install Dir["target/lib/*"]
    
    # Install share files
    (share/"grainspace").install Dir["resources/*"]
    
    # Install man pages (if they exist)
    if File.directory?("doc/man")
      man1.install Dir["doc/man/*.1"]
    end
    
    # Create wrapper script
    (bin/"grainspace").write <<~EOS
      #!/bin/bash
      export GRAINSPACE_HOME="#{prefix}"
      export GRAINSPACE_LIB="#{lib}"
      export JAVA_HOME="#{Formula["openjdk@17"].opt_prefix}"
      exec "#{Formula["clojure/tools/clojure"].opt_bin}/clojure" \\
        -Sdeps '{:paths ["#{lib}"] :deps {}}' \\
        -M -m grainspace.core "$@"
    EOS
    
    # Make wrapper executable
    chmod 0755, bin/"grainspace"
  end
  
  def post_install
    # Create necessary directories
    (var/"grainspace").mkpath
    (var/"grainspace/data").mkpath
    (var/"grainspace/config").mkpath
    
    # Set up configuration
    unless File.exist?(etc/"grainspace/config.edn")
      (etc/"grainspace").mkpath
      (etc/"grainspace/config.edn").write <<~EOS
        {:grainspace
         {:mode :production
          :data-dir "#{var}/grainspace/data"
          :config-dir "#{etc}/grainspace"
          :icp
          {:enabled false
           :canister-id nil}
          :nostr
          {:enabled false
           :relay nil}
          :urbit
          {:enabled false
           :ship nil}}}
      EOS
    end
  end
  
  def caveats
    <<~EOS
      🌾 Grainspace has been installed!
      
      Configuration file: #{etc}/grainspace/config.edn
      Data directory: #{var}/grainspace/data
      
      To start Grainspace:
        grainspace serve
      
      To configure ICP integration:
        grainspace config set icp.enabled true
        grainspace config set icp.canister-id YOUR_CANISTER_ID
      
      For more information:
        grainspace help
        man grainspace
        
      Documentation: https://grain.network/docs
    EOS
  end
  
  service do
    run [opt_bin/"grainspace", "serve"]
    keep_alive true
    log_path var/"log/grainspace.log"
    error_log_path var/"log/grainspace.error.log"
    working_dir var/"grainspace"
    environment_variables GRAINSPACE_ENV: "production"
  end
  
  test do
    # Test that grainspace runs
    system bin/"grainspace", "version"
    assert_match "Grainspace v#{version}", shell_output("#{bin}/grainspace version")
    
    # Test configuration
    assert_predicate etc/"grainspace/config.edn", :exist?
    
    # Test data directory
    assert_predicate var/"grainspace/data", :directory?
  end
end

# Linuxbrew-specific configuration
if OS.linux?
  class Grainspace
    # Linux-specific dependencies
    depends_on "gcc" # For native compilation
    depends_on "glibc" # Standard C library
    
    def install
      super
      
      # Linux-specific post-install
      # Set up systemd service (optional)
      if File.directory?("/etc/systemd/system")
        (buildpath/"grainspace.service").write <<~EOS
          [Unit]
          Description=Grainspace Service
          After=network.target
          
          [Service]
          Type=simple
          User=#{ENV["USER"]}
          ExecStart=#{opt_bin}/grainspace serve
          Restart=on-failure
          RestartSec=5s
          
          [Install]
          WantedBy=multi-user.target
        EOS
        
        puts "Systemd service file created at: #{buildpath}/grainspace.service"
        puts "To install: sudo cp #{buildpath}/grainspace.service /etc/systemd/system/"
        puts "To enable: sudo systemctl enable grainspace"
        puts "To start: sudo systemctl start grainspace"
      end
    end
  end
end


