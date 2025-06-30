package com.slyph.coords.manager;

import com.slyph.coords.util.ColorUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public final class MessageManager {

    private final Plugin plugin;
    private YamlConfiguration data;

    public MessageManager(Plugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) plugin.saveResource("messages.yml", false);
        data = YamlConfiguration.loadConfiguration(file);
    }

    public String get(String key) {
        String raw = data.getString(key, "§c<not-found:" + key + ">");
        raw = raw.replace("{prefix}", data.getString("prefix", ""));
        return ColorUtil.translateHexColors(raw);
    }

    public List<String> getLines(String key) {
        List<String> lines = data.getStringList(key);
        if (lines == null || lines.isEmpty()) lines = Collections.singletonList(get(key));
        String prefix = data.getString("prefix", "");
        List<String> out = new ArrayList<>(lines.size());
        for (String l : lines) {
            l = l.replace("{prefix}", prefix);
            out.add(ColorUtil.translateHexColors(l));
        }
        return out;
    }

    public List<String> formatLines(String key, Map<String, String> placeholders) {
        List<String> base = getLines(key);
        List<String> result = new ArrayList<>(base.size());
        for (String line : base) {
            for (var e : placeholders.entrySet())
                line = line.replace(e.getKey(), e.getValue());
            result.add(line);
        }
        return result;
    }

    public List<String> formatLines(String key, String ph, String val) {
        return formatLines(key, Map.of(ph, val));
    }
}
