package de.kamiql.core.source.economy.vault.econ;

import de.kamiql.Main;
import de.kamiql.core.source.economy.database.EconomyManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class MyEconomy implements Economy {
    private final YamlConfiguration config = Main.getConfig("server/economy/settings.yml");

    @Override
    public boolean isEnabled() {
        return config.getBoolean("economy.vault.enabled", false);
    }

    @Override
    public String getName() {
        return config.getString("economy.vault.name", "Economy");
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return config.getInt("economy.vault.fractional-digits", 2);
    }

    @Override
    public String format(double amount) {
        return String.format("%,.2f %s", amount, currencyNameSingular());
    }

    @Override
    public String currencyNamePlural() {
        return currencyNameSingular();
    }

    @Override
    public String currencyNameSingular() {
        return config.getString("economy.vault.currency-symbol", "$");
    }

    @Override
    public boolean hasAccount(String playerName) {
        try (EconomyManager manager = new EconomyManager(Bukkit.getOfflinePlayer(playerName), EconomyManager.TransactionType.MONEY)) {
            return manager.hasAccount();
        }
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return hasAccount(player.getName());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String playerName) {
        try (EconomyManager manager = new EconomyManager(Bukkit.getOfflinePlayer(playerName), EconomyManager.TransactionType.MONEY)) {
            return manager.getBalance();
        }
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return getBalance(player.getName());
    }

    @Override
    public double getBalance(String playerName, String worldName) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String worldName) {
        return getBalance(player.getName());
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String s, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, String s1, double amount) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(name), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        try (EconomyManager manager = new EconomyManager(offlinePlayer, EconomyManager.TransactionType.MONEY)) {
            if (!manager.hasAccount()) {
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account does not exist");
            }
            if (!has(offlinePlayer, amount)) {
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
            }
            boolean success = manager.updateBalance(-amount);
            double balance = manager.getBalance();
            return new EconomyResponse(amount, balance, success ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
        }
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        try (EconomyManager manager = new EconomyManager(player, EconomyManager.TransactionType.MONEY)) {
            if (!manager.hasAccount()) {
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account does not exist");
            }
            boolean success = manager.updateBalance(amount);
            double balance = manager.getBalance();
            return new EconomyResponse(amount, balance, success ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
        }
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return depositPlayer(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return depositPlayer(offlinePlayer, v);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return createPlayerAccount(Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        try (EconomyManager manager = new EconomyManager(player, EconomyManager.TransactionType.MONEY)) {
            return manager.createAccount();
        }
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return createPlayerAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return createPlayerAccount(offlinePlayer);
    }

    @Override
    public EconomyResponse createBank(String name, String owner) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking system not supported");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer owner) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking system not supported");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking system not supported");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking system not supported");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking system not supported");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking system not supported");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking system not supported");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String owner) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking system not supported");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer owner) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking system not supported");
    }

    @Override
    public EconomyResponse isBankMember(String name, String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking system not supported");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking system not supported");
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }
}
