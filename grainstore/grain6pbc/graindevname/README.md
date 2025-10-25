# 🌾 GrainDevName - Username Convention

**Grain Network Developer Username Convention for High-Likelihood GitHub Availability**

## Overview

The Grain Network uses a systematic 5-character username convention designed to maximize the likelihood of GitHub username availability while maintaining memorability and professional appearance.

## Convention Format

**Pattern**: `{letter}{letter}{number}{letter}{letter}`

**Examples**:
- `kae3g` - K + A + E + 3 + G
- `jen3g` - J + E + N + 3 + G
- `mik5a` - M + I + K + 5 + A
- `sop2x` - S + O + P + 2 + X

## Design Principles

### 1. **One Syllable + Number**
- **Syllable**: 3 letters forming a pronounceable syllable
- **Number**: Single digit (0-9) for uniqueness
- **Suffix**: 2 letters for additional differentiation

### 2. **High Availability Probability**
- **5 characters**: Short enough to be memorable, long enough to avoid conflicts
- **Mixed alphanumeric**: Reduces collision with common words
- **Number inclusion**: Most usernames don't include numbers in this pattern

### 3. **Professional Appearance**
- **Pronounceable**: Easy to say and remember
- **Clean format**: No special characters or underscores
- **Consistent structure**: Predictable pattern for team members

## Username Generation Guidelines

### Syllable Patterns
- **CVC**: Consonant-Vowel-Consonant (e.g., `kat`, `ben`, `mik`)
- **VCV**: Vowel-Consonant-Vowel (e.g., `aka`, `eno`, `iki`)
- **Avoid**: Common words, names, or brand names

### Number Placement
- **Position 3**: Always in the middle position
- **Range**: 0-9 (single digits only)
- **Avoid**: 1 (looks like lowercase L) and 0 (looks like O)

### Suffix Letters
- **Length**: Exactly 2 letters
- **Pattern**: Usually consonant + vowel or vowel + consonant
- **Avoid**: Common suffixes like `er`, `ly`, `ed`

## Examples by Category

### Technical/Development
- `dev3k` - Development + 3 + K
- `cod2x` - Code + 2 + X
- `git5a` - Git + 5 + A
- `sys1m` - System + 1 + M

### Creative/Design
- `art4b` - Art + 4 + B
- `des2n` - Design + 2 + N
- `mus7c` - Music + 7 + C
- `vid3p` - Video + 3 + P

### Business/Management
- `biz6k` - Business + 6 + K
- `mgm8t` - Management + 8 + T
- `pro9x` - Project + 9 + X
- `ops4z` - Operations + 4 + Z

## Availability Checking

### GitHub Username Check
```bash
# Check if username is available
curl -s "https://api.github.com/users/kae3g" | grep -q '"message":"Not Found"' && echo "Available" || echo "Taken"
```

### Automated Check Script
```bash
#!/bin/bash
# Check multiple usernames
usernames=("kae3g" "jen3g" "mik5a" "sop2x")
for username in "${usernames[@]}"; do
    if curl -s "https://api.github.com/users/$username" | grep -q '"message":"Not Found"'; then
        echo "✅ $username - Available"
    else
        echo "❌ $username - Taken"
    fi
done
```

## Integration with Grain Network

### Graintime Integration
- Usernames used as `{author}` in grainpath format
- Format: `/course/{author}/{name}/{graintime}/`
- Example: `/course/kae3g/grain-fundamentals/12025-10-22-2219-PDT-vishakha-gemini000-06th-system/`

### Repository Naming
- Course repos: `course-{author}-{name}-{graintime}`
- Module repos: `{module-name}-{author}`
- Personal repos: `{author}-{project-name}`

## Best Practices

### 1. **Check Before Committing**
- Always verify availability before adopting a username
- Check GitHub, Codeberg, and other platforms
- Consider future expansion needs

### 2. **Document Your Choice**
- Add to team username registry
- Update documentation when adopting new usernames
- Maintain consistency across platforms

### 3. **Reserve Early**
- Claim usernames on major platforms even if not immediately needed
- Consider variations for different purposes
- Plan for team growth and new members

## Username Registry

### Active Grain Network Usernames
- `kae3g` - Primary developer (Kae + 3 + G)
- `jen3g` - Secondary developer (Jen + 3 + G)

### Reserved for Future Use
- `mik5a` - Reserved for Mike
- `sop2x` - Reserved for Sophie
- `ale7k` - Reserved for Alex
- `sam4n` - Reserved for Sam

## Tools and Scripts

### Username Generator
```bash
#!/bin/bash
# Generate potential usernames
syllables=("kae" "jen" "mik" "sop" "ale" "sam" "tom" "ann" "bob" "cat")
numbers=(0 1 2 3 4 5 6 7 8 9)
suffixes=("3g" "2x" "5a" "7k" "4n" "1m" "8t" "6z" "9p" "0y")

for syllable in "${syllables[@]}"; do
    for number in "${numbers[@]}"; do
        for suffix in "${suffixes[@]}"; do
            echo "${syllable}${number}${suffix}"
        done
    done
done
```

### Availability Checker
```bash
#!/bin/bash
# Check username availability across platforms
check_username() {
    local username=$1
    echo "Checking $username..."
    
    # GitHub
    if curl -s "https://api.github.com/users/$username" | grep -q '"message":"Not Found"'; then
        echo "  ✅ GitHub: Available"
    else
        echo "  ❌ GitHub: Taken"
    fi
    
    # Codeberg
    if curl -s "https://codeberg.org/api/v1/users/$username" | grep -q '"message":"Not Found"'; then
        echo "  ✅ Codeberg: Available"
    else
        echo "  ❌ Codeberg: Taken"
    fi
}
```

## Philosophy

> "From granules to grains to THE WHOLE GRAIN"

The GrainDevName convention embodies the Grain Network philosophy of systematic, scalable growth. Just as individual granules combine to form grains, and grains combine to form the whole grain, our username convention creates a systematic approach to identity that scales from individual developers to the entire network.

## Contributing

To add new usernames to the registry:

1. Follow the 5-character convention
2. Check availability across platforms
3. Update this documentation
4. Add to the team registry
5. Test with graintime integration

---

**🌾 Grain Network - Systematic Username Convention for Scalable Development**
