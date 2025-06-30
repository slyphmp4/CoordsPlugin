package com.slyph.coords.command;

import com.slyph.coords.CoordsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public final class CordCommand implements CommandExecutor, TabCompleter {

    private final CoordsPlugin plugin;
    private final LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();

    public CordCommand(CoordsPlugin plugin) {
        this.plugin = plugin;
    }

    /* ---------------- исполнение ---------------- */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        var msg = plugin.getMsgManager();

        if (args.length == 0) {
            sendLines(sender, "usage");
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "reload":
                if (!sender.hasPermission("coords.reload")) {
                    sendLines(sender, "no-perm");
                    return true;
                }
                plugin.restartPlugin();
                sendLines(sender, "reload-success");
                return true;

            case "on":
            case "off":
                if (!(sender instanceof Player)) {
                    sendLines(sender, "player-only");
                    return true;
                }
                Player p = (Player) sender;
                if (!p.hasPermission("coords.toggle")) {
                    sendLines(p, "no-perm");
                    return true;
                }
                boolean enable = args[0].equalsIgnoreCase("on");
                plugin.getToggleManager().setEnabled(p, enable);
                sendLines(p, enable ? "coords-enabled" : "coords-disabled");
                return true;

            default:
                sendLines(sender, "unknown-subcmd");
                return true;
        }
    }

    /* ---------------- таб-комплиты ---------------- */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subs = new ArrayList<>();
            if (sender.hasPermission("coords.reload")) subs.add("reload");
            if (sender instanceof Player && sender.hasPermission("coords.toggle")) {
                subs.add("on"); subs.add("off");
            }
            String entered = args[0].toLowerCase();
            return subs.stream().filter(s -> s.startsWith(entered)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /* ---------------- helper ---------------- */
    private void sendLines(CommandSender target, String key) {
        for (String raw : plugin.getMsgManager().getLines(key)) {
            Component comp = legacy.deserialize(raw);
            plugin.adventure().sender(target).sendMessage(comp);
        }
    }
}
