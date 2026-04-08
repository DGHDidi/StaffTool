package org.dghdidi.stafftool.feature.reports;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

import static org.dghdidi.stafftool.StaffTool.databaseManager;
import static org.dghdidi.stafftool.StaffTool.proxy;
import static org.dghdidi.stafftool.feature.reports.ReportPunishment.getRemainTime;
import static org.dghdidi.stafftool.feature.reports.ReportPunishment.isUnderPunish;
import static org.dghdidi.stafftool.util.PlayerUtil.*;

public class PlayerCMD extends Command implements TabExecutor {

    public static String reportPlayerPrefix;

    public PlayerCMD() {
        super("report", "reports.player");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            sendMessage(sender, "§c只有玩家才能使用此命令!");
            return;
        }

        if (args.length != 2) {
            sendMessage(sender, "§c用法: /report <ID> <原因>");
            return;
        }

        String reportedName = args[0], reason = args[1], playerName = player.getName();
        ProxiedPlayer reportedPlayer = proxy.getPlayer(reportedName);

        if (isUnderPunish(player)) {
            sendMessage(sender, reportPlayerPrefix + "§c§l举报失败 §7(您因为违规举报被工作人员处罚, 在 " + getRemainTime(player) + "§7后才能进行举报");
            return;
        }
        if (Objects.equals(reportedName, playerName)) {
            sendMessage(sender, reportPlayerPrefix + "§c您不能举报你自己!");
            return;
        }
        if (reportedPlayer == null) {
            sendMessage(sender, reportPlayerPrefix + "§c该名玩家不存在或离线!");
            return;
        }
        if (reportedPlayer.hasPermission("reports.bypass")) {
            sendMessage(sender, reportPlayerPrefix + "§c你不能举报这名玩家!");
            return;
        }
        if (ReportsStorage.containsPair(playerName, reportedName)) {
            sendMessage(sender, reportPlayerPrefix + "§c您已举报过玩家 " + getDisplayName(reportedName) + "§c, 请勿重复举报!");
            return;
        }

        String serverName = reportedPlayer.getServer().getInfo().getName();
        try {
            String sql = "INSERT INTO report_info (player_name, reported_name, staff_name, server_name, reason) VALUES (?, ?, ?, ?, ?)";
            try (Connection connection = databaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, playerName);
                statement.setString(2, reportedName);
                statement.setString(3, "null");
                statement.setString(4, serverName);
                statement.setString(5, reason);
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        int nowId = keys.getInt(1);
                        ReportsStorage.add(nowId, playerName, reportedName);
                        for (ProxiedPlayer staff : getPlayers("reports.staff")) {
                            if (!ReportsStorage.showReport(staff, nowId, false)) {
                                sendMessage(staff, "§c该举报不存在!");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            sendMessage(sender, "§c数据库错误, 请联系管理员查看服务器后台.");
            throw new RuntimeException(e);
        }

        sendMessage(sender, reportPlayerPrefix + "§a举报成功, 请耐心等待处理!");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return args.length == 1 ? tabComplete(args) : new ArrayList<>();
    }
}