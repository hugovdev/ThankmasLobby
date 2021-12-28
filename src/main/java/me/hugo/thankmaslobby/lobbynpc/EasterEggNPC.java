package me.hugo.thankmaslobby.lobbynpc;

import me.hugo.thankmaslobby.ThankmasLobby;
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

public enum EasterEggNPC {

    KWEEBEC_CORNER(0, "Kweebec Corner", PlayerSkin.fromUsername("KweebecCorner"), new Pos(-6.5, 40, 14.5, -90, 0), "Stay safe and keep free!"),
    ;

    private final int id;
    private final String name;
    private final PlayerSkin npcSkin;
    private Pos npcPosition;
    private String[] dialogue;
    private Hologram[] holograms = new Hologram[5];

    private final NPC npc;

    EasterEggNPC(int id, String name, PlayerSkin npcSkin, Pos npcPosition, String... dialogue) {
        this.id = id;
        this.name = name;
        this.npcSkin = npcSkin;
        this.dialogue = dialogue;
        this.npcPosition = npcPosition;

        Instance instance = MinecraftServer.getInstanceManager().getInstances().iterator().next();

        this.npc = new NPC(instance, this.npcPosition, this.npcSkin, TriState.TRUE, npcInteraction -> {
            Player player = npcInteraction.player();
            GamePlayer gamePlayer = ThankmasLobby.getInstance().getPlayerManager().getPlayerData(player);

            if (gamePlayer.getUnlockedNPCs().contains(this)) {
                player.playSound(Sound.sound(Key.key("minecraft:entity.villager.no"), Sound.Source.VOICE, 1.0f, 1.0f));

                player.sendMessage(Component.text("You already talked to ", NamedTextColor.RED)
                        .append(Component.text(this.name, NamedTextColor.AQUA))
                        .append(Component.text("!", NamedTextColor.RED)));
            } else {
                player.playSound(Sound.sound(Key.key("minecraft:entity.villager.yes"), Sound.Source.VOICE, 1.0f, 1.0f));
                player.playSound(Sound.sound(Key.key("minecraft:entity.player.levelup"), Sound.Source.AMBIENT, 1.0f, 1.0f));

                Component hoverMessage = Component.text("NPC Quest", NamedTextColor.GREEN).append(Component.newline())
                        .append(Component.text(""));

                player.sendMessage(Component.text("NEW NPC! ", NamedTextColor.YELLOW, TextDecoration.BOLD)
                        .append(Component.text("You unlocked ", NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false))
                        .append(Component.text(this.name + "'s ", NamedTextColor.AQUA).decoration(TextDecoration.BOLD, false))
                        .append(Component.text("NPC! ", NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false))
                        .append(Component.text("HOVER", NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true).hoverEvent(hoverMessage)));

                player.sendMessage(Component.text("[NPC] " + this.name, NamedTextColor.GOLD).append(Component.text(": " + this.dialogue[0], NamedTextColor.WHITE)));

                gamePlayer.getUnlockedNPCs().add(this);
                gamePlayer.updateEasterEggCounter();
            }
        });
        spawnHolograms(instance);
    }

    private void spawnHolograms(Instance instance) {
        int count = 0;

        Component[] hologramLines = new Component[]{
                Component.text(this.name, NamedTextColor.AQUA),
                Component.text("CLICK", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)};

        for (int i = hologramLines.length - 1; i >= 0; i--) {
            Component component = hologramLines[i];
            Hologram hologram = new Hologram(instance, new Pos(this.npcPosition).add(0, 1.7 + (0.3 * count), 0), component);
            holograms[count] = hologram;

            count++;
        }
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
