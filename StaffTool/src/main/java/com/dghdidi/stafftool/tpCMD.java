package com.dghdidi.stafftool;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class tpCMD extends Command implements TabExecutor, Listener {

    private final Plugin plugin;

    public tpCMD(Plugin plugin) {
        super("tpto", "staff.tpto");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("只有玩家才能执行此命令!");
            return;
        }
        if (!sender.hasPermission("staff.tpto")) {
            sender.sendMessage("§c你没有权限执行此命令!");
            return;
        }
        if (args.length != 1) {
            sender.sendMessage("§c用法: /tpto <ID>");
            return;
        }
        String targetID = args[0];
        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetID);
        if (targetPlayer == null) {
            sender.sendMessage("§c当前玩家不在线或不存在!");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        Server server = targetPlayer.getServer();
        if (server.getInfo().equals(player.getServer().getInfo())) {
            player.chat("/tp " + targetID);
            return;
        }
        String serverName = server.getInfo().getName();
        sender.sendMessage("§7正在传送到服务器 " + serverName + "...");
        player.connect(server.getInfo());
        plugin.getProxy().getScheduler().schedule(plugin, () -> {
            player.chat("/tp " + targetID);
        }, 3, TimeUnit.SECONDS);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
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
