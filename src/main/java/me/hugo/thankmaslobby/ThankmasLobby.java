package me.hugo.thankmaslobby;

import me.hugo.thankmaslobby.commands.SecretMenuCommand;
import me.hugo.thankmaslobby.commands.StopCommand;
import me.hugo.thankmaslobby.commands.TestCommand;
import me.hugo.thankmaslobby.cosmetics.menus.CosmeticsMenu;
import me.hugo.thankmaslobby.entities.NPC;
import me.hugo.thankmaslobby.entities.TextNPC;
import me.hugo.thankmaslobby.events.*;
import me.hugo.thankmaslobby.games.GameSelectorMenu;
import me.hugo.thankmaslobby.lobbynpc.EasterEggNPC;
import me.hugo.thankmaslobby.lobbynpc.ServerJoinNPC;
import me.hugo.thankmaslobby.player.PlayerManager;
import me.hugo.thankmaslobby.secrets.SecretCategoryManager;
import me.hugo.thankmaslobby.settings.OptionManager;
import me.hugo.thankmaslobby.world.EmptyGenerator;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.TriState;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.command.CommandManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.Instance;
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
    private Instance mainInstance;

    private Book welcomeBook;

    private GameSelectorMenu gameSelectorMenu;
    private CosmeticsMenu cosmeticsMenu;

    private PlayerManager playerManager;
    private SecretCategoryManager secretCategoryManager;
    private OptionManager optionManager;

    private Pos spawnLocation;

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MinecraftServer.getConnectionManager().setShutdownText(Component.text("Thankmas lobby is restarting! Come back soon!", NamedTextColor.RED));
        main = new ThankmasLobby();

        MojangAuth.init();
        main.mainInstance = main.initMainWorld();
        main.initManagers();

        main.spawnLocation = new Pos(-0.5, 47, -14.5, 0, 0);

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
        new PlayerChat(globalEventHandler);

        /*
        Register all commands
         */
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new SecretMenuCommand());
        commandManager.register(new TestCommand());
        commandManager.register(new StopCommand());

        minecraftServer.start("0.0.0.0", 25565);

        for (ServerJoinNPC lobbyNPC : ServerJoinNPC.values())
            System.out.println("[Server NPC] '" + lobbyNPC.getServerName() + "' has been registered!");

        for (EasterEggNPC easterEggNPC : EasterEggNPC.values())
            System.out.println("[EasterEgg NPC] '" + easterEggNPC.getName() + "' has been registered!");

        new TextNPC(getInstance().getMainInstance(),
                new Pos(-3.5, 40, -5.5, -145, 0),
                new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTY0MDI3Mzk0OTk4MCwKICAicHJvZmlsZUlkIiA6ICJjMDNlZTUxNjIzZTU0ZThhODc1NGM1NmVhZmJjZDA4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJjMDNlZTUxNjIzZTU0ZThhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE3NzllODk2M2FjYTFjMmZiMzI1MmIyMjY0MGQ1OTYxMTEzYTBlODk5N2JkMDE2MjYwZWQwNWUyYTM3OWYwYjEiCiAgICB9CiAgfQp9", "aXCm+tnkfOD1bi4RP7XVfi01QB08M116uLvsKeU+TVIv5e4GIph851zycdjHfR1tX4hD4CRlR0hSQPldFxRHZegfVuhQ4JSsoUDSYRtjcm/4ZvLR7lMELf8tQ3ZWAU7Od6uyB9RwqDiXwt9nXCtiWKCkT6/ZA7pcbKS7Nl7SCXD5SSJ0jcQ3jEPocHkEy73EFLZgdElAAhu3clNE/xkh4xcYgU7y1xiHiXqZRmY5UxDl08Sf7MmbRIkwe+84m0Bi9vJNNceHTs9R1sgjohjpMXQYfBrlJjxDvziFUk6Dpnbo0s53DQq15ByrBPLjkEIRrcEelwyKuLKbLxCVSIQhlR18FEI5E4bat0AMO5WJmKw1Rw8ORaXU1zO3UNLCCAzl/rKtwCl8kgX9ChNx5jK0SgLKLy/489Y6X1h8gMlHxvR/XA2HWc3x0CAAKt6SxKem1lU5UJq0+cv7wJbg0p4FKY7a8OMQSbdY/udyoew9KLjkNBVkA442wE1gdTlbZKE7gIyNOnn86IiIg26IhEXiNZv43gPEfMBceY6RpU06wdHhLhMzt4IOvWbp5z31MnNy5408nGHkXe3k8G+Ljhnf9xN8MuBqQcOjBUhyI4my1PL5ImER+RztuRQgTxdSJ5rGkCAKtYdUUAqVJ111ukZMZl38JjFwRjiZbqhXXQT70d0="),
                TriState.TRUE,
                npcInteraction -> {
                    Player player = npcInteraction.player();

                    player.playSound(Sound.sound(Key.key("minecraft:entity.villager.yes"), Sound.Source.VOICE, 1.0f, 1.0f));
                    player.openBook(main.welcomeBook);
                }, Component.text("Thankmas Guide", NamedTextColor.RED), Component.text("CLICK", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));

        System.out.println("Guide NPC registered!");

        getInstance().startBenchmark();
    }

    private final NamespaceID DIMENSION_ID = NamespaceID.from("thankmas:lobby_world");
    public final DimensionType DIMENSION_TYPE = DimensionType.builder(DIMENSION_ID).ambientLight(1.0f).build();

    private Instance initMainWorld() {
        if (!MinecraftServer.getDimensionTypeManager().isRegistered(DIMENSION_ID))
            MinecraftServer.getDimensionTypeManager().addDimension(DIMENSION_TYPE);

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer(DIMENSION_TYPE);
        instanceContainer.setChunkGenerator(new EmptyGenerator());

        MinecraftServer.getInstanceManager().registerInstance(instanceContainer);
        instanceContainer.loadChunk(0, 0).join();

        return instanceContainer;
    }

    private void initManagers() {
        playerManager = new PlayerManager();
        optionManager = new OptionManager();
        secretCategoryManager = new SecretCategoryManager();
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

    public Instance getMainInstance() {
        return mainInstance;
    }

    public Pos getSpawnLocation() {
        return spawnLocation;
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

    public SecretCategoryManager getSecretCategoryManager() {
        return secretCategoryManager;
    }
}
