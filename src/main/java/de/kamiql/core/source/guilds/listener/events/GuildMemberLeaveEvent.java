package de.kamiql.core.source.guilds.listener.events;

import de.kamiql.core.source.guilds.system.Guild;
import org.bukkit.entity.HumanEntity;

public class GuildMemberLeaveEvent {
    private final Guild guild;
    private final HumanEntity member;

    public GuildMemberLeaveEvent(Guild guild, HumanEntity member) {
        this.guild = guild;
        this.member = member;
    }

    public Guild getGuild() {
        return guild;
    }

    public HumanEntity getMember() {
        return member;
    }
}
