package com.dghdidi.stafftool;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;
import java.sql.*;
import java.util.logging.Level;

public final class StaffTool extends Plugin {
    @Override
    public void onEnable() {
        try {
            connectSQL();
        } catch (SQLException e) {
            getLogger().log(Level.WARNING, "§c连接数据库失败, 请检查配置文件");
        }
        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerCommand(this, new PunishCMD(this));
        pluginManager.registerCommand(this, new BroadCastCMD(this));
        pluginManager.registerCommand(this, new TpCMD(this));
        pluginManager.registerCommand(this, new OnlineStaffCMD(this));
        pluginManager.registerCommand(this, new AutoMsgCheckCMD(this));
        pluginManager.registerCommand(this, new CancelAmcCMD(this));
        pluginManager.registerCommand(this, new ChatHistoryCMD(this));
        pluginManager.registerCommand(this, new ReloadCMD(this));
        pluginManager.registerListener(this, new MyListener());
        getLogger().log(Level.INFO, "§a§l插件已成功加载");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "插件已卸载");
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                getLogger().info("数据库连接断开");
            }
        } catch (SQLException e) {
            getLogger().severe("§c断开数据库连接失败: " + e.getMessage());
        }
    }

    static Connection connection;

    public void connectSQL() throws SQLException {
        File configFile = new File("StaffTool.properties");
        PluginConfig config = new PluginConfig(configFile, this);
        String host = config.getProperty("host");
        int port = Integer.parseInt(config.getProperty("port"));
        String database = config.getProperty("database");
        String username = config.getProperty("username");
        String password = config.getProperty("password");
        if (connection != null && !connection.isClosed())
            return;
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
        connection = DriverManager.getConnection(url, username, password);
        getLogger().info("Database connected!");
        createSheet(database);
    }

    public static void createSheet(String database) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "chat_log", null);
            if (!resultSet.next()) {
                String sql = "CREATE TABLE chat_log (player_name VARCHAR(64), message VARCHAR(512), timestamp TIMESTAMP, server_name VARCHAR(64));";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                sql = "ALTER TABLE `" + database + "`.`chat_log` ADD INDEX `Index`(`player_name`)";
                statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                sql = "CREATE EVENT `" + database +"`.`ClearHistory`\n" +
                        "ON SCHEDULE\n" +
                        "EVERY '1' DAY STARTS '2023-7-11 00:00:00'\n" +
                        "DO BEGIN\n" +
                        "DELETE FROM chat_log WHERE TO_DAYS(NOW()) - TO_DAYS(timestamp) > 7;\n" +
                        "END;";
                statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                sql = "ALTER EVENT ClearHistory ON COMPLETION PRESERVE ENABLE";
                statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
