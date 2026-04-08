package org.dghdidi.stafftool.feature;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

import static org.dghdidi.stafftool.StaffTool.luckPerms;
import static org.dghdidi.stafftool.StaffTool.plugin;
import static org.dghdidi.stafftool.util.PlayerUtil.getDisplayName;
import static org.dghdidi.stafftool.util.PlayerUtil.sendMessage;

public class OnlineStaff extends Command {

    public OnlineStaff() {
        super("staffs", "staff.online");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<ProxiedPlayer> staffs = new ArrayList<>();
        for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if (player.hasPermission("staff.online")) {
                staffs.add(player);
            }
        }
        staffs.sort((o1, o2) -> {
            User u1 = luckPerms.getUserManager().getUser(o1.getUniqueId());
            User u2 = luckPerms.getUserManager().getUser(o2.getUniqueId());
            int w1 = 0;
            int w2 = 0;
            if (u1 != null) {
                Group g1 = luckPerms.getGroupManager().getGroup(u1.getPrimaryGroup());
                if (g1 != null) w1 = g1.getWeight().orElse(0);
            }
            if (u2 != null) {
                Group g2 = luckPerms.getGroupManager().getGroup(u2.getPrimaryGroup());
                if (g2 != null) w2 = g2.getWeight().orElse(0);
            }
            if (w1 == w2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
            return Integer.compare(w2, w1);
        });

        sendMessage(sender, "§e在线工作人员 §a" + staffs.size() + "人§e:");
        for (ProxiedPlayer staff : staffs) {
            String server = " §7(" + staff.getServer().getInfo().getName() + ")";
            sendMessage(sender, getDisplayName(staff) + server);
        }

    }

}