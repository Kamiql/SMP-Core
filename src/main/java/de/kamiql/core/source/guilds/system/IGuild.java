package de.kamiql.core.source.guilds.system;

import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IGuild {
    @NotNull String getTag();

    @NotNull String getName();

    @NotNull String getId();

    @Nullable List<HumanEntity> getMember();

    @Nullable HumanEntity getOwner();
}
