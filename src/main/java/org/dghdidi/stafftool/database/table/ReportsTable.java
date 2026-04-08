package org.dghdidi.stafftool.database.table;

import org.dghdidi.stafftool.StaffTool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.dghdidi.stafftool.StaffTool.databaseManager;

public class ReportsTable {

    public static void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS report_info (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    player_name VARCHAR(64) NOT NULL,
                    reported_name VARCHAR(64) NOT NULL,
                    staff_name VARCHAR(64),
                    server_name VARCHAR(64),
                    reason VARCHAR(128)
                )
                """;
        databaseManager.executeUpdate(sql);
        StaffTool.logger.info("§a举报记录表已创建或已存在");
    }

    public static List<String> getReportInfo(int id) throws SQLException {
        String sql = "SELECT player_name, reported_name, staff_name, server_name, reason FROM report_info WHERE id = ?";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                List<String> resultList = new ArrayList<>();
                resultList.add(resultSet.getString("player_name"));
                resultList.add(resultSet.getString("reported_name"));
                resultList.add(resultSet.getString("staff_name"));
                resultList.add(resultSet.getString("server_name"));
                resultList.add(resultSet.getString("reason"));
                return resultList;
            }
        }
    }

}
