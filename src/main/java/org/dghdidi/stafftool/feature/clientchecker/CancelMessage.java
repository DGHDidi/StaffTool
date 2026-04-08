package org.dghdidi.stafftool.feature.clientchecker;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

import static org.dghdidi.stafftool.StaffTool.plugin;
import static org.dghdidi.stafftool.listener.LoginLogoutListener.staffPrefix;
import static org.dghdidi.stafftool.util.PlayerUtil.*;


public class CancelMessage extends Command implements TabExecutor {

    public CancelMessage() {
        super("unamc", "staff.amc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sendMessage(sender, "§c只有玩家才能使用此命令!");
            return;
        }
        if (args.length != 1) {
            sendMessage(sender, "§c用法: /unamc <ID>");
            return;
        }
        String ID = args[0];
        ProxiedPlayer player = plugin.getProxy().getPlayer(ID);
        if (player == null || !player.isConnected()) {
            sendMessage(sender, "§c当前玩家不在线!");
            return;
        }
        if (!InfoBook.check(player)) {
            sendMessage(sender, "§c当前玩家没有被发送查端命令!");
            return;
        }
        InfoBook.get(player).cancel(true);
        InfoBook.del(player);
        sendMessage(sender, "§a§l成功解除对玩家§e§l" + ID + "§a§l的查端命令");
        sendMessage(player, "§a§l您的查端命令已被工作人员解除");
        sendAll(staffPrefix + getDisplayName(sender) + " §a解除了对玩家 " + getDisplayName(player) + " §a的查端命令");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length != 1)
            return new ArrayList<>();
        else
            return tabComplete(args);
    }
}