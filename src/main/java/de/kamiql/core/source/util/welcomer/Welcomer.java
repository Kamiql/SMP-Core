package de.kamiql.core.source.util.welcomer;

import de.kamiql.core.util.language.i18n.I18n;
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
                new I18n.Builder("firstjoin", player)
                        .hasPrefix(false)
                        .withPlaceholder("PLAYER", player.getName())
                        .withPlaceholder("COUNT", 1)
                        .build()
                        .sendMessageAsComponent(true);

                new I18n.Builder("join", player)
                        .hasPrefix(false)
                        .withPlaceholder("PLAYER", player.getName())
                        .build()
                        .sendMessageAsComponent(true);

            } else {
                new I18n.Builder("join", player)
                        .hasPrefix(false)
                        .withPlaceholder("PLAYER", player.getName())
                        .build()
                        .sendMessageAsComponent(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        if (event.getPlayer() instanceof Player player) {
            new I18n.Builder("quit", player)
                    .hasPrefix(false)
                    .withPlaceholder("{PLAYER}", player.getName())
                    .build()
                    .sendMessageAsComponent(true);
        }
    }
}
