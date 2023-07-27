package com.dghdidi.stafftool;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class onlineStaffCMD extends Command {

    private final Plugin plugin;

    public onlineStaffCMD(Plugin plugin) {
        super("staffs", "staff.online");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(new TextComponent("§e在线工作人员:"));
        List<ProxiedPlayer> staffs = new ArrayList<>();
        for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if (player.hasPermission("staff.online")) {
                staffs.add(player);
            }
        }
        staffs.sort((o1, o2) -> {
            if (o1.hasPermission("group.admin") && !o2.hasPermission("group.admin") ||
                    o1.hasPermission("group.mod") && !o2.hasPermission("group.mod"))
                return -1;
            else if (!o1.hasPermission("group.admin") && o2.hasPermission("group.admin") ||
                    !o1.hasPermission("group.mod") && o2.hasPermission("group.mod"))
                return 1;
            else
                return o1.getName().length() - o2.getName().length();
        });
        for (ProxiedPlayer staff : staffs) {
            String name = staff.getName();
            if (staff.hasPermission("group.admin"))
                sender.sendMessage(new TextComponent("§c" + name));
            else if (staff.hasPermission("group.mod"))
                sender.sendMessage(new TextComponent("§2" + name));
            else if (staff.hasPermission("group.helper"))
                sender.sendMessage(new TextComponent("§9" + name));
        }

    }

}
