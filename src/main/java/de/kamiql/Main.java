package de.kamiql;

import de.kamiql.core.source.economy.commands.BalTopCommand;
import de.kamiql.core.source.economy.commands.BalanceCommand;
import de.kamiql.core.source.economy.commands.EconomyCommand;
import de.kamiql.core.source.economy.commands.PayCommand;
import de.kamiql.core.source.economy.custom.gemstones.econ.MyGemstones;
import de.kamiql.core.source.economy.custom.gemstones.econ.Gemstones;
import de.kamiql.core.source.economy.listener.CreatePlayerAccounts;
import de.kamiql.core.source.economy.vault.econ.MyEconomy;
import de.kamiql.core.source.util.commands.player.item.RenameCommand;
import de.kamiql.core.source.util.commands.player.item.SignCommand;
import de.kamiql.core.source.util.commands.player.misc.EcCommand;
import de.kamiql.core.source.util.commands.player.misc.InvseeCommand;
import de.kamiql.core.source.util.commands.player.misc.WorkUtil;
import de.kamiql.core.source.util.commands.staff.misc.VanishCommand;
import de.kamiql.core.source.util.modification.chat.ChatFormater;
import de.kamiql.core.source.util.modification.chat.Welcomer;
import de.kamiql.core.source.util.modification.chestshop.handler.ChestShopHandler;
import de.kamiql.core.source.util.modification.rewards.TimeRewards;
import de.kamiql.core.toolkit.extension.invframework.GUI;
import de.kamiql.core.toolkit.extension.invframework.GUIListener;
import de.kamiql.core.toolkit.extension.papi.SMPcorePlaceholderExtension;
import de.kamiql.i18n.api.provider.I18nProvider;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends JavaPlugin {
    private static JavaPlugin instance;

    private static Economy econ;
    private static Gemstones gems;
    private static LuckPerms luckPerms;

    @Override
    public void onLoad() {
        try {
            instance = this;
            this.getDataFolder().mkdirs();
            this.getServer().getServicesManager().register(Economy.class, new MyEconomy(), this, ServicePriority.Highest);
            this.getServer().getServicesManager().register(Gemstones.class, new MyGemstones(), this, ServicePriority.Highest);
        } catch (Exception e) {
            getLogger().severe(e::getMessage);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        try {
            new ChestShopHandler().setupDatabase();

            new I18nProvider(this, getConfig("server/language/i18n.yml"));

            this.setupProvider();

            registerCommands();
            registerListener();
            registerPapiExtension();

            new TimeRewards().start();
        } catch (Exception e) {
            getLogger().severe(e::getMessage);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {

    }

    private void registerPapiExtension() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SMPcorePlaceholderExtension().register();
        }
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("sign")).setExecutor(new SignCommand());
        Objects.requireNonNull(getCommand("rename")).setExecutor(new RenameCommand());
        Objects.requireNonNull(getCommand("vanish")).setExecutor(new VanishCommand());
        Objects.requireNonNull(getCommand("eco")).setExecutor(new EconomyCommand());
        Objects.requireNonNull(getCommand("balTop")).setExecutor(new BalTopCommand());
        Objects.requireNonNull(getCommand("balance")).setExecutor(new BalanceCommand());
        Objects.requireNonNull(getCommand("pay")).setExecutor(new PayCommand());
        Objects.requireNonNull(getCommand("workutil")).setExecutor(new WorkUtil());
        Objects.requireNonNull(getCommand("invsee")).setExecutor(new InvseeCommand());
    }

    private void registerListener() {
        getServer().getPluginManager().registerEvents(new Welcomer(), this);
        getServer().getPluginManager().registerEvents(new CreatePlayerAccounts(), this);
        getServer().getPluginManager().registerEvents(new ChatFormater(), this);
        getServer().getPluginManager().registerEvents(new ChestShopHandler(), this);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
    }

    private void setupProvider() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            try {
                econ = rsp.getProvider();
            } catch (Exception e) {
                this.getLogger().severe(String.format(" - Disabled due to no Vault provider found!", getDescription().getName()));
            }
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            try {
                luckPerms = provider.getProvider();
            } catch (Exception e) {
                this.getLogger().severe(String.format(" - Disabled due to no LuckPerms provider found!", getDescription().getName()));
            }
        }

        RegisteredServiceProvider<Gemstones> gemstones = getServer().getServicesManager().getRegistration(Gemstones.class);
        if (gemstones != null) {
            try {
                gems = gemstones.getProvider();
            } catch (Exception e) {
                this.getLogger().severe(String.format(" - Disabled due to no Gemstones provider found!", getDescription().getName()));
            }
        }
    }

    public static YamlConfiguration getConfig(String path) {
        File file = new File(instance.getDataFolder(), path);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            Main.getInstance().saveResource(path, false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public static Connection createConnection() throws SQLException {
        YamlConfiguration config = getConfig("server/db.yml");
        return DriverManager.getConnection(config.getString("url"), config.getString("user"), config.getString("password"));
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

    public static Gemstones getGems() {
        return gems;
    }
}