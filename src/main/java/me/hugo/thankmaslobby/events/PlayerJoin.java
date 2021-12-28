package me.hugo.thankmaslobby.events;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.item.HotBarItem;
import me.hugo.thankmaslobby.player.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.advancements.notifications.Notification;
import net.minestom.server.advancements.notifications.NotificationCenter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class PlayerJoin {

    Notification joinNotification;

    public PlayerJoin(GlobalEventHandler globalEventHandler) {
        ThankmasLobby thankmasLobby = ThankmasLobby.getInstance();
        joinNotification = new Notification(
                Component.text("Log into ", NamedTextColor.WHITE).append(Component.text("Hytale Thankmas 2022").color(NamedTextColor.AQUA)),
                FrameType.TASK,
                ItemStack.of(Material.GOLD_NUGGET)
        );

        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            thankmasLobby.getPlayerManager().getPlayerData(player);

            event.setSpawningInstance(MinecraftServer.getInstanceManager().getInstances().iterator().next());
            player.setRespawnPoint(new Pos(-0.5, 47, -4.5));
        });

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();

            NotificationCenter.send(joinNotification, player);
            thankmasLobby.getPlayerManager().getPlayerData(player).loadSidebar();

            player.sendTitlePart(TitlePart.TITLE, Component.text("Hytale Thankmas", TextColor.color(0xFFB400)).decorate(TextDecoration.BOLD));
            player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Year 2022", TextColor.color(0xFFD358)));

            player.getInventory().addInventoryCondition((player1, i, clickType, inventoryConditionResult) -> {
                if (player1.getOpenInventory() == null) {
                    HotBarItem hotBarItem = HotBarItem.fromItem(inventoryConditionResult.getClickedItem());

                    if (hotBarItem != null) {
                        hotBarItem.getClickAction().execute(player);
                    }
                }
                inventoryConditionResult.setCancel(true);
            });

            for (HotBarItem hotBarItem : HotBarItem.values())
                player.getInventory().setItemStack(hotBarItem.getSlot(), hotBarItem.getItem());

            int onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().size();

            for (GamePlayer gamePlayer : ThankmasLobby.getInstance().getPlayerManager().getPlayerStorage().values()) {
                gamePlayer.getScoreboard().updateLineContent("lobbyPlayerCounter", Component.text("Players: ").color(NamedTextColor.WHITE)
                        .append(Component.text(onlinePlayers).color(NamedTextColor.GREEN)));
            }
        });
    }
}
