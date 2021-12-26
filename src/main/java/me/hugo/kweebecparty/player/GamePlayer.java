package me.hugo.kweebecparty.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.scoreboard.Sidebar;

import javax.naming.Name;

public class GamePlayer {

    Player player;
    PlayerSkin playerSkin;
    Sidebar scoreboard;

    public GamePlayer(Player player) {
        this.player = player;
        this.playerSkin = player.getSkin();

        scoreboard = new Sidebar(Component.text("KWEEBEC PARTY").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
        scoreboard.createLine(new Sidebar.ScoreboardLine("date", Component.text("12/26/2021").color(NamedTextColor.GRAY), 4));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space1", Component.text(""), 3));
        scoreboard.createLine(new Sidebar.ScoreboardLine("playerCounter", Component.text("Players: ").append(Component.text("0")).color(NamedTextColor.GREEN), 2));
        scoreboard.createLine(new Sidebar.ScoreboardLine("space2", Component.text(" "), 1));
        scoreboard.createLine(new Sidebar.ScoreboardLine("ip", Component.text("mc.hugo.net").color(NamedTextColor.YELLOW), 0));

        scoreboard.updateLineContent("playerCounter", Component.text("Players: ").append(Component.text("1")).color(NamedTextColor.GREEN));

        scoreboard.addViewer(player);
    }

    private void resetPlayerSkin() {
        if(playerSkin != null) player.setSkin(playerSkin);
        /* TODO: else custom default player skin? */
    }

}
