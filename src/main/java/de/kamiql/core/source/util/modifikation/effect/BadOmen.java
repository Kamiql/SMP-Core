package de.kamiql.core.source.util.modifikation.effect;

import de.kamiql.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.Raid;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class BadOmen implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onPillagerSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Pillager pillager && isBadOmenEnabled()) {
            if (pillager.isPatrolLeader()) {
                pillager.setLootTable(new LootTable() {
                    @Override
                    public @NotNull Collection<ItemStack> populateLoot(@Nullable Random random, @NotNull LootContext lootContext) {
                        List<ItemStack> loot = new ArrayList<>();

                        if (pillager.getEquipment() != null && pillager.getEquipment().getHelmet() != null) {
                            loot.add(pillager.getEquipment().getHelmet());
                        }

                        if (pillager.getEquipment() != null && pillager.getEquipment().getItemInMainHand() != null) {
                            loot.add(pillager.getEquipment().getItemInMainHand());
                        }

                        return loot;
                    }

                    @Override
                    public void fillInventory(@NotNull Inventory inventory, @Nullable Random random, @NotNull LootContext lootContext) {

                    }

                    @Override
                    public @NotNull NamespacedKey getKey() {
                        return new NamespacedKey(Main.getPlugin(Main.class), "custom_pillager_loot");
                    }
                });
            }
        }
    }

    @EventHandler
    public void onRaidCaptainDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Pillager pillager && isBadOmenEnabled()) {
            if (pillager.isPatrolLeader()) {
                if (event.getEntity().getKiller() != null) {
                    Player killer = event.getEntity().getKiller();

                    int newDuration = random.nextInt(120000) + 6000;
                    int newAmplifier = random.nextInt(5);

                    if (!isRaidActive(killer)) {
                        killer.sendMessage("1");
                        if (killer.hasPotionEffect(PotionEffectType.BAD_OMEN)) {
                            PotionEffect currentEffect = killer.getPotionEffect(PotionEffectType.BAD_OMEN);
                            int currentDuration = currentEffect.getDuration();
                            int currentAmplifier = currentEffect.getAmplifier();

                            int finalDuration = Math.min(currentDuration + newDuration, 120000);
                            int finalAmplifier = Math.min(currentAmplifier + newAmplifier, 5);

                            killer.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, finalDuration, finalAmplifier));
                        } else {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, newDuration, newAmplifier));
                        }
                    } else if (isRaidWon(killer) || isRaidLoss(killer) || isRaidStopped(killer)){
                        killer.sendMessage("2");
                        int finalAmplifier = Math.min(killer.getWorld().locateNearestRaid(killer.getLocation(), 128).getBadOmenLevel() + 1, 5);
                        killer.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 120000, finalAmplifier));
                    }
                }
            }
        }
    }

    private boolean isRaidActive(Player player) {
        Raid raid = player.getWorld().locateNearestRaid(player.getLocation(), 128);
        return raid != null && raid.getStatus() == Raid.RaidStatus.ONGOING;
    }

    private boolean isRaidWon(Player player) {
        Raid raid = player.getWorld().locateNearestRaid(player.getLocation(), 128);
        return raid != null && raid.getStatus() == Raid.RaidStatus.VICTORY;
    }

    private boolean isRaidLoss(Player player) {
        Raid raid = player.getWorld().locateNearestRaid(player.getLocation(), 128);
        return raid != null && raid.getStatus() == Raid.RaidStatus.LOSS;
    }

    private boolean isRaidStopped(Player player) {
        Raid raid = player.getWorld().locateNearestRaid(player.getLocation(), 128);
        return raid != null && raid.getStatus() == Raid.RaidStatus.STOPPED;
    }

    private boolean isBadOmenEnabled() {
        return Main.getConfiguration("config").getBoolean("modifications.BadOmenToggle", false);
    }
}
