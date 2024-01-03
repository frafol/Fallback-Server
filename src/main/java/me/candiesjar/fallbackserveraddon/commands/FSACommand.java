package me.candiesjar.fallbackserveraddon.commands;

import me.candiesjar.fallbackserveraddon.FallbackServerAddon;
import me.candiesjar.fallbackserveraddon.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FSACommand implements CommandExecutor {

    private final FallbackServerAddon plugin;

    public FSACommand(FallbackServerAddon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length != 1) {
            sender.sendMessage(("&8&l» &7Running &b&nFallback Server Addon version&r&7 by &b&nCandiesJar").replace('&', '§')
                    .replace("version", plugin.getDescription().getVersion()));
            return true;
        }

        if (!args[0].equalsIgnoreCase("reload")) {
            sendDefaultMessage(sender);
            return true;
        }

        if (!sender.hasPermission(plugin.getConfig().getString("settings.reload_permission", "fallbackserveradmin.reload"))) {
            sendDefaultMessage(sender);
            return true;
        }

        String oldValue = plugin.getConfig().getString("settings.mode", "NONE");
        reloadConfig();
        plugin.executeReload(oldValue);

        if (sender instanceof Player) {
            sender.sendMessage(ChatUtil.color((Player) sender, plugin.getConfig().getString("settings.reload_message"))
                    .replace("%version%", plugin.getDescription().getVersion()));
            return true;
        }

        sender.sendMessage((plugin.getConfig().getString("settings.reload_message")).replace('&', '§')
                .replace("%version%", plugin.getDescription().getVersion()));
        return true;
    }

    private void reloadConfig() {
        plugin.reloadConfig();
        plugin.loadConfig();
    }

    private void sendDefaultMessage(CommandSender sender) {
        if (plugin.getConfig().getBoolean("settings.hide_command", false)) {
            return;
        }

        sender.sendMessage(("&8&l» &7Running &b&nFallback Server Addon version&r&7 by &b&nCandiesJar").replace('&', '§')
                .replace("version", plugin.getDescription().getVersion()));
    }
}
