package org.dghdidi.stafftool.feature;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import static org.dghdidi.stafftool.StaffTool.manager;
import static org.dghdidi.stafftool.StaffTool.proxy;
import static org.dghdidi.stafftool.util.PlayerUtil.sendMessage;

public class PunishAlert extends Command {

    public PunishAlert() {
        super("mp", "staff.alert");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            sendMessage(sender, "§c该指令只能由玩家执行!");
            return;
        }
        if (!sender.hasPermission("staff.bc")) {
            sendMessage(sender, "§c你没有权限使用此命令!");
            return;
        }
        String message = "§c§l一位玩家因为违反游戏规定而被处罚 §e使用 §a/report <ID> §e进行举报!";
        proxy.broadcast(new TextComponent(message));
        if (args.length == 0)
            return;
        StringBuilder command = new StringBuilder();
        for (String arg : args) {
            command.append(arg).append(" ");
        }
        manager.dispatchCommand(player, command.toString());
    }
}