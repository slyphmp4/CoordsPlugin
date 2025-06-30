package com.slyph.coords.manager;

import com.slyph.coords.util.ColorUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public final class MessageManager {

    private final Plugin  plugin;
    private final String  fallback = "en";
    private final Map<String, YamlConfiguration> langs = new HashMap<>();
    private       String  configLang;

    public MessageManager(Plugin plugin) {
        this.plugin = plugin;
        loadAll();
    }

    public void reload() { loadAll(); }
    private void loadAll() {
        File dir = new File(plugin.getDataFolder(), "langs");
        if (!dir.exists()) dir.mkdirs();

        langs.clear();
        List<String> codes = List.of("en", "ru", "uk", "pl");

        for (String code : codes) {
            String res = "langs/messages_" + code + ".yml";
            File dst   = new File(dir, "messages_" + code + ".yml");
            if (!dst.exists()) plugin.saveResource(res, false);
            langs.put(code, YamlConfiguration.loadConfiguration(dst));
        }

        configLang = plugin.getConfig().getString("language", "auto").toLowerCase();
        if (!configLang.equals("auto") && !langs.containsKey(configLang))
            configLang = fallback;
    }

    private String determineLang(Player p) {
        if (!"auto".equals(configLang)) return configLang;
        if (p == null) return fallback;
        String loc  = p.getLocale();
        String code = (loc == null || loc.length() < 2) ? fallback : loc.substring(0, 2);
        return langs.containsKey(code) ? code : fallback;
    }

    public List<String> lines(Player p, String key) {
        return lines(determineLang(p), key);
    }

    public List<String> lines(String lang, String key) {
        YamlConfiguration yml = langs.getOrDefault(lang, langs.get(fallback));
        List<String> raw = yml.getStringList(key);
        if (raw.isEmpty())
            raw = Collections.singletonList(yml.getString(key, "§c<" + key + ">"));

        String prefix = yml.getString("prefix", "");
        List<String> out = new ArrayList<>(raw.size());
        for (String l : raw)
            out.add(ColorUtil.translateHexColors(l.replace("{prefix}", prefix)));
        return out;
    }

    public List<String> format(Player p, String key, Map<String, String> ph) {
        List<String> base = lines(p, key);
        List<String> res  = new ArrayList<>(base.size());
        for (String s : base) {
            for (var e : ph.entrySet()) s = s.replace(e.getKey(), e.getValue());
            res.add(s);
        }
        return res;
    }

    public String single(Player p, String key) {
        return lines(p, key).get(0);
    }
}
