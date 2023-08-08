package com.dghdidi.stafftool;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TpCMD extends Command implements TabExecutor {

    private final Plugin plugin;

    public TpCMD(Plugin plugin) {
        super("tpto", "staff.tpto");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("只有玩家才能执行此命令!"));
            return;
        }
        if (!sender.hasPermission("staff.tpto")) {
            sender.sendMessage(new TextComponent("§c你没有权限执行此命令!"));
            return;
        }
        if (args.length != 1) {
            sender.sendMessage(new TextComponent("§c用法: /tpto <ID>"));
            return;
        }
        String targetID = args[0];
        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetID);
        if (targetPlayer == null) {
            sender.sendMessage(new TextComponent("§c当前玩家不在线或不存在!"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        Server server = targetPlayer.getServer();
        if (server.getInfo().equals(player.getServer().getInfo())) {
            player.chat("/tp " + targetID);
            return;
        }
        String serverName = server.getInfo().getName();
        sender.sendMessage(new TextComponent("§7正在传送到服务器 " + serverName + "..."));
        player.connect(server.getInfo());
        plugin.getProxy().getScheduler().schedule(plugin, () -> player.chat("/tp " + targetID), 3, TimeUnit.SECONDS);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length != 1)
            return new ArrayList<>();
        else
            return getStrings(args);
    }

    public static Iterable<String> getStrings(String[] args) {
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
