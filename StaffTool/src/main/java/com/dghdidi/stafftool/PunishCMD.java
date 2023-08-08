package com.dghdidi.stafftool;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

import static com.dghdidi.stafftool.TpCMD.getStrings;

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
        BaseComponent[] BAN_7D = createClickableText
                ("[7D]", "/mp ipban " + ID + " 7d 使用第三方软件破坏游戏规则", "§e点击封禁7天");
        BaseComponent[] BAN_14D = createClickableText
                ("[14D]", "/mp ipban " + ID + " 14d 使用第三方软件破坏游戏规则", "§e点击封禁14天");
        BaseComponent[] BAN_30D = createClickableText
                ("[30D]", "/mp ipban " + ID + " 30d 使用第三方软件破坏游戏规则", "§e点击封禁30天");
        BaseComponent[] BAN_90D = createClickableText
                ("[90D]", "/mp ipban " + ID + " 90d 使用第三方软件破坏游戏规则", "§e点击封禁90天");
        BaseComponent[] BAN_360D = createClickableText
                ("[360D]", "/mp ipban " + ID + " 360d 使用第三方软件破坏游戏规则", "§e点击封禁360天");
        BaseComponent[] BAN_PERM = createClickableText
                ("[永久]", "/mp ipban " + ID + " 使用第三方软件破坏游戏规则", "§e点击封禁永久");
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
        BaseComponent[] MUTE_10MIN = createClickableText
                ("[10MIN]", "/mp ipmute " + ID + " 10min 言辞过激或违规, 请注意言行举止", "§e点击禁言10分钟");
        BaseComponent[] MUTE_3H = createClickableText
                ("[3H]", "/mp ipmute " + ID + " 3h 言辞过激或违规, 请注意言行举止", "§e点击禁言3小时");
        BaseComponent[] MUTE_1D = createClickableText
                ("[1D]", "/mp ipmute " + ID + " 1d 言辞过激或违规, 请注意言行举止", "§e点击禁言1天");
        BaseComponent[] MUTE_3D = createClickableText
                ("[3D]", "/mp ipmute " + ID + " 3d 言辞过激或违规, 请注意言行举止", "§e点击禁言3天");
        BaseComponent[] MUTE_7D = createClickableText
                ("[7D]", "/mp ipmute " + ID + " 7d 言辞过激或违规, 请注意言行举止", "§e点击禁言7天");
        BaseComponent[] MUTE_30D = createClickableText
                ("[30D]", "/mp ipmute " + ID + " 30d 言辞过激或违规, 请注意言行举止", "§e点击禁言30天");
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
        sender.sendMessage(new TextComponent("§e§l请选择你需要的处罚:" + " §b§l" + ID));
        if (sender.hasPermission("staff.punish.ban"))
            sender.sendMessage(LineOne);
        if (sender.hasPermission("staff.punish.mute"))
            sender.sendMessage(LineTwo);
        sender.sendMessage(new TextComponent("§c---------------------------------"));

    }

    private BaseComponent[] createClickableText(String text, String Command, String HoverText) {
        return new ComponentBuilder(text)
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Command))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(HoverText).create()))
                .create();
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length != 1)
            return new ArrayList<>();
        else
            return getStrings(args);
    }
}

