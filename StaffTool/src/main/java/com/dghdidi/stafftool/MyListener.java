package com.dghdidi.stafftool;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import static com.dghdidi.stafftool.StaffTool.connection;

public class MyListener implements Listener {
    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        if (event.isCommand())
            return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String playerName = player.getName();
        String message = event.getMessage();
        String serverName = player.getServer().getInfo().getName();
        Timestamp timestamp = new Timestamp(Instant.now().getEpochSecond() * 1000);
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement
                    ("INSERT INTO chat_log (player_name, message, timestamp, server_name) VALUES (?, ?, ?, ?)");
            statement.setString(1, playerName);
            statement.setString(2, message);
            statement.setTimestamp(3, timestamp);
            statement.setString(4, serverName);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
    

