package de.kamiql.core.source.economy.commands;

import de.kamiql.Main;
import de.kamiql.core.source.economy.database.EconomyManager;
import de.kamiql.i18n.core.source.I18n;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BalTopCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("")) {
                if (args.length == 1) {
                    HashMap<Double, OfflinePlayer> balance = new HashMap<>();
                    String message;
                    String total;
                    String selfPlacement;

                    switch (args[0].toLowerCase()) {
                        case "money" -> {
                            new EconomyManager(player, EconomyManager.TransactionType.MONEY).getAllPlayersWithAccount()
                                    .forEach(p -> balance.put(Main.getEcon().getBalance(p), p));

                            List<OfflinePlayer> top5 = balance.entrySet().stream()
                                    .sorted((o1, o2) -> o2.getKey().compareTo(o1.getKey()))
                                    .limit(5)
                                    .map(Map.Entry::getValue).toList();

                            message = IntStream.range(0, top5.size())
                                    .mapToObj(i -> (i + 1) + ". " + top5.get(i).getName() + " -> " + Main.getEcon().format(Main.getEcon().getBalance(top5.get(i))))
                                    .collect(Collectors.joining("\n"));

                            total = Main.getEcon().format(balance.keySet().stream().reduce(0.0, Double::sum));

                            double playerBalance = Main.getEcon().getBalance(player);
                            long selfRank = balance.entrySet().stream()
                                    .sorted((o1, o2) -> o2.getKey().compareTo(o1.getKey()))
                                    .map(Map.Entry::getValue)
                                    .takeWhile(p -> Main.getEcon().getBalance(p) > playerBalance)
                                    .count() + 1;

                            if (top5.contains(player)) {
                                selfPlacement = "";
                            } else {
                                selfPlacement = selfRank + ". " + player.getName() + " -> " + Main.getEcon().format(playerBalance);
                            }
                        }
                        case "gems" -> {
                            new EconomyManager(player, EconomyManager.TransactionType.GEMS).getAllPlayersWithAccount()
                                    .forEach(p -> balance.put(Main.getGems().getBalance(p), p));

                            List<OfflinePlayer> top5 = balance.entrySet().stream()
                                    .sorted((o1, o2) -> o2.getKey().compareTo(o1.getKey()))
                                    .limit(5)
                                    .map(Map.Entry::getValue).toList();

                            message = IntStream.range(0, top5.size())
                                    .mapToObj(i -> (i + 1) + ". " + top5.get(i).getName() + " -> " + Main.getGems().format(Main.getGems().getBalance(top5.get(i))))
                                    .collect(Collectors.joining("\n"));

                            total = Main.getGems().format(balance.keySet().stream().reduce(0.0, Double::sum));

                            double playerBalance = Main.getGems().getBalance(player);
                            long selfRank = balance.entrySet().stream()
                                    .sorted((o1, o2) -> o2.getKey().compareTo(o1.getKey()))
                                    .map(Map.Entry::getValue)
                                    .takeWhile(p -> Main.getGems().getBalance(p) > playerBalance)
                                    .count() + 1;

                            if (top5.contains(player)) {
                                selfPlacement = "";
                            } else {
                                selfPlacement = selfRank + ". " + player.getName() + " -> " + Main.getGems().format(playerBalance);
                            }
                        }
                        default -> {
                            new I18n.Builder("misc.invalid_usage", player)
                                    .hasPrefix(true)
                                    .withPlaceholder("ALIAS", "/balTop <money/gems>")
                                    .build()
                                    .sendMessageAsComponent();
                            return false;
                        }
                    }

                    new I18n.Builder("commands.economy.balTop_command.top5", player)
                            .hasPrefix(false)
                            .withPlaceholder("TOP5", message)
                            .withPlaceholder("TOTAL", total)
                            .withPlaceholder("SELF", selfPlacement)
                            .build()
                            .sendMessageAsComponent();
                } else {
                    new I18n.Builder("misc.invalid_usage", player)
                            .hasPrefix(true)
                            .withPlaceholder("ALIAS", "/balTop <money/gems>")
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
        }
        return completions;
    }
}
