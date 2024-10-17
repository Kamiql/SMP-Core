package de.kamiql.core.source.guilds.listener.events;

import de.kamiql.core.source.guilds.system.Guild;
import org.jetbrains.annotations.NotNull;

public class GuildCreateEvent {
    private final Guild guild;

    public GuildCreateEvent(@NotNull Guild guild) {
        this.guild = guild;
    }

    public Guild getGuild() {
        return guild;
    }
}
