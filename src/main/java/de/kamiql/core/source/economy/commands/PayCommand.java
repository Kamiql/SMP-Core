package de.kamiql.core.source.economy.commands;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import de.kamiql.Main;
import de.kamiql.core.source.economy.custom.gemstones.econ.Gemstones;
import de.kamiql.i18n.core.source.I18n;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PayCommand implements TabExecutor {
    private final Economy econ = Main.getEcon();
    private final Gemstones gems = Main.getGems();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("smp-core.commands.economy.pay")) {

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    new I18n.Builder("commands.economy.pay_command.target_not_found", player)
                            .hasPrefix(true)
                            .withPlaceholder("PLAYER", args[0])
                            .build()
                            .sendMessageAsComponent();
                    return false;
                }

                if (args.length < 3) {
                    new I18n.Builder("misc.invalid_usage", player)
                            .hasPrefix(true)
                            .withPlaceholder("USAGE", "/pay <player> <amount> <gems/money>")
                            .build()
                            .sendMessageAsComponent();
                    return false;
                }

                double amount = Double.parseDouble(args[1]);

                switch (args[2].toLowerCase()) {
                    case "gems" -> {
                        if (amount > 0) {
                            if (econ.has(player, amount)) {
                                if (econ.hasAccount(target) || econ.hasAccount(player)) {
                                    econ.withdrawPlayer(player, amount);
                                    econ.depositPlayer(target, amount);

                                    new I18n.Builder("commands.economy.pay_command.success", player)
                                            .hasPrefix(true)
                                            .withPlaceholder("AMOUNT", String.valueOf(amount))
                                            .withPlaceholder("PLAYER", target.getName())
                                            .withPlaceholder("TYPE", "gems")
                                            .build()
                                            .sendMessageAsComponent();

                                    new I18n.Builder("commands.economy.pay_command.success_sender", target)
                                            .hasPrefix(true)
                                            .withPlaceholder("AMOUNT", String.valueOf(amount))
                                            .withPlaceholder("PLAYER", player.getName())
                                            .withPlaceholder("TYPE", "gems")
                                            .build()
                                            .sendMessageAsComponent();
                                } else {
                                    new I18n.Builder("commands.economy.pay_command.no_account", player)
                                            .hasPrefix(true)
                                            .withPlaceholder("PLAYER", target.getName())
                                            .build()
                                            .sendMessageAsComponent();
                                }
                            } else {
                                new I18n.Builder("commands.economy.pay_command.not_enough_money", player)
                                        .hasPrefix(true)
                                        .build()
                                        .sendMessageAsComponent();
                            }
                        } else {
                            new I18n.Builder("commands.economy.pay_command.insufficient_amount", player)
                                    .hasPrefix(true)
                                    .build()
                                    .sendMessageAsComponent();
                        }
                    }

                    case "money" -> {
                        if (amount > 0) {
                            if (gems.has(amount, player)) {
                                if (gems.hasAccount(target) || gems.hasAccount(player)) {
                                    gems.withdraw(amount, player);
                                    gems.deposit(amount, target);

                                    new I18n.Builder("commands.economy.pay_command.success", player)
                                            .hasPrefix(true)
                                            .withPlaceholder("AMOUNT", String.valueOf(amount))
                                            .withPlaceholder("PLAYER", target.getName())
                                            .withPlaceholder("TYPE", "money")
                                            .build()
                                            .sendMessageAsComponent();

                                    new I18n.Builder("commands.economy.pay_command.success_sender", target)
                                            .hasPrefix(true)
                                            .withPlaceholder("AMOUNT", String.valueOf(amount))
                                            .withPlaceholder("PLAYER", player.getName())
                                            .withPlaceholder("TYPE", "money")
                                            .build()
                                            .sendMessageAsComponent();
                                } else {
                                    new I18n.Builder("commands.economy.pay_command.no_account", player)
                                            .hasPrefix(true)
                                            .withPlaceholder("PLAYER", target.getName())
                                            .build()
                                            .sendMessageAsComponent();
                                }
                            } else {
                                new I18n.Builder("commands.economy.pay_command.not_enough_money", player)
                                        .hasPrefix(true)
                                        .build()
                                        .sendMessageAsComponent();
                            }
                        } else {
                            new I18n.Builder("commands.economy.pay_command.insufficient_amount", player)
                                    .hasPrefix(true)
                                    .build()
                                    .sendMessageAsComponent();
                        }
                    }

                    default -> {
                        new I18n.Builder("misc.invalid_usage", player)
                                .hasPrefix(true)
                                .withPlaceholder("USAGE", "/pay <player> <amount> <gems/money>")
                                .build()
                                .sendMessageAsComponent();
                    }
                }
            } else {
                new I18n.Builder("misc.no_permission", player)
                        .hasPrefix(true)
                        .withPlaceholder("PERMISSION", "smp-core.commands.economy.pay")
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
            Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .forEach(completions::add);
        } else if (args.length == 2) {
            completions.add("<amount>");
        } else if (args.length == 3) {
            completions.add("gems");
            completions.add("money");
        }
        return completions;
    }
}
