package me.hugo.thankmaslobby.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;

public class TeamUtil {

    public static final Team NPC_TEAM;

    static {
        NPC_TEAM = MinecraftServer.getTeamManager().createTeam(
                "NPC-TEAM",
                Component.text("[NPC] ", NamedTextColor.DARK_GRAY),
                NamedTextColor.DARK_GRAY,
                Component.empty()
        );

        NPC_TEAM.setNameTagVisibility(TeamsPacket.NameTagVisibility.NEVER);
    }

}
