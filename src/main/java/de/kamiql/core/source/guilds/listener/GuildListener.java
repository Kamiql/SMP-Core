package de.kamiql.core.source.guilds.listener;

import de.kamiql.core.source.guilds.listener.events.GuildCreateEvent;
import de.kamiql.core.source.guilds.listener.events.GuildDeleteEvent;
import de.kamiql.core.source.guilds.listener.events.GuildChangeEvent;
import de.kamiql.core.source.guilds.listener.events.GuildMemberJoinEvent;
import de.kamiql.core.source.guilds.listener.events.GuildMemberLeaveEvent;

public interface GuildListener {
    void onGuildCreate(GuildCreateEvent event);
    void onGuildDelete(GuildDeleteEvent event);
    void onGuildChange(GuildChangeEvent event);
    void onGuildMemberJoin(GuildMemberJoinEvent event);
    void onGuildMemberLeave(GuildMemberLeaveEvent event);
}
