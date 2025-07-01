package com.slyph.coords.listener;

import com.slyph.coords.CoordsPlugin;
import com.slyph.coords.manager.BiomeAlertManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class BiomeListener implements Listener {

    private final CoordsPlugin          plugin;
    private final BiomeAlertManager     alertManager;
    private final LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();
    private final Map<UUID, Biome> lastBiome = new ConcurrentHashMap<>();

    public BiomeListener(CoordsPlugin plugin, BiomeAlertManager am) {
        this.plugin = plugin;
        this.alertManager = am;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {

        if (e.getFrom().getBlockX() == e.getTo().getBlockX()
                && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;

        Player p      = e.getPlayer();
        Biome  biome  = e.getTo().getBlock().getBiome();

        if (biome == lastBiome.get(p.getUniqueId())) return;
        lastBiome.put(p.getUniqueId(), biome);

        String nice = human(biome);
        String raw  = plugin.getMsgManager()
                .single(p, "enter-biome")
                .replace("{biome}", nice);

        alertManager.push(p, raw, 6000);
    }

    private String human(Biome b) {
        String key;
        try { key = b.getKey().getKey(); }
        catch (Throwable t) { key = b.name().toLowerCase(); }

        key = key.replace('_', ' ');
        StringBuilder sb = new StringBuilder();
        for (String w : key.split(" ")) {
            if (w.isEmpty()) continue;
            sb.append(Character.toUpperCase(w.charAt(0)))
                    .append(w.substring(1)).append(' ');
        }
        return sb.toString().trim();
    }
}
