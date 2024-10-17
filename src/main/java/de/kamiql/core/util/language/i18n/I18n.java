package de.kamiql.core.util.language.i18n;

import de.kamiql.api.provider.i18n.I18nProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class I18n {

    private final String key;
    private final Player player;
    private final boolean hasPrefix;
    private final String customPrefix;
    private final Map<String, Object> placeholders;

    private I18n(Builder builder) {
        this.key = builder.key;
        this.player = builder.player;
        this.hasPrefix = builder.hasPrefix;
        this.customPrefix = builder.customPrefix;
        this.placeholders = builder.placeholders;
    }

    public void sendMessageAsComponent() {
        String message = getMessage();
        if (hasPrefix) {
            String prefix = customPrefix != null ? customPrefix : getPrefix();
            message = prefix + " " + message;
        }
        Component component = MiniMessage.miniMessage().deserialize(message);

        player.sendMessage(component);
    }

    public void broadcastMessageAsComponent() {
        String message = getMessage();
        if (hasPrefix) {
            String prefix = customPrefix != null ? customPrefix : getPrefix();
            message = prefix + " " + message;
        }
        Component component = MiniMessage.miniMessage().deserialize(message);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(component);
        }
    }

    private String getMessage() {
        String locale = formatLocale(player.getLocale());
        return getMessageForLocale(locale);
    }

    private String formatLocale(String locale) {
        return locale.toLowerCase(Locale.ROOT).replace("_", "-");
    }

    private String getMessageForLocale(String locale) {
        FileConfiguration config = I18nProvider.getConfig();
        List<String> messages = config.getStringList("translations." + key + "." + locale);

        if (messages.isEmpty()) {
            messages = config.getStringList("translations." + key + "." + config.getString("defaultLocale", "en"));
        }

        if (messages.isEmpty()) {
            return "<gray>No Message for key \"<yellow>" + key + "<gray>\"!";
        }

        String combinedMessage = String.join("\n", messages);

        return formatMessage(combinedMessage);
    }

    private String formatMessage(String message) {
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            String placeholderKey = entry.getKey();
            Object placeholderValue = entry.getValue();
            String placeholderString = placeholderValue.toString();

            message = message.replace("{" + placeholderKey + "}", placeholderString);
        }
        return message;
    }

    private String getPrefix() {
        String locale = formatLocale(player.getLocale());
        List<String> prefixList = I18nProvider.getConfig().getStringList("translations.prefix." + locale);

        if (prefixList.isEmpty()) {
            prefixList = I18nProvider.getConfig().getStringList("translations.prefix." + I18nProvider.getConfig().getString("defaultLocale", "en"));
        }

        return prefixList.isEmpty() ? "" : String.join("\n", prefixList);
    }

    public static class Builder {

        private final String key;
        private final Player player;
        private boolean hasPrefix = false;
        private String customPrefix = null;
        private Map<String, Object> placeholders = new HashMap<>();

        public Builder(String key, Player player) {
            this.key = key;
            this.player = player;
        }

        public Builder hasPrefix(boolean hasPrefix) {
            this.hasPrefix = hasPrefix;
            return this;
        }

        public Builder withPrefix(String prefix) {
            this.customPrefix = prefix;
            return this;
        }

        public Builder withPlaceholder(String key, Object value) {
            this.placeholders.put(key, value);
            return this;
        }

        public I18n build() {
            return new I18n(this);
        }
    }
}
