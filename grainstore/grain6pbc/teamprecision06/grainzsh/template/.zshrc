# ============================================================================
# GRAINZSH - The Lovers' Shell Configuration
# ============================================================================
# teamprecision06 (Virgo ♍ / VI. The Lovers)
# "Every configuration is a conscious choice"
# ============================================================================

# ----------------------------------------------------------------------------
# CORE PHILOSOPHY
# ----------------------------------------------------------------------------
# The Lovers teach: Perfect union comes from perfect choices
# This .zshrc is your marriage vow to your shell environment
# Every line: a commitment. Every alias: a promise.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# PROMPT - The Lambda (λ) Choice
# ----------------------------------------------------------------------------
# Simple. Clean. Functional. Timeless.
PROMPT='λ '

# ----------------------------------------------------------------------------
# HISTORY - Remember Your Choices
# ----------------------------------------------------------------------------
HISTFILE=~/.zsh_history
HISTSIZE=10000
SAVEHIST=10000
setopt SHARE_HISTORY          # Share history across terminals
setopt HIST_IGNORE_DUPS       # No duplicate commands
setopt HIST_IGNORE_SPACE      # Ignore commands starting with space
setopt HIST_REDUCE_BLANKS     # Remove unnecessary blanks

# ----------------------------------------------------------------------------
# COMPLETION - Intelligent Selection
# ----------------------------------------------------------------------------
autoload -Uz compinit
compinit

zstyle ':completion:*' matcher-list 'm:{a-z}={A-Za-z}'  # Case-insensitive
zstyle ':completion:*' menu select                       # Menu selection
zstyle ':completion:*' list-colors "${(s.:.)LS_COLORS}" # Colored

# ----------------------------------------------------------------------------
# ENVIRONMENT VARIABLES - The First Choice
# ----------------------------------------------------------------------------

# Grain Network paths
export GRAIN_HOME="$HOME/kae3g/grainkae3g"
export GRAINSTORE="$GRAIN_HOME/grainstore"

# Editor choice (conscious selection)
export EDITOR="vim"
export VISUAL="vim"

# Path choice (only essential binaries)
export PATH="$HOME/.local/bin:$PATH"
export PATH="$GRAINSTORE/grain6pbc/teamstructure10/graintime/bin:$PATH"

# ----------------------------------------------------------------------------
# GRAINENVVARS INTEGRATION - Load Environment Variables
# ----------------------------------------------------------------------------

# Load personal environment variables (if they exist)
GRAIN_ENV_FILE="$GRAINSTORE/grain6pbc/teamprecision06/grainenvvars/personal/.env"
if [ -f "$GRAIN_ENV_FILE" ]; then
    export $(cat "$GRAIN_ENV_FILE" | grep -v '^#' | grep -v '^$' | xargs)
fi

# Or load from 1Password (if configured)
GRAIN_1PASS_LOADER="$GRAINSTORE/grain6pbc/teamprecision06/grainenvvars/personal/load-from-1password.sh"
if [ -f "$GRAIN_1PASS_LOADER" ]; then
    source "$GRAIN_1PASS_LOADER"
fi

# ----------------------------------------------------------------------------
# ALIASES - Git (The Essential Choices)
# ----------------------------------------------------------------------------

alias g='git'
alias gs='git status'
alias ga='git add'
alias gc='git commit'
alias gp='git push'
alias gl='git log --oneline --graph --decorate'
alias gco='git checkout'
alias gb='git branch'
alias gd='git diff'

# ----------------------------------------------------------------------------
# ALIASES - Grain Network (The Sacred Union)
# ----------------------------------------------------------------------------

# Grainbarrel (build system)
alias grain-flow='bb flow'
alias grain-deploy='bb deploy'
alias grain-pseudo='bb pseudo'
alias grain-build='bb build'

# Graintime (temporal precision)
alias gt='gt'  # graintime command

# Navigation (conscious movement)
alias cdg='cd $GRAIN_HOME'
alias cdstore='cd $GRAINSTORE'
alias cddocs='cd $GRAIN_HOME/docs'

# ----------------------------------------------------------------------------
# ALIASES - System (Chosen Carefully)
# ----------------------------------------------------------------------------

alias ls='ls --color=auto'
alias ll='ls -lah'
alias la='ls -A'
alias l='ls -CF'

alias ..='cd ..'
alias ...='cd ../..'

alias grep='grep --color=auto'
alias fgrep='fgrep --color=auto'
alias egrep='egrep --color=auto'

# ----------------------------------------------------------------------------
# FUNCTIONS - The Lovers' Helpers
# ----------------------------------------------------------------------------

# Navigate to grain module
grain() {
    if [ -z "$1" ]; then
        cd "$GRAINSTORE"
    else
        # Search for module in grainstore
        local module_path=$(find "$GRAINSTORE" -maxdepth 3 -type d -name "*$1*" | head -1)
        if [ -n "$module_path" ]; then
            cd "$module_path"
        else
            echo "Module not found: $1"
            return 1
        fi
    fi
}

# Find grain modules
grain-find() {
    if [ -z "$1" ]; then
        echo "Usage: grain-find <pattern>"
        return 1
    fi
    find "$GRAINSTORE" -maxdepth 3 -type d -name "*$1*"
}

# Create session document
grain-session() {
    local session_file="$GRAIN_HOME/docs/SESSION-$(date +%Y-%m-%d--%H%M).md"
    echo "# Session $(date +%Y-%m-%d--%H%M)" > "$session_file"
    echo "" >> "$session_file"
    echo "## Notes" >> "$session_file"
    echo "" >> "$session_file"
    $EDITOR "$session_file"
}

# ----------------------------------------------------------------------------
# KEYBINDINGS - Emacs Style (Chosen for Efficiency)
# ----------------------------------------------------------------------------

bindkey -e  # Emacs keybindings

# ----------------------------------------------------------------------------
# COLORS - Visual Clarity
# ----------------------------------------------------------------------------

export LS_COLORS='di=34:ln=35:so=32:pi=33:ex=31:bd=34;46:cd=34;43:su=30;41:sg=30;46:tw=30;42:ow=30;43'

# ----------------------------------------------------------------------------
# PERSONAL CONFIGURATION - Your Choices
# ----------------------------------------------------------------------------

# Load personal config if it exists (template/personal split)
PERSONAL_ZSHRC="$GRAINSTORE/grain6pbc/teamprecision06/grainzsh/personal/.zshrc"
if [ -f "$PERSONAL_ZSHRC" ]; then
    source "$PERSONAL_ZSHRC"
fi

# Also check user-specific personal config
USER_PERSONAL_ZSHRC="$GRAINSTORE/grain6pbc/teamprecision06/grainzsh/personal/$USER/.zshrc"
if [ -f "$USER_PERSONAL_ZSHRC" ]; then
    source "$USER_PERSONAL_ZSHRC"
fi

# ----------------------------------------------------------------------------
# WELCOME MESSAGE - The Lovers' Blessing
# ----------------------------------------------------------------------------

# Uncomment to see welcome message on shell start
# echo "λ Grainzsh loaded - The Lovers choose precision 💕"

# ============================================================================
# END GRAINZSH TEMPLATE
# ============================================================================
# "Every choice is an act of love. Every configuration is a commitment."
# - teamprecision06 (Virgo ♍ / VI. The Lovers)
# ============================================================================
