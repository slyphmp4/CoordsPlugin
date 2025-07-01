package com.slyph.coords.manager;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class BiomeAlertManager {

    private static final class Alert {
        final String message;
        final long   until;
        Alert(String m, long until) { this.message = m; this.until = until; }
    }

    private final Map<UUID, Alert> alerts = new ConcurrentHashMap<>();

    public void push(Player p, String msg, long durationMillis) {
        alerts.put(p.getUniqueId(),
                new Alert(msg, System.currentTimeMillis() + durationMillis));
    }

    public String poll(Player p) {
        Alert a = alerts.get(p.getUniqueId());
        if (a == null) return null;
        if (System.currentTimeMillis() > a.until) { alerts.remove(p.getUniqueId()); return null; }
        return a.message;
    }
}
