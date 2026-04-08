package org.dghdidi.stafftool.feature.clientchecker;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import static org.dghdidi.stafftool.StaffTool.plugin;
import static org.dghdidi.stafftool.feature.clientchecker.Service.executorService;
import static org.dghdidi.stafftool.listener.LoginLogoutListener.staffPrefix;
import static org.dghdidi.stafftool.util.PlayerUtil.*;

public class AutoMessage extends Command implements TabExecutor {

    public static String playerPrefix;

    public AutoMessage() {
        super("amc", "staff.amc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer staff)) {
            sendMessage(sender, "§c只有玩家才能使用此命令!");
            return;
        }
        if (args.length != 2) {
            sendMessage(sender, "§c用法: /amc <ID> <你的QQ>");
            return;
        }
        String targetID = args[0];
        String QQ = args[1];
        ProxiedPlayer player = plugin.getProxy().getPlayer(targetID);
        if (player == null) {
            sendMessage(sender, "§c当前玩家不在线或不存在!");
            return;
        }
        if (InfoBook.check(player)) {
            sendMessage(sender, "§c当前玩家已经被发送查端信息!");
            return;
        }
        sendAll(staffPrefix + getDisplayName(sender) + " §a正在向 " + getDisplayName(player) + " §a发送查端信息", "staff.notify");
        InfoBook.add(player, executorService.submit((Callable<Void>) () -> {
            runTask(player, staff, QQ, targetID);
            return null;
        }));
    }

    private void runTask(ProxiedPlayer player, ProxiedPlayer staff, String QQ, String targetID) {
        int counter = 1;
        while (counter <= 20) {
            if (!player.isConnected()) {
                sendMessage(staff, "§a§l玩家§e§l" + targetID + "§c§l已经离线§7(视为拒绝查端), §a§l请执行处罚");
                plugin.getProxy().getPluginManager().dispatchCommand(staff, "punish " + targetID);
                InfoBook.del(player);
                return;
            } else {
                sendMessage(staff, "§a§l已向玩家§e§l" + targetID + "§a§l发送查端信息 §7(" + counter + "/20)!");
            }
            sendMessage(player, playerPrefix + "§f您的游戏行为被§e§l检测异常§f, 为创造良好的游戏环境, 需要对您的客户端进行检查," + "请您§e在五分钟内§f添加工作人员QQ: §b" + QQ + ", §e§l无视或退出服务器§f将会被视为作弊§c§l封禁§f, 感谢您的理解和配合!");
            sendTitle(player, "§c§l查端通知", "§e请在五分钟内添加QQ§b§l" + QQ, 10, 250, 10);
            counter++;
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                plugin.getLogger().log(Level.WARNING, "§c线程等待异常§e(可能是有工作人员取消信息发送)");
                if (InfoBook.check(player))
                    InfoBook.del(player);
                return;
            }
        }
        sendMessage(staff, "§a§l查端信息已发送完毕 请判断是否施加处罚");
        InfoBook.del(player);
    }


    public void sendTitle(ProxiedPlayer player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        Title object = plugin.getProxy().createTitle()
                .title(new TextComponent(title))
                .subTitle(new TextComponent(subTitle))
                .fadeIn(fadeIn).stay(stay).fadeOut(fadeOut);
        object.send(player);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length != 1)
            return new ArrayList<>();
        else
            return tabComplete(args);
    }
}