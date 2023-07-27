package com.dghdidi.stafftool;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class broadCastCMD extends Command {

    public final Plugin plugin;

    public broadCastCMD(Plugin plugin) {
        super("mp", "staff.bc");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("该指令只能由玩家执行!");
            return;
        }
        if (!sender.hasPermission("staff.bc")) {
            sender.sendMessage("§c你没有权限使用此命令!");
            return;
        }
        String message = "§c§l一位玩家因为违反游戏规定而被处罚 §e使用§a/report§e进行举报!";
        plugin.getProxy().broadcast(new TextComponent(message));
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length == 0)
            return;
        StringBuilder command = new StringBuilder();
        for (String arg : args) command.append(arg).append(" ");
        plugin.getProxy().getPluginManager().dispatchCommand(player, command.toString());
    }
}
