package de.kamiql.core.source.economy.custom.gemstones.econ;

import de.kamiql.Main;
import de.kamiql.core.source.economy.database.EconomyManager;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MyGemstones implements Gemstones {
    private final YamlConfiguration config = Main.getConfig("server/economy/settings.yml");

    @Override
    public boolean isEnabled() {
        return config.getBoolean("economy.gemstones.enabled", false);
    }

    @Override
    public @NotNull String getName() {
        return config.getString("economy.gemstones.name", "Economy");
    }

    @Override
    public @NotNull Integer fractionalDigits() {
        return config.getInt("economy.gemstones.fractional-digits", 2);
    }

    @Override
    public @NotNull String format(Double amount) {
        return String.format("%,.2f %s", amount, currencyName());
    }

    @Override
    public @NotNull String currencyName() {
        return config.getString("economy.gemstones.currency-symbol", "\uD83D\uDC8E");
    }

    @Override
    public boolean hasAccount(@NotNull OfflinePlayer player) {
        return new EconomyManager(player, EconomyManager.TransactionType.GEMS)
                .hasAccount();
    }

    @Override
    public boolean hasAccount(@NotNull UUID uuid) {
        return hasAccount(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public boolean hasAccount(@NotNull String name) {
        return hasAccount(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public boolean createAccount(@NotNull OfflinePlayer player) {
        return new EconomyManager(player, EconomyManager.TransactionType.GEMS)
                .createAccount();
    }

    @Override
    public boolean createAccount(@NotNull UUID uuid) {
        return createAccount(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public boolean createAccount(@NotNull String name) {
        return createAccount(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public boolean deleteAccount(@NotNull OfflinePlayer player) {
        return new EconomyManager(player, EconomyManager.TransactionType.GEMS)
                .deleteAccount();
    }

    @Override
    public boolean deleteAccount(@NotNull UUID uuid) {
        return deleteAccount(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public boolean deleteAccount(@NotNull String name) {
        return deleteAccount(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public @NotNull Double getBalance(@NotNull OfflinePlayer player) {
        return new EconomyManager(player, EconomyManager.TransactionType.GEMS)
                .getBalance();
    }

    @Override
    public @NotNull Double getBalance(@NotNull UUID uuid) {
        return getBalance(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public @NotNull Double getBalance(@NotNull String name) {
        return getBalance(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public boolean has(@NotNull Double amount, @NotNull OfflinePlayer player) {
        return new EconomyManager(player, EconomyManager.TransactionType.GEMS)
                .getBalance() >= amount;
    }

    @Override
    public boolean has(@NotNull Double amount, @NotNull UUID uuid) {
        return has(amount, Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public boolean has(@NotNull Double amount, @NotNull String name) {
        return has(amount, Bukkit.getOfflinePlayer(name));
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull Double amount, @NotNull OfflinePlayer player) {
        try (EconomyManager manager = new EconomyManager(player, EconomyManager.TransactionType.GEMS)) {
            if (!manager.hasAccount()) {
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account does not exist");
            }
            if (!has(amount, player)) {
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
            }
            boolean success = manager.updateBalance(-amount);
            double balance = manager.getBalance();
            return new EconomyResponse(amount, balance, success ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
        }
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull Double amount, @NotNull UUID uuid) {
        return withdraw(amount, Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull Double amount, @NotNull String name) {
        return withdraw(amount, Bukkit.getOfflinePlayer(name));
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull Double amount, @NotNull OfflinePlayer player) {
        try (EconomyManager manager = new EconomyManager(player, EconomyManager.TransactionType.GEMS)) {
            if (!manager.hasAccount()) {
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account does not exist");
            }
            boolean success = manager.updateBalance(amount);
            double balance = manager.getBalance();
            return new EconomyResponse(amount, balance, success ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
        }
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull Double amount, @NotNull UUID uuid) {
        return deposit(amount, Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull Double amount, @NotNull String name) {
        return deposit(amount, Bukkit.getOfflinePlayer(name));
    }
}
