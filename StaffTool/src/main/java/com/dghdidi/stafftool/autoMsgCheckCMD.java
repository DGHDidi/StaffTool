package com.dghdidi.stafftool;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.concurrent.Callable;

import static com.dghdidi.stafftool.AsyncMsgCheck.executorService;
import static com.dghdidi.stafftool.tpCMD.getStrings;

public class autoMsgCheckCMD extends Command implements TabExecutor {

    private final Plugin plugin;
    private ProxiedPlayer player, staff;
    private String QQ, targetID;

    public autoMsgCheckCMD(Plugin plugin) {
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
        staff = (ProxiedPlayer) sender;
        targetID = args[0];
        QQ = args[1];
        player = plugin.getProxy().getPlayer(targetID);
        if (player == null) {
            sender.sendMessage(new TextComponent("§c当前玩家不在线或不存在!"));
            return;
        }

        executorService.submit((Callable<Void>) () -> {
            runTask(player, staff, QQ, targetID);
            return null;
        });


    }

    private void runTask(ProxiedPlayer _player, ProxiedPlayer _staff, String _QQ, String _targetID) {
        int counter = 1;


        while (counter <= 20) {
            if (plugin.getProxy().getPlayer(_targetID) == null) {
                _staff.sendMessage(new TextComponent("§a§l玩家§e§l" + _targetID + "§c§l已经离线§7(视为拒绝查端), §a§l请执行处罚"));
                plugin.getProxy().getPluginManager().dispatchCommand(_staff, "punish " + _targetID);
                return;
            } else {
                _staff.sendMessage(new TextComponent("§a§l已向玩家§e§l" + _targetID + "§a§l发送查端信息 §7(" + counter + "/20)!"));
            }
            _player.sendMessage(new TextComponent("§cInf Staff §8> §f您的游戏行为被§e§l检测异常§f, 为创造良好的游戏环境, 需要对您的客户端进行检查," + "请您§e在五分钟内§f添加工作人员QQ: §b" + _QQ + ", §e§l无视或退出服务器§f将会被视为作弊§c§l封禁§f, 感谢您的理解和配合!"));
            counter++;
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        _staff.sendMessage(new TextComponent("§a§l查端信息已发送完毕 请判断是否施加处罚"));
    }


    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return getStrings(args);
    }
}
