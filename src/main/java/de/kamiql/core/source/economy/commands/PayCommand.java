package de.kamiql.core.source.economy.commands;

import de.kamiql.Main;
import de.kamiql.core.util.language.i18n.I18n;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class PayCommand implements TabExecutor {
    private final Economy econ = Main.getEcon();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length != 2) {
            new I18n.Builder("invalid_usage", player).hasPrefix(true).build().sendMessageAsComponent(false);
            return false;
        }

        // Target player
        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        if (targetPlayer == null) {
            new I18n.Builder("player_must_be_online", player)
                    .hasPrefix(true)
                    .withPlaceholder("PLAYER", args[0])
                    .build()
                    .sendMessageAsComponent(false);
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
            if (amount <= 0) {
                new I18n.Builder("invalid_amount", player)
                        .hasPrefix(true)
                        .build()
                        .sendMessageAsComponent(false);
                return true;
            }
        } catch (NumberFormatException e) {
            new I18n.Builder("invalid_amount", player)
                    .hasPrefix(true)
                    .build()
                    .sendMessageAsComponent(false);
            return true;
        }

        if (!econ.has(player, amount)) {
            new I18n.Builder("insufficient_funds", player)
                    .hasPrefix(true)
                    .build()
                    .sendMessageAsComponent(false);
            return true;
        }

        econ.withdrawPlayer(player, amount);
        econ.depositPlayer(targetPlayer, amount);

        new I18n.Builder("payment_sent", player)
                .hasPrefix(true)
                .withPlaceholder("PLAYER", targetPlayer.getName())
                .withPlaceholder("AMOUNT", amount)
                .build()
                .sendMessageAsComponent(false);

        new I18n.Builder("payment_received", targetPlayer)
                .hasPrefix(true)
                .withPlaceholder("PLAYER", player.getName())
                .withPlaceholder("AMOUNT", amount)
                .build()
                .sendMessageAsComponent(false);

        return true;
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
