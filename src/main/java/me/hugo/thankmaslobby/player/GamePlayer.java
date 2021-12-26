package me.hugo.thankmaslobby.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.scoreboard.Sidebar;

public class GamePlayer {

    Player player;
    PlayerSkin playerSkin;
    Sidebar scoreboard;

    public GamePlayer(Player player) {
        this.player = player;
        this.playerSkin = player.getSkin();
    }

    public void loadSidebar() {
        scoreboard = new Sidebar(Component.text("THANKMAS 2022").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
        scoreboard.createLine(new Sidebar.ScoreboardLine("date", Component.text("12/26/2021").color(NamedTextColor.GRAY), 4));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space1", Component.text(""), 3));
        scoreboard.createLine(new Sidebar.ScoreboardLine("playerCounter", Component.text("Players: ").color(NamedTextColor.WHITE)
                .append(Component.text("0").color(NamedTextColor.GREEN)), 2));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space2", Component.text(" "), 1));
        scoreboard.createLine(new Sidebar.ScoreboardLine("ip", Component.text("mc.hugo.net").color(NamedTextColor.YELLOW), 0));

        scoreboard.addViewer(player);
    }

    private void resetPlayerSkin() {
        if(playerSkin != null) player.setSkin(playerSkin);
        /* TODO: else custom default player skin? */
    }

}
