package me.hugo.thankmaslobby.player;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.settings.OptionManager;
import me.hugo.thankmaslobby.settings.option.Option;
import me.hugo.thankmaslobby.settings.option.OptionState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.scoreboard.Sidebar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GamePlayer {

    Player player;
    PlayerSkin playerSkin;
    Sidebar scoreboard;

    HashMap<Option, OptionState> optionState = new HashMap<>();
    Inventory settingsMenu;

    public GamePlayer(Player player) {
        this.player = player;
        this.playerSkin = player.getSkin();

        initOptions();
    }

    private void initOptions() {
        OptionManager optionManager = ThankmasLobby.getInstance().getOptionManager();

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

            if(i == 39) {
                player1.closeInventory();
                return;
            }

            for (Option option : optionManager.getList()) {
                if (option.getSlot() == i || option.getSlot() + 9 == i) {
                    OptionState newOptionState = option.getNextState(this.optionState.get(option));
                    this.optionState.put(option, newOptionState);
                    option.run(this, newOptionState, true);

                    settingsMenu.setItemStack(option.getSlot(), newOptionState.getMenuIcon());
                    settingsMenu.setItemStack(option.getSlot() + 9, newOptionState.getToggleMenuIcon());
                }
            }
        });
    }

    public void loadSidebar() {
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        scoreboard = new Sidebar(Component.text("THANKMAS").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
        scoreboard.createLine(new Sidebar.ScoreboardLine("date", Component.text(dateFormat.format(new Date())).color(NamedTextColor.GRAY), 12));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space1", Component.text(""), 11));
        scoreboard.createLine(new Sidebar.ScoreboardLine("rank", Component.text("Rank: ").color(NamedTextColor.WHITE)
                .append(Component.text("Donator").color(NamedTextColor.YELLOW)), 10));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space2", Component.text("", NamedTextColor.AQUA), 9));
        scoreboard.createLine(new Sidebar.ScoreboardLine("lobbyCounter", Component.text("Lobby: ").color(NamedTextColor.WHITE)
                .append(Component.text("#1").color(NamedTextColor.GREEN)), 8));
        scoreboard.createLine(new Sidebar.ScoreboardLine("lobbyPlayerCounter", Component.text("Players: ").color(NamedTextColor.WHITE)
                .append(Component.text("0").color(NamedTextColor.GREEN)), 7));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space3", Component.text("", NamedTextColor.RED), 6));
        scoreboard.createLine(new Sidebar.ScoreboardLine("playerCounter", Component.text("Global Players: ").color(NamedTextColor.WHITE)
                .append(Component.text("0").color(NamedTextColor.GREEN)), 5));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space4", Component.text(" ", NamedTextColor.LIGHT_PURPLE), 4));
        scoreboard.createLine(new Sidebar.ScoreboardLine("easterEggCounter", Component.text("Easter Eggs: ").color(NamedTextColor.WHITE)
                .append(Component.text("0/15").color(NamedTextColor.GREEN)), 3));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space5", Component.text(" ", NamedTextColor.YELLOW), 2));
        scoreboard.createLine(new Sidebar.ScoreboardLine("ip", Component.text("events.thoriumcu.be").color(NamedTextColor.YELLOW), 1));

        scoreboard.addViewer(player);
    }

    public Inventory getSettingsMenu() {
        return settingsMenu;
    }

    private void resetPlayerSkin() {
        if (playerSkin != null) player.setSkin(playerSkin);
        /* TODO: else custom default player skin? */
    }

    public Player getPlayer() {
        return player;
    }
}
