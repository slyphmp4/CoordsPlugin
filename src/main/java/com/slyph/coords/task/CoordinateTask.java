package com.slyph.coords.task;

import com.slyph.coords.CoordsPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class CoordinateTask extends BukkitRunnable {

    private final CoordsPlugin plugin;
    private final BukkitAudiences adventure;
    private final LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();

    public CoordinateTask(CoordsPlugin p, BukkitAudiences adv) { this.plugin = p; this.adventure = adv; }

    @Override
    public void run() {
        for (Player pl : plugin.getServer().getOnlinePlayers()) {
            if (!plugin.getToggleManager().isEnabled(pl)) continue;
            Location l = pl.getLocation();
            String tpl = plugin.getMsgManager().single(pl,"coordinate-message")
                    .replace("{x}", String.format("%.1f", l.getX()))
                    .replace("{y}", String.format("%.1f", l.getY()))
                    .replace("{z}", String.format("%.1f", l.getZ()));
            Component comp = legacy.deserialize(tpl);
            adventure.player(pl).sendActionBar(comp);
        }
    }
}
