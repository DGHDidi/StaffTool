package org.dghdidi.stafftool;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.dghdidi.stafftool.config.CreateConfig;
import org.dghdidi.stafftool.config.*;
import org.dghdidi.stafftool.database.DatabaseManager;
import org.dghdidi.stafftool.database.task.ChatLogCleanTask;
import org.dghdidi.stafftool.listener.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class StaffTool extends Plugin {

    public static Logger logger;
    public static PluginManager manager;
    public static ProxyServer proxy;
    public static LuckPerms luckPerms;
    public static Plugin plugin;
    public static DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        CreateConfig.createDefaultConfig(this);

        luckPerms = LuckPermsProvider.get();
        logger = getLogger();
        proxy = getProxy();
        manager = getProxy().getPluginManager();
        manager.registerListener(this, new LoginLogoutListener());
        plugin = this;

        try {
            LoadConfig.loadConfig();
        } catch (IOException e) {
            getLogger().warning("§c§l加载配置文件失败");
        }

        try {
            databaseManager = new DatabaseManager();
            databaseManager.init();
        } catch (RuntimeException e) {
            getLogger().warning("§c§l数据库连接失败，请检查配置文件");
        }

        proxy.getScheduler().schedule(
                this,
                new ChatLogCleanTask(),
                0,
                24,
                TimeUnit.HOURS
        ); // 每天执行一次清理聊天记录的任务

        getLogger().info("§a§l插件已成功加载");
    }

    @Override
    public void onDisable() {
        databaseManager.close();
        getLogger().info("§a§l插件已成功卸载");
    }
}
