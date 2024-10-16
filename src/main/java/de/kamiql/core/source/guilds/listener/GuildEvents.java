package de.kamiql.core.source.guilds.listener;

import de.kamiql.core.source.guilds.listener.events.*;

import java.util.ArrayList;
import java.util.List;

public class GuildEvents {
    private static final List<GuildListener> listeners = new ArrayList<>();

    public static void addListener(GuildListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(GuildListener listener) {
        listeners.remove(listener);
    }

    public static void notifyGuildCreate(GuildCreateEvent event) {
        for (GuildListener listener : listeners) {
            listener.onGuildCreate(event);
        }
    }

    public static void notifyGuildDelete(GuildDeleteEvent event) {
        for (GuildListener listener : listeners) {
            listener.onGuildDelete(event);
        }
    }

    public static void notifyGuildChange(GuildChangeEvent event) {
        for (GuildListener listener : listeners) {
            listener.onGuildChange(event);
        }
    }

    public static void notifyGuildMemberJoin(GuildMemberJoinEvent event) {
        for (GuildListener listener : listeners) {
            listener.onGuildMemberJoin(event);
        }
    }

    public static void notifyGuildMemberLeave(GuildMemberLeaveEvent event) {
        for (GuildListener listener : listeners) {
            listener.onGuildMemberLeave(event);
        }
    }
}
