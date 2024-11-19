package de.kamiql.core.source.economy.commands;

import de.kamiql.Main;
import de.kamiql.core.source.economy.custom.gemstones.econ.Gemstones;
import de.kamiql.i18n.core.source.I18n;
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
import java.util.logging.Logger;

public class EconomyCommand implements TabExecutor {
    private final Economy econ = Main.getEcon();
    private final Gemstones gems = Main.getGems();

    private final Logger logger = Logger.getLogger("EconomyCommand");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("")) {
                if (args.length == 0) {
                    new I18n.Builder("commands.economy.eco_command.usage", player)
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();
                    return false;
                }

                logger.info("Position: Constructor");

                if (args[0].equalsIgnoreCase("money")) {
                    return handleMoneyCommand(player, sender, args);
                } else if (args[0].equalsIgnoreCase("gems")) {
                    return handleGemsCommand(player, sender, args);
                }
            }
        }

        return false;
    }

    private boolean handleMoneyCommand(Player player, CommandSender sender, String[] args) {
        logger.info("Position: Handle Money");

        if (args.length < 2) {
            if (player != null) {
                new I18n.Builder("commands.economy.eco_command.usage_money", player)
                        .hasPrefix(true)
                        .build()
                        .sendMessageAsComponent();
            }
            return false;
        }

        Player target = null;
        if (args.length > 2) {
            target = sender.getServer().getPlayer(args[1]);
        }

        switch (args[2].toLowerCase()) {
            case "add":
                if (args.length == 4 && target != null) {
                    double amount = Double.parseDouble(args[3]);
                    econ.depositPlayer(target, amount);
                    if (player != null) {
                        new I18n.Builder("commands.economy.eco_command.money.add.sender", player)
                                .hasPrefix(true)
                                .withPlaceholder("PLAYER", target.getName())
                                .withPlaceholder("AMOUNT", String.valueOf(amount))
                                .build()
                                .sendMessageAsComponent();
                    }
                    new I18n.Builder("commands.economy.eco_command.money.add.target", target)
                            .hasPrefix(true)
                            .withPlaceholder("AMOUNT", String.valueOf(amount))
                            .build()
                            .sendMessageAsComponent();
                }
                break;
            case "withdraw":
                if (args.length == 4 && target != null) {
                    double amount = Double.parseDouble(args[3]);
                    if (econ.withdrawPlayer(target, amount).transactionSuccess()) {
                        if (player != null) {
                            new I18n.Builder("commands.economy.eco_command.money.withdraw.sender_success", player)
                                    .hasPrefix(true)
                                    .withPlaceholder("PLAYER", target.getName())
                                    .withPlaceholder("AMOUNT", String.valueOf(amount))
                                    .build()
                                    .sendMessageAsComponent();
                        }
                        new I18n.Builder("commands.economy.eco_command.money.withdraw.target", target)
                                .hasPrefix(true)
                                .withPlaceholder("AMOUNT", String.valueOf(amount))
                                .build()
                                .sendMessageAsComponent();
                    } else {
                        if (player != null) {
                            new I18n.Builder("commands.economy.eco_command.money.withdraw.sender_fail", player)
                                    .hasPrefix(true)
                                    .build()
                                    .sendMessageAsComponent();
                        }
                    }
                }
                break;
            case "set":
                if (args.length == 4 && target != null) {
                    double amount = Double.parseDouble(args[3]);
                    econ.depositPlayer(target, amount - econ.getBalance(target));
                    if (player != null) {
                        new I18n.Builder("commands.economy.eco_command.money.set.sender", player)
                                .hasPrefix(true)
                                .withPlaceholder("PLAYER", target.getName())
                                .withPlaceholder("AMOUNT", String.valueOf(amount))
                                .build()
                                .sendMessageAsComponent();
                    }
                    new I18n.Builder("commands.economy.eco_command.money.set.target", target)
                            .hasPrefix(true)
                            .withPlaceholder("AMOUNT", String.valueOf(amount))
                            .build()
                            .sendMessageAsComponent();
                }
                break;
            case "reset":
                if (target != null) {
                    econ.depositPlayer(target, -econ.getBalance(target));
                    if (player != null) {
                        new I18n.Builder("commands.economy.eco_command.money.reset.sender", player)
                                .hasPrefix(true)
                                .withPlaceholder("PLAYER", target.getName())
                                .build()
                                .sendMessageAsComponent();
                    }
                    new I18n.Builder("commands.economy.eco_command.money.reset.target", target)
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();
                }
                break;
            case "balance":
                if (target != null) {
                    if (player != null) {
                        new I18n.Builder("commands.economy.eco_command.money.balance.sender_other", player)
                                .hasPrefix(true)
                                .withPlaceholder("PLAYER", target.getName())
                                .withPlaceholder("BALANCE", String.valueOf(econ.getBalance(target)))
                                .build()
                                .sendMessageAsComponent();
                    }
                } else if (player != null) {
                    new I18n.Builder("commands.economy.money.eco_command.balance.sender_self", player)
                            .hasPrefix(true)
                            .withPlaceholder("BALANCE", String.valueOf(econ.getBalance(player)))
                            .build()
                            .sendMessageAsComponent();
                }
                break;
            default:
                if (player != null) {
                    new I18n.Builder("commands.economy.eco_command.unknown_command_money", player)
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();
                }
                break;
        }
        return true;
    }

    private boolean handleGemsCommand(Player player, CommandSender sender, String[] args) {
        logger.info("Position: Handle Gems");

        if (args.length < 2) {
            if (player != null) {
                new I18n.Builder("commands.economy.eco_command.usage_gems", player)
                        .hasPrefix(true)
                        .build()
                        .sendMessageAsComponent();
            }
            return false;
        }

        Player target = null;
        if (args.length > 2) {
            target = sender.getServer().getPlayer(args[1]);
        }

        switch (args[2].toLowerCase()) {
            case "add":
                if (args.length == 4 && target != null) {
                    double amount = Double.parseDouble(args[3]);
                    gems.deposit(amount, target);
                    if (player != null) {
                        new I18n.Builder("commands.economy.eco_command.gems.add.sender", player)
                                .hasPrefix(true)
                                .withPlaceholder("PLAYER", target.getName())
                                .withPlaceholder("AMOUNT", String.valueOf(amount))
                                .build()
                                .sendMessageAsComponent();
                    }
                    new I18n.Builder("commands.economy.eco_command.gems.add.target", target)
                            .hasPrefix(true)
                            .withPlaceholder("AMOUNT", String.valueOf(amount))
                            .build()
                            .sendMessageAsComponent();
                }
                break;
            case "withdraw":
                if (args.length == 4 && target != null) {
                    double amount = Double.parseDouble(args[3]);
                    if (gems.withdraw(amount, target).transactionSuccess()) {
                        if (player != null) {
                            new I18n.Builder("commands.economy.eco_command.gems.withdraw.sender_success", player)
                                    .hasPrefix(true)
                                    .withPlaceholder("PLAYER", target.getName())
                                    .withPlaceholder("AMOUNT", String.valueOf(amount))
                                    .build()
                                    .sendMessageAsComponent();
                        }
                        new I18n.Builder("commands.economy.eco_command.gems.withdraw.target", target)
                                .hasPrefix(true)
                                .withPlaceholder("AMOUNT", String.valueOf(amount))
                                .build()
                                .sendMessageAsComponent();
                    } else {
                        if (player != null) {
                            new I18n.Builder("commands.economy.eco_command.gems.withdraw.sender_fail", player)
                                    .hasPrefix(true)
                                    .build()
                                    .sendMessageAsComponent();
                        }
                    }
                }
                break;
            case "set":
                if (args.length == 4 && target != null) {
                    double amount = Double.parseDouble(args[3]);
                    gems.deposit(amount - gems.getBalance(target), target);
                    if (player != null) {
                        new I18n.Builder("commands.economy.eco_command.gems.set.sender", player)
                                .hasPrefix(true)
                                .withPlaceholder("PLAYER", target.getName())
                                .withPlaceholder("AMOUNT", String.valueOf(amount))
                                .build()
                                .sendMessageAsComponent();
                    }
                    new I18n.Builder("commands.economy.eco_command.gems.set.target", target)
                            .hasPrefix(true)
                            .withPlaceholder("AMOUNT", String.valueOf(amount))
                            .build()
                            .sendMessageAsComponent();
                }
                break;
            case "reset":
                if (target != null) {
                    gems.deposit(-gems.getBalance(target), target);
                    if (player != null) {
                        new I18n.Builder("commands.economy.eco_command.gems.reset.sender", player)
                                .hasPrefix(true)
                                .withPlaceholder("PLAYER", target.getName())
                                .build()
                                .sendMessageAsComponent();
                    }
                    new I18n.Builder("commands.economy.eco_command.gems.reset.target", target)
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();
                }
                break;
            case "balance":
                if (target != null) {
                    if (player != null) {
                        new I18n.Builder("commands.economy.eco_command.gems.balance.sender_other", player)
                                .hasPrefix(true)
                                .withPlaceholder("PLAYER", target.getName())
                                .withPlaceholder("BALANCE", String.valueOf(gems.getBalance(target)))
                                .build()
                                .sendMessageAsComponent();
                    }
                } else if (player != null) {
                    new I18n.Builder("commands.economy.eco_command.gems.balance.sender_self", player)
                            .hasPrefix(true)
                            .withPlaceholder("BALANCE", String.valueOf(gems.getBalance(player)))
                            .build()
                            .sendMessageAsComponent();
                }
                break;
            default:
                if (player != null) {
                    new I18n.Builder("commands.economy.eco_command.unknown_command_gems", player)
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();
                }
                break;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("money");
            completions.add("gems");
        } else if (args.length == 2) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                completions.add(player.getName());
            });
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("money") || args[0].equalsIgnoreCase("gems")) {
                completions.add("add");
                completions.add("withdraw");
                completions.add("set");
                completions.add("reset");
                completions.add("balance");
            }
        } else if (args.length == 4) {
            if (args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("withdraw") || args[2].equalsIgnoreCase("set")) {
                completions.add("<amount>");
            }
        }
        return completions;
    }
}
