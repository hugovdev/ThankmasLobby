package me.hugo.thankmaslobby.commands;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.player.GamePlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class SecretMenuCommand extends Command {

    public SecretMenuCommand() {
        super("opensecretsmenu");

        setDefaultExecutor(((commandSender, commandContext) -> {
            if(!(commandSender instanceof Player)) {
                commandSender.sendMessage("You need to be a player to execute this command!");
                return;
            }

            GamePlayer gamePlayer = ThankmasLobby.getInstance().getPlayerManager().getPlayerData((Player) commandSender);
            gamePlayer.getUnlockedNPCMenu().open((Player) commandSender);
        }));
    }

}
