package de.kamiql.core.source.util.commands.player.item;

import de.kamiql.i18n.core.source.I18n;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class RenameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("")) {
                if (args.length >= 1) {
                    String name = String.join(" ", args);

                    name = ChatColor.translateAlternateColorCodes('&', name);

                    ItemStack item = player.getItemInHand();
                    ItemMeta meta = item.getItemMeta();

                    if (meta != null) {

                        int currentLevel = player.getLevel();
                        double baseCost = 0.1;
                        int nameLength = name.length();

                        double cost = Math.pow(baseCost, nameLength);

                        if (currentLevel > cost) {
                            meta.setDisplayName(name);
                            item.setItemMeta(meta);
                            player.setItemInHand(item);

                            player.setLevel((int)(currentLevel - cost));

                            new I18n.Builder("commands.rename_command.success", player)
                                    .hasPrefix(true)
                                    .build()
                                    .sendMessageAsComponent();
                        } else {
                            new I18n.Builder("commands.rename_command.not_enough_xp", player)
                                    .hasPrefix(true)
                                    .build()
                                    .sendMessageAsComponent();
                        }

                    } else {
                        new I18n.Builder("commands.rename_command.no_item", player)
                                .hasPrefix(true)
                                .build()
                                .sendMessageAsComponent();
                    }
                } else {
                    new I18n.Builder("misc.invalid_usage", player).hasPrefix(true).build().sendMessageAsComponent();
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
}
