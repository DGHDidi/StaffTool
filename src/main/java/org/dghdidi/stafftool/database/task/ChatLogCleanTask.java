package org.dghdidi.stafftool.database.task;

import org.dghdidi.stafftool.StaffTool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.dghdidi.stafftool.StaffTool.databaseManager;

public class ChatLogCleanTask implements Runnable {

    @Override
    public void run() {
        int deleted = cleanOldChatLogs();

        if (deleted >= 0) {
            StaffTool.logger.info("§a已清理 " + deleted + " 条 7 天前的聊天记录");
        }
    }

    public int cleanOldChatLogs() {
        String sql = """
                DELETE FROM chat_log
                WHERE created_at < NOW() - INTERVAL 7 DAY
                """;

        try (Connection connection = databaseManager.getConnection();
             Statement statement = connection.createStatement()) {

            return statement.executeUpdate(sql);

        } catch (SQLException e) {
            StaffTool.logger.warning("§e清理过期聊天记录失败");
            e.printStackTrace();
            return -1;
        }
    }
}