package de.kamiql.core.source.util.modification.rewards;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IReward {

    @NotNull Boolean isEnabled();

    @Nullable String getRank();

    @Nullable Double getMoney();

    @Nullable Double getGems();

    @Nullable List<String> getCommands();

    @Nullable List<String> getPermissions();
}
