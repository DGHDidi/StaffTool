package com.dghdidi.stafftool;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class BookAMC {
    public static final Map<ProxiedPlayer, Future<?>> map = new HashMap<>();
    public static void add(ProxiedPlayer player, Future<?> thread) {
        map.put(player, thread);
    }
    public static void del(ProxiedPlayer player) {
        map.remove(player);
    }
    public static boolean check(ProxiedPlayer player) {
        return map.containsKey(player);
    }
    public static Future<?> get(ProxiedPlayer player) {
        return map.get(player);
    }
}
