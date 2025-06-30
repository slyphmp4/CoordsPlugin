package com.slyph.coords.listener;

import com.slyph.coords.manager.ToggleManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class CleanupListener implements Listener {

    private final ToggleManager toggleManager;

    public CleanupListener(ToggleManager toggleManager) {
        this.toggleManager = toggleManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        toggleManager.setEnabled(event.getPlayer(), true);
    }
}
