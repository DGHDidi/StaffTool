package org.dghdidi.stafftool.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.dghdidi.stafftool.StaffTool.databaseManager;
import static org.dghdidi.stafftool.StaffTool.plugin;
import static org.dghdidi.stafftool.StaffTool.logger;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer player)) {
            return;
        }
        String message = event.getMessage();
        if (event.isCommand() || message.startsWith("/") || message.isBlank()) {
            return;
        }

        String playerName = player.getName();
        String serverName = player.getServer() != null
                ? player.getServer().getInfo().getName()
                : "未知";

        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            String sql = """
                    INSERT INTO chat_log (player_name, message, server_name)
                    VALUES (?, ?, ?)
                    """;

            try (Connection connection = databaseManager.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, playerName);
                ps.setString(2, message);
                ps.setString(3, serverName);

                ps.executeUpdate();

            } catch (SQLException e) {
                logger.warning("§c聊天记录写入失败: " + playerName);
                e.printStackTrace();
            }
        });
    }
}