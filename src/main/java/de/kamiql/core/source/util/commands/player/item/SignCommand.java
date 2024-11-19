package de.kamiql.core.source.util.commands.player.item;

import de.kamiql.Main;
import de.kamiql.i18n.core.source.I18n;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SignCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("")) {
                ItemStack item = player.getItemInHand();
                ItemMeta meta = item.getItemMeta();

                if (meta != null) {
                    PersistentDataContainer data = meta.getPersistentDataContainer();
                    String owner = data.get(new NamespacedKey(Main.getInstance(), "sign.owner"), PersistentDataType.STRING);

                    if (owner == null || owner.equals(player.getUniqueId().toString()) || player.hasPermission("smp-core.commands.sign.admin")) {
                        int currentLevel = player.getLevel();
                        int cost = (int) (currentLevel * 0.9);

                        if (currentLevel > cost) {
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy:MM:dd");
                            String formattedDate = now.format(formatter);

                            data.set(new NamespacedKey(Main.getInstance(), "sign.owner"), PersistentDataType.STRING, player.getUniqueId().toString());
                            data.set(new NamespacedKey(Main.getInstance(), "sign.date"), PersistentDataType.STRING, formattedDate);

                            String signature = ChatColor.GRAY + "Signed by " + ChatColor.AQUA + player.getName() + ChatColor.GRAY + " at " + ChatColor.AQUA + formattedDate;

                            meta.setLore(List.of(signature));
                            item.setItemMeta(meta);

                            player.setLevel((player.getLevel() - cost));

                            new I18n.Builder("commands.sign_command.success", player)
                                    .hasPrefix(true)
                                    .build()
                                    .sendMessageAsComponent();
                        } else {
                            new I18n.Builder("commands.sign_command.no_xp", player)
                                    .hasPrefix(true)
                                    .build()
                                    .sendMessageAsComponent();
                        }
                    } else {
                        new I18n.Builder("commands.sign_command.not_owner", player)
                                .hasPrefix(true)
                                .build()
                                .sendMessageAsComponent();
                    }
                } else {
                    new I18n.Builder("commands.sign_command.no_item", player)
                            .hasPrefix(true)
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
        return true;
    }
}
