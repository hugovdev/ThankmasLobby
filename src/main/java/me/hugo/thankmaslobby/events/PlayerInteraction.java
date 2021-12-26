package me.hugo.thankmaslobby.events;

import me.hugo.thankmaslobby.item.HotBarItem;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerUseItemEvent;

public class PlayerInteraction {

    public PlayerInteraction(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerUseItemEvent.class, event -> {
            Player player = event.getPlayer();

            event.setCancelled(true);
            HotBarItem hotBarItem = HotBarItem.fromItem(event.getItemStack());

            if(hotBarItem != null) {
                hotBarItem.getClickAction().execute(player);
            }
        });
    }

}
