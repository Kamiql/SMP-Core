package de.kamiql.core.toolkit.extension.papi;

import de.kamiql.Main;
import de.kamiql.core.source.economy.custom.gemstones.econ.Gemstones;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class SMPcorePlaceholderExtension extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "smp-core";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", Main.getInstance().getPluginMeta().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public Type getExpansionType() {
        return Type.INTERNAL;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        Economy econ = Main.getEcon();
        Gemstones gems = Main.getGems();

        String[] parts = params.split("_", 2);

        if (parts.length < 2) {
            return "> Invalid placeholder: " + params + " <";
        }

        String directory = parts[0];
        String action = parts[1];

        switch (directory) {
            case "gemstones":
                switch (action) {
                    case "formated-balance":
                        return gems.format(gems.getBalance(player));
                    case "balance":
                        return String.valueOf(gems.getBalance(player));
                    default:
                        return "> Placeholder " + getIdentifier() + "_" + params + " does not exist <";
                }

            case "money":
                switch (action) {
                    case "formated-balance":
                        return econ.format(econ.getBalance(player));
                    case "balance":
                        return String.valueOf(econ.getBalance(player));
                    default:
                        return "> Placeholder " + getIdentifier() + "_" + params + " does not exist <";
                }

            default:
                return "> Placeholder " + getIdentifier() + "_" + params + " does not exist <";
        }
    }

}
