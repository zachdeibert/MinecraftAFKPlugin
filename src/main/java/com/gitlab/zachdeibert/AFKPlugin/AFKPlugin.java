package com.gitlab.zachdeibert.AFKPlugin;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AFKPlugin extends JavaPlugin implements Listener {
    private boolean      isEnabled;
    private List<String> AFKPlayers;

    @Override
    public boolean isInitialized() {
        return AFKPlayers != null;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command,
                    final String label, final String[] args) {
        if ( isEnabled && command.getName().equalsIgnoreCase("afk") ) {
            if ( sender instanceof ConsoleCommandSender ) {
                sender.sendMessage("The console cannot go AFK");
            } else {
                final String playerName = sender.getName();
                final Server server = sender.getServer();
                if ( AFKPlayers.contains(playerName) ) {
                    AFKPlayers.remove(playerName);
                    server.broadcastMessage(playerName.concat(
                                " is no longer AFK."));
                } else {
                    AFKPlayers.add(playerName);
                    server.broadcastMessage(playerName.concat(" is now AFK."));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        isEnabled = false;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        isEnabled = true;
    }

    @Override
    public void onLoad() {
        AFKPlayers = new LinkedList<String>();
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getDisplayName();
        if ( AFKPlayers.contains(playerName) ) {
            AFKPlayers.remove(playerName);
            player.getServer().broadcastMessage(playerName.concat(
                        " is no longer AFK."));
        }
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        if ( !AFKPlayers.isEmpty() ) {
            player.sendMessage("Currently AFK Players:");
            for ( String playerName : AFKPlayers ) {
                player.sendMessage("  - ".concat(playerName));
            }
        } else {
            player.sendMessage("There are no players currently AFK.");
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final String playerName = event.getPlayer().getDisplayName();
        if ( AFKPlayers.contains(playerName) ) {
            AFKPlayers.remove(playerName);
        }
    }
}
