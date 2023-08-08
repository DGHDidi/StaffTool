package com.dghdidi.stafftool;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;

import static com.dghdidi.stafftool.StaffTool.connection;
import static com.dghdidi.stafftool.TpCMD.getStrings;

public class ChatHistoryCMD extends Command implements TabExecutor {

    private final Plugin plugin;

    public ChatHistoryCMD(Plugin plugin) {
        super("chathistory", "staff.chathistory");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(new TextComponent("§c用法: /chathistory <ID> <页码>"));
            return;
        }
        String ID = args[0];
        String sql = "SELECT COUNT(*) AS record_count FROM chat_log WHERE player_name = ?";
        int pageLimit = 0;
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int total = resultSet.getInt("record_count");
                pageLimit = total / 15 + 1;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "§c无法连接至数据库, 请检查配置文件");
            sender.sendMessage(new TextComponent("§c无法连接至数据库, 请检查配置文件"));
            return;
        }
        int Page = Integer.parseInt(args[1]);
        sender.sendMessage(new TextComponent("§e" + ID + "§a 的聊天记录: §7第" + Page + "页/共" + pageLimit + "页"));
        try {
            String sql_2 = "SELECT * FROM chat_log WHERE player_name = ? ORDER BY timestamp DESC LIMIT ?, 15";
            statement = connection.prepareStatement(sql_2);
            statement.setString(1, ID);
            statement.setInt(2, (Page - 1) * 15);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String playerName = resultSet.getString("player_name");
                String message = resultSet.getString("message");
                String serverName = resultSet.getString("server_name");
                Timestamp timestamp = resultSet.getTimestamp("timestamp");

                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String nowTime = localDateTime.format(formatter);

                TextComponent text = new TextComponent("§7<" + nowTime + " " + serverName + "> §f" + playerName + ": " + message);
                sender.sendMessage(text);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "§c抓取聊天记录失败!");
            sender.sendMessage(new TextComponent("§c抓取聊天记录失败!"));
        }

    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length != 1)
            return new ArrayList<>();
        else
            return getStrings(args);
    }
}
