package de.kamiql.core.toolkit.extension.invframework;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class GUIListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        GUI.handleClickEvent(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        GUI.handleCloseEvent(event);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        GUI.handleOpenEvent(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        GUI.handleDragEvent(event);
    }
}