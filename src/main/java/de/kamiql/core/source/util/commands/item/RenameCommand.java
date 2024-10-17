package de.kamiql.core.source.util.commands.item;

import de.kamiql.Main;
import de.kamiql.core.util.language.i18n.I18n;
import net.kyori.adventure.text.event.HoverEvent;
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
            if (args.length == 1) {
                ItemStack item = player.getItemInHand();
                ItemMeta meta = item.getItemMeta();

                if (meta != null) {

                    meta.setDisplayName(args[0]);
                    item.setItemMeta(meta);
                    player.setItemInHand(item);

                    int currentLevel = player.getLevel();
                    player.setLevel(Math.max(currentLevel - 1, 0));

                    new I18n.Builder("item_renamed", player)
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();
                } else {
                    new I18n.Builder("no_item_found", player)
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();
                }
            } else {
                new I18n.Builder("invalid_usage", player).hasPrefix(true).build().sendMessageAsComponent();
            }
        }

        return false;
    }
}
