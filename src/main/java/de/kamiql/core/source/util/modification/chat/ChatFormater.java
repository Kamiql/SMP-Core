package de.kamiql.core.source.util.modification.chat;

import de.kamiql.Main;
import de.kamiql.i18n.core.source.I18n;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.regex.Pattern;

public class ChatFormater implements Listener {
    private final YamlConfiguration config = Main.getConfig("server/modification/chat/config.yml");
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Bukkit.getOnlinePlayers().forEach(recipient -> recipient.sendMessage(applyReplacements(format(e), e)));
    }

    private Component format(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        final CachedMetaData metaData = Main.getLuckPerms().getPlayerAdapter(Player.class).getMetaData(player);

        String format = Objects.requireNonNull(config.getString("chat-format"))
                .replace("{prefix}", colorize(translateHexColorCodes(metaData.getPrefix() != null ? metaData.getPrefix() : "")))
                .replace("{suffix}", colorize(translateHexColorCodes(metaData.getSuffix() != null ? metaData.getSuffix() : "")))
                .replace("{world}", player.getWorld().getName())
                .replace("{name}", player.getName())
                .replace("{displayname}", player.getDisplayName())
                .replace("{username-color}", metaData.getMetaValue("username-color") != null ? metaData.getMetaValue("username-color") : "")
                .replace("{message-color}", metaData.getMetaValue("message-color") != null ? metaData.getMetaValue("message-color") : "");

        String prefix_component = Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") ? PlaceholderAPI.setPlaceholders(player, format) : format;

        if (player.hasPermission("smp-core.modification.chat.highlight")) {
            prefix_component = """
                  <reset><gray>»
                  <reset>{MESSAGE}
                  <reset><gray>»"""
                    .replace("{MESSAGE}", prefix_component);
        }

        Component component = MiniMessage.miniMessage().deserialize(prefix_component);

        return component.replaceText(builder -> builder.match(Pattern.compile("\\{message}"))
                .replacement(player.hasPermission("smp-core.modification.chat.colorcodes")
                        && player.hasPermission("smp-core.modification.chat.rgbcodes")
                        ? colorize(translateHexColorCodes(e.getMessage()))
                        : player.hasPermission("smp-core.modification.chat.colorcodes")
                        ? colorize(e.getMessage())
                        : player.hasPermission("smp-core.modification.chat.rgbcodes")
                        ? translateHexColorCodes(e.getMessage())
                        : e.getMessage()));
    }

    private Component applyReplacements(Component component, AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (PlainTextComponentSerializer.plainText().serialize(component).contains("[item]")) {
            if (player.hasPermission("smp-core.modification.chat.placeholder.[item]")) {
                if (player.getItemInHand().hasItemMeta()) {
                    ItemStack item = player.getItemInHand();

                    component = component
                            .replaceText(builder -> builder
                            .matchLiteral("[item]")
                            .replacement(Component.text( "§7[" + (item.getItemMeta().hasDisplayName() ? (item.getItemMeta().getDisplayName()) : (item.getType().getData().getName())) + "§7]")
                                    .hoverEvent(HoverEvent.showItem(item.asHoverEvent().value()))));
                } else {
                    player.sendActionBar(new I18n.Builder("chat_formater.invalid_usage", player)
                            .hasPrefix(true)
                            .build()
                            .getMessageAsComponent());
                }
            } else {
                new I18n.Builder("misc.no_permission", player)
                        .hasPrefix(true)
                        .withPlaceholder("PERMISSION", "smp-core.modification.chat.placeholder.[item]")
                        .build()
                        .sendMessageAsComponent();
            }
        }

        return component;
    }

    private String colorize(final String message) {
        return message
                .replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<gray>")
                .replace("&8", "<dark_gray>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")
                .replace("&k", "<obfuscated>")
                .replace("&l", "<bold>")
                .replace("&o", "<italic>")
                .replace("&n", "<underlined>");
    }

    private String translateHexColorCodes(final String message) {
        return message.replaceAll("&#([A-Fa-f0-9]{6})", "<#$1>");
    }
}
