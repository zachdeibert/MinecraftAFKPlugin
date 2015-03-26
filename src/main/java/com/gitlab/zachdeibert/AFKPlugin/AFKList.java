package com.gitlab.zachdeibert.AFKPlugin;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.Server;

public class AFKList {
    private final List<String> AFKPlayers;
    private final Server       server;
    
    public boolean isAFK(final String player) {
        return AFKPlayers.contains(player);
    }
    
    protected void _addAFK(final String player) {
        AFKPlayers.add(player);
    }
    
    public void addAFK(final String player) {
        if ( !isAFK(player) ) {
            _addAFK(player);
        }
    }
    
    protected void _enterAFK(final String player) {
        _addAFK(player);
        server.broadcastMessage(player.concat(" is now AFK."));
    }
    
    public void enterAFK(final String player) {
        if ( !isAFK(player) ) {
            _enterAFK(player);
        }
    }
    
    protected void _deleteAFK(final String player) {
        AFKPlayers.remove(player);
    }
    
    public void deleteAFK(final String player) {
        if ( isAFK(player) ) {
            _deleteAFK(player);
        }
    }
    
    protected void _leaveAFK(final String player) {
        _deleteAFK(player);
        server.broadcastMessage(player.concat(" is no longer AFK."));
    }
    
    public void leaveAFK(final String player) {
        if ( isAFK(player) ) {
            _leaveAFK(player);
        }
    }
    
    public void toggleAFK(final String player) {
        if ( isAFK(player) ) {
            _leaveAFK(player);
        } else {
            _enterAFK(player);
        }
    }
    
    public String[] getAFKs() {
        return AFKPlayers.toArray(new String[0]);
    }
    
    public AFKList(final Server server) {
        this.server     = server;
        this.AFKPlayers = new LinkedList<String>();
    }
}
