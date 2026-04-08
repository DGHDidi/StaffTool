package org.dghdidi.stafftool.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.dghdidi.stafftool.feature.reports.ReportsStorage;

import static org.dghdidi.stafftool.feature.reports.StaffCMD.reportStaffPrefix;
import static org.dghdidi.stafftool.util.PlayerUtil.sendMessage;

public class ReportListener implements Listener {
    @EventHandler
    public void onStaffJoin(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (!player.hasPermission("reports.staff") || ReportsStorage.getNum() == 0)
            return;
        sendMessage(player, reportStaffPrefix + "§a当前有 §e" + ReportsStorage.getNum() + " §a个举报等待处理, 输入 §b/reports §a查看详情.");
    }
}
