package me.hugo.thankmaslobby.player.rank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.scoreboard.Team;

public enum Rank {

    USER("User", NamedTextColor.GRAY, 0, Component.text("[User] ", NamedTextColor.GRAY)),
    DONATOR("Donator", NamedTextColor.YELLOW, 1, Component.text("[Donator] ", NamedTextColor.YELLOW)),
    ADMIN("Admin", NamedTextColor.RED, 2, Component.text("[Admin] ", NamedTextColor.RED));

    private final String rankName;
    private final NamedTextColor rankColor;
    private final int value;
    private final Component prefix;

    private final Team team;

    Rank(String rankName, NamedTextColor rankColor, int value, Component prefix) {
        this.rankName = rankName;
        this.rankColor = rankColor;
        this.value = value;
        this.prefix = prefix;

        this.team = MinecraftServer.getTeamManager().createTeam(
                (rankName.toLowerCase() + "_team"),
                prefix,
                rankColor,
                Component.empty());
    }

    public Team getTeam() {
        return team;
    }

    public String getRankName() {
        return rankName;
    }

    public TextColor getRankColor() {
        return rankColor;
    }

    public int getValue() {
        return value;
    }

    public Component getPrefix() {
        return prefix;
    }
}
