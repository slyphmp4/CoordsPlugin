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

    public CordCommand(CoordsPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        var mm     = plugin.getMsgManager();
        Player pl  = sender instanceof Player ? (Player) sender : null;

        if (args.length == 0) { send(sender, mm.lines(pl,"usage")); return true; }

        switch (args[0].toLowerCase()) {

            case "reload" -> {
                if (!sender.hasPermission("coords.reload")) {
                    send(sender, mm.lines(pl,"no-perm")); return true;
                }
                plugin.restartPlugin();
                send(sender, mm.lines(pl,"reload-success"));
            }

            case "on", "off" -> {
                if (pl == null) { send(sender, mm.lines("en","player-only")); return true; }
                if (!pl.hasPermission("coords.toggle")) { send(pl, mm.lines(pl,"no-perm")); return true; }

                boolean wantEnable = args[0].equals("on");
                boolean enabled    = plugin.getToggleManager().isEnabled(pl);

                if (wantEnable == enabled) {
                    send(pl, mm.lines(pl, wantEnable ? "already-enabled"
                            : "already-disabled"));
                    return true;
                }

                plugin.getToggleManager().setEnabled(pl, wantEnable);
                send(pl, mm.lines(pl, wantEnable ? "coords-enabled"
                        : "coords-disabled"));
            }

            default -> send(sender, mm.lines(pl,"unknown-subcmd"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String a, String[] args) {
        if (args.length != 1) return Collections.emptyList();
        List<String> subs = new ArrayList<>();
        if (s.hasPermission("coords.reload")) subs.add("reload");
        if (s instanceof Player p && p.hasPermission("coords.toggle"))
            Collections.addAll(subs, "on", "off");
        return subs.stream()
                .filter(v -> v.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }

    private void send(CommandSender tgt, List<String> lines) {
        for (String raw : lines)
            plugin.adventure().sender(tgt)
                    .sendMessage(Component.text("").append(legacy.deserialize(raw)));
    }
}
