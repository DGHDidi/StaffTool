package com.dghdidi.stafftool;

import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;


public class PluginConfig {

    private final Properties properties;
    private final File configFile;

    private final Plugin plugin;
    public PluginConfig(File configFile, Plugin plugin) {
        this.configFile = configFile;
        this.plugin = plugin;
        properties = new Properties();
        if (!configFile.exists()) {
            createDefaultConfig();
            plugin.getLogger().log(Level.WARNING, "§c当前配置文件为默认, 请修改配置文件");
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(configFile);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "§c读取配置文件失败");
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }


    private void createDefaultConfig() {
        properties.setProperty("host", "localhost");
        properties.setProperty("port", "3306");
        properties.setProperty("database", "Example");
        properties.setProperty("username", "root");
        properties.setProperty("password", "password");
        try {
            FileOutputStream fos = new FileOutputStream(configFile);
            properties.store(fos, "Database Configuration");
            fos.close();
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "§c保存默认配置文件失败");
        }
    }

}
