package me.hugo.thankmaslobby.events;

import me.hugo.thankmaslobby.entities.NPC;
import me.hugo.thankmaslobby.entities.TextNPC;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerEntityInteractEvent;

public class EntityInteraction {

    public EntityInteraction(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerEntityInteractEvent.class, event -> {
            var target = event.getTarget();

            if (!(target instanceof NPC)) return;

            if (event.getHand() == Player.Hand.MAIN) {
                NPC npc = (NPC) target;
                npc.getInteraction().accept(new NPC.NPCInteraction(event.getPlayer(), npc));
            }
        });
    }
}
