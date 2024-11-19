package de.kamiql.core.source.util.modification.rewards;

import de.kamiql.Main;
import de.kamiql.core.source.util.modification.rewards.assets.Reward;
import de.kamiql.i18n.core.source.I18n;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class TimeRewards {
    private final YamlConfiguration config;
    private Connection connection;

    private final String ROOT = "rewards.";

    public TimeRewards() {
        this.config = Main.getConfig("server/modification/rewards/rewards.yml");
    }

    public void start() {
        if (config.getBoolean("enabled")) {
            startSequence();
        }
    }

    private void startSequence() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    getRewards().forEach((time, reward) -> {
                        if (getPlayTimeInSeconds(player) >= time) {
                            NamespacedKey key = new NamespacedKey(Main.getInstance(), "rewardsClaimed");
                            PersistentDataContainer dataContainer = player.getPersistentDataContainer();
                            int[] claimedRewards = dataContainer.get(key, PersistentDataType.INTEGER_ARRAY);

                            if (claimedRewards == null || Arrays.stream(claimedRewards).noneMatch(r -> r == time)) {
                                applyReward(reward, player);

                                int[] updatedRewards = claimedRewards == null ?
                                        new int[]{time} :
                                        Arrays.copyOf(claimedRewards, claimedRewards.length + 1);
                                updatedRewards[claimedRewards == null ? 0 : claimedRewards.length] = time;

                                dataContainer.set(key, PersistentDataType.INTEGER_ARRAY, updatedRewards);
                            }
                        }
                    });
                });
            }
        }, 0L, 20L);
    }


    private void applyReward(Reward reward, Player player) {
        assert reward.getCommands() != null;
        reward.getCommands().forEach(command -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                    .replace("%player%", player.getName())
                    .replace("%all%", "@a")
                    .replace("%random%", "@r"));
        });

        assert reward.getPermissions() != null;
        reward.getPermissions().forEach(permission -> {
            User user = Main.getLuckPerms().getUserManager().getUser(player.getUniqueId());
            if (user != null) {
                user.data().add(Node.builder(permission).build());
                user.data().add(Node.builder("group." + reward.getRank()).build());
                Main.getLuckPerms().getUserManager().saveUser(user);
            }
        });

        Main.getGems().deposit(Objects.requireNonNull(reward.getGems()), player);
        Main.getEcon().depositPlayer(player, Objects.requireNonNull(reward.getMoney()));

        new I18n.Builder("rewards.received", player)
                .withPlaceholder("MONEY", reward.getMoney() != null ? Main.getEcon().format(reward.getMoney()) : "none")
                .withPlaceholder("GEMS", reward.getGems() != null ? Main.getGems().format(reward.getGems()) : "none")
                .withPlaceholder("RANK", reward.getRank() != null ? reward.getRank() : "none")
                .withPlaceholder("PERMISSIONS", !reward.getPermissions().isEmpty() ? String.join(", ", reward.getPermissions()) : "none")
                .build()
                .sendMessageAsComponent();

        for (int i = 0; i < 2; i++) {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 6f);
        }
    }

    public int getPlayTimeInSeconds(Player player) {
        int ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        return ticks / 20;
    }

    public HashMap<Integer, Reward> getRewards() {
        final HashMap<Integer, Reward> rewards = new HashMap<>();

        config.getConfigurationSection(ROOT).getKeys(false).forEach(key -> {
            String ROOT = this.ROOT + key + ".";
            rewards.put(
                    Integer.valueOf(key),
                    new Reward.Builder()
                            .setEnabled(config.getBoolean(ROOT + "enabled"))
                            .setCommands(config.getStringList(ROOT + "custom.commands"))
                            .setMoney(config.getDouble(ROOT + "money"))
                            .setGems(config.getDouble(ROOT + "gems"))
                            .setRank(config.getString(ROOT + "custom.rank"))
                            .setPermissions(config.getStringList(ROOT + "custom.permissions"))
                            .build()
            );
        });
        return rewards;
    }
}
