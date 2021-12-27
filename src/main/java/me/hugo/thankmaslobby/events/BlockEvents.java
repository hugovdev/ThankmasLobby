package me.hugo.thankmaslobby.events;

import me.hugo.thankmaslobby.item.HotBarItem;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;

public class BlockEvents {

    public BlockEvents(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerBlockPlaceEvent.class, event -> {
            event.setCancelled(true);
            HotBarItem.executeItemAction(event.getPlayer(), event.getPlayer().getItemInMainHand());
        });

        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event -> {
            event.setCancelled(true);
        });
    }

}
