package org.dghdidi.stafftool.feature.chat;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import static org.dghdidi.stafftool.listener.LoginLogoutListener.staffPrefix;
import static org.dghdidi.stafftool.util.PlayerUtil.*;

public class StaffChat extends Command {

    public StaffChat() {
        super("sc", "staff.chat.staff", "staffchat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            sendMessage(sender, "§c该指令只能由玩家执行!");
            return;
        }
        if (args.length != 1) {
            sendMessage(sender, "§c用法: /sc <消息>");
            return;
        }
        sendAll(staffPrefix + getDisplayName(sender) + "§f: " + args[0], "staff.chat.staff");
    }
}
