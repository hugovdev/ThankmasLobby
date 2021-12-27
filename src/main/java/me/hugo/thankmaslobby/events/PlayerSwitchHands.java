package me.hugo.thankmaslobby.events;

import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerSwapItemEvent;

public class PlayerSwitchHands {

    public PlayerSwitchHands(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerSwapItemEvent.class, event -> {
            event.setCancelled(true);
        });
    }

}
