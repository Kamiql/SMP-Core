package de.kamiql.core.source.util.commands.player;

import de.kamiql.Main;
import de.kamiql.core.util.language.i18n.I18n;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;

public class VanishCommand implements TabExecutor, Listener {

    private final Plugin plugin;
    private static final HashSet<Player> vanishedPlayers = new HashSet<>();

    public VanishCommand() {
        this.plugin = Main.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length == 0) {
            if (vanishedPlayers.contains(player)) {
                for (Player onlinePlayer : getServer().getOnlinePlayers()) {
                    onlinePlayer.showPlayer(plugin, player);
                }
                vanishedPlayers.remove(player);
                new I18n.Builder("vanish_visible", player)
                        .hasPrefix(true)
                        .build()
                        .sendMessageAsComponent();

                new I18n.Builder("join", player)
                        .hasPrefix(false)
                        .withPlaceholder("PLAYER", player.getName())
                        .build()
                        .broadcastMessageAsComponent();

            } else {
                for (Player onlinePlayer : getServer().getOnlinePlayers()) {
                    if (!onlinePlayer.hasPermission("smp-core.commands.vanish.bypass")) {
                        onlinePlayer.hidePlayer(plugin, player);
                    }
                }
                vanishedPlayers.add(player);
                new I18n.Builder("vanish_invisible", player)
                        .hasPrefix(true)
                        .build()
                        .sendMessageAsComponent();

                new I18n.Builder("quit", player)
                        .hasPrefix(false)
                        .withPlaceholder("PLAYER", player.getName())
                        .build()
                        .broadcastMessageAsComponent();
            }
        } else if (args.length == 1) {
            switch (args[0]) {
                case "list" -> {
                    new I18n.Builder("vanish_list_players", player)
                            .hasPrefix(true)
                            .withPlaceholder("PLAYERS", String.join(", ",
                                    vanishedPlayers.stream()
                                            .map(Player::getName)
                                            .collect(Collectors.toList())
                            ))
                            .build()
                            .sendMessageAsComponent();
                }
                default -> {
                    new I18n.Builder("player_not_found", player)
                            .withPlaceholder("PLAYER", args[0])
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("toggle") && Bukkit.getPlayer(args[1]) != null) {

                Player player1 = Bukkit.getPlayer(args[1]);

                new I18n.Builder("vanish_toggle_other", player)
                        .hasPrefix(true)
                        .withPlaceholder("PLAYER", player1.getName())
                        .build()
                        .sendMessageAsComponent();

                if (vanishedPlayers.contains(player1)) {
                    for (Player onlinePlayer : getServer().getOnlinePlayers()) {
                        onlinePlayer.showPlayer(plugin, player1);
                    }
                    vanishedPlayers.remove(player1);
                    new I18n.Builder("vanish_visible", player1)
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();

                    new I18n.Builder("join", player1)
                            .hasPrefix(false)
                            .withPlaceholder("PLAYER", player1.getName())
                            .build()
                            .broadcastMessageAsComponent();

                } else {
                    for (Player onlinePlayer : getServer().getOnlinePlayers()) {
                        if (!onlinePlayer.hasPermission("smp-core.commands.vanish.bypass")) {
                            onlinePlayer.hidePlayer(plugin, player1);
                        }
                    }
                    vanishedPlayers.add(player1);
                    new I18n.Builder("vanish_invisible", player1)
                            .hasPrefix(true)
                            .build()
                            .sendMessageAsComponent();

                    new I18n.Builder("quit", player1)
                            .hasPrefix(false)
                            .withPlaceholder("PLAYER", player1.getName())
                            .build()
                            .broadcastMessageAsComponent();
                }
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        for (Player vanishedPlayer : vanishedPlayers) {
            joinedPlayer.hidePlayer(plugin, vanishedPlayer);
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("toggle");
            completions.add("list");
        }
        if (args.length == 2) {
            return  Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return completions;
    }
}
