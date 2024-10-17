package de.kamiql.core.source.economy.commands;

import de.kamiql.Main;
import de.kamiql.core.util.language.i18n.I18n;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class BalanceCommand implements TabExecutor {
    private final Economy econ = Main.getEcon();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        // Own balance if no args
        if (args.length == 0) {
            double balance = econ.getBalance(player);
            new I18n.Builder("own_balance", player)
                    .hasPrefix(true)
                    .withPlaceholder("BALANCE", balance)
                    .build()
                    .sendMessageAsComponent();
            return true;
        }

        // Other player's balance if one argument is provided
        if (args.length == 1) {
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
            if (!targetPlayer.hasPlayedBefore()) {
                new I18n.Builder("player_not_found", player)
                        .hasPrefix(true)
                        .withPlaceholder("PLAYER", args[0])
                        .build()
                        .sendMessageAsComponent();
                return true;
            }

            double balance = econ.getBalance(targetPlayer);
            new I18n.Builder("other_balance", player)
                    .hasPrefix(true)
                    .withPlaceholder("PLAYER", targetPlayer.getName())
                    .withPlaceholder("BALANCE", balance)
                    .build()
                    .sendMessageAsComponent();
            return true;
        }

        new I18n.Builder("invalid_usage", player).hasPrefix(true).build().sendMessageAsComponent();
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
