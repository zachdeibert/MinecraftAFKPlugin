package com.gitlab.zachdeibert.AFKPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AFKPlugin extends JavaPlugin implements Listener {
    private boolean isEnabled;
    private AFKList afks;
    
    @Override
    public boolean isInitialized() {
        return afks != null;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command,
                    final String label, final String[] args) {
        if ( isEnabled && command.getName().equalsIgnoreCase("afk") ) {
            if ( sender instanceof ConsoleCommandSender ) {
                sender.sendMessage("The console cannot go AFK!");
            } else {
                afks.toggleAFK(sender.getName());
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
        afks = new AFKList(getServer());
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        afks.leaveAFK(event.getPlayer().getDisplayName());
    }
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        afks.leaveAFK(event.getPlayer().getDisplayName());
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        String afks[] = this.afks.getAFKs();
        if ( afks.length >= 0 ) {
            player.sendMessage("Currently AFK Players:");
            for ( String playerName : afks ) {
                player.sendMessage("  - ".concat(playerName));
            }
        } else {
            player.sendMessage("There are no players currently AFK.");
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        afks.deleteAFK(event.getPlayer().getDisplayName());
    }
}
