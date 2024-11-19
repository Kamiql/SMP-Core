package de.kamiql.core.source.util.modification.rewards.assets;

import de.kamiql.core.source.util.modification.rewards.IReward;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Reward implements IReward {
    private final boolean ENABLED;
    private final String RANK;
    private final Double MONEY;
    private final Double GEMS;
    private final List<String> COMMANDS;
    private final List<String> PERMISSIONS;

    public Reward(Builder builder) {
        ENABLED = builder.ENABLED;
        RANK = builder.RANK;
        MONEY = builder.MONEY;
        GEMS = builder.GEMS;
        COMMANDS = builder.COMMANDS;
        PERMISSIONS = builder.PERMISSIONS;
    }

    @Override
    public @NotNull Boolean isEnabled() {
        return ENABLED;
    }

    @Override
    public @Nullable String getRank() {
        return RANK;
    }

    @Override
    public @Nullable Double getMoney() {
        return MONEY;
    }

    @Override
    public @Nullable Double getGems() {
        return GEMS;
    }

    @Override
    public @Nullable List<String> getCommands() {
        return COMMANDS;
    }

    @Override
    public @Nullable List<String> getPermissions() {
        return PERMISSIONS;
    }

    public static class Builder {
        private boolean ENABLED;
        private @Nullable String RANK;
        private @Nullable Double MONEY;
        private @Nullable Double GEMS;
        private @Nullable List<String> COMMANDS;
        private @Nullable List<String> PERMISSIONS;

        public Builder() {}

        public Builder setEnabled(boolean ENABLED) {
            this.ENABLED = ENABLED;
            return this;
        }

        public Builder setRank(@Nullable String RANK) {
            this.RANK = RANK;
            return this;
        }

        public Builder setMoney(@Nullable Double MONEY) {
            this.MONEY = MONEY;
            return this;
        }

        public Builder setGems(@Nullable Double GEMS) {
            this.GEMS = GEMS;
            return this;
        }

        public Builder setCommands(@Nullable List<String> COMMANDS) {
            this.COMMANDS = COMMANDS;
            return this;
        }

        public Builder setPermissions(@Nullable List<String> PERMISSIONS) {
            this.PERMISSIONS = PERMISSIONS;
            return this;
        }

        public Reward build() {
            return new Reward(this);
        }
    }
}
