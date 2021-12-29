package me.hugo.thankmaslobby.lobbynpc;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.entities.TextNPC;
import me.hugo.thankmaslobby.player.GamePlayer;
import me.hugo.thankmaslobby.entities.NPC;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.TriState;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.instance.Instance;

import java.util.function.Consumer;

public enum EasterEggNPC {

    KWEEBEC_CORNER(0, "Kweebec Corner", PlayerSkin.fromUsername("KweebecCorner"), new Pos(-6.5, 40, 14.5, -90, 0), "Stay safe and keep free!"),
    EFS3(1, "EFS3", PlayerSkin.fromUsername("EFS3"), new Pos(11, 40, 13.5, 140, 0), "ඞ"),
    BUDDHACAT(2, "BuddhaCat", PlayerSkin.fromUsername("BuddhaCat"), new Pos(6.5, 40, 23.5, 160, 0), "Remember to ask me what phrase I want for my NPC!");

    private final int id;
    private final String name;
    private final PlayerSkin npcSkin;
    private Pos npcPosition;
    private String[] dialogue;

    private final TextNPC npc;

    EasterEggNPC(int id, String name, PlayerSkin npcSkin, Pos npcPosition, String... dialogue) {
        this.id = id;
        this.name = name;
        this.npcSkin = npcSkin;
        this.dialogue = dialogue;
        this.npcPosition = npcPosition;

        Instance instance = MinecraftServer.getInstanceManager().getInstances().iterator().next();

        this.npc = new TextNPC(instance, this.npcPosition, this.npcSkin, TriState.TRUE, getNPCInteraction(), Component.text(this.name, NamedTextColor.AQUA),
                Component.text("CLICK", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));
    }

    private Consumer<NPC.NPCInteraction> getNPCInteraction() {
        return npcInteraction -> {
            Player player = npcInteraction.player();
            GamePlayer gamePlayer = ThankmasLobby.getInstance().getPlayerManager().getPlayerData(player);
            player.playSound(Sound.sound(Key.key("minecraft:entity.villager.yes"), Sound.Source.VOICE, 1.0f, 1.0f));

            if (!gamePlayer.getUnlockedNPCs().contains(this)) {
                gamePlayer.getUnlockedNPCs().add(this);
                gamePlayer.updateEasterEggCounter();

                player.playSound(Sound.sound(Key.key("minecraft:entity.player.levelup"), Sound.Source.AMBIENT, 1.0f, 1.0f));

                String unlockedNPCs = gamePlayer.getUnlockedNPCs().size() + "/" + EasterEggNPC.values().length;

                Component hoverMessage = Component.text("NPC Quest", NamedTextColor.GREEN).append(Component.newline())
                        .append(Component.text("Your Journey", NamedTextColor.GRAY).append(Component.newline()).append(Component.newline())
                                .append(Component.text("Your NPCs: ", NamedTextColor.WHITE))
                                .append(Component.text(unlockedNPCs, NamedTextColor.GRAY))
                                .append(Component.newline()).append(Component.newline())
                                .append(Component.text("Click to see more!", NamedTextColor.YELLOW)));

                player.sendMessage(Component.text("NEW NPC! ", NamedTextColor.YELLOW, TextDecoration.BOLD)
                        .append(Component.text("You unlocked ", NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false))
                        .append(Component.text(this.name + "'s ", NamedTextColor.AQUA).decoration(TextDecoration.BOLD, false))
                        .append(Component.text("NPC! ", NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false))
                        .append(Component.text("CLICK", NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true).hoverEvent(hoverMessage))
                        .append(Component.text(" (" + unlockedNPCs + ")", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false)));
            }

            player.sendMessage(Component.text("[NPC] " + this.name, NamedTextColor.GOLD).append(Component.text(": " + this.dialogue[0], NamedTextColor.WHITE)));
        };
    }

    public Pos getPosition() {
        return npcPosition;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PlayerSkin getNpcSkin() {
        return npcSkin;
    }

    public String[] getDialogue() {
        return dialogue;
    }
}
