package me.hugo.thankmaslobby.events;

import me.hugo.thankmaslobby.ThankmasLobby;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public class PlayerLeave {

    public PlayerLeave(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            final Player player = event.getPlayer();

            ThankmasLobby.getInstance().getPlayerManager().getPlayerData(player).getRank().getTeam().removeMember(player.getUsername());

            ThankmasLobby.getInstance().getPlayerManager().removePlayerData(player);
            System.out.println("[ThankmasLobby] " + player.getUsername() + " left!");
        });
    }

}
