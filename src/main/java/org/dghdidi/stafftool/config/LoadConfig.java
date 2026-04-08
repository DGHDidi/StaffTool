package org.dghdidi.stafftool.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import org.dghdidi.stafftool.database.DatabaseManager;
import org.dghdidi.stafftool.database.table.ChatLogTable;
import org.dghdidi.stafftool.database.table.ReportsTable;
import org.dghdidi.stafftool.feature.*;
import org.dghdidi.stafftool.feature.chat.AdminChat;
import org.dghdidi.stafftool.feature.chat.StaffChat;
import org.dghdidi.stafftool.feature.clientchecker.AutoMessage;
import org.dghdidi.stafftool.feature.clientchecker.CancelMessage;
import org.dghdidi.stafftool.feature.reports.PlayerCMD;
import org.dghdidi.stafftool.feature.reports.StaffCMD;
import org.dghdidi.stafftool.listener.ChatListener;
import org.dghdidi.stafftool.listener.LoginLogoutListener;
import org.dghdidi.stafftool.listener.ReportListener;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import static org.dghdidi.stafftool.StaffTool.*;

public class LoadConfig {

    public static void loadConfig() throws IOException{
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.getLogger().log(Level.WARNING, "§e配置文件不存在, 已创建默认配置文件, 请自行配置参数!");
            CreateConfig.createDefaultConfig(plugin);
            return;
        }
        Configuration config = YamlConfiguration.getProvider(YamlConfiguration.class).load(configFile);

        // 数据库部分
        DatabaseManager.mysqlHost = config.getString("MySQL.host");
        DatabaseManager.mysqlDatabase = config.getString("MySQL.database");
        DatabaseManager.mysqlPort = config.getInt("MySQL.port");
        DatabaseManager.mysqlUsername = config.getString("MySQL.username");
        DatabaseManager.mysqlPassword = config.getString("MySQL.password");

        StaffCMD.delay = config.getInt("Style.teleportDelay(ms)");
        PlayerCMD.reportPlayerPrefix = config.getString("Style.reportPlayerPrefix");
        StaffCMD.reportStaffPrefix = config.getString("Style.reportStaffPrefix");
        AutoMessage.playerPrefix = config.getString("Style.playerPrefix");
        AdminChat.adminPrefix = config.getString("Style.adminPrefix");
        LoginLogoutListener.staffPrefix = config.getString("Style.staffPrefix");

        enableStaffChat = config.getBoolean("Function.staffChat");
        enableReports = config.getBoolean("Function.reports");
        enableChatHistory = config.getBoolean("Function.chatHistory");

        // 依据配置重新注册命令
        manager.unregisterCommands(plugin);
        registerCommand();

    }

    private static boolean enableStaffChat;
    public static boolean enableReports;
    public static boolean enableChatHistory;

    private static void registerCommand() {
        manager.registerCommand(plugin, new Reload());
        manager.registerCommand(plugin, new PunishAlert());
        manager.registerCommand(plugin, new Punish());
        manager.registerCommand(plugin, new CancelMessage());
        manager.registerCommand(plugin, new AutoMessage());
        manager.registerCommand(plugin, new OnlineStaff());
        manager.registerCommand(plugin, new Teleport());
        if (enableStaffChat) {
            manager.registerCommand(plugin, new StaffChat());
            manager.registerCommand(plugin, new AdminChat());
        }
        if (enableChatHistory) {
            manager.registerListener(plugin, new ChatListener());
            manager.registerCommand(plugin, new ChatHistory());
        }
        if (enableReports) {
            manager.registerCommand(plugin, new PlayerCMD());
            manager.registerCommand(plugin, new StaffCMD());
            manager.registerListener(plugin, new ReportListener());
        }
    }

}