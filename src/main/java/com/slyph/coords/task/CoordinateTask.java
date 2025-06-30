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

    public CoordinateTask(CoordsPlugin plugin, BukkitAudiences adventure) {
        this.plugin    = plugin;
        this.adventure = adventure;
    }

    @Override
    public void run() {
        String template = plugin.getMsgManager().get("coordinate-message");

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!plugin.getToggleManager().isEnabled(player)) continue;

            Location loc = player.getLocation();
            String parsed = template
                    .replace("{x}", String.format("%.1f", loc.getX()))
                    .replace("{y}", String.format("%.1f", loc.getY()))
                    .replace("{z}", String.format("%.1f", loc.getZ()));

            Component msg = legacy.deserialize(parsed);
            adventure.player(player).sendActionBar(msg);
        }
    }
}
