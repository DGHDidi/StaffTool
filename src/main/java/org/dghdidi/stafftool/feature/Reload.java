package org.dghdidi.stafftool.feature;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.dghdidi.stafftool.config.LoadConfig;

import java.util.Objects;

import static org.dghdidi.stafftool.StaffTool.databaseManager;
import static org.dghdidi.stafftool.StaffTool.plugin;
import static org.dghdidi.stafftool.util.PlayerUtil.sendMessage;


public class Reload extends Command {

    public Reload() {
        super("stafftool", "staff.reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sendMessage(sender, "§c用法: /stafftool reload|help");
            return;
        }
        if (Objects.equals(args[0], "reload")) {
            sendMessage(sender, "§e正在重载配置与数据库连接...");

            plugin.getProxy().getScheduler().runAsync(plugin, () -> {
                try {
                    LoadConfig.loadConfig();
                    databaseManager.reload();
                    sendMessage(sender, "§a数据库连接成功，插件已成功重载!");
                } catch (Exception e) {
                    sendMessage(sender, "§c重载失败或数据库连接失败，请检查配置文件!");
                    e.printStackTrace();
                }
            });
        } else if (Objects.equals(args[0], "help")) {
            sendMessage(sender, "§8---------§b§lStaffTool§8---------");
            sendMessage(sender, "§a/punish <ID> §7对玩家实施处罚");
            sendMessage(sender, "§a/tpto <ID> §7跨服传送到某玩家的位置");
            sendMessage(sender, "§a/amc|unamc <ID> §7向某玩家发送(解除)查端命令");
            sendMessage(sender, "§a/chathistory <ID> §7查看某玩家的聊天记录");
            sendMessage(sender, "§a/staffs §7查看在线工作人员");
            sendMessage(sender, "§a/stafftool reload §7重载插件");
            sendMessage(sender, "§8---------------------------");
        } else {
            sendMessage(sender, "§c用法: /stafftool reload|help");
        }
    }
}