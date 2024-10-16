package de.kamiql.core.source.guilds.system.handler;

import de.kamiql.Main;
import de.kamiql.core.database.MySQL;
import de.kamiql.core.source.guilds.listener.GuildListener;
import de.kamiql.core.source.guilds.listener.events.*;
import de.kamiql.core.source.guilds.system.Guild;
import net.luckperms.api.LuckPerms;

import java.sql.Connection;
import java.sql.SQLException;

public class GuildHandler implements GuildListener {
    private String url = "jdbc:mysql://localhost/guilds";
    private String user = "root";
    private String password = "VNk0qMp0ph79OR";

    private final LuckPerms lp;

    public GuildHandler() {
        this.lp = Main.getLuckPerms();
    }

    private Connection getConnection() throws SQLException {
        return new MySQL(url, user, password).connect();
    }

    @Override
    public void onGuildCreate(GuildCreateEvent event) {
        Guild guild = event.getGuild();

//        for (HumanEntity entity : guild.getMember()) {
//            SuffixNode node = SuffixNode.builder("&d[&7" + guild.getTag() +"&d]", 150).build();
//            lp.getUserManager().loadUser(entity.getUniqueId()).thenAccept(user -> {
//                user.data().add(node);
//                lp.getUserManager().saveUser(user);
//            });
//        }
    }

    @Override
    public void onGuildDelete(GuildDeleteEvent event) {
        Guild guild = event.getGuild();

    }

    @Override
    public void onGuildChange(GuildChangeEvent event) {
        Guild guild = event.getGuild();

    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();

    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        Guild guild = event.getGuild();

    }
}
