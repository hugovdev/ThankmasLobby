package me.hugo.thankmaslobby.player;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.lobbynpc.EasterEggNPC;
import me.hugo.thankmaslobby.player.rank.Rank;
import me.hugo.thankmaslobby.secrets.SecretCategory;
import me.hugo.thankmaslobby.secrets.SecretCategoryManager;
import me.hugo.thankmaslobby.settings.OptionManager;
import me.hugo.thankmaslobby.settings.option.Option;
import me.hugo.thankmaslobby.settings.option.OptionState;
import me.hugo.thankmaslobby.util.PaginatedGUI;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.scoreboard.Sidebar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GamePlayer {

    ThankmasLobby main;
    Player player;

    PlayerSkin playerSkin;
    Sidebar scoreboard;
    Rank rank;

    HashMap<Option, OptionState> optionState = new HashMap<>();
    Inventory settingsMenu;

    PaginatedGUI secretsMenu;
    List<EasterEggNPC> unlockedNPCs = new ArrayList<>();
    PaginatedGUI unlockedNPCMenu;

    public GamePlayer(Player player, ThankmasLobby main) {
        this.main = main;
        this.player = player;
        this.playerSkin = player.getSkin();

        // Initialize the options menu loading their preferences.
        initOptions();

        // Initialize the secrets menu loading their unlocked and locked secrets.
        initSecretsMenu();
        initUnlockedSecrets();

        /* Get from local storage */
        this.rank = Rank.USER;
    }

    private void initOptions() {
        OptionManager optionManager = main.getOptionManager();

        for (Option option : optionManager.getList()) optionState.put(option, option.getDefaultState());

        settingsMenu = new Inventory(InventoryType.CHEST_5_ROW, "Lobby Options");

        for (Option option : optionManager.getList()) {
            OptionState currentState = this.optionState.get(option);
            settingsMenu.setItemStack(option.getSlot(), currentState.getMenuIcon());
            settingsMenu.setItemStack(option.getSlot() + 9, currentState.getToggleMenuIcon());
        }

        settingsMenu.setItemStack(39, ItemStack.of(Material.ACACIA_DOOR)
                .withDisplayName(Component.text("Close Menu", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));

        settingsMenu.setItemStack(40, ItemStack.of(Material.COMPARATOR)
                .withDisplayName(Component.text("Lobby Options", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));

        settingsMenu.addInventoryCondition((player1, i, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);

            if (i == 39) {
                player1.playSound(Sound.sound(Key.key("minecraft:block.wooden_button.click_on"), Sound.Source.AMBIENT, 1.0f, 1.0f));
                player1.closeInventory();
                return;
            }

            for (Option option : optionManager.getList()) {
                if (option.getSlot() == i || option.getSlot() + 9 == i) {
                    OptionState newOptionState = option.getNextState(this.optionState.get(option));

                    if (option.run(this, newOptionState, true)) {
                        this.optionState.put(option, newOptionState);

                        settingsMenu.setItemStack(option.getSlot(), newOptionState.getMenuIcon());
                        settingsMenu.setItemStack(option.getSlot() + 9, newOptionState.getToggleMenuIcon());
                    }

                    player.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), Sound.Source.AMBIENT, 1.0f, 1.3f));
                    break;
                }
            }
        });
    }

    private void initSecretsMenu() {
        secretsMenu = new PaginatedGUI(InventoryType.CHEST_4_ROW, ItemStack.of(Material.TURTLE_EGG)
                .withDisplayName(Component.text("Secrets", NamedTextColor.GREEN)), "Secrets", null, null);

        SecretCategoryManager secretCategoryManager = main.getSecretCategoryManager();

        for (SecretCategory secretCategory : secretCategoryManager.getSecretCategorySlots().values()) {
            secretsMenu.setItem(secretCategory.getMenuIcon(this), 0, secretCategoryManager.getSecretCategorySlots().inverse().get(secretCategory));
        }

        secretsMenu.addInventoryCondition((player, i, clickType, inventoryConditionResult) -> {
            SecretCategory secretCategory = secretCategoryManager.getSecretCategorySlots().get(i);

            if (secretCategory != null) secretCategory.getSecretChecker().openMenu(this);
        });
    }

    private void initUnlockedSecrets() {
        unlockedNPCMenu = new PaginatedGUI(InventoryType.CHEST_4_ROW, ItemStack.of(Material.IRON_HELMET).withDisplayName(Component.text("Secret NPCs", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)),
                "Secret NPCs", PaginatedGUI.PageFormat.TWO_ROWS, secretsMenu.getPages().get(0));

        for (EasterEggNPC easterEggNPC : EasterEggNPC.values())
            unlockedNPCMenu.addItem(unlockedNPCs.contains(easterEggNPC) ? easterEggNPC.getUnlockedState() : easterEggNPC.getLockedState());

        unlockedNPCMenu.addInventoryCondition((playerWhoClicked, i, clickType, inventoryConditionResult) -> {
            if (inventoryConditionResult.getClickedItem().getMaterial() != Material.PLAYER_HEAD) return;

            for (EasterEggNPC easterEggNPC : EasterEggNPC.values()) {
                if (inventoryConditionResult.getClickedItem() == easterEggNPC.getUnlockedState()) {
                    playerWhoClicked.closeInventory();

                    playerWhoClicked.teleport(easterEggNPC.getPosition());
                    playerWhoClicked.sendMessage(Component.text("You have been teleported to ", NamedTextColor.YELLOW)
                            .append(Component.text(easterEggNPC.getName() + "'s ", NamedTextColor.AQUA))
                            .append(Component.text("NPC!")));

                    player.playSound(Sound.sound(Key.key("minecraft:entity.enderman.teleport"), Sound.Source.AMBIENT, 1.0f, 1.0f));
                    return;
                }
            }

            playerWhoClicked.sendMessage(Component.text("You have not unlocked this secret NPC!", NamedTextColor.RED));
        });
    }

    public void loadSidebar() {
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        int onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().size();

        scoreboard = new Sidebar(Component.text("THANKMAS").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));

        scoreboard.createLine(new Sidebar.ScoreboardLine("date", Component.text(dateFormat.format(new Date())).color(NamedTextColor.GRAY), 12));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space1", Component.text(""), 11));
        scoreboard.createLine(new Sidebar.ScoreboardLine("rank", Component.text("Rank: ").color(NamedTextColor.WHITE)
                .append(Component.text(this.rank.getRankName()).color(this.rank.getRankColor())), 10));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space2", Component.text("", NamedTextColor.AQUA), 9));
        scoreboard.createLine(new Sidebar.ScoreboardLine("lobbyCounter", Component.text("Lobby: ").color(NamedTextColor.WHITE)
                .append(Component.text("#1").color(NamedTextColor.GREEN)), 8));
        scoreboard.createLine(new Sidebar.ScoreboardLine("lobbyPlayerCounter", Component.text("Players: ").color(NamedTextColor.WHITE)
                .append(Component.text(onlinePlayers).color(NamedTextColor.GREEN)), 7));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space3", Component.text("", NamedTextColor.RED), 6));
        scoreboard.createLine(new Sidebar.ScoreboardLine("playerCounter", Component.text("Global Players: ").color(NamedTextColor.WHITE)
                .append(Component.text("0").color(NamedTextColor.GREEN)), 5));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space4", Component.text(" ", NamedTextColor.LIGHT_PURPLE), 4));
        scoreboard.createLine(new Sidebar.ScoreboardLine("secretCounter", Component.text("Secrets Found: ").color(NamedTextColor.WHITE)
                .append(Component.text(this.unlockedNPCs.size() + "/" + EasterEggNPC.values().length).color(NamedTextColor.GREEN)), 3));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space5", Component.text(" ", NamedTextColor.YELLOW), 2));
        scoreboard.createLine(new Sidebar.ScoreboardLine("ip", Component.text("events.thoriumcu.be").color(NamedTextColor.YELLOW), 1));

        scoreboard.addViewer(player);

        this.rank.getTeam().addMember(player.getUsername());
        this.player.setTeam(this.rank.getTeam());
    }

    public OptionState getState(Option option) {
        return optionState.get(option);
    }

    public void updateSecretCounter() {
        scoreboard.updateLineContent("secretCounter", Component.text("Secrets Found: ").color(NamedTextColor.WHITE)
                .append(Component.text(this.unlockedNPCs.size() + "/" + EasterEggNPC.values().length).color(NamedTextColor.GREEN)));
    }

    public void updateCategory(SecretCategory secretCategory) {
        secretsMenu.setItem(secretCategory.getMenuIcon(this), 0, main.getSecretCategoryManager().getSecretCategorySlots().inverse().get(secretCategory));
    }

    public PaginatedGUI getUnlockedNPCMenu() {
        return unlockedNPCMenu;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isDonator(String verb, String perk) {
        if (this.rank.getValue() > 0) {
            return true;
        } else {
            if (perk != null) sendDonatorMessage(verb, perk);
            return false;
        }
    }

    public void sendDonatorMessage(String verb, String perk) {
        this.player.sendMessage(Component.text("You need to be ", NamedTextColor.RED)
                .append(Component.text("Donator", NamedTextColor.YELLOW))
                .append(Component.text(" " + verb + " ", NamedTextColor.RED))
                .append(Component.text(perk, NamedTextColor.AQUA))
                .append(Component.text(", please donate on the ", NamedTextColor.RED))
                .append(Component.text("EVENT PAGE", NamedTextColor.GREEN).decorate(TextDecoration.BOLD)
                        .hoverEvent(Component.text("Open Site", NamedTextColor.GREEN)
                                .append(Component.newline())
                                .append(Component.newline())
                                .append(Component.text("Donation site to get", NamedTextColor.GRAY))
                                .append(Component.newline())
                                .append(Component.text("all the perks.", NamedTextColor.GRAY))
                                .append(Component.newline())
                                .append(Component.newline())
                                .append(Component.text("Click to open!", NamedTextColor.GOLD)))
                        .clickEvent(ClickEvent.openUrl("https://www.donationsite.com")))
                .append(Component.text(" to get all the perks!", NamedTextColor.RED).decoration(TextDecoration.BOLD, false)));
    }

    public PaginatedGUI getSecretsMenu() {
        return secretsMenu;
    }

    public List<EasterEggNPC> getUnlockedNPCs() {
        return unlockedNPCs;
    }

    public Sidebar getScoreboard() {
        return scoreboard;
    }

    public Inventory getSettingsMenu() {
        return settingsMenu;
    }

    private void resetPlayerSkin() {
        if (playerSkin != null) player.setSkin(playerSkin);
    }

    public Player getPlayer() {
        return player;
    }
}
