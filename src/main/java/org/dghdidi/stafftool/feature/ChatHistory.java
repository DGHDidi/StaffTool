package org.dghdidi.stafftool.feature;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;

import static org.dghdidi.stafftool.StaffTool.databaseManager;
import static org.dghdidi.stafftool.StaffTool.logger;
import static org.dghdidi.stafftool.util.PlayerUtil.sendMessage;
import static org.dghdidi.stafftool.util.PlayerUtil.tabComplete;

public class ChatHistory extends Command implements TabExecutor {

    public ChatHistory() {
        super("chathistory", "staff.chathistory");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sendMessage(sender, "§c用法: /chathistory <ID> <页码>");
            return;
        }

        if (databaseManager == null) {
            sendMessage(sender, "§c数据库未初始化, 请联系管理员检查配置");
            return;
        }

        String playerId = args[0];
        int page;
        try {
            page = Integer.parseInt(args[1]);
            if (page <= 0) {
                sendMessage(sender, "§c页码必须大于 0");
                return;
            }
        } catch (NumberFormatException e) {
            sendMessage(sender, "§c页码必须是数字");
            return;
        }

        final int pageSize = 15;
        final String countSql = "SELECT COUNT(*) AS record_count FROM chat_log WHERE player_name = ?";
        final String querySql = "SELECT player_name, message, server_name, created_at " +
                "FROM chat_log WHERE player_name = ? " +
                "ORDER BY created_at DESC, id DESC LIMIT ? OFFSET ?";

        try (Connection connection = databaseManager.getConnection()) {
            int total;
            try (PreparedStatement countStatement = connection.prepareStatement(countSql)) {
                countStatement.setString(1, playerId);
                try (ResultSet resultSet = countStatement.executeQuery()) {
                    total = resultSet.next() ? resultSet.getInt("record_count") : 0;
                }
            }

            if (total == 0) {
                sendMessage(sender, "§e没有找到玩家 §f" + playerId + " §e的聊天记录");
                return;
            }

            int pageLimit = (total + pageSize - 1) / pageSize;
            if (page > pageLimit) {
                sendMessage(sender, "§c页码超出范围, 最大页码为 §f" + pageLimit);
                return;
            }

            sendMessage(sender, "§e" + playerId + "§a 的聊天记录: §7第" + page + "页/共" + pageLimit + "页");

            try (PreparedStatement queryStatement = connection.prepareStatement(querySql)) {
                queryStatement.setString(1, playerId);
                queryStatement.setInt(2, pageSize);
                queryStatement.setInt(3, (page - 1) * pageSize);

                try (ResultSet resultSet = queryStatement.executeQuery()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
                    while (resultSet.next()) {
                        String playerName = resultSet.getString("player_name");
                        String message = resultSet.getString("message");
                        String serverName = resultSet.getString("server_name");
                        if (serverName == null || serverName.isBlank()) {
                            serverName = "Unknown";
                        }
                        Timestamp createdAt = resultSet.getTimestamp("created_at");
                        LocalDateTime localDateTime = createdAt.toLocalDateTime();
                        String timeText = localDateTime.format(formatter);

                        TextComponent text = new TextComponent("§7<" + timeText + " " + serverName + "> §f" + message);
                        sendMessage(sender, text);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "抓取聊天记录失败", e);
            sendMessage(sender, "§c抓取聊天记录失败!");
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length != 1)
            return new ArrayList<>();
        else
            return tabComplete(args);
    }
}
