# ☠️ InstantDeath (1.1‑R5 Compatible Branch)

> ⚠️ **Warning:** This branch is for legacy servers (Minecraft 1.5 / CraftBukkit 1.1-R5) only.  
> If you are running Minecraft 1.6.1 or newer, you should **use the [main 1.6.1+ branch](https://github.com/emipazd/InstantDeath/tree/master)** instead for better compatibility and features.

**InstantDeath** is an ultra‑lightweight plugin that lets players quickly kill themselves (with `/kill` or `/suicide`), with optional death coordinates and permission checks. This branch is specifically **back‑ported for CraftBukkit 1.1‑R5 (Minecraft 1.5)**—making it usable on legacy servers as early as 1.6.1 and beyond.

---

## 🔧 Branch Overview

- 📌 Built against **CraftBukkit 1.1‑R5**
- ✅ Has all runtime logic aligned with the **modern behavior**, except where modern API features aren’t available
- ⚠️ Does **not** rely on `ChatColor.translateAlternateColorCodes`, `setHealth(double)`, or health APIs that exist only in versions **post‑1.6**

---

## 📦 Features

- ✅ **Self‑Kill**: Use `/kill` or `/suicide` to die instantly and respawn
- 🔐 **Optional permission**: Control self‑kill via `self-kill-requires-permission`
- 🗺️ **Death coordinates**: Configurable toggle to show location of death (only to the victim)
- 💬 **Fully customizable messages** via `config.yml`
- 🧩 **Legacy support**: Compatible with CraftBukkit 1.1‑R5 through current versions (e.g. Spigot, Paper)

---

## 📋 Commands & Permissions

### Commands
| Command         | Description                            |
|-----------------|----------------------------------------|
| `/kill`         | Kill yourself instantly                |
| `/suicide`      | Alias for `/kill`                      |
| `/kill <player>`| Kill another player (requires permission) 

### Permissions
| Permission                  | Description                          | Default |
|----------------------------|--------------------------------------|---------|
| `instantdeath.kill.self`   | Allows self‑killing via `/kill`/`/suicide` | `true`  |
| `instantdeath.kill.others` | Allows targeting `/kill <player>`     | `op`    |

> ⚠️ To ensure compatibility with 1.1‑R5, self‑kill permission enforcement is manually implemented via config.

---

## ⚙️ Configuration (`config.yml`)

```yaml
settings:
  # Require permission to self-kill?
  self-kill-requires-permission: false

  # Show death coordinates when killed by others?
  show-death-location-on-kill: true

messages:
  self-kill:         "&cYou have died at &eX: %x%&c, &eY: %y%&c, &eZ: %z%"
  target-kill:       "&cYou have killed %target%."
  death-location:    "&cYou died at &eX: %x%&c, &eY: %y%&c, &eZ: %z%"
  player-not-found:  "&cPlayer '%target%' not found."
  no-permission:     "&cYou do not have permission to kill others."
  usage:             "&cUsage: /%label% [player]"
  console-kill:      "Player %target% killed from console."
  console-player-not-found: "Player '%target%' not found."
  console-usage:     "Usage from console: /%label% <player>"
