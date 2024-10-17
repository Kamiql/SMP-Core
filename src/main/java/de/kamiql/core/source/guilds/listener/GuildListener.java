package de.kamiql.core.source.guilds.listener;

import de.kamiql.core.source.guilds.listener.events.*;

public interface GuildListener {
    void onGuildCreate(GuildCreateEvent event);
    void onGuildDelete(GuildDeleteEvent event);
    void onGuildChange(GuildChangeEvent event);
    void onGuildMemberJoin(GuildMemberJoinEvent event);
    void onGuildMemberLeave(GuildMemberLeaveEvent event);
}
