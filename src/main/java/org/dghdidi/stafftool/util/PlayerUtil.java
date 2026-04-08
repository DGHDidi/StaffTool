package org.dghdidi.stafftool.util;

import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.dghdidi.stafftool.StaffTool;

import java.util.ArrayList;
import java.util.List;

import static org.dghdidi.stafftool.StaffTool.luckPerms;

public class PlayerUtil {

    public static List<ProxiedPlayer> getPlayers(String perm) {
        List<ProxiedPlayer> players = new ArrayList<>();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.hasPermission(perm)) {
                players.add(player);
            }
        }
        return players;
    }

    public static String getDisplayName(CommandSender player) {
        return getDisplayName((ProxiedPlayer) player);
    }

    public static String getDisplayName(ProxiedPlayer player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            StaffTool.logger.info("§c§l无法获取玩家 " + player.getName() + " 的 LuckPerms 用户数据");
            return null;
        }
        String prefix = user.getCachedData().getMetaData().getPrefix() == null ? "§7" : user.getCachedData().getMetaData().getPrefix();
        String suffix = user.getCachedData().getMetaData().getSuffix() == null ? "§7" : user.getCachedData().getMetaData().getSuffix();
        return transColor(prefix) + player.getName() + transColor(suffix);
    }

    public static String getDisplayName(String playerName) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        return getDisplayName(player);
    }

    public static String transColor(String arg) {
        return ChatColor.translateAlternateColorCodes('&', String.join(" ", arg));
    }

    public static void sendMessage(CommandSender player, String msg) {
        player.sendMessage(new TextComponent(msg));
    }

    public static void sendMessage(CommandSender player, BaseComponent... components) {
        player.sendMessage(components);
    }

    public static void sendMessage(ProxiedPlayer player, String msg) {
        player.sendMessage(new TextComponent(msg));
    }

    public static void sendMessage(ProxiedPlayer player, BaseComponent... components) {
        player.sendMessage(components);
    }

    public static void sendMessage(String playerName, String msg) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        if (player != null) {
            player.sendMessage(new TextComponent(msg));
        }
    }

    public static void sendAll(String msg) {
        for (ProxiedPlayer player : StaffTool.proxy.getPlayers()) {
            sendMessage(player, msg);
        }
    }

    public static void sendAll(String msg, String perm) {
        for (ProxiedPlayer player : getPlayers(perm)) {
            sendMessage(player, msg);
        }
    }

    public static Iterable<String> tabComplete(String[] args) {
        if (args.length == 1) {
            List<String> playerIDs = new ArrayList<>();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                playerIDs.add(player.getName());
            }
            List<String> matchedIDs = new ArrayList<>();
            String partialID = args[0].toLowerCase();
            for (String id : playerIDs) {
                if (id.toLowerCase().startsWith(partialID)) {
                    matchedIDs.add(id);
                }
            }
            return matchedIDs;
        }
        return null;
    }
}
