package de.kamiql.api.provider.i18n;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class I18nProvider {

    private static FileConfiguration config;
    private final Plugin plugin;

    public I18nProvider(Plugin plugin) {
        this.plugin = plugin;
        setupLanguageFile();
        loadConfig();
    }

    private void setupLanguageFile() {
        File languageFile = new File(plugin.getDataFolder(), "language/i18n.yml");
        if (!languageFile.exists()) {
            try {
                languageFile.getParentFile().mkdirs();
                plugin.saveResource("language/i18n.yml", false);
            } catch (Exception e) {
                plugin.getLogger().severe("Could not create language file: " + e.getMessage());
            }
        }
    }

    // Lädt die Konfiguration aus der Datei
    private void loadConfig() {
        File languageFile = new File(plugin.getDataFolder(), "language/i18n.yml");
        config = YamlConfiguration.loadConfiguration(languageFile);
    }

    // Statische Methode, um die Config global verfügbar zu machen
    public static FileConfiguration getConfig() {
        return config;
    }
}
