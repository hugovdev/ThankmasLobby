package me.hugo.thankmaslobby;

import me.hugo.thankmaslobby.cosmetics.menus.CosmeticsMenu;
import me.hugo.thankmaslobby.events.*;
import me.hugo.thankmaslobby.games.GameSelectorMenu;
import me.hugo.thankmaslobby.lobbynpc.EasterEggNPC;
import me.hugo.thankmaslobby.lobbynpc.ServerJoinNPC;
import me.hugo.thankmaslobby.player.PlayerManager;
import me.hugo.thankmaslobby.settings.OptionManager;
import me.hugo.thankmaslobby.world.EmptyGenerator;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.monitoring.BenchmarkManager;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.world.DimensionType;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

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
        main.initManagers();
        main.initMainWorld();

        main.welcomeBook = Book.builder().addPage(Component.text("Welcome to Thankmas 2022!").decorate(TextDecoration.BOLD)
                        .decorate(TextDecoration.UNDERLINED)
                        .append(Component.text("\n\nLet's play together and have fun while fighting for a great cause!")
                                .decoration(TextDecoration.BOLD, false).decoration(TextDecoration.UNDERLINED, false)
                                .append(Component.text("\n\nMore information about the server on the next pages!\n\n\nKweebec Party →"))
                        )
                ).addPage(Component.text("KWEEBEC PARTY")
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.UNDERLINED)
                        .append(Component.text("\n\nPlay quick Hytale-themed minigames and get the most points to win!")
                                .decoration(TextDecoration.BOLD, false).decoration(TextDecoration.UNDERLINED, false)))
                .author(Component.text("Thankmas 2022")).build();

        getInstance().gameSelectorMenu = new GameSelectorMenu();
        getInstance().cosmeticsMenu = new CosmeticsMenu();

        /*
        Load all the listeners
         */
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        new PlayerInteraction(globalEventHandler);
        new PlayerJoin(globalEventHandler);
        new BlockEvents(globalEventHandler);
        new PlayerLeave(globalEventHandler);
        new PlayerSwitchHands(globalEventHandler);
        new EntityInteraction(globalEventHandler);

        minecraftServer.start("0.0.0.0", 25565);

        for (ServerJoinNPC lobbyNPC : ServerJoinNPC.values())
            System.out.println("[Server NPC] '" + lobbyNPC.getServerName() + "' has been registered!");

        for (EasterEggNPC easterEggNPC : EasterEggNPC.values())
            System.out.println("[EasterEgg NPC] '" + easterEggNPC.getName() + "' has been registered!");

        getInstance().startBenchmark();
    }

    private final NamespaceID DIMENSION_ID = NamespaceID.from("thankmas:lobby_world");
    public final DimensionType DIMENSION_TYPE = DimensionType.builder(DIMENSION_ID).ambientLight(1.0f).build();

    private void initMainWorld() {
        if (!MinecraftServer.getDimensionTypeManager().isRegistered(DIMENSION_ID))
            MinecraftServer.getDimensionTypeManager().addDimension(DIMENSION_TYPE);

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer(DIMENSION_TYPE);
        instanceContainer.setChunkGenerator(new EmptyGenerator());

        MinecraftServer.getInstanceManager().registerInstance(instanceContainer);
        instanceContainer.loadChunk(0, 0).join();
    }

    private void initManagers() {
        playerManager = new PlayerManager();
        optionManager = new OptionManager();
    }

    private void startBenchmark() {
        BenchmarkManager benchmarkManager = MinecraftServer.getBenchmarkManager();
        benchmarkManager.enable(Duration.ofMillis(Long.MAX_VALUE));

        AtomicReference<TickMonitor> lastTick = new AtomicReference<>();
        MinecraftServer.getUpdateManager().addTickMonitor(lastTick::set);

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            Collection<Player> players = MinecraftServer.getConnectionManager().getOnlinePlayers();
            if (players.isEmpty())
                return;

            long ramUsage = benchmarkManager.getUsedMemory();
            ramUsage /= 1e6; // bytes to MB

            TickMonitor tickMonitor = lastTick.get();
            final Component header = Component.text("RAM USAGE: " + ramUsage + " MB")
                    .append(Component.newline())
                    .append(Component.text("TICK TIME: " + MathUtils.round(tickMonitor.getTickTime(), 2) + "ms"));

            final Component footer = benchmarkManager.getCpuMonitoringMessage();
            Audiences.players().sendPlayerListHeaderAndFooter(header, footer);
        }).repeat(10, TimeUnit.SERVER_TICK).schedule();
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
