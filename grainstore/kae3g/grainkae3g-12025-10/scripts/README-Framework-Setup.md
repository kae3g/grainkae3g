# Framework Alpine Linux Setup Script

## Quick Start

1. **Clone this repository** on your Framework laptop:
   ```bash
   git clone https://codeberg.org/your-repo/12025-10.git
   cd 12025-10/scripts
   ```

2. **Run the setup script**:
   ```bash
   sudo sh framework-alpine-s6-setup.sh
   ```

## What This Script Does

- **Installs all essential packages** for Framework Laptop 16 (AMD Ryzen, Radeon)
- **Sets up s6 init system** with proper service definitions
- **Configures networking** with WiFi support
- **Installs Wayland + Sway** desktop environment
- **Creates recovery scripts** for future troubleshooting
- **Sets up admin user** with sudo permissions
- **Verifies musl libc purity**

## Prerequisites

- Alpine Linux 3.22+ installed on Framework Laptop
- Internet connection working
- Running as root user

## After Running the Script

1. **Configure WiFi**:
   ```bash
   setup-interfaces
   ```

2. **Test network**:
   ```bash
   ping -c 3 8.8.8.8
   apk update
   ```

3. **Customize recovery scripts** with your WiFi credentials:
   - Edit `/usr/local/bin/framework-recovery.sh`
   - Edit `/usr/local/bin/netfix.sh`
   - Replace `YOUR_STARLINK_WIFI_NAME` and `YOUR_PASSWORD`

4. **Reboot to test s6 init system**:
   ```bash
   reboot
   ```

## Recovery Commands

- **Quick network fix**: `netfix`
- **Full system recovery**: `recover`
- **Check network status**: `netstatus`

## Boot Failure Recovery

If you get stuck in boot limbo:

1. Boot from Alpine ISO
2. Mount root partition: `mount /dev/nvme0n1p2 /mnt/alpine`
3. Chroot: `chroot /mnt/alpine /bin/sh`
4. Run recovery: `/usr/local/bin/framework-recovery.sh`

## Package Verification

Check musl libc purity:
```bash
scanelf -n /usr/bin/* /usr/sbin/* 2>/dev/null | grep -q glibc && echo "❌ CONTAMINATED" || echo "✅ MUSL PURE"
```

## Troubleshooting

- **Network issues**: Run `netfix` or see `/root/RECOVERY-GUIDE.md`
- **s6 boot failure**: Use boot failure recovery steps above
- **Package issues**: Check musl libc purity with scanelf commands
- **Missing packages**: Some packages may not be available in all Alpine versions:
  - `mesa-vulkan-radeon` - May not exist, Vulkan support attempted separately
  - `font-noto` and `font-noto-emoji` - Correct names for Noto fonts

## Files Created

- `/usr/local/bin/framework-recovery.sh` - Full system recovery
- `/usr/local/bin/netfix.sh` - Quick network fix
- `/root/RECOVERY-GUIDE.md` - Recovery documentation
- `/root/backup/` - Config file backups
