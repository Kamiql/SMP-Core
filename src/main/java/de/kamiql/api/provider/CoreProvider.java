package de.kamiql.api.provider;

import de.kamiql.api.provider.i18n.I18nProvider;
import org.bukkit.plugin.Plugin;

public class CoreProvider {
    private final I18nProvider i18nProvider;

    public CoreProvider(Plugin plugin) {
        this.i18nProvider = new I18nProvider(plugin);
    }

    public I18nProvider getI18nProvider() {
        return i18nProvider;
    }
}
