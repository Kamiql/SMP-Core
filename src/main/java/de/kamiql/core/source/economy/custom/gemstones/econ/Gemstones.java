package de.kamiql.core.source.economy.custom.gemstones.econ;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Gemstones {

    // General
    boolean isEnabled();

    @NotNull String getName();

    @NotNull Integer fractionalDigits();

    @NotNull String format(Double amount);

    @NotNull String currencyName();

    // Accounts
    boolean hasAccount(@NotNull OfflinePlayer player);

    boolean hasAccount(@NotNull UUID uuid);

    boolean hasAccount(@NotNull String name);

    boolean createAccount(@NotNull OfflinePlayer player);

    boolean createAccount(@NotNull UUID uuid);

    boolean createAccount(@NotNull String name);

    boolean deleteAccount(@NotNull OfflinePlayer player);

    boolean deleteAccount(@NotNull UUID uuid);

    boolean deleteAccount(@NotNull String name);

    // Econ
    @NotNull Double getBalance(@NotNull OfflinePlayer player);

    @NotNull Double getBalance(@NotNull UUID uuid);

    @NotNull Double getBalance(@NotNull String name);

    boolean has(@NotNull Double amount,@NotNull OfflinePlayer player);

    boolean has(@NotNull Double amount,@NotNull UUID uuid);

    boolean has(@NotNull Double amount,@NotNull String name);

    @NotNull EconomyResponse withdraw(@NotNull Double amount, @NotNull OfflinePlayer player);

    @NotNull EconomyResponse withdraw(@NotNull Double amount, @NotNull UUID uuid);

    @NotNull EconomyResponse withdraw(@NotNull Double amount, @NotNull String name);

    @NotNull EconomyResponse deposit(@NotNull Double amount, @NotNull OfflinePlayer player);

    @NotNull EconomyResponse deposit(@NotNull Double amount, @NotNull UUID uuid);

    @NotNull EconomyResponse deposit(@NotNull Double amount, @NotNull String name);
}
