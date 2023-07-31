package com.dghdidi.stafftool;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.dghdidi.stafftool.StaffTool.connection;
import static com.dghdidi.stafftool.StaffTool.createSheet;

public class ReloadCMD extends Command {

    private final Plugin plugin;

    public ReloadCMD(Plugin plugin) {
        super("stafftool", "staff.reload");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(new TextComponent("§c用法: /stafftool reload"));
            return;
        }
        try {
            File configFile = new File("StaffTool.properties");
            PluginConfig config = new PluginConfig(configFile, plugin);
            String host = config.getProperty("host");
            int port = Integer.parseInt(config.getProperty("port"));
            String database = config.getProperty("database");
            String username = config.getProperty("username");
            String password = config.getProperty("password");
            if (connection != null && !connection.isClosed())
                connection.close();
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
            connection = DriverManager.getConnection(url, username, password);
            plugin.getLogger().info("§a成功连接数据库!");
            sender.sendMessage(new TextComponent("§a成功重载StaffTool, 已连接至数据库!"));
            createSheet(database);
        } catch (SQLException e) {
            plugin.getLogger().severe("§c连接数据库失败, 请检查配置文件" + e.getMessage());
        }
    }
}
