package com.slyph.coords.update;

import com.slyph.coords.CoordsPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class UpdateChecker {

    private static final String REPO = "slyphmp4/CoordsPlugin";
    private static final String API  = "https://api.github.com/repos/" + REPO + "/releases/latest";
    private static final String DL   = "https://github.com/" + REPO + "/releases/latest";

    private final CoordsPlugin plugin;

    public UpdateChecker(CoordsPlugin plugin) {
        this.plugin = plugin;
    }

    public void runCheck() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::fetch);
    }

    private void fetch() {
        String current = plugin.getDescription().getVersion();

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(API).openConnection();
            conn.setRequestProperty("Accept", "application/vnd.github+json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);

                String latest = parseTag(sb.toString());
                if (latest == null) throw new IllegalStateException("tag_name not found");

                if (!latest.equalsIgnoreCase(current)) {
                    plugin.getLogger().info("");
                    plugin.getLogger().info("=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=");
                    plugin.getLogger().info("");
                    plugin.getLogger().info("[CoordsPlugin] Доступна новая версия " + latest +
                            " (текущая " + current + ")");
                    plugin.getLogger().info("[CoordsPlugin] Скачать: " + DL);
                    plugin.getLogger().info("");
                    plugin.getLogger().info("=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=");
                    plugin.getLogger().info("");
                } else {
                    plugin.getLogger().info("");
                    plugin.getLogger().info("=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=");
                    plugin.getLogger().info("");
                    plugin.getLogger().info("[CoordsPlugin] Плагин актуален (" + current + ").");
                    plugin.getLogger().info("");
                    plugin.getLogger().info("=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=");
                    plugin.getLogger().info("");
                }
            }
        } catch (Exception ex) {
            plugin.getLogger().info("");
            plugin.getLogger().info("=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=");
            plugin.getLogger().info("");
            plugin.getLogger().warning("Не удалось проверить обновления: " + ex.getMessage());
            plugin.getLogger().info("");
            plugin.getLogger().info("=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=+=-=");
            plugin.getLogger().info("");
        }
    }

    private String parseTag(String json) {
        int idx = json.indexOf("\"tag_name\"");
        if (idx == -1) return null;
        int start = json.indexOf('"', idx + 11) + 1;
        int end   = json.indexOf('"', start);
        return json.substring(start, end);
    }
}