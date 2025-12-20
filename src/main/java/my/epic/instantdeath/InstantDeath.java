package my.epic.instantdeath;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

import java.util.HashMap;
import java.util.Map;

public final class InstantDeath extends JavaPlugin {

    private Map<String, String> messages = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadMessages();
        getLogger().info("[InstantDeath] Made with love by Emilia");
        getLogger().info("[InstantDeath] Trans lives matter! :3");
        int pluginId = 12345;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        getLogger().info("[InstantDeath] Thanks for using Instant Death <3");
    }

    private void loadMessages() {
        messages.clear();
        if (getConfig().isConfigurationSection("messages")) {
            for (String key : getConfig().getConfigurationSection("messages").getKeys(false)) {
                String msg = getConfig().getString("messages." + key, "");
                messages.put(key, ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
    }

    private String getMessage(String key) {
        return messages.getOrDefault(key, ChatColor.RED + "Missing message: " + key);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase();

        if (!(sender instanceof Player)) {
            handleConsoleCommand(sender, cmd, args);
            return true;
        }

        Player player = (Player) sender;

        if ((cmd.equals("kill") || cmd.equals("suicide")) && args.length <= 1) {
            if (args.length == 0) {
                // Self kill with optional permission requirement
                boolean requirePerm = getConfig().getBoolean("settings.self-kill-requires-permission", false);
                if (!requirePerm || player.hasPermission("instantdeath.kill.self") || player.isOp()) {
                    handleKill(player, true, player);
                } else {
                    player.sendMessage(getMessage("no-permission"));
                }
            } else {
                // Targeted kill requires permission
                if (hasKillPermission(player)) {
                    Player targetPlayer = getServer().getPlayerExact(args[0]);
                    if (targetPlayer != null) {
                        handleKill(targetPlayer, false, player);
                        // Inform the killer with a message without coords
                        String msg = formatMessage(getMessage("target-kill"), targetPlayer, player);
                        player.sendMessage(msg);
                    } else {
                        player.sendMessage(getMessage("player-not-found").replace("%target%", args[0]));
                    }
                } else {
                    player.sendMessage(getMessage("no-permission"));
                }
            }
            return true;
        }

        player.sendMessage(getMessage("usage").replace("%label%", label));
        return true;
    }

    private void handleConsoleCommand(CommandSender sender, String cmd, String[] args) {
        if ((cmd.equals("kill") || cmd.equals("suicide")) && args.length == 1) {
            Player targetPlayer = getServer().getPlayerExact(args[0]);

            if (targetPlayer != null) {
                targetPlayer.setHealth(0.0);
                getLogger().info(getMessage("console-kill").replace("%target%", targetPlayer.getName()));
            } else {
                getLogger().info(getMessage("console-player-not-found").replace("%target%", args[0]));
            }
        } else {
            getLogger().info(getMessage("console-usage").replace("%label%", cmd));
        }
    }

    private void handleKill(Player target, boolean selfKill, Player killer) {
        if (selfKill) {
            target.damage(Float.MAX_VALUE);
            String msg = formatMessage(getMessage("self-kill"), target, killer);
            target.sendMessage(msg);
        } else {
            target.damage(Float.MAX_VALUE, killer);
            boolean showDeathLoc = getConfig().getBoolean("settings.show-death-location-on-kill", false);
            if (showDeathLoc) {
                String deathLocMsg = formatMessage(getMessage("death-location"), target, killer);
                target.sendMessage(deathLocMsg);
            }
            // killer gets target-kill message (already handled in onCommand)
        }
    }

    private boolean hasKillPermission(Player player) {
        return player.hasPermission("instantdeath.kill.others") || player.isOp();
    }

    private String formatMessage(String template, Player target, Player killer) {
        return template
                .replace("%x%", String.valueOf(target.getLocation().getBlockX()))
                .replace("%y%", String.valueOf(target.getLocation().getBlockY()))
                .replace("%z%", String.valueOf(target.getLocation().getBlockZ()))
                .replace("%target%", target.getName())
                .replace("%killer%", killer != null ? killer.getName() : "Console");
    }
}
