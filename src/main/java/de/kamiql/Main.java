package de.kamiql;

import de.kamiql.api.provider.CoreProvider;
import de.kamiql.api.provider.i18n.I18nProvider;
import de.kamiql.core.source.economy.provider.MyEconomy;
import de.kamiql.core.source.util.welcomer.Welcomer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {
    private static JavaPlugin instance;

    private static Economy econ;
    private static LuckPerms luckPerms;
    private static CoreProvider core;

    private static final HashMap<String, YamlConfiguration> configurations = new HashMap<>();

    @Override
    public void onLoad() {
        try {
            instance = this;
            this.setupConfigFiles();

            this.getServer().getServicesManager().register(CoreProvider.class, new CoreProvider(this), this, ServicePriority.Normal);
            this.getServer().getServicesManager().register(Economy.class, new MyEconomy(), this, ServicePriority.Normal);
        } catch (Exception e) {
            getLogger().severe(e::getMessage);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        try {
            this.setupProvider();
            new I18nProvider(this);

            getServer().getPluginManager().registerEvents(new Welcomer(), this);
        } catch (Exception e) {
            getLogger().severe(e::getMessage);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {

    }

    private void setupProvider() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            try {
                econ = rsp.getProvider();
            } catch (Exception e) {
                getLogger().severe(String.format(" - Disabled due to no Vault provider found!", getDescription().getName()));
            }
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            try {
                luckPerms = provider.getProvider();
            } catch (Exception e) {
                getLogger().severe(String.format(" - Disabled due to no LuckPerms provider found!", getDescription().getName()));
            }
        }

        RegisteredServiceProvider<CoreProvider> coreProvider = getServer().getServicesManager().getRegistration(CoreProvider.class);
        if (core != null) {
            try {
                core = coreProvider.getProvider();
            } catch (Exception e) {
                getLogger().severe(String.format(" - Disabled due to no Core provider found!", getDescription().getName()));
            }
        }
    }

    private void setupConfigFiles() {
        List<String> configurations = List.of(
            "language/i18n.yml",
            "economy/settings.yml"
        );

        for (String child : configurations) {
            File file = new File(this.getDataFolder(), child);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                saveResource(child, false);
            }

            String key = child.replaceAll(".*/|\\.yml", "");

            Main.configurations.put(key, YamlConfiguration.loadConfiguration(file));
        }
    }


    public static YamlConfiguration getConfiguration(String key) {
        return configurations.get(key);
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public static Economy getEcon() {
        return econ;
    }

    public static CoreProvider getCore() {
        return core;
    }

    public static @NotNull String getPrefix() {
        return LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize("<#28AFFB>S<#2E8EFD>M<#336CFF>P"));
    }
}