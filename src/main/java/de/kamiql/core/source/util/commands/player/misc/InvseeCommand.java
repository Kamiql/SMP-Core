package de.kamiql.core.source.util.commands.player.misc;

import de.kamiql.Main;
import de.kamiql.core.toolkit.extension.invframework.GUI;
import de.kamiql.i18n.core.source.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InvseeCommand implements TabExecutor, Listener {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        if (!player.hasPermission("smp-core.commands.invsee")) {
            new I18n.Builder("misc.no_permission", player)
                    .hasPrefix(true)
                    .withPlaceholder("PERMISSION", "smp-core.commands.invsee")
                    .build()
                    .sendMessageAsComponent();
            return false;
        }

        if (args.length != 1) {
            new I18n.Builder("misc.invalid_usage", player)
                    .hasPrefix(true)
                    .withPlaceholder("ALIAS", "/invsee <player>")
                    .build()
                    .sendMessageAsComponent();
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            return false;
        }

        if (!player.hasPermission("smp-core.commands.invsee.staff")) {
            GUI gui = GUI.createGUI(target, 45, target.getName() + "'s Inventory")
                    .setContents(target.getInventory().getContents())
                    .onClick((event, currentGUI) -> {
                        event.setCancelled(true);
                        event.getWhoClicked().getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 6f);
                    })
                    .onClose((event, currentGUI) -> {
                        if (event.getViewers().isEmpty()) {
                            currentGUI.unregister();
                        }
                    });

            gui.show(player);

            Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    ItemStack[] originalContents = target.getInventory().getContents().clone();
                    ItemStack[] contents = new ItemStack[45];

                    System.arraycopy(originalContents, 0, contents, 0, Math.min(originalContents.length, contents.length));

                    for (int i = originalContents.length; i < contents.length; i++) {
                        contents[i] = null;
                    }

                    if (target.getInventory().getHelmet() == null) {
                        ItemStack redGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                        ItemMeta meta = redGlass.getItemMeta();
                        meta.setEnchantmentGlintOverride(true);
                        meta.setDisplayName("§cNo Helmet");
                        redGlass.setItemMeta(meta);

                        contents[39] = redGlass;
                    } else {
                        contents[39] = target.getInventory().getHelmet();
                    }

                    if (target.getInventory().getChestplate() == null) {
                        ItemStack redGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                        ItemMeta meta = redGlass.getItemMeta();
                        meta.setEnchantmentGlintOverride(true);
                        meta.setDisplayName("§cNo Chestplate");
                        redGlass.setItemMeta(meta);

                        contents[38] = redGlass;
                    } else {
                        contents[38] = target.getInventory().getChestplate();
                    }

                    if (target.getInventory().getLeggings() == null) {
                        ItemStack redGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                        ItemMeta meta = redGlass.getItemMeta();
                        meta.setEnchantmentGlintOverride(true);
                        meta.setDisplayName("§cNo Leggings");
                        redGlass.setItemMeta(meta);

                        contents[37] = redGlass;
                    } else {
                        contents[37] = target.getInventory().getLeggings();
                    }

                    if (target.getInventory().getBoots() == null) {
                        ItemStack redGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                        ItemMeta meta = redGlass.getItemMeta();
                        meta.setEnchantmentGlintOverride(true);
                        meta.setDisplayName("§cNo Boots");
                        redGlass.setItemMeta(meta);

                        contents[36] = redGlass;
                    } else {
                        contents[36] = target.getInventory().getBoots();
                    }

                    if (target.getInventory().getItemInOffHand().getType().equals(Material.AIR)) {
                        ItemStack yellowGlass = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
                        ItemMeta meta = yellowGlass.getItemMeta();
                        meta.setEnchantmentGlintOverride(true);
                        meta.setDisplayName("§cNo Offhand-Item");
                        yellowGlass.setItemMeta(meta);

                        contents[40] = yellowGlass;
                    } else {
                        contents[40] = target.getInventory().getItemInOffHand();
                    }

                    for (int i = 41; i <= 44; i++) {
                        if (contents[i] == null || contents[i].getType() == Material.AIR) {
                            ItemStack whiteGlass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
                            ItemMeta meta = whiteGlass.getItemMeta();
                            meta.setEnchantmentGlintOverride(true);
                            meta.setDisplayName("");
                            meta.setHideTooltip(true);
                            whiteGlass.setItemMeta(meta);

                            contents[i] = whiteGlass;
                        }
                    }
                    gui.setContents(contents).update();
                }
            }, 0L, 2L);
            return true;
        } else {
            GUI gui = GUI.createGUI(target.getInventory())
                    .onClick((event, currentGUI) -> {
                        event.setCancelled(true);
                        event.getWhoClicked().getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 6f);
                    });

            gui.show(player);

            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .forEach(completions::add);
        }
        return completions;
    }
}
