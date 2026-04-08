package org.dghdidi.stafftool.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static org.dghdidi.stafftool.util.PlayerUtil.getDisplayName;
import static org.dghdidi.stafftool.util.PlayerUtil.sendAll;

public class LoginLogoutListener implements Listener {

    public static String staffPrefix;

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player.hasPermission("staff.notify")) {
            sendAll(staffPrefix + getDisplayName(player) + " §e上线了.", "staff.notify");
        }
    }

    @EventHandler
    public void onPlayerExit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player.hasPermission("stafftool.notify")) {
            sendAll(staffPrefix + getDisplayName(player) + " §e离线了.", "staff.notify");
        }
    }

}
