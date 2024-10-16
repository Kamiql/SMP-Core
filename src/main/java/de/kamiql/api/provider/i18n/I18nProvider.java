package de.kamiql.api.provider.i18n;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Logger;

public class I18nProvider {

    private static FileConfiguration config;
    private final Plugin plugin;

    public I18nProvider(Plugin plugin) {
        this.plugin = plugin;
        try {
            setupLanguageFile();
            loadConfig();
            logInitialization();
        } catch (Exception e) {
            plugin.getLogger().severe("Couldnt load Language system: " + e);
        }
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

    private void loadConfig() {
        File languageFile = new File(plugin.getDataFolder(), "language/i18n.yml");
        config = YamlConfiguration.loadConfiguration(languageFile);
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    private void logInitialization() {
        String message =
                """
                
                ===========================================================================================================
                         _                                                _____           _                 \s
                        | |                                              /  ___|         | |                \s
                        | |     __ _ _ __   __ _ _   _  __ _  __ _  ___  \\ --. _   _ ___| |_ ___ _ __ ___  \s
                        | |    / _ | '_ \\ / _ | | | |/ _ |/ _ |/ _ \\  --. \\ | | / __| __/ _ \\ '_  _ \\ \s
                        | |___| (_| | | | | (_| | |_| | (_| | (_| |  __/ /\\__/ / |_| \\__ \\ ||  __/ | | | | |\s
                        \\_____/\\__,_|_| |_|\\__, |\\__,_|\\__,_|\\__, |\\___| \\____/ \\__, |___/\\__\\___|_| |_| |_|\s
                                            __/ |             __/ |              __/ |                      \s
                                           |___/             |___/              |___/                       \s
                ============================================================================================================
                Language System by: kamiql
                Version: 2.0
                ============================================================================================================
                """;
        String logger_name = plugin.getName() + "-> {class.name}";
        Logger.getLogger(logger_name
                        .replace("{class.name}", this.getClass()
                                .getSimpleName()))
                .info(message);
    }
}
