package de.kamiql.core.toolkit.extension.invframework;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GUI {
    private final static Map<Inventory, GUI> guiMap = new HashMap<>();
    private final Inventory inventory;

    private BiConsumer<InventoryClickEvent, GUI> clickEventBiConsumer;
    private BiConsumer<InventoryCloseEvent, GUI> closeEventBiConsumer;
    private BiConsumer<InventoryOpenEvent, GUI> openEventBiConsumer;
    private BiConsumer<InventoryDragEvent, GUI> dragEventBiConsumer;

    private BiConsumer<GUI, Player> updateBiConsumer;

    private Consumer<InventoryClickEvent> clickEventConsumer;
    private Consumer<InventoryCloseEvent> closeEventConsumer;
    private Consumer<InventoryOpenEvent> openEventConsumer;
    private Consumer<InventoryDragEvent> dragEventConsumer;

    private Consumer<GUI> updateConsumer;

    public GUI(@Nullable Player owner, int size, String title) {
        this.inventory = Bukkit.createInventory(owner, size, title);
        guiMap.put(inventory, this);
    }

    public GUI(@Nullable Player owner, InventoryType type, String title) {
        this.inventory = Bukkit.createInventory(owner, type, title);
        guiMap.put(inventory, this);
    }

    public GUI(Inventory inventory) {
        this.inventory = inventory;
        guiMap.put(inventory, this);
    }

    public static GUI createGUI(@NotNull Inventory inv) {
        return new GUI(inv);
    }

    public static GUI createGUI(@Nullable Player owner, int size, String title) {
        return new GUI(owner, size, title);
    }

    public static GUI createGUI(@Nullable Player owner, InventoryType type, String title) {
        return new GUI(owner, type, title);
    }

    public static void handleClickEvent(InventoryClickEvent event) {
        Inventory clickedInventory = event.getInventory();
        GUI gui = guiMap.get(clickedInventory);

        if (gui != null && gui.clickEventConsumer != null) {
            gui.clickEventConsumer.accept(event);
        }

        if (gui != null && gui.clickEventBiConsumer != null) {
            gui.clickEventBiConsumer.accept(event, gui);
        }
    }

    public static void handleCloseEvent(InventoryCloseEvent event) {
        Inventory closedInventory = event.getInventory();
        GUI gui = guiMap.get(closedInventory);

        if (gui != null && gui.closeEventConsumer != null) {
            gui.closeEventConsumer.accept(event);
        }

        if (gui != null && gui.closeEventBiConsumer != null) {
            gui.closeEventBiConsumer.accept(event, gui);
        }
    }

    public static void handleOpenEvent(InventoryOpenEvent event) {
        Inventory openedInventory = event.getInventory();
        GUI gui = guiMap.get(openedInventory);

        if (gui != null && gui.openEventConsumer != null) {
            gui.openEventConsumer.accept(event);
        }

        if (gui != null && gui.openEventBiConsumer != null) {
            gui.openEventBiConsumer.accept(event, gui);
        }
    }

    public static void handleDragEvent(InventoryDragEvent event) {
        Inventory draggedInventory = event.getInventory();
        GUI gui = guiMap.get(draggedInventory);

        if (gui != null && gui.dragEventConsumer != null) {
            gui.dragEventConsumer.accept(event);
        }

        if (gui != null && gui.dragEventBiConsumer != null) {
            gui.dragEventBiConsumer.accept(event, gui);
        }
    }

    public GUI onClick(Consumer<InventoryClickEvent> consumer) {
        this.clickEventConsumer = consumer;
        return this;
    }

    public GUI onClose(Consumer<InventoryCloseEvent> consumer) {
        this.closeEventConsumer = consumer;
        return this;
    }

    public GUI onOpen(Consumer<InventoryOpenEvent> consumer) {
        this.openEventConsumer = consumer;
        return this;
    }

    public GUI onItemDrag(Consumer<InventoryDragEvent> consumer) {
        this.dragEventConsumer = consumer;
        return this;
    }

    public GUI onClose(BiConsumer<InventoryCloseEvent, GUI> consumer) {
        this.closeEventBiConsumer = consumer;
        return this;
    }

    public GUI onClick(BiConsumer<InventoryClickEvent, GUI> consumer) {
        this.clickEventBiConsumer = consumer;
        return this;
    }

    public GUI onOpen(BiConsumer<InventoryOpenEvent, GUI> consumer) {
        this.openEventBiConsumer = consumer;
        return this;
    }

    public GUI onItemDrag(BiConsumer<InventoryDragEvent, GUI> consumer) {
        this.dragEventBiConsumer = consumer;
        return this;
    }

    public GUI onUpdate(Consumer<GUI> consumer) {
        this.updateConsumer = consumer;
        return this;
    }

    public GUI onUpdate(BiConsumer<GUI, Player> consumer) {
        this.updateBiConsumer = consumer;
        return this;
    }

    public void show(Player player) {
        player.openInventory(inventory);
    }

    public void update() {
        for (HumanEntity viewer : inventory.getViewers()) {
            if (viewer instanceof Player player) {
                player.updateInventory();

                if (updateBiConsumer != null) {
                    updateBiConsumer.accept(this, player);
                }
            }
        }
        if (updateConsumer != null) {
            updateConsumer.accept(this);
        }
    }

    public boolean isRegistered() {
        return guiMap.containsKey(inventory);
    }

    public void unregister() {
        guiMap.remove(inventory);
    }

    public int getSize() {
        return inventory.getSize();
    }

    public InventoryHolder getHolder() {
        return inventory.getHolder();
    }

    public InventoryType getType() {
        return inventory.getType();
    }

    public List<HumanEntity> getViewers() {
        return inventory.getViewers();
    }

    public ItemStack getItem(int index) {
        return inventory.getItem(index);
    }

    public GUI setItem(int index, ItemStack item) {
        inventory.setItem(index, item);
        return this;
    }

    public boolean contains(Material material) {
        return inventory.contains(material);
    }

    public boolean contains(ItemStack item) {
        return inventory.contains(item);
    }

    public boolean contains(Material material, int amount) {
        return inventory.contains(material, amount);
    }

    public boolean contains(ItemStack item, int amount) {
        return inventory.contains(item, amount);
    }

    public boolean containsAtLeast(ItemStack item, int amount) {
        return inventory.containsAtLeast(item, amount);
    }

    public int first(Material material) {
        return inventory.first(material);
    }

    public int first(ItemStack item) {
        return inventory.first(item);
    }

    public int firstEmpty() {
        return inventory.firstEmpty();
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    public GUI remove(Material material) {
        inventory.remove(material);
        return this;
    }

    public GUI remove(ItemStack item) {
        inventory.remove(item);
        return this;
    }

    public GUI clear(int index) {
        inventory.clear(index);
        return this;
    }

    public GUI clear() {
        inventory.clear();
        return this;
    }

    public ListIterator<ItemStack> iterator() {
        return inventory.iterator();
    }

    public ListIterator<ItemStack> iterator(int index) {
        return inventory.iterator(index);
    }

    public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
        return inventory.addItem(items);
    }

    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
        return inventory.removeItem(items);
    }

    public ItemStack[] getContents() {
        return inventory.getContents();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public GUI setContents(ItemStack[] items) throws IllegalArgumentException {
        inventory.setContents(items);
        return this;
    }

    public ItemStack[] getStorageContents() {
        return inventory.getStorageContents();
    }

    public GUI setStorageContents(ItemStack[] items) throws IllegalArgumentException {
        inventory.setStorageContents(items);
        return this;
    }
}
