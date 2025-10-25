# Grainzsh Template Configuration
# Template/Personal Split - Grain Network Standard

# ============================================================================
# GRAIN NETWORK ZSH CONFIGURATION (TEMPLATE)
# ============================================================================
# This is the template configuration shared across all Grain Network users.
# Personal customizations go in: personal/USERNAME/.zshrc
# ============================================================================

# ----------------------------------------------------------------------------
# PATH CONFIGURATION (MUST BE FIRST)
# ----------------------------------------------------------------------------
# Add ~/.local/bin to PATH for Grain Network tools (gb, bb, gh, etc.)
export PATH="$HOME/.local/bin:$PATH"

# Grain Network paths
export GRAIN_HOME="$HOME/kae3g/grainkae3g"
export GRAINSTORE="$GRAIN_HOME/grainstore"

# ----------------------------------------------------------------------------
# PROMPT CONFIGURATION
# ----------------------------------------------------------------------------
# Minimalist lambda prompt - clean and fast
PROMPT='λ '

# ----------------------------------------------------------------------------
# GRAIN NETWORK ALIASES
# ----------------------------------------------------------------------------

# Grainbarrel (gb) build system
alias gb='gb'
alias grainbarrel='gb'

# Grainstore management
alias gs-validate='gb grainstore:validate'
alias gs-stats='gb grainstore:stats'
alias gs-list='gb grainstore:list'
alias gs-docs='gb grainstore:generate-docs'

# Git shortcuts with Grain flavor
alias g='git'
alias gs='git status'
alias ga='git add'
alias gc='git commit'
alias gp='git push'
alias gl='git log --oneline --graph --decorate'
alias gd='git diff'
alias gco='git checkout'
alias gb-new='git checkout -b'
alias gm='git checkout main'

# Grain Network flow
alias grain-flow='bb flow'
alias grain-pseudo='bb pseudo'
alias grain-deploy='bb deploy'

# Directory shortcuts
alias cdg='cd ~/kae3g/grainkae3g'
alias cdstore='cd ~/kae3g/grainkae3g/grainstore'
alias cddocs='cd ~/kae3g/grainkae3g/docs'

# Babashka/Grainbarrel tasks
alias bb='bb'
alias bbt='bb tasks'

# Display management
alias grain-display='gb display:info'
alias grain-warm='gb nightlight:status'

# Productivity
alias ll='ls -lah'
alias la='ls -A'
alias l='ls -CF'

# ----------------------------------------------------------------------------
# ENVIRONMENT VARIABLES
# ----------------------------------------------------------------------------

# Editor preferences
export EDITOR='nano'
export VISUAL='nano'

# Clojure/Babashka
export BABASHKA_CLASSPATH="$GRAINSTORE"

# ----------------------------------------------------------------------------
# FUNCTIONS
# ----------------------------------------------------------------------------

# Quick grain module navigation
grain() {
    if [ -z "$1" ]; then
        echo "Usage: grain <module-name>"
        echo "Example: grain grainbarrel"
        return 1
    fi
    cd "$GRAINSTORE/$1" || echo "Module not found: $1"
}

# Create new grain session document
grain-session() {
    local timestamp=$(date +"%Y-%m-%d--%H%M")
    local session_file="$GRAIN_HOME/docs/SESSION-$timestamp.md"
    echo "Creating new session: $session_file"
    touch "$session_file"
    $EDITOR "$session_file"
}

# Quick grainstore module search
grain-find() {
    if [ -z "$1" ]; then
        echo "Usage: grain-find <search-term>"
        return 1
    fi
    find "$GRAINSTORE" -type d -name "*$1*" -maxdepth 1
}

# ----------------------------------------------------------------------------
# HISTORY CONFIGURATION
# ----------------------------------------------------------------------------

HISTFILE=~/.zsh_history
HISTSIZE=10000
SAVEHIST=10000
setopt SHARE_HISTORY
setopt HIST_IGNORE_DUPS
setopt HIST_IGNORE_ALL_DUPS
setopt HIST_FIND_NO_DUPS
setopt HIST_SAVE_NO_DUPS

# ----------------------------------------------------------------------------
# COMPLETION SYSTEM
# ----------------------------------------------------------------------------

autoload -Uz compinit
compinit

# Case-insensitive completion
zstyle ':completion:*' matcher-list 'm:{a-zA-Z}={A-Za-z}'

# Colored completion
zstyle ':completion:*' list-colors ''

# ----------------------------------------------------------------------------
# KEY BINDINGS
# ----------------------------------------------------------------------------

# Emacs-style key bindings (familiar to most users)
bindkey -e

# ----------------------------------------------------------------------------
# GRAIN ENVIRONMENT VARIABLES
# ----------------------------------------------------------------------------

# Load from personal .env file (plain file method)
GRAIN_ENV_FILE="$GRAINSTORE/grainenvvars/personal/.env"
if [ -f "$GRAIN_ENV_FILE" ]; then
    export $(cat "$GRAIN_ENV_FILE" | grep -v '^#' | grep -v '^$' | xargs)
fi

# Or load from 1Password (more secure)
GRAIN_1PASS_LOADER="$GRAINSTORE/grainenvvars/personal/load-from-1password.sh"
if [ -f "$GRAIN_1PASS_LOADER" ]; then
    source "$GRAIN_1PASS_LOADER"
fi

# ----------------------------------------------------------------------------
# PERSONAL CONFIGURATION
# ----------------------------------------------------------------------------

# Source personal configuration if it exists
PERSONAL_CONFIG="$HOME/.config/grainzsh/personal/.zshrc"
if [ -f "$PERSONAL_CONFIG" ]; then
    source "$PERSONAL_CONFIG"
fi

# Or source from grainstore personal config
GRAINSTORE_PERSONAL="$GRAINSTORE/grainzsh/personal/.zshrc"
if [ -f "$GRAINSTORE_PERSONAL" ]; then
    source "$GRAINSTORE_PERSONAL"
fi

# ----------------------------------------------------------------------------
# WELCOME MESSAGE
# ----------------------------------------------------------------------------

# Show Grain Network welcome on new shell (optional - remove if unwanted)
if [ -t 0 ]; then
    echo "🌾 Grain Network Shell"
    echo "Type 'gb --help' for build system commands"
fi

# ============================================================================
# END GRAINZSH TEMPLATE CONFIGURATION
# ============================================================================

