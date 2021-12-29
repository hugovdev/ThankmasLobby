package me.hugo.thankmaslobby.events;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.player.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public class PlayerLeave {

    public PlayerLeave(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            final Player player = event.getPlayer();

            ThankmasLobby.getInstance().getPlayerManager().getPlayerData(player).getRank().getTeam().removeMember(player.getUsername());

            ThankmasLobby.getInstance().getPlayerManager().removePlayerData(player);
            updatePlayerCounter();
            System.out.println("[ThankmasLobby] " + player.getUsername() + " left!");
        });
    }

    private void updatePlayerCounter() {
        int onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().size();

        for (GamePlayer gamePlayer : ThankmasLobby.getInstance().getPlayerManager().getPlayerStorage().values())
            gamePlayer.getScoreboard().updateLineContent("lobbyPlayerCounter",
                    Component.text("Players: ").color(NamedTextColor.WHITE).append(Component.text(onlinePlayers).color(NamedTextColor.GREEN)));
    }

}
