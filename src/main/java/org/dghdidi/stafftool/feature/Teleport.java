package org.dghdidi.stafftool.feature;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.dghdidi.stafftool.StaffTool.plugin;
import static org.dghdidi.stafftool.StaffTool.proxy;
import static org.dghdidi.stafftool.util.PlayerUtil.sendMessage;
import static org.dghdidi.stafftool.util.PlayerUtil.tabComplete;

public class Teleport extends Command implements TabExecutor {

    public Teleport() {
        super("tpto", "staff.tpto");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            sendMessage(sender, "只有玩家才能执行此命令!");
            return;
        }
        if (!sender.hasPermission("staff.tpto")) {
            sendMessage(sender, "§c你没有权限执行此命令!");
            return;
        }
        if (args.length != 1) {
            sendMessage(sender, "§c用法: /tpto <ID>");
            return;
        }
        String targetID = args[0];
        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetID);
        if (targetPlayer == null) {
            sendMessage(sender, "§c当前玩家不在线或不存在!");
            return;
        }
        Server server = targetPlayer.getServer();
        if (server.getInfo().equals(player.getServer().getInfo())) {
            player.chat("/tp " + targetID);
            return;
        }
        String serverName = server.getInfo().getName();
        sendMessage(sender, "§7正在传送到服务器 " + serverName + "...");
        player.connect(server.getInfo());
        proxy.getScheduler().schedule(plugin, () -> player.chat("/tp " + targetID), 3, TimeUnit.SECONDS);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length != 1)
            return new ArrayList<>();
        else
            return tabComplete(args);
    }
}