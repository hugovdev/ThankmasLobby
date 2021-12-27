package me.hugo.thankmaslobby.player;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.settings.option.Option;
import me.hugo.thankmaslobby.settings.option.OptionState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.scoreboard.Sidebar;

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

        for (Option option : ThankmasLobby.getInstance().getOptionManager().getList())
            optionState.put(option, option.getDefaultState());
    }

    public void loadSidebar() {
        scoreboard = new Sidebar(Component.text("THANKMAS").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
        scoreboard.createLine(new Sidebar.ScoreboardLine("date", Component.text("12/26/2021").color(NamedTextColor.GRAY), 12));
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

    private void resetPlayerSkin() {
        if(playerSkin != null) player.setSkin(playerSkin);
        /* TODO: else custom default player skin? */
    }

    public Player getPlayer() {
        return player;
    }
}
