package de.kamiql.core.source.util.modification.chat;

import de.kamiql.i18n.core.source.I18n;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Welcomer implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        if (event.getPlayer() instanceof Player player) {
            if (!player.hasPlayedBefore()) {
                new I18n.Builder("welcomer.firstjoin", player)
                        .hasPrefix(false)
                        .withPlaceholder("PLAYER", player.getName())
                        .withPlaceholder("COUNT", PlaceholderAPI.setPlaceholders(player, "%server_unique_joins%"))
                        .build()
                        .broadcastMessageAsComponent();

                new I18n.Builder("welcomer.join", player)
                        .hasPrefix(false)
                        .withPlaceholder("PLAYER", player.getName())
                        .build()
                        .broadcastMessageAsComponent();
            } else {
                new I18n.Builder("welcomer.join", player)
                        .hasPrefix(false)
                        .withPlaceholder("PLAYER", player.getName())
                        .build()
                        .broadcastMessageAsComponent();
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        if (event.getPlayer() instanceof Player player) {
            new I18n.Builder("welcomer.quit", player)
                    .hasPrefix(false)
                    .withPlaceholder("PLAYER", player.getName())
                    .build()
                    .broadcastMessageAsComponent();
        }
    }
}
