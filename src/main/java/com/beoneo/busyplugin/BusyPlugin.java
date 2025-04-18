package com.beoneo.busyplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.UUID;

public class BusyPlugin extends JavaPlugin implements Listener {

    private final HashSet<UUID> busyPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("BusyPlugin Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BusyPlugin Disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        UUID id = player.getUniqueId();

        if (busyPlayers.contains(id)) {
            busyPlayers.remove(id);
            player.sendMessage(ChatColor.GREEN + "You are now available.");
        } else {
            busyPlayers.add(id);
            player.sendMessage(ChatColor.RED + "You are now marked as busy.");
        }
        return true;
    }

    @EventHandler
    public void onMsgCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage();
        if (msg.startsWith("/msg") || msg.startsWith("/tell") || msg.startsWith("/w")) {
            String[] parts = msg.split(" ");
            if (parts.length < 2) return;

            Player target = Bukkit.getPlayerExact(parts[1]);
            if (target != null && busyPlayers.contains(target.getUniqueId())) {
                event.getPlayer().sendMessage(ChatColor.RED + "The user is busy.");
                event.setCancelled(true);
            }
        }
    }
}
