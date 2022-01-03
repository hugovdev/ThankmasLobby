package me.hugo.thankmaslobby.commands;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.player.GamePlayer;
import me.hugo.thankmaslobby.player.rank.Rank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", "close", "bye");

        setDefaultExecutor((commandSender, commandContext) -> {
            if(!(commandSender instanceof Player)) {
                MinecraftServer.stopCleanly();
                return;
            }

            GamePlayer gamePlayer = ThankmasLobby.getInstance().getPlayerManager().getPlayerData((Player)commandSender);

            if(gamePlayer.getRank() == Rank.ADMIN) {
                commandSender.sendMessage(Component.text("Closing server instance...", NamedTextColor.RED));
                MinecraftServer.stopCleanly();
            } else {
                commandSender.sendMessage(Component.text("You need rank ", NamedTextColor.RED)
                        .append(Component.text(Rank.ADMIN.getRankName(), NamedTextColor.AQUA))
                        .append(Component.text(" to close the server!", NamedTextColor.RED)));
            }
        });
    }
}
