package de.kamiql.core.source.economy.listener;

import de.kamiql.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {
    private final Economy econ = Main.getEcon();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!econ.hasAccount(event.getPlayer())) {
            econ.createPlayerAccount(event.getPlayer());
        }
    }
}
