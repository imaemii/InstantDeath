# ☠️ InstantDeath

**InstantDeath** is a very lightweight, highly flexible and compatible Minecraft plugin that allows players to quickly and easily end their die using `/kill` or `/suicide`.

---

## 📦 Features

- ✅ **Self-Kill Support** — Players can use `/kill` or `/suicide` to instantly respawn.
- 🔐 **Permission Support** — Optional setting to require permission for self-kills.
- 🗺️ **Death Coordinates** — Configurable setting to show the location of death.
- 💬 **Customizable Messages** — All messages are configurable via `config.yml`.
- 🧩 **Highly Compatible** — Works with Bukkit, Spigot, Paper, Purpur, Folia, and more — from **Minecraft 1.6.1** through the latest versions. (Yes 1.6.1 not 1.16)

---

## 🔧 Commands

| Command                | Description                                   |
|------------------------|-----------------------------------------------|
| `/kill`                | Kill yourself instantly.                      |
| `/suicide`             | Alias for `/kill`.                            |
| `/kill <player>`       | Kill another player (requires permission).    |

---

## 🛡️ Permissions

| Permission                    | Description                                        | Default |
|------------------------------|----------------------------------------------------|---------|
| `instantdeath.kill.self`     | Allows the player to kill themselves.              | `true`  |
| `instantdeath.kill.others`   | Allows the player to kill other players.           | `op`    |

**Optional config toggle:** You can enforce that players must have `instantdeath.kill.self` by setting `self-kill-requires-permission: true` in the config.

---

## ⚙️ Configuration

```yaml
# InstantDeath plugin configuration
settings:
  # You need instantdeath.kill.self to kill yourself
  self-kill-requires-permission: false
  # If you die it tells you where you died in chat
  show-death-location-on-kill: true

messages:
  self-kill: "&cYou have died at &eX: %x%&c, &eY: %y%&c, &eZ: %z%"
  target-kill: "&cYou have killed %target%."
  death-location: "&cYou died at &eX: %x%&c, &eY: %y%&c, &eZ: %z%"
  player-not-found: "&cPlayer '%target%' not found."
  no-permission: "&cYou do not have permission to kill others."
  usage: "&cUsage: /%label% [player]"
  console-kill: "Player %target% killed from console."
  console-player-not-found: "Player '%target%' not found."
  console-usage: "Usage from console: /%label% <player>"
