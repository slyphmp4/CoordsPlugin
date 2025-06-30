package com.slyph.coords.manager;

import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Хранит игроков, отключивших показ координат.
 */
public final class ToggleManager {

    private final Set<UUID> disabled = ConcurrentHashMap.newKeySet();

    public boolean isEnabled(Player player) {
        return !disabled.contains(player.getUniqueId());
    }

    public void setEnabled(Player player, boolean enabled) {
        if (enabled) disabled.remove(player.getUniqueId());
        else         disabled.add(player.getUniqueId());
    }
}
