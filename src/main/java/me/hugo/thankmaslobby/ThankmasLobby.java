package me.hugo.thankmaslobby;

import me.hugo.thankmaslobby.cosmetics.menus.CosmeticsMenu;
import me.hugo.thankmaslobby.events.*;
import me.hugo.thankmaslobby.games.GameSelectorMenu;
import me.hugo.thankmaslobby.item.HotBarItem;
import me.hugo.thankmaslobby.player.GamePlayer;
import me.hugo.thankmaslobby.player.PlayerManager;
import me.hugo.thankmaslobby.settings.OptionManager;
import net.kyori.adventure.inventory.Book;
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
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryCondition;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ThankmasLobby {

    private static ThankmasLobby main;
    private PlayerManager playerManager;
    private Book welcomeBook;

    private GameSelectorMenu gameSelectorMenu;
    private CosmeticsMenu cosmeticsMenu;

    private OptionManager optionManager;

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        main = new ThankmasLobby();
        MojangAuth.init();

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        main.initManagers();
        main.welcomeBook = Book.builder().addPage(Component.text("Welcome to Thankmas 2022!").decorate(TextDecoration.BOLD).decorate(TextDecoration.UNDERLINED)
                        .append(Component.text("\n\nLet's play together and have fun while fighting for a great cause!").decoration(TextDecoration.BOLD, false).decoration(TextDecoration.UNDERLINED, false)
                                .append(Component.text("\n\nMore information about the server on the next pages!\n\n\nKweebec Party →"))
                        )
                ).addPage(Component.text("KWEEBEC PARTY")
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.UNDERLINED)
                        .append(Component.text("\n\nPlay quick Hytale-themed minigames and get the most points to win!").decoration(TextDecoration.BOLD, false).decoration(TextDecoration.UNDERLINED, false)))
                .author(Component.text("Thankmas 2022")).build();

        getInstance().gameSelectorMenu = new GameSelectorMenu();
        getInstance().cosmeticsMenu = new CosmeticsMenu();

        /*
        Load all the listeners
         */
        new PlayerInteraction(globalEventHandler);
        new PlayerJoin(globalEventHandler);
        new BlockEvents(globalEventHandler);
        new PlayerLeave(globalEventHandler);
        new PlayerSwitchHands(globalEventHandler);

        minecraftServer.start("0.0.0.0", 25565);
    }

    private void initManagers() {
        playerManager = new PlayerManager();
        optionManager = new OptionManager();
    }

    public Book getWelcomeBook() {
        return welcomeBook;
    }

    public GameSelectorMenu getGameSelectorMenu() {
        return gameSelectorMenu;
    }

    public CosmeticsMenu getCosmeticsMenu() {
        return cosmeticsMenu;
    }

    public static ThankmasLobby getInstance() {
        return main;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public OptionManager getOptionManager() {
        return optionManager;
    }
}
