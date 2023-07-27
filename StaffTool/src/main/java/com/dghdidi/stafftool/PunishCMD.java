package com.dghdidi.stafftool;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;


import static com.dghdidi.stafftool.tpCMD.getStrings;

public class PunishCMD extends Command implements TabExecutor {

    private final Plugin plugin;
    public PunishCMD(Plugin plugin) {
        super("punish", "staff.punish");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§c该指令只能由玩家执行!"));
            return;
        }
        if (args.length != 1) {
            sender.sendMessage(new TextComponent("§c用法: /punish <ID>"));
            return;
        }
        String ID = args[0];

        ProxiedPlayer player = (ProxiedPlayer) sender;
        plugin.getProxy().getPluginManager().dispatchCommand(player, "history " + ID);
        BaseComponent LineOne = new TextComponent("封禁类:");
        BaseComponent[] BAN_7D = new ComponentBuilder("[7D]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp ban " + ID + " 7d 使用第三方软件破坏游戏平衡"))
                .create();
        BaseComponent[] BAN_14D = new ComponentBuilder("[14D]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp ban " + ID + " 14d 使用第三方软件破坏游戏平衡"))
                .create();
        BaseComponent[] BAN_30D = new ComponentBuilder("[30D]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp ban " + ID + " 30d 使用第三方软件破坏游戏平衡"))
                .create();
        BaseComponent[] BAN_90D = new ComponentBuilder("[90D]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp ban " + ID + " 90d 使用第三方软件破坏游戏平衡"))
                .create();
        BaseComponent[] BAN_360D = new ComponentBuilder("[360D]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp ban " + ID + " 360d 使用第三方软件破坏游戏平衡"))
                .create();
        BaseComponent[] BAN_PERM = new ComponentBuilder("[永久]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp ban " + ID + " 使用第三方软件破坏游戏平衡"))
                .create();
        LineOne.addExtra(" ");
        LineOne.addExtra(BAN_7D[0]);
        LineOne.addExtra(" ");
        LineOne.addExtra(BAN_14D[0]);
        LineOne.addExtra(" ");
        LineOne.addExtra(BAN_30D[0]);
        LineOne.addExtra(" ");
        LineOne.addExtra(BAN_90D[0]);
        LineOne.addExtra(" ");
        LineOne.addExtra(BAN_360D[0]);
        LineOne.addExtra(" ");
        LineOne.addExtra(BAN_PERM[0]);

        BaseComponent LineTwo = new TextComponent("禁言类:");
        BaseComponent[] MUTE_10MIN = new ComponentBuilder("[10MIN]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp mute " + ID + " 10min 言论过激或违规, 请注意言行举止"))
                .create();
        BaseComponent[] MUTE_3H = new ComponentBuilder("[3H]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp mute " + ID + " 3h 言论过激或违规, 请注意言行举止"))
                .create();
        BaseComponent[] MUTE_1D = new ComponentBuilder("[1D]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp mute " + ID + " 1d 言论过激或违规, 请注意言行举止"))
                .create();
        BaseComponent[] MUTE_3D = new ComponentBuilder("[3D]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp mute " + ID + " 3d 言论过激或违规, 请注意言行举止"))
                .create();
        BaseComponent[] MUTE_7D = new ComponentBuilder("[7D]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp mute " + ID + " 7d 言论过激或违规, 请注意言行举止"))
                .create();
        BaseComponent[] MUTE_30D = new ComponentBuilder("[30D]")
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mp mute " + ID + " 30d 言论过激或违规, 请注意言行举止"))
                .create();

        LineTwo.addExtra(" ");
        LineTwo.addExtra(MUTE_10MIN[0]);
        LineTwo.addExtra(" ");
        LineTwo.addExtra(MUTE_3H[0]);
        LineTwo.addExtra(" ");
        LineTwo.addExtra(MUTE_1D[0]);
        LineTwo.addExtra(" ");
        LineTwo.addExtra(MUTE_3D[0]);
        LineTwo.addExtra(" ");
        LineTwo.addExtra(MUTE_7D[0]);
        LineTwo.addExtra(" ");
        LineTwo.addExtra(MUTE_30D[0]);

        sender.sendMessage(new TextComponent("§c---------------------------------"));
        sender.sendMessage(new TextComponent("§e§l请选择你需要的处罚:"));
        sender.sendMessage(LineOne);
        sender.sendMessage(LineTwo);
        sender.sendMessage(new TextComponent("§c---------------------------------"));

    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return getStrings(args);
    }
}
