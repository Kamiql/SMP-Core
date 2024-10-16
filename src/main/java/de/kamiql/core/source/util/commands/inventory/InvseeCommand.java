package de.kamiql.core.source.util.commands.inventory;

import de.kamiql.core.util.language.i18n.I18n;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvseeCommand implements Listener, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1 && player.hasPermission("smp-core.commands.invsee.other")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                new I18n.Builder("player_not_found", player)
                        .withPlaceholder("PLAYER", args[0])
                        .hasPrefix(true)
                        .build()
                        .sendMessageAsComponent(false);
                return true;
            }
            player.openInventory(target.getInventory());
            return true;
        } else {
            new I18n.Builder("target_player_missing", player)
                    .hasPrefix(true)
                    .build()
                    .sendMessageAsComponent(false);
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && sender.hasPermission("smp-core.commands.invsee.other")) {
            String currentInput = args[0].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(currentInput))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("Player") &&
                !event.getWhoClicked().hasPermission("smp-core.commands.invsee.modify") &&
                !event.getWhoClicked().equals(event.getView().getPlayer())) {

            event.setCancelled(true);
        }
    }
}
