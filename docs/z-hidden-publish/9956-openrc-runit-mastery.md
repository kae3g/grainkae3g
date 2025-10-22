# kae3g 9956: The Training Grounds — OpenRC and runit Hands-On Mastery

**Timestamp:** 12025-10-10--rhizome-valley  
**Series:** Rhizome Valley Chronicles  
**Category:** Init Systems, Service Supervision, Practical Skills  
**Reading Time:** 35 minutes

> **"Theory is beautiful. Proofs are elegant. Compilers are reassuring. But there's nothing quite like the moment your hands touch real metal, real code, real production systems. This is where you become a builder."**

---

## The Practice Field

*We've met visionaries—the Gentle Gardener, the Proof-Keeper, the Rust Blacksmith. We've learned philosophies, examined architectures, marveled at innovations.*

*Now we stand in the valley's **Training Grounds**, where the Pragmatic Pioneer—an old Unix hand with decades of battle scars—waits with a knowing smile.*

*"Enough talk," he says, rolling up his sleeves. "You've seen the future (seL4, Redox). Now let me show you what actually runs on millions of servers TODAY. Let me show you OpenRC and runit—the tools that just work, day after day, year after year."*

*He gestures to a workbench covered in hardware. "That Framework laptop from essay 9958? Today you're going to install Artix or Void Linux on it. And you're going to manage services with tools that won't fight you, won't surprise you, won't mysteriously break at 3 AM."*

## The Question Every Practitioner Asks

*How do I actually USE OpenRC and runit to manage services on a real Linux system? Not theory. Not diagrams. **Real commands. Real configurations. Real troubleshooting.***

## Introduction: From Theory to Mastery

In [9951](9951-init-systems-landscape) you learned ABOUT init systems—the orchestra metaphor, the landscape, the philosophy.  
Now you learn to **conduct the orchestra yourself**—hands-on, on real hardware.

---

## Part I: OpenRC Deep Dive — The Gentle Conductor's Score

*The Pragmatic Pioneer picks up a service script, weathered from years of use.*

*"OpenRC," he explains, "is like sheet music for a gentle orchestra conductor. Each service is a score—clear instructions that anyone can read, modify, and understand. No binary magic. No hidden complexity. Just bash scripts doing exactly what they say."*

### Service Script Anatomy: Reading the Score

```bash
#!/sbin/openrc-run

name="my-app"
command="/usr/bin/my-app"
command_args="--config /etc/my-app.conf"
command_user="appuser:appgroup"

depend() {
  need net
  use dns logger
  after firewall
}

start_pre() {
  checkpath --directory --owner $command_user /var/run/my-app
}
```

### Hands-On Exercise

```bash
# Create service on Artix Linux (from 9958)
sudo vim /etc/init.d/hello-service

# Make executable
sudo chmod +x /etc/init.d/hello-service

# Add to default runlevel
sudo rc-update add hello-service default

# Start service
sudo rc-service hello-service start

# Check status
sudo rc-status
```

---

## Part II: runit Deep Dive

### Service Directory Structure

```bash
/etc/sv/my-service/
  ├── run         # Start script
  ├── finish      # Cleanup after exit
  └── log/
      └── run     # Logging script
```

### Example Service

```bash
# /etc/sv/myapp/run
#!/bin/sh
exec 2>&1
exec chpst -u myapp:myapp /usr/bin/myapp --config /etc/myapp.conf

# /etc/sv/myapp/log/run
#!/bin/sh
exec chpst -u log:log svlogd -tt /var/log/myapp
```

### Hands-On Exercise

```bash
# Create service
sudo mkdir -p /etc/sv/myapp/log
sudo vim /etc/sv/myapp/run
sudo vim /etc/sv/myapp/log/run
sudo chmod +x /etc/sv/myapp/run /etc/sv/myapp/log/run

# Enable
sudo ln -s /etc/sv/myapp /run/runit/service/

# Control
sudo sv up myapp      # Start
sudo sv down myapp    # Stop
sudo sv restart myapp # Restart
sudo sv status myapp  # Check status
```

---

## Part III: Real-World Stack (db → app → web)

### PostgreSQL Service (runit)

```bash
# /etc/sv/postgresql/run
#!/bin/sh
exec 2>&1
exec chpst -u postgres:postgres /usr/bin/postgres -D /var/lib/postgresql/data
```

### Application Service

```bash
# /etc/sv/myapp/run
#!/bin/sh
exec 2>&1
# Wait for PostgreSQL
sv check postgresql || exit 1
exec chpst -u myapp:myapp /usr/local/bin/myapp
```

### Nginx Service

```bash
# /etc/sv/nginx/run
#!/bin/sh
exec 2>&1
exec /usr/sbin/nginx -g 'daemon off;'
```

**Exercise:** Deploy this stack on your Framework laptop, test dependencies, observe supervision.

---

## Conclusion: The Pragmatic Pioneer's Wisdom

*As the sun sets on the Training Grounds, the Pragmatic Pioneer wipes oil from his hands and surveys the workbench—now covered with successfully configured services, running processes, cleanly supervised daemons.*

*"You've learned the theories from the Wise Elders," he says, his voice carrying the weight of experience. "You've seen the visions from the Proof-Keeper and the Rust Blacksmith. But now you've got something more valuable: **dirty hands and working systems**."*

*He points to the Framework laptop, now running Artix with OpenRC humming quietly. "This is what thousands of servers run. This is what survives 3 AM emergencies. This is what you can debug with nothing but grep and a text editor. No magic. No mystery. Just solid, understandable tools."*

*SixOS nods approvingly. "And notice—s6, the heart of my system, is just an evolution of these principles. Same philosophy, refined over years of production use."*

### Your Hands-On Mastery

You now have **real experience** with:
- ✓ OpenRC service scripts (bash-based, readable, debuggable)
- ✓ runit supervision (directory-based, elegant, minimal)
- ✓ Dependency management (need/use/after for OpenRC, sv dependencies for runit)
- ✓ Logging patterns (readable text logs, no binary mysteries)
- ✓ Production troubleshooting (tools that work when everything else fails)

**The bridge awaits:** [9957](9957-rust-supervision-frameworks) — Build modern supervision in Rust, combining old wisdom with new safety

---

**Next Writing:** [9957-rust-supervision-frameworks](9957-rust-supervision-frameworks) — Rust-Based Init and Supervision  
**Previous Writing:** [9955-redox-os-rust-microkernel](9955-redox-os-rust-microkernel) — Redox OS

---

*"The master has failed more times than the beginner has even tried."*  
— Stephen McCranie

*In the valley, we learn by doing. Theory guides us, but practice makes us builders.*

---

[View All Essays](/12025-10/)


---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*