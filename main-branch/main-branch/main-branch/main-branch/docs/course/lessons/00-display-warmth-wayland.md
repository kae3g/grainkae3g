# Lesson 01: Your First Line of Code - Making Screens Comfortable

**Course:** Building Sustainable Technology Systems  
**Grade Level:** High School (Grades 10-12)  
**Duration:** 90 minutes  
**Prerequisites:** None - This is where we start!

---

## 🎯 **Welcome to Coding!**

**Your first program will make your computer screen more comfortable to look at.**

No previous experience needed. By the end of this 90-minute class, you'll:
1. Run your first command-line program
2. Understand why screens hurt your eyes at night
3. Learn the difference between X11 and Wayland
4. Create a simple bash script
5. Make technology serve **you**, not the other way around

---

## 🌙 **The Problem: Why Do Screens Keep You Awake?**

**Quick Poll:** How many of you use your phone/computer before bed?

**The Science:**
- Computer screens emit **blue light**
- Blue light tells your brain "it's daytime!"
- Your brain stops making **melatonin** (the sleep chemical)
- Result: You can't fall asleep, even though you're tired

**The Numbers:**
- Normal screen: **6500K** (Kelvin) - lots of blue light
- Candlelight: **1000K** - warm orange glow
- Our goal: Change from 6500K → 1000K

**What does "K" mean?**
- Kelvin = temperature scale
- Lower = warmer (more red/orange)
- Higher = cooler (more blue/white)

---

## 💻 **Let's Fix It! (Your First Commands)**

### **Step 1: Open the Terminal**

**On Ubuntu/GNOME:**
1. Press `Ctrl + Alt + T`
2. You'll see a black (or white) window
3. This is your **terminal** - your direct line to the computer

**What you'll see:**
```
username@computer:~$
```

This is called the **prompt**. It's waiting for your command!

### **Step 2: Check Your Display Type**

Type this command and press Enter:
```bash
echo $XDG_SESSION_TYPE
```

**What you'll see:**
- If it says `wayland` → You're on the modern system! ✅
- If it says `x11` → You're on the old system (we can still help)

**What's the difference?**

**X11** (the old way):
- Created in 1984 (older than you!)
- Complex and insecure
- Works, but outdated

**Wayland** (the new way):
- Created in 2010s
- Simpler and more secure
- This is what we'll use!

### **Step 3: Make Your Screen Warm!**

Copy and paste this command:
```bash
gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 1000
```

**Watch your screen!** It should turn orange/red right now!

**What just happened?**
1. `gsettings` = GNOME settings tool
2. `night-light-enabled true` = Turn on Night Light
3. `night-light-temperature 1000` = Set to 1000K (very warm)

**How do you feel?**
- First impression: "Wow, that's orange!"
- After 2 minutes: "Actually, this is kinda nice"
- After 10 minutes: "Normal screens look weirdly blue now"

Your eyes adapt quickly!

---

## 🛠️ **Making It Easy: Your First Script**

Typing those commands every time is annoying. Let's create a **script** - a program that runs those commands for you!

### **Activity: Create `warm` Command**

**Step 1: Create the file**
```bash
nano ~/.local/bin/warm
```

**Step 2: Type this code:**
```bash
#!/bin/bash
# My first script - Make screen warm!

gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 1000

echo "🌙 Screen is now warm! Perfect for bedtime."
```

**Step 3: Save and exit**
- Press `Ctrl + O` (save)
- Press Enter (confirm)
- Press `Ctrl + X` (exit)

**Step 4: Make it executable**
```bash
chmod +x ~/.local/bin/warm
```

**Step 5: Run it!**
```bash
warm
```

**🎉 You just wrote your first program!**

---

## 📚 **Understanding What You Did**

### **The Shebang Line**
```bash
#!/bin/bash
```
- `#!` = "Shebang" (weird name, right?)
- `/bin/bash` = Use the bash shell to run this
- **Always the first line** in bash scripts

### **Comments**
```bash
# My first script - Make screen warm!
```
- `#` = This is a comment (for humans, computer ignores it)
- Use comments to explain **why**, not **what**

### **Commands**
```bash
gsettings set ...
```
- Each line is a command
- Computer runs them top to bottom
- Like a recipe!

### **User Feedback**
```bash
echo "🌙 Screen is now warm! Perfect for bedtime."
```
- `echo` = Print a message
- Always let the user know what happened
- Emojis work! (Welcome to 2025)

---

## 🎨 **Extension: Multiple Modes**

Let's make it better! Let's add different warmth levels.

**Enhanced Script:**
```bash
#!/bin/bash
# Display Warmth Controller

case "$1" in
    bedtime)
        gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true
        gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 1000
        echo "🌙 Bedtime mode: 1000K (very warm)"
        ;;
    
    evening)
        gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled true
        gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 2000
        echo "🌆 Evening mode: 2000K (warm)"
        ;;
    
    off)
        gsettings set org.gnome.settings-daemon.plugins.color night-light-enabled false
        echo "💡 Normal colors restored"
        ;;
    
    *)
        echo "Usage: warm {bedtime|evening|off}"
        ;;
esac
```

**Now you can run:**
```bash
warm bedtime    # Very warm (1000K)
warm evening    # Medium warm (2000K)
warm off        # Back to normal
```

**New Concepts:**
- `case` statement = Like multiple-choice for code
- `$1` = First argument (what you typed after `warm`)
- `;;` = End of this choice
- `*)` = Default case (if nothing else matches)

---

## 🧪 **Experiment: The Temperature Scale**

Try these different values and see how they feel:

```bash
# Super warm (like fire)
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 1000

# Warm white (like old light bulbs)
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 2700

# Neutral (like office lights)
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 4000

# Normal screen (what you're used to)
gsettings set org.gnome.settings-daemon.plugins.color night-light-temperature 6500
```

**Take notes:**
- Which feels most comfortable?
- Which makes text easiest to read?
- Which would you use for movies?
- Which for coding?

**There's no "right" answer** - it's personal preference!

---

## 🌐 **Why Wayland Matters**

**Quick History Lesson:**

**The X11 Way (Old):**
```
Your script → xrandr command → X Server → Hardware
```
- Any program could change display settings
- Security problem: malware could mess with your screen
- Direct hardware access

**The Wayland Way (New):**
```
Your script → gsettings → GNOME Shell → Wayland → Hardware
```
- Only the compositor (GNOME Shell) can change displays
- Security: apps can't mess with your screen
- Everything goes through the compositor

**Why we care:**
- `xrandr` doesn't work on Wayland
- `gsettings` works perfectly on Wayland
- We're using the **modern, secure** approach!

---

## 🏥 **The Health Connection**

**Why This Matters:**

1. **Sleep Quality**
   - Blue light suppresses melatonin
   - Warm screens help you fall asleep faster
   - Better sleep = better learning, mood, health

2. **Eye Strain**
   - Blue light causes eye fatigue
   - Warm colors are easier on eyes
   - Especially important for long coding sessions!

3. **Circadian Rhythm**
   - Your body has a natural 24-hour cycle
   - Blue light disrupts this cycle
   - Warm screens help maintain natural rhythms

**Research:**
- Harvard study: Blue light suppresses melatonin 2x as much as green light
- f.lux study: Users report 30% better sleep quality
- Recommended by optometrists worldwide

---

## 💬 **Discussion Questions**

1. **Before/After:** How does your screen look now vs. before? How do you feel about it?

2. **Personal Use:** When would you use warm mode? When would you use normal mode?

3. **Sharing:** Would you recommend this to friends? Family? Why or why not?

4. **Technology Design:** Why don't computers do this automatically? Should they?

5. **Ethics:** Is it ethical for tech companies to make products that hurt our sleep? What should they do?

---

## 🎯 **Key Concepts Learned**

### **Technical Skills**
- ✅ Using the terminal/command line
- ✅ Running commands
- ✅ Creating bash scripts
- ✅ Making scripts executable (`chmod`)
- ✅ Using `gsettings` (GNOME configuration)
- ✅ Understanding `case` statements

### **Computer Science Concepts**
- Display servers (X11 vs. Wayland)
- System configuration
- Scripting and automation
- User interaction design
- Command-line arguments

### **Life Skills**
- Taking control of your technology
- Understanding health impacts of technology
- Problem-solving: identify problem → research → implement solution
- Teaching others (you can show your friends now!)

---

## 📝 **Quick Assessment**

### **Check Your Understanding**

1. **What does 1000K mean?**
   - Answer: A warm color temperature (like candlelight)

2. **Why does blue light matter?**
   - Answer: It suppresses melatonin and disrupts sleep

3. **What's the difference between X11 and Wayland?**
   - Answer: X11 is old/insecure, Wayland is modern/secure

4. **What does `#!/bin/bash` do?**
   - Answer: Tells the system to run the script with bash

5. **Can you modify the script yourself now?**
   - Answer: Yes! Try changing the temperature values

---

## 🏠 **Homework**

### **Required: The One-Week Experiment**

**Instructions:**
1. Use warm mode (1000K) every evening for one week
2. Track your sleep:
   - What time you go to bed
   - What time you fall asleep
   - How you feel in the morning
3. Write a 1-page reflection:
   - Did it help?
   - What was surprising?
   - Would you keep using it?

### **Optional: Teach Someone**

Show a family member or friend how to:
1. Open the terminal
2. Run the `gsettings` commands
3. Install your script

Teaching is the best way to learn!

### **Challenge: Auto-Scheduler**

Create a script that automatically changes warmth based on time:
- Morning (6 AM - 12 PM): Normal (6500K)
- Afternoon (12 PM - 6 PM): Normal (6500K)
- Evening (6 PM - 10 PM): Warm (2000K)
- Night (10 PM - 6 AM): Very warm (1000K)

**Hint:** Use `date +%H` to get the current hour!

---

## 🌾 **Welcome to the Grain Network**

**What You Just Learned Is Bigger Than You Think**

You didn't just learn to change screen colors. You learned:

1. **Autonomy**: You control your technology, not the other way around
2. **Health First**: Technology should serve human well-being
3. **Simple Solutions**: Often the best solutions are simple
4. **Open Source**: Share what you learn freely
5. **Practical Impact**: Start with real problems that affect real people

**"From granules to grains to THE WHOLE GRAIN"**

This is the Grain Network philosophy:
- Start small (one command)
- Build up (a script)
- Create something meaningful (better sleep!)
- Share it (teach others)

**You're not just learning to code. You're learning to make technology more human.**

---

## 🎉 **Congratulations!**

You've completed Lesson 01! You can now:
- ✅ Use the terminal
- ✅ Understand display color temperature
- ✅ Write bash scripts
- ✅ Control GNOME settings
- ✅ Make technology serve your health

**Next Lesson:** We'll build on this foundation and learn about blockchains, micropayments, and how small financial transactions can support creators and open-source developers.

---

## 📚 **Further Reading**

- [f.lux Research](https://justgetflux.com/research.html) - The science
- [GNOME Night Light](https://help.gnome.org/users/gnome-help/stable/display-night-light.html) - Official docs
- [Wayland](https://wayland.freedesktop.org/) - The future of Linux displays
- [Harvard Blue Light Study](https://www.health.harvard.edu/staying-healthy/blue-light-has-a-dark-side) - Research

---

**Teacher's Note:** This lesson is designed to be immediately practical and empowering. Students should leave feeling like they've accomplished something real. Emphasize that coding isn't magic - it's just telling the computer what to do, step by step. Celebrate their success! They just wrote their first program.

---

**Version:** 1.0.0  
**Author:** kae3g (Grain PBC)  
**License:** CC BY-SA 4.0  
**Date:** October 23, 2025

🌙 **Your journey into sustainable technology starts here!**
