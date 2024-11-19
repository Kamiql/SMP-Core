package de.kamiql.core.source.util.modification.chestshop;

import de.kamiql.core.source.util.modification.chestshop.assets.ShopType;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IChestShop {
    @NotNull ShopType getType();

    @NotNull OfflinePlayer getOwner();

    @NotNull ItemStack getItem();

    @NotNull Integer getCount();

    @NotNull Double getPrice();
}
