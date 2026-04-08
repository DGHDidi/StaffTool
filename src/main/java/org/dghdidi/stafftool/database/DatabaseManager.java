package org.dghdidi.stafftool.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.dghdidi.stafftool.StaffTool;
import org.dghdidi.stafftool.database.table.ChatLogTable;
import org.dghdidi.stafftool.database.table.ReportsTable;
import org.dghdidi.stafftool.feature.ChatHistory;
import org.dghdidi.stafftool.feature.reports.PlayerCMD;
import org.dghdidi.stafftool.feature.reports.StaffCMD;
import org.dghdidi.stafftool.listener.ChatListener;
import org.dghdidi.stafftool.listener.ReportListener;

import java.sql.*;

import static org.dghdidi.stafftool.StaffTool.manager;
import static org.dghdidi.stafftool.StaffTool.plugin;
import static org.dghdidi.stafftool.config.LoadConfig.enableChatHistory;
import static org.dghdidi.stafftool.config.LoadConfig.enableReports;

public class DatabaseManager {

    private HikariDataSource dataSource;
    public static String mysqlHost;
    public static int mysqlPort;
    public static String mysqlDatabase;
    public static String mysqlUsername;
    public static String mysqlPassword;

    public void init() {
        HikariConfig config = getHikariConfig();
        this.dataSource = new HikariDataSource(config);
        try (Connection ignored = dataSource.getConnection()) {
            StaffTool.logger.info("§a数据库连接成功");
        } catch (SQLException e) {
            StaffTool.logger.warning("§c数据库连接失败，请检查配置文件");
            dataSource.close();
        }
        createTables();
    }

    public void reload() {
        HikariDataSource old = this.dataSource;
        HikariConfig config = getHikariConfig();
        HikariDataSource newDataSource = new HikariDataSource(config);

        try (Connection ignored = newDataSource.getConnection()) {
            StaffTool.logger.info("§a数据库连接成功");
        } catch (SQLException e) {
            StaffTool.logger.warning("§c数据库连接失败");
            newDataSource.close();
        }

        this.dataSource = newDataSource;

        if (old != null && !old.isClosed()) {
            old.close();
        }

    }

    private static @NonNull HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
                "jdbc:mysql://" +
                        mysqlHost + ":" +
                        mysqlPort + "/" +
                        mysqlDatabase +
                        "?useSSL=false&characterEncoding=utf8"
        );
        config.setUsername(mysqlUsername);
        config.setPassword(mysqlPassword);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(10000);
        return config;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public void executeUpdate(String sql) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            StaffTool.logger.info("§a执行 SQL " + sql);
        } catch (SQLException e) {
            StaffTool.logger.severe("§c执行 SQL 失败:");
            StaffTool.logger.severe(sql);
            e.printStackTrace();
        }
    }

    public void createIndex(String sql) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            // 索引已存在时忽略
            if (e.getErrorCode() != 1061) {
                StaffTool.logger.severe("创建索引失败");
                e.printStackTrace();
            }
        }
    }

    public int executeCommand(String sql) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(String sql) {
        Statement statement;
        try {
            statement = getConnection().createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTables() {
        if (enableChatHistory) {
            ChatLogTable.createTable();
        }
        if (enableReports) {
            ReportsTable.createTable();
        }
    }
}