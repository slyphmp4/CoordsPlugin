package com.slyph.coords;

import com.slyph.coords.command.CordCommand;
import com.slyph.coords.listener.CleanupListener;
import com.slyph.coords.manager.MessageManager;
import com.slyph.coords.manager.ToggleManager;
import com.slyph.coords.task.CoordinateTask;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoordsPlugin extends JavaPlugin {

    private BukkitAudiences adventure;
    private CoordinateTask  coordinateTask;
    private ToggleManager   toggleManager;
    private MessageManager  messageManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();           // config.yml
        adventure       = BukkitAudiences.create(this);
        toggleManager   = new ToggleManager();
        messageManager  = new MessageManager(this);

        startTask();

        /* ------- команда /cord ------- */
        PluginCommand cord = getCommand("cord");
        if (cord != null) {
            CordCommand handler = new CordCommand(this);
            cord.setExecutor(handler);
            cord.setTabCompleter(handler);
        } else getLogger().severe("Не найдена команда 'cord'!");

        /* ------- очистка флагов ------- */
        getServer().getPluginManager()
                .registerEvents(new CleanupListener(toggleManager), this);

        getLogger().info("CoordsPlugin включён.");
    }

    @Override
    public void onDisable() {
        if (coordinateTask != null) coordinateTask.cancel();
        if (adventure != null) adventure.close();
    }

    /* ---------- getters ---------- */
    public ToggleManager getToggleManager()  { return toggleManager;  }
    public MessageManager getMsgManager()    { return messageManager; }
    public BukkitAudiences adventure()       { return adventure;      }

    /* ---------- утилити ---------- */
    public void startTask() {
        int interval = getConfig().getInt("update-interval-ticks", 20);
        coordinateTask = new CoordinateTask(this, adventure);
        coordinateTask.runTaskTimer(this, 0L, interval);
    }
    public void restartPlugin() {
        reloadConfig();
        messageManager.load();          // перечитать messages.yml
        if (coordinateTask != null) coordinateTask.cancel();
        startTask();
    }
}
