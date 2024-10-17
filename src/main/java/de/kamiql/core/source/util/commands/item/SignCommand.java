package de.kamiql.core.source.util.commands.item;

import de.kamiql.Main;
import de.kamiql.core.util.language.i18n.I18n;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.UnaryOperator;

public class SignCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            ItemStack item = player.getItemInHand();
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                String owner = data.get(new NamespacedKey(Main.getInstance(), "sign.owner"), PersistentDataType.STRING);

                if (owner == null || owner.equals(player.getUniqueId().toString())) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy:MM:dd");
                    String formattedDate = now.format(formatter);

                    data.set(new NamespacedKey(Main.getInstance(), "sign.owner"), PersistentDataType.STRING, player.getUniqueId().toString());
                    data.set(new NamespacedKey(Main.getInstance(), "sign.date"), PersistentDataType.STRING, formattedDate);

                    String signature = ChatColor.GRAY + "Signed by " + ChatColor.AQUA + player.getName() + ChatColor.GRAY + " at " + ChatColor.AQUA + formattedDate;

                    meta.setLore(Arrays.asList(signature));
                    item.setItemMeta(meta);

                    int currentLevel = player.getLevel();
                    player.setLevel(Math.max(currentLevel - 1, 0));

                    new I18n.Builder("item_signed", player)
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();
                } else {
                    new I18n.Builder("not_owner", player)
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();
                }
            } else {
                new I18n.Builder("no_item_found", player)
                        .hasPrefix(true)
                        .build()
                        .sendMessageAsComponent();
            }
        }
        return true;
    }
}
