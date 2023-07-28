package com.dghdidi.stafftool;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

import static com.dghdidi.stafftool.TpCMD.getStrings;


public class CancelAmcCMD extends Command implements TabExecutor {

    private final Plugin plugin;

    public CancelAmcCMD(Plugin plugin) {
        super("unamc", "staff.amc");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§c只有玩家才能使用此命令!"));
            return;
        }
        if (args.length != 1) {
            sender.sendMessage(new TextComponent("§c用法: /unamc <ID>"));
            return;
        }
        String ID = args[0];
        ProxiedPlayer player = plugin.getProxy().getPlayer(ID);
        if (!player.isConnected()) {
            sender.sendMessage(new TextComponent("§c当前玩家不在线!"));
            return;
        }
        if (!BookAMC.check(player)) {
            sender.sendMessage(new TextComponent("§c当前玩家没有被发送查端命令!"));
            return;
        }
        BookAMC.get(player).cancel(true);
        BookAMC.del(player);
        sender.sendMessage(new TextComponent("§a§l成功解除对玩家§e§l" + ID + "§a§l的查端命令"));
        player.sendMessage(new TextComponent("§a§l您的查端命令已被工作人员解除"));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return getStrings(args);
    }
}
