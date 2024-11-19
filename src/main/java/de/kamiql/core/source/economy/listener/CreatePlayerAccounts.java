package de.kamiql.core.source.economy.listener;

import de.kamiql.Main;
import de.kamiql.core.source.economy.custom.gemstones.econ.Gemstones;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CreatePlayerAccounts implements Listener {
    private final Economy econ = Main.getEcon();
    private final Gemstones gems = Main.getGems();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!econ.hasAccount(event.getPlayer())) {
            econ.createPlayerAccount(event.getPlayer());
            gems.createAccount(event.getPlayer());
        }
    }
}
