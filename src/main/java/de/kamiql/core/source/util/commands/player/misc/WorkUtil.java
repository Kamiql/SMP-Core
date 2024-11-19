package de.kamiql.core.source.util.commands.player.misc;

import de.kamiql.i18n.core.source.I18n;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WorkUtil implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("smp-core.commands.work-utils")) {
                if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "anvil":
                            player.openInventory(Bukkit.createInventory(player, InventoryType.ANVIL, "Anvil"));
                            break;

                        case "workbench":
                            player.openInventory(Bukkit.createInventory(player, InventoryType.WORKBENCH, "Workbench"));
                            break;

                        case "smithing":
                            player.openInventory(Bukkit.createInventory(player, InventoryType.SMITHING, "Smithing"));
                            break;

                        case "enchanting":
                            player.openInventory(Bukkit.createInventory(player, InventoryType.ENCHANTING, "Enchanting Table"));
                            break;

                        case "loom":
                            player.openInventory(Bukkit.createInventory(player, InventoryType.LOOM, "Loom"));
                            break;

                        case "grindstone":
                            player.openInventory(Bukkit.createInventory(player, InventoryType.GRINDSTONE, "Grindstone"));
                            break;

                        case "stonecutter":
                            player.openInventory(Bukkit.createInventory(player, InventoryType.STONECUTTER, "Stonecutter"));
                            break;

                        case "cartography":
                            player.openInventory(Bukkit.createInventory(player, InventoryType.CARTOGRAPHY, "Cartography Table"));
                            break;

                        case "trash":
                            player.openInventory(Bukkit.createInventory(player, 27, "Trash"));
                            break;
                    }
                } else {
                    new I18n.Builder("misc.invalid_usage", player)
                            .hasPrefix(true)
                            .withPlaceholder("ALIAS", "/work <anvil|workbench|smithing|enchanting|loom|grindstone|stonecutter|trash|cartography>")
                            .build()
                            .sendMessageAsComponent();
                }
            } else {
                new I18n.Builder("misc.no_permission", player)
                        .hasPrefix(true)
                        .withPlaceholder("PERMISSION", "smp-core.commands.work-utils")
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
            completions.add("anvil");
            completions.add("workbench");
            completions.add("smithing");
            completions.add("enchanting");
            completions.add("loom");
            completions.add("grindstone");
            completions.add("stonecutter");
            completions.add("trash");
            completions.add("cartography");
        }

        return completions;
    }
}
