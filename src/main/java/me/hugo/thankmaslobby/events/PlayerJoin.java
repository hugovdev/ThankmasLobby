package me.hugo.thankmaslobby.events;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.item.HotBarItem;
import me.hugo.thankmaslobby.player.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.advancements.notifications.Notification;
import net.minestom.server.advancements.notifications.NotificationCenter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.*;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PlayerJoin {

    Notification joinNotification;

    public PlayerJoin(GlobalEventHandler globalEventHandler) {
        ThankmasLobby thankmasLobby = ThankmasLobby.getInstance();
        joinNotification = new Notification(
                Component.text("Log into ", NamedTextColor.WHITE).append(Component.text("Hytale Thankmas 2022").color(NamedTextColor.AQUA)),
                FrameType.TASK,
                ItemStack.of(Material.GOLD_NUGGET)
        );
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setChunkGenerator(new GeneratorDemo());

        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            GamePlayer gamePlayer = thankmasLobby.getPlayerManager().getPlayerData(player);

            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            NotificationCenter.send(joinNotification, player);
            thankmasLobby.getPlayerManager().getPlayerData(player).loadSidebar();
            player.openBook(thankmasLobby.getWelcomeBook());

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

            player.setAllowFlying(true);
        });
    }

    private static class GeneratorDemo implements ChunkGenerator {

        @Override
        public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
            // Set chunk blocks
            for (byte x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
                for (byte z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                    for (byte y = 0; y < 40; y++) {
                        batch.setBlock(x, y, z, Block.STONE);
                    }
                }
            }
        }

        @Override
        public void fillBiomes(Biome[] biomes, int chunkX, int chunkZ) {
            Arrays.fill(biomes, Biome.PLAINS);
        }

        @Override
        public List<ChunkPopulator> getPopulators() {
            return null;
        }
    }

}
