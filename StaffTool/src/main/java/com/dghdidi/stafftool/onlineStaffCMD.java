package com.dghdidi.stafftool;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class onlineStaffCMD extends Command {

    private final Plugin plugin;

    public onlineStaffCMD(Plugin plugin) {
        super("staff", "staff.online");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§e在线工作人员:");
        List<ProxiedPlayer> staffs = new ArrayList<>();
        for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if (player.hasPermission("staff.online")) {
                staffs.add(player);
            }
        }
        for (ProxiedPlayer staff : staffs) {
            sender.sendMessage(staff.getDisplayName());
        }

    }

}
