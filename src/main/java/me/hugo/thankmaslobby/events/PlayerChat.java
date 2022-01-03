package me.hugo.thankmaslobby.events;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.player.GamePlayer;
import me.hugo.thankmaslobby.player.PlayerManager;
import me.hugo.thankmaslobby.settings.OptionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerChatEvent;

public class PlayerChat {

    public PlayerChat(GlobalEventHandler globalEventHandler) {
        PlayerManager playerManager = ThankmasLobby.getInstance().getPlayerManager();
        OptionManager optionManager = ThankmasLobby.getInstance().getOptionManager();

        globalEventHandler.addListener(PlayerChatEvent.class, event -> {
            Player player = event.getPlayer();
            GamePlayer gamePlayer = playerManager.getPlayerData(player);

            event.setCancelled(true);

            /*
            If the state id is 1 it means the sender's chat is disabled.
             */
            if(gamePlayer.getState(optionManager.CHAT).getStateId() == 1) {
                player.sendMessage(Component.text("Your chat is disabled! Activate it to speak!", NamedTextColor.RED));
                return;
            }

            /*
            We build the message that will be sent to the ones
            who have chat enabled.
             */
            Component chatMessage = gamePlayer.getRank().getPrefix()
                    .append(Component.text(player.getUsername()))
                    .hoverEvent(Component.text("test"))
                    .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(event.getMessage(), (gamePlayer.getRank().getValue() > 0 ? NamedTextColor.WHITE : NamedTextColor.GRAY)));

            for(GamePlayer players : playerManager.getPlayerStorage().values()) {
                /*
                If the state id is 0 it means their chat is enabled.
                 */
                if(players.getState(optionManager.CHAT).getStateId() == 0) players.getPlayer().sendMessage(chatMessage);
            }

            System.out.println(player.getUsername() + " said '" + event.getMessage() + "'.");
        });
    }

}
