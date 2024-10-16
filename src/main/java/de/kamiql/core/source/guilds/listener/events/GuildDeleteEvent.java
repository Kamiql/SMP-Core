package de.kamiql.core.source.guilds.listener.events;

import de.kamiql.core.source.guilds.system.Guild;
import org.jetbrains.annotations.NotNull;

public class GuildDeleteEvent {
    private final Guild guild;

    public GuildDeleteEvent(@NotNull Guild guild) {
        this.guild = guild;
    }

    public Guild getGuild() {
        return guild;
    }
}
