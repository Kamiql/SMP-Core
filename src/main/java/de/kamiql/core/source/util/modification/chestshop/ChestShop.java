package de.kamiql.core.source.util.modification.chestshop;

import de.kamiql.core.source.util.modification.chestshop.assets.ShopType;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ChestShop implements IChestShop {
    private final @NotNull ShopType type;
    private final @NotNull OfflinePlayer owner;
    private final @NotNull ItemStack item;
    private final @NotNull Integer count;
    private final @NotNull Double price;

    public ChestShop(@NotNull Builder builder) {
        this.type = builder.type;
        this.owner = builder.owner;
        this.item = builder.item;
        this.count = builder.count;
        this.price = builder.price;
    }

    @Override
    public @NotNull ShopType getType() {
        return type;
    }

    @Override
    public @NotNull OfflinePlayer getOwner() {
        return owner;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return item;
    }

    @Override
    public @NotNull Integer getCount() {
        return count;
    }

    @Override
    public @NotNull Double getPrice() {
        return price;
    }

    public static class Builder {
        private @NotNull ShopType type;
        private @NotNull OfflinePlayer owner;
        private @NotNull ItemStack item;
        private @NotNull Integer count;
        private @NotNull Double price;

        public Builder setType(@NotNull ShopType type) {
            this.type = type;
            return this;
        }

        public Builder setOwner(@NotNull OfflinePlayer owner) {
            this.owner = owner;
            return this;
        }

        public Builder setItem(@NotNull ItemStack item) {
            this.item = item;
            return this;
        }

        public Builder setCount(@NotNull Integer count) {
            this.count = count;
            return this;
        }

        public Builder setPrice(@NotNull Double price) {
            this.price = price;
            return this;
        }

        public ChestShop build() {
            return new ChestShop(this);
        }
    }
}
