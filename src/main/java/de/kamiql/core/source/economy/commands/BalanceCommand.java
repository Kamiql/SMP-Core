package de.kamiql.core.source.economy.commands;

import de.kamiql.Main;
import de.kamiql.i18n.core.source.I18n;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BalanceCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (player.hasPermission("")) {

                if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "money" -> {
                            new I18n.Builder("commands.economy.balance_command.money_self", player)
                                    .hasPrefix(true)
                                    .withPlaceholder("AMOUNT", Main.getEcon().getBalance(player))
                                    .build()
                                    .sendMessageAsComponent();
                        }

                        case "gems" -> {
                            new I18n.Builder("commands.economy.balance_command.gems_self", player)
                                    .hasPrefix(true)
                                    .withPlaceholder("AMOUNT", Main.getGems().getBalance(player))
                                    .build()
                                    .sendMessageAsComponent();
                        }

                        default -> {
                            new I18n.Builder("misc.invalid_usage", player)
                                    .hasPrefix(true)
                                    .withPlaceholder("ALIAS", "/balance <money/gems> [player]")
                                    .build()
                                    .sendMessageAsComponent();
                        }
                    }

                } else if (args.length == 2) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                    switch (args[0].toLowerCase()) {
                        case "money" -> {
                            new I18n.Builder("commands.economy.balance_command.money_other", player)
                                    .hasPrefix(true)
                                    .withPlaceholder("AMOUNT", Main.getEcon().getBalance(target))
                                    .withPlaceholder("PLAYER", target.getName())
                                    .build()
                                    .sendMessageAsComponent();
                        }

                        case "gems" -> {
                            new I18n.Builder("commands.economy.balance_command.gems_other", player)
                                    .hasPrefix(true)
                                    .withPlaceholder("AMOUNT", Main.getEcon().getBalance(target))
                                    .withPlaceholder("PLAYER", target.getName())
                                    .build()
                                    .sendMessageAsComponent();
                        }

                        default -> {
                            new I18n.Builder("misc.invalid_usage", player)
                                    .hasPrefix(true)
                                    .withPlaceholder("ALIAS", "/balance <money/gems> [player]")
                                    .build()
                                    .sendMessageAsComponent();
                        }
                    }
                } else {
                    new I18n.Builder("misc.invalid_usage", player)
                            .hasPrefix(true)
                            .withPlaceholder("ALIAS", "/balance <money/gems> [player]")
                            .build()
                            .sendMessageAsComponent();
                }

            } else {
                new I18n.Builder("misc.no_permission", player)
                        .hasPrefix(true)
                        .withPlaceholder("PERMISSION", "")
                        .build()
                        .sendMessageAsComponent();
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("money");
            completions.add("gems");
        } else if (args.length == 2) {
            Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                .forEach(completions::add);
        }
        return completions;
    }
}
