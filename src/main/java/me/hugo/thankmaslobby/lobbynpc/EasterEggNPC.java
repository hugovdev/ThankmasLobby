package me.hugo.thankmaslobby.lobbynpc;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.entities.TextNPC;
import me.hugo.thankmaslobby.player.GamePlayer;
import me.hugo.thankmaslobby.entities.NPC;
import me.hugo.thankmaslobby.util.SkinUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.TriState;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.goal.DoNothingGoal;
import net.minestom.server.entity.ai.goal.RandomLookAroundGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public enum EasterEggNPC {

    KWEEBEC_CORNER(0, "Kweebec Corner", PlayerSkin.fromUsername("KweebecCorner"), new Pos(-6.5, 40, 14.5, -90, 0), "Stay safe and keep free!"),
    EFS3(1, "EFS3", PlayerSkin.fromUsername("EFS3"), new Pos(11, 40, 13.5, 140, 0), "ඞ"),
    BUDDHACAT(2, "BuddhaCat", PlayerSkin.fromUsername("BuddhaCat"), new Pos(6.5, 40, 23.5, 160, 0), "Remember to ask me what phrase I want for my NPC!"),
    CANADIAN_FLASH(3, "CanadianFlash", PlayerSkin.fromUsername("CanadianFlash"), new Pos(10.5, 40, 16.5, 160, 0), "I'm CanadianFlash and I'm always late!"),
    PROPZIE(4, "Propzie", PlayerSkin.fromUsername("Propzie"), new Pos(-5.5, 40, 32.5, 160, 0), "Propzie!");

    private final int id;
    private final String name;
    private final PlayerSkin npcSkin;
    private Pos npcPosition;
    private String[] dialogue;

    private final TextNPC npc;

    private ItemStack lockedState, unlockedState;

    EasterEggNPC(int id, String name, PlayerSkin npcSkin, Pos npcPosition, String... dialogue) {
        this.id = id;
        this.name = name;
        this.npcSkin = npcSkin;
        this.dialogue = dialogue;
        this.npcPosition = npcPosition;

        Instance instance = MinecraftServer.getInstanceManager().getInstances().iterator().next();

        // We create a TextNPC on the specified position, with the specified skin and properties.
        this.npc = new TextNPC(instance, this.npcPosition, this.npcSkin, TriState.FALSE, getNPCInteraction(), Component.text(this.name, NamedTextColor.AQUA),
                Component.text("CLICK", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));

        // We create the icons for both the locked and unlocked npc for menus.
        buildMenuIcons();
    }

    private Consumer<NPC.NPCInteraction> getNPCInteraction() {
        return npcInteraction -> {
            Player player = npcInteraction.player();
            GamePlayer gamePlayer = ThankmasLobby.getInstance().getPlayerManager().getPlayerData(player);

            if (!gamePlayer.getUnlockedNPCs().contains(this)) {
                player.playSound(Sound.sound(Key.key("minecraft:entity.player.levelup"), Sound.Source.AMBIENT, 1.0f, 1.0f));

                /*
                Add NPC to their unlocked list and update their scoreboard line
                 */
                gamePlayer.getUnlockedNPCs().add(this);
                gamePlayer.updateSecretCounter();

                sendUnlockMessage(gamePlayer);

                // We update the menu
                gamePlayer.getUnlockedNPCMenu().replaceItem(this.lockedState, this.unlockedState);
            }

            this.npc.getNavigator().setPathTo(ThankmasLobby.getInstance().getSpawnLocation());

            // We pick a random dialogue line and send it.
            player.sendMessage(Component.text("[NPC] " + this.name, NamedTextColor.GOLD).append(Component.text(": " + this.dialogue[ThreadLocalRandom.current().nextInt(this.dialogue.length)], NamedTextColor.WHITE)));
            player.playSound(Sound.sound(Key.key("minecraft:entity.villager.yes"), Sound.Source.VOICE, 1.0f, 1.0f));
        };
    }

    private void sendUnlockMessage(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        String unlockedNPCs = gamePlayer.getUnlockedNPCs().size() + "/" + EasterEggNPC.values().length;

        /*
        Hover state for the message to be sent.
        */
        Component hoverMessage = Component.text("NPC Quest", NamedTextColor.GREEN).append(Component.newline())
                .append(Component.text("Your Journey", NamedTextColor.GRAY).append(Component.newline()).append(Component.newline())
                        .append(Component.text("Your NPCs: ", NamedTextColor.WHITE))
                        .append(Component.text(unlockedNPCs, NamedTextColor.GRAY))
                        .append(Component.newline()).append(Component.newline())
                        .append(Component.text("Click to see more!", NamedTextColor.YELLOW)));

        /*
        We build the NPC unlocked message.
        */
        Component npcUnlockMessage = Component.text("NEW NPC! ", NamedTextColor.YELLOW, TextDecoration.BOLD)
                .append(Component.text("You unlocked ", NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false))
                .append(Component.text(this.name + "'s ", NamedTextColor.AQUA).decoration(TextDecoration.BOLD, false))
                .append(Component.text("NPC! ", NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false))
                .append(Component.text("CLICK", NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true)
                        .hoverEvent(hoverMessage).clickEvent(ClickEvent.runCommand("/opensecretsmenu")))
                .append(Component.text(" (" + unlockedNPCs + ")", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false));

        // We send it
        player.sendMessage(npcUnlockMessage);
    }

    private void buildMenuIcons() {
        this.lockedState = ItemStack.builder(Material.PLAYER_HEAD)
                .meta(PlayerHeadMeta.class, meta -> meta.playerSkin(SkinUtil.LOCKED_STATE).skullOwner(UUID.randomUUID())
                        .displayName(Component.text("???", NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false))
                        .lore(Component.text("Secret NPC", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false), Component.text(""),
                                Component.text("This secret is locked!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false), Component.text(""),
                                Component.text("Locked!", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                        .build()).build();

        this.unlockedState = ItemStack.builder(Material.PLAYER_HEAD)
                .meta(PlayerHeadMeta.class, meta -> meta.playerSkin(npcSkin).skullOwner(UUID.randomUUID())
                        .displayName(Component.text(this.name, NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                        .lore(Component.text("Secret NPC", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false), Component.text(""),
                                Component.text("Secret found!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false), Component.text(""),
                                Component.text("Click to teleport!", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
                        .build()).build();
    }

    public ItemStack getLockedState() {
        return lockedState;
    }

    public ItemStack getUnlockedState() {
        return unlockedState;
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

    public TextNPC getNpc() {
        return npc;
    }
}
