package de.kamiql.core.source.util.modification.chestshop.handler;

import org.bukkit.event.Listener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ChestShopHandler implements Listener {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:SMPcore.db");
    }

    public void setupDatabase() {
        try (Connection conn = getConnection()) {
            String createTable = "CREATE TABLE IF NOT EXISTS ChestShops (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "owner TEXT NOT NULL, " +
                    "item TEXT NOT NULL, " +
                    "count INTEGER NOT NULL, " +
                    "price DOUBLE NOT NULL, " +
                    "type TEXT NOT NULL);";
            conn.createStatement().executeUpdate(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
