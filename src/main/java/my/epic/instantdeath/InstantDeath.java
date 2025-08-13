package my.epic.instantdeath;

import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class InstantDeath extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("[InstantDeath] RUNNING LEGACY EDITION");
        getLogger().info("[InstantDeath] Made with love by Emilia");
        getLogger().info("[InstantDeath] Trans lives matter! :3");
    }

    @Override
    public void onDisable() {
        getLogger().info("[InstantDeath] Thanks for using Instant Death <3");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("kill")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            // Console only supports: /kill <player>
            if (args.length == 1) {
                Player target = getServer().getPlayerExact(args[0]);
                String tpl = colorize(getConfig().getString("messages.console-kill", "Player %target% killed from console."));
                if (target != null) {
                    target.setHealth(0);  // ← int
                    getLogger().info(tpl.replace("%target%", target.getName()));
                } else {
                    String msg = colorize(getConfig().getString("messages.console-player-not-found",
                            "Player '%target%' not found."));
                    getLogger().info(msg.replace("%target%", args[0]));
                }
            } else {
                String usg = colorize(getConfig().getString("messages.console-usage",
                        "Usage from console: /%label% <player>"));
                getLogger().info(usg.replace("%label%", label));
            }
            return true;
        }

        Player player = (Player) sender;
        boolean requireSelf = getConfig().getBoolean("settings.self-kill-requires-permission", false);
        boolean showDeathLoc = getConfig().getBoolean("settings.show-death-location-on-kill", true);
        boolean suicideAlias = label.equalsIgnoreCase("suicide");

        if (args.length == 0) {
            // Self-kill
            if (requireSelf && !player.hasPermission("instantdeath.kill.self")) {
                String nomsg = colorize(getConfig().getString("messages.no-permission",
                        "&cYou do not have permission to kill yourself."));
                player.sendMessage(nomsg);
                return true;
            }
            handleKill(player, player, showDeathLoc, true);
            return true;
        }

        if (args.length == 1) {
            if (suicideAlias) {
                String usg = colorize(getConfig().getString("messages.usage", "&cUsage: /%label% [player]"));
                player.sendMessage(usg.replace("%label%", label));
                return true;
            }

            Player target = getServer().getPlayerExact(args[0]);
            if (target == null) {
                String notf = colorize(getConfig().getString("messages.player-not-found",
                        "&cPlayer '%target%' not found."));
                player.sendMessage(notf.replace("%target%", args[0]));
                return true;
            }

            if (!player.hasPermission("instantdeath.kill.others")) {
                String nomsg = colorize(getConfig().getString("messages.no-permission",
                        "&cYou do not have permission to kill others."));
                player.sendMessage(nomsg);
                return true;
            }

            handleKill(target, player, showDeathLoc, false);

            String killerTpl = colorize(getConfig().getString("messages.target-kill", "&cYou have killed %target%."));
            player.sendMessage(killerTpl.replace("%target%", target.getName()));
            return true;
        }

        // Too many args
        String usg = colorize(getConfig().getString("messages.usage", "&cUsage: /%label% [player]"));
        player.sendMessage(usg.replace("%label%", label));
        return true;
    }

    private void handleKill(Player victim, Player killer, boolean showDeathLoc, boolean self) {
        Location loc = victim.getLocation();
        int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();

        victim.setHealth(0);  // kills immediately (int overload)

        if (self) {
            String selfTpl = getConfig().getString("messages.self-kill",
                    "&cYou have died at &eX: %x%&c, &eY: %y%&c, &eZ: %z%");
            String msg = colorize(selfTpl)
                    .replace("%x%", String.valueOf(x))
                    .replace("%y%", String.valueOf(y))
                    .replace("%z%", String.valueOf(z));
            victim.sendMessage(msg);
        } else if (showDeathLoc) {
            String locTpl = getConfig().getString("messages.death-location",
                    "&cYou died at &eX: %x%&c, &eY: %y%&c, &eZ: %z%");
            String msg = colorize(locTpl)
                    .replace("%x%", String.valueOf(x))
                    .replace("%y%", String.valueOf(y))
                    .replace("%z%", String.valueOf(z));
            victim.sendMessage(msg);
        }
    }

    // simple Bukkit 1.1‑R5 fallback for colors—avoids translateAlternateColorCodes
    private static String colorize(String text) {
        return text == null ? "" : text.replace('&', '§');
    }
}
