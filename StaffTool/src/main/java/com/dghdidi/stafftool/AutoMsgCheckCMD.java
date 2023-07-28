package com.dghdidi.stafftool;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.concurrent.Callable;
import java.util.logging.Level;

import static com.dghdidi.stafftool.AsyncMsgCheck.executorService;
import static com.dghdidi.stafftool.TpCMD.getStrings;

public class AutoMsgCheckCMD extends Command implements TabExecutor {

    private final Plugin plugin;

    public AutoMsgCheckCMD(Plugin plugin) {
        super("amc", "staff.amc");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§c只有玩家才能使用此命令!"));
            return;
        }
        if (args.length != 2) {
            sender.sendMessage(new TextComponent("§c用法: /amc <ID> <你的QQ>"));
            return;
        }
        ProxiedPlayer staff = (ProxiedPlayer) sender;
        String targetID = args[0];
        String QQ = args[1];
        ProxiedPlayer player = plugin.getProxy().getPlayer(targetID);
        if (player == null) {
            sender.sendMessage(new TextComponent("§c当前玩家不在线或不存在!"));
            return;
        }
        if (BookAMC.check(player)) {
            sender.sendMessage(new TextComponent("§c当前玩家已经被发送查端信息!"));
            return;
        }
        BookAMC.add(player, executorService.submit((Callable<Void>) () -> {
            runTask(player, staff, QQ, targetID);
            return null;
        }));
    }

    private void runTask(ProxiedPlayer player, ProxiedPlayer staff, String QQ, String targetID) {
        int counter = 1;
        while (counter <= 20) {
            if (!player.isConnected()) {
                staff.sendMessage(new TextComponent("§a§l玩家§e§l" + targetID + "§c§l已经离线§7(视为拒绝查端), §a§l请执行处罚"));
                plugin.getProxy().getPluginManager().dispatchCommand(staff, "punish " + targetID);
                BookAMC.del(player);
                return;
            } else {
                staff.sendMessage(new TextComponent("§a§l已向玩家§e§l" + targetID + "§a§l发送查端信息 §7(" + counter + "/20)!"));
            }
            player.sendMessage(new TextComponent("§cInf Staff §8> §f您的游戏行为被§e§l检测异常§f, 为创造良好的游戏环境, 需要对您的客户端进行检查," + "请您§e在五分钟内§f添加工作人员QQ: §b" + QQ + ", §e§l无视或退出服务器§f将会被视为作弊§c§l封禁§f, 感谢您的理解和配合!"));
            counter++;
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                plugin.getLogger().log(Level.WARNING, "§c线程等待异常§e(可能是有工作人员取消信息发送)");
                if (BookAMC.check(player))
                    BookAMC.del(player);
                return;
            }
        }
        staff.sendMessage(new TextComponent("§a§l查端信息已发送完毕 请判断是否施加处罚"));
        BookAMC.del(player);
    }


    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return getStrings(args);
    }
}
