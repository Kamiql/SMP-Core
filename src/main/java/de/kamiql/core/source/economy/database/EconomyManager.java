package de.kamiql.core.source.economy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.kamiql.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class EconomyManager implements AutoCloseable {
    private Connection connection;
    private final OfflinePlayer player;
    private final TransactionType transactionType;

    public enum TransactionType {
        GEMS, MONEY;
    }

    public EconomyManager(@NotNull OfflinePlayer player, @NotNull TransactionType transactionType) {
        this.player = player;
        this.transactionType = transactionType;
        connect();
        createTableIfNotExists();
    }

    private void connect() {
        try {
            YamlConfiguration config = Main.getConfig("server/economy/settings.yml");

            String url = config.getString("database.url");
            String user = config.getString("database.user");
            String password = config.getString("database.password");

            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            Main.getInstance().getLogger().severe(e::getMessage);
            Bukkit.getPluginManager().disablePlugin(Main.getInstance());
        }
    }

    public void createTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS accounts (UUID VARCHAR(36) PRIMARY KEY, Gems DOUBLE DEFAULT 0.0, Money DOUBLE DEFAULT 0.0)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.getInstance().getLogger().severe(e::getMessage);
        }
    }

    public List<OfflinePlayer> getAllPlayersWithAccount() {
        String query = "SELECT UUID FROM accounts";
        List<OfflinePlayer> players = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    players.add(Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID"))));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all players with account", e);
        }
        return players;
    }

    public boolean hasAccount() {
        String query = "SELECT 1 FROM accounts WHERE UUID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, player.getUniqueId().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check account existence", e);
        }
    }

    public boolean createAccount() {
        String query = """
            INSERT INTO accounts (UUID, isEconomyAdmin, Gems, Money)
            VALUES (?, false, 0.0, 0.0)
            ON DUPLICATE KEY UPDATE UUID=UUID
        """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create account", e);
        }
    }

    public boolean deleteAccount() {
        String query = "DELETE FROM accounts WHERE UUID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, player.getUniqueId().toString());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete account", e);
        }
    }

    public boolean set(double amount) {
        String balanceField = transactionType.name();
        String query = "UPDATE accounts SET " + balanceField + " = ? WHERE UUID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, amount);
            stmt.setString(2, player.getUniqueId().toString());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set balance", e);
        }
    }

    public boolean updateBalance(double amount) {
        String balanceField = transactionType.name();
        String query = "UPDATE accounts SET " + balanceField + " = " + balanceField + " + ? WHERE UUID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, amount);
            stmt.setString(2, player.getUniqueId().toString());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update balance", e);
        }
    }

    public double getBalance() {
        String balanceField = transactionType.name();
        String query = "SELECT " + balanceField + " FROM accounts WHERE UUID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, player.getUniqueId().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                } else {
                    throw new IllegalStateException("Account not found for UUID: " + player.getUniqueId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get balance", e);
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close database connection", e);
        }
    }
}
