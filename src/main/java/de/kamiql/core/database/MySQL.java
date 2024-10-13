package de.kamiql.core.database;

import java.sql.*;
import java.util.UUID;

public class MySQL {
    private String url;
    private String user;
    private String password;

    public MySQL(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Insert a secret key for a player into the database.
     *
     * @param uuid the player's UUID
     * @param secretKey the secret key to insert
     * @throws SQLException if a database access error occurs
     */
    public void insertKey(UUID uuid, String secretKey) throws SQLException {
        String query = "INSERT INTO players (uuid, secret_key) VALUES (?, ?) ON DUPLICATE KEY UPDATE secret_key = VALUES(secret_key)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, secretKey);
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieve a secret key for a player from the database.
     *
     * @param uuid the player's UUID
     * @return the secret key, or null if no key is found
     * @throws SQLException if a database access error occurs
     */
    public String getKey(UUID uuid) throws SQLException {
        String query = "SELECT secret_key FROM players WHERE uuid = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("secret_key");
            }
        }
        return null;
    }
}
