package com.slyph.coords.task;

import com.slyph.coords.CoordsPlugin;
import com.slyph.coords.manager.BiomeAlertManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class CoordinateTask extends BukkitRunnable {

    private final CoordsPlugin        plugin;
    private final BukkitAudiences     adventure;
    private final BiomeAlertManager   alertManager;
    private final LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();

    public CoordinateTask(CoordsPlugin plugin,
                          BukkitAudiences adventure,
                          BiomeAlertManager am) {
        this.plugin = plugin;
        this.adventure = adventure;
        this.alertManager = am;
    }

    @Override
    public void run() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (!plugin.getToggleManager().isEnabled(p)) continue;

            /* -------- проверка алерта биома -------- */
            String alert = alertManager.poll(p);
            if (alert != null) {
                adventure.player(p).sendActionBar(legacy.deserialize(alert));
                continue;
            }

            /* -------- обычные координаты -------- */
            Location l = p.getLocation();
            String raw = plugin.getMsgManager()
                    .single(p, "coordinate-message")
                    .replace("{x}", String.format("%.1f", l.getX()))
                    .replace("{y}", String.format("%.1f", l.getY()))
                    .replace("{z}", String.format("%.1f", l.getZ()));

            Component msg = legacy.deserialize(raw);
            adventure.player(p).sendActionBar(msg);
        }
    }
}
