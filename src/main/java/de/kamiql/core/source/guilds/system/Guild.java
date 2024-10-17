package de.kamiql.core.source.guilds.system;

import de.kamiql.core.source.guilds.listener.GuildEvents;
import de.kamiql.core.source.guilds.listener.events.*;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Guild implements IGuild {
    private @NotNull String tag;
    private @NotNull String name;
    private final @NotNull String id;
    private final @NotNull List<HumanEntity> members;
    private @NotNull HumanEntity owner;

    private Guild(@NotNull String tag,
                  @NotNull String name,
                  @NotNull String id,
                  @Nullable List<HumanEntity> members,
                  @NotNull HumanEntity owner)
    {
        this.tag = tag;
        this.name = name;
        this.id = id;
        this.members = members != null ? new ArrayList<>(members) : new ArrayList<>(List.of(owner));
        this.owner = owner;
    }

    @Override
    public @NotNull List<HumanEntity> getMember() {
        return members;
    }

    @Override
    public @NotNull HumanEntity getOwner() {
        return owner;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getTag() {
        return tag;
    }

    public boolean removeMember(@NotNull HumanEntity member) {
        if (hasMember(member)) {
            this.members.remove(member);
            GuildEvents.notifyGuildMemberLeave(new GuildMemberLeaveEvent(this, member));
            return true;
        }
        return false;
    }

    public boolean addMember(@NotNull HumanEntity member) {
        if (!hasMember(member)) {
            this.members.add(member);
            GuildEvents.notifyGuildMemberJoin(new GuildMemberJoinEvent(this, member));
            return true;
        }
        return false;
    }

    public void changeTag(@NotNull String newTag, @NotNull HumanEntity changer) {
        this.tag = newTag;
        GuildEvents.notifyGuildChange(new GuildChangeEvent(this));
    }

    public void changeName(@NotNull String newName, @NotNull HumanEntity changer) {
        this.name = newName;
        GuildEvents.notifyGuildChange(new GuildChangeEvent(this));
    }

    public void changeOwner(@NotNull HumanEntity newOwner, @NotNull HumanEntity changer) {
        this.owner = newOwner;
        GuildEvents.notifyGuildChange(new GuildChangeEvent(this));
    }


    public boolean hasMember(HumanEntity entity) {
        return members.contains(entity);
    }

    public boolean isOwner(HumanEntity entity) {
        return owner.equals(entity);
    }

    public static class Manager {
        public void createGuild(@NotNull HumanEntity owner, @NotNull String name, @NotNull String tag) {
            Guild guild = new Guild.Builder()
                    .setId(new Random().ints(8, 0, 36).mapToObj(i -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(i) + "").collect(Collectors.joining()))
                    .setName(name)
                    .setOwner(owner)
                    .setTag(tag)
                    .build();
            GuildEvents.notifyGuildCreate(new GuildCreateEvent(guild));
        }

        public void deleteGuild(@NotNull Guild guild) {
            GuildEvents.notifyGuildDelete(new GuildDeleteEvent(guild));
        }
    }

    public static class Builder {
        private String tag;
        private String name;
        private String id;
        private List<HumanEntity> members;
        private HumanEntity owner;

        public Builder setTag(@NotNull String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setName(@NotNull String name) {
            this.name = name;
            return this;
        }

        public Builder setId(@NotNull String id) {
            this.id = id;
            return this;
        }

        public Builder setMembers(List<HumanEntity> members) {
            this.members = members;
            return this;
        }

        public Builder setOwner(@NotNull HumanEntity owner) {
            this.owner = owner;
            return this;
        }

        public Guild build() {
            return new Guild(tag, name, id, members, owner);
        }
    }
}
