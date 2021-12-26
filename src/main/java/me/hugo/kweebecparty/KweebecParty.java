package me.hugo.kweebecparty;

import me.hugo.kweebecparty.player.GamePlayer;
import me.hugo.kweebecparty.player.PlayerManager;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.*;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.scoreboard.BelowNameTag;
import net.minestom.server.utils.mojang.MojangUtils;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class KweebecParty {

    private static KweebecParty main;
    private PlayerManager playerManager;
    private Book welcomeBook;

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        main = new KweebecParty();
        MojangAuth.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setChunkGenerator(new GeneratorDemo());

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        main.init();
        main.welcomeBook = Book.builder().addPage(Component.text("Welcome to Thankmas 2022!").decorate(TextDecoration.BOLD).decorate(TextDecoration.UNDERLINED)
                        .append(Component.text("\n\nLet's play together and have fun while fighting for a great cause!").decoration(TextDecoration.BOLD, false).decoration(TextDecoration.UNDERLINED, false)
                                .append(Component.text("\n\nMore information about the server on the next pages!\n\n\nKweebec Party →"))
                        )
                ).addPage(Component.text("KWEEBEC PARTY")
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.UNDERLINED)
                        .append(Component.text("\n\nPlay quick Hytale-themed minigames and get the most points to win!").decoration(TextDecoration.BOLD, false).decoration(TextDecoration.UNDERLINED, false)))
                .author(Component.text("Thankmas 2022")).build();

        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            player.setGameMode(GameMode.SURVIVAL);

            System.out.println(player.getUsername() + " joined.");
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));

            GamePlayer gamePlayer = main.getPlayerManager().getPlayerData(player);
        });

        Notification notification = new Notification(
                Component.text("Log into ", NamedTextColor.WHITE).append(Component.text("Hytale Thankmas 2022").color(NamedTextColor.AQUA)),
                FrameType.TASK,
                ItemStack.of(Material.GOLD_NUGGET)
        );

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            NotificationCenter.send(notification, player);
            getInstance().getPlayerManager().getPlayerData(player).loadSidebar();
            player.openBook(main.welcomeBook);

            player.getInventory().setItemStack(0, ItemStack.of(Material.COMPASS).withDisplayName(Component.text("Game Selector").color(NamedTextColor.GREEN)
                    .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
            player.getInventory().setItemStack(1, ItemStack.of(Material.CHEST).withDisplayName(Component.text("Cosmetics").color(NamedTextColor.GREEN)
                    .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
            player.getInventory().setItemStack(2, ItemStack.of(Material.WRITTEN_BOOK).withDisplayName(Component.text("Information").color(NamedTextColor.GREEN)
                    .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
            player.getInventory().setItemStack(4, ItemStack.of(Material.ENDER_PEARL).withDisplayName(Component.text("Ride Pearl").color(NamedTextColor.GREEN)
                    .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
            player.getInventory().setItemStack(7, ItemStack.of(Material.COMPARATOR).withDisplayName(Component.text("Lobby Settings").color(NamedTextColor.GREEN)
                    .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
            player.getInventory().setItemStack(8, ItemStack.of(Material.NETHER_STAR).withDisplayName(Component.text("Lobby Selector").color(NamedTextColor.GREEN)
                    .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
        });

        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            final Player player = event.getPlayer();
            getInstance().getPlayerManager().removePlayerData(player);
        });

        globalEventHandler.addListener(PlayerBlockInteractEvent.class, event -> {
            final Player player = event.getPlayer();
            if (event.getHand() == Player.Hand.MAIN) {
                player.setSkin(new PlayerSkin("eyJ0aW1lc3RhbXAiOjE1NTM1MDgyODMwOTAsInByb2ZpbGVJZCI6IjU2Njc1YjIyMzJmMDRlZTA4OTE3OWU5YzkyMDZjZmU4IiwicHJvZmlsZU5hbWUiOiJUaGVJbmRyYSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmJlNjZiZDE0OTViZmNhMjhhZjEzZDJhZDY4Yjk3MWU4YTUwNmI0MjRjZWQ0N2E0ZTdiYWVmODA5ZjU4MjMwNSIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19",
                        "xsDdtsBEAQsq2LcYojHAsq7h1C+obUHqwX7xS2YZH/GanYRXTEdnHma2mAlrSB5vAjE1AynItHBoLvW2vfnBw8Z9Avk1MwsoQBxYaXd9L/1x/W/KnqEtaHkSQCpq0JPBn2yqKNW8yZQBM0ZfXu1PsCzsNzksAwCR56o+MIir0rauT2ne/Sugfkl0qE2GAynbj6B01qKuWqSJdAzrety4Xe62Yh0ZK4v2crnalwk0tCynKmO+8tP2gnjyGkXOdBkDmFZpm7MBjnCGRFXMO4hN6UiPQ4hZtb6SX6Wlmgl8WEp/g/er3zR+QKTF/509Cl4v3cFF5vb6rhWYYltR2UWGMjB92Apizc1Im5EbCyOmA7V8d/7a7I5GMjVjcxBXzLVvCsskwtpL5tOroZXBJxVzVEtgiRcbq0gMuqba3zWyDunXSHQRnawUvR7bmA9DJ82qynj0SvnAsEH+/q4oEQkbqiq+lpI4SBuvT36tAB020ZVE1WO1vhwiqEsN2b9fyaBp2iHfiv3SCc5NQ7Xd6QF4Shu6L9+Pi143Om5FofoLSBnes7uoJA2kpHzZEPH0UTgcoA6CNDYKQt7nD+MYdUWIdV0QuleK7rdVTNLgb7LxuvdwhceNBVRim568VqQyxMwi/3G5iyzVG7Oa6n8kRbqufPOJrPM8q71QBZH3QOeIiWc="));
            }
        });

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", 25565);
    }

    private void init() {
        playerManager = new PlayerManager();
    }

    public static KweebecParty getInstance() {
        return main;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
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
