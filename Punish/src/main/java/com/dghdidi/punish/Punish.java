package com.dghdidi.punish;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.logging.Level;

public final class Punish extends Plugin {

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "插件已成功加载");
        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerCommand(this, new PunishCMD(this));
        pluginManager.registerCommand(this, new broadCastCMD(this));
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "插件已卸载");
    }
}
