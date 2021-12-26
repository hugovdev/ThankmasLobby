package me.hugo.kweebecparty;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.advancements.notifications.Notification;
import net.minestom.server.advancements.notifications.NotificationCenter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.*;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class KweebecParty {

    public static void main(String[] args) {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setChunkGenerator(new GeneratorDemo());

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            player.setGameMode(GameMode.SURVIVAL);

            System.out.println(player.getUsername() + " joined.");
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));

            /* Set Players Skin */
            player.setSkin(PlayerSkin.fromUsername(player.getUsername()));
        });

        Notification notification = new Notification(
                Component.text("Skin Changed", NamedTextColor.GREEN),
                FrameType.GOAL,
                ItemStack.of(Material.GOLD_INGOT)
        );

        globalEventHandler.addListener(PlayerBlockInteractEvent.class, event -> {
            final Player player = event.getPlayer();
            if(event.getHand() == Player.Hand.MAIN) {
                player.sendMessage("Interaction found!");

                NotificationCenter.send(notification, player);

                player.setSkin(new PlayerSkin("eyJ0aW1lc3RhbXAiOjE1NTM1MDgyODMwOTAsInByb2ZpbGVJZCI6IjU2Njc1YjIyMzJmMDRlZTA4OTE3OWU5YzkyMDZjZmU4IiwicHJvZmlsZU5hbWUiOiJUaGVJbmRyYSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmJlNjZiZDE0OTViZmNhMjhhZjEzZDJhZDY4Yjk3MWU4YTUwNmI0MjRjZWQ0N2E0ZTdiYWVmODA5ZjU4MjMwNSIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19",
                        "xsDdtsBEAQsq2LcYojHAsq7h1C+obUHqwX7xS2YZH/GanYRXTEdnHma2mAlrSB5vAjE1AynItHBoLvW2vfnBw8Z9Avk1MwsoQBxYaXd9L/1x/W/KnqEtaHkSQCpq0JPBn2yqKNW8yZQBM0ZfXu1PsCzsNzksAwCR56o+MIir0rauT2ne/Sugfkl0qE2GAynbj6B01qKuWqSJdAzrety4Xe62Yh0ZK4v2crnalwk0tCynKmO+8tP2gnjyGkXOdBkDmFZpm7MBjnCGRFXMO4hN6UiPQ4hZtb6SX6Wlmgl8WEp/g/er3zR+QKTF/509Cl4v3cFF5vb6rhWYYltR2UWGMjB92Apizc1Im5EbCyOmA7V8d/7a7I5GMjVjcxBXzLVvCsskwtpL5tOroZXBJxVzVEtgiRcbq0gMuqba3zWyDunXSHQRnawUvR7bmA9DJ82qynj0SvnAsEH+/q4oEQkbqiq+lpI4SBuvT36tAB020ZVE1WO1vhwiqEsN2b9fyaBp2iHfiv3SCc5NQ7Xd6QF4Shu6L9+Pi143Om5FofoLSBnes7uoJA2kpHzZEPH0UTgcoA6CNDYKQt7nD+MYdUWIdV0QuleK7rdVTNLgb7LxuvdwhceNBVRim568VqQyxMwi/3G5iyzVG7Oa6n8kRbqufPOJrPM8q71QBZH3QOeIiWc="));

                Inventory inventory = new Inventory(InventoryType.CHEST_1_ROW, "The inventory name");
                player.openInventory(inventory);
                player.playSound(Sound.sound(Key.key("chest_open"), Sound.Source.BLOCK, 1.0f, 1.0f));
            }
        });

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", 25565);
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
