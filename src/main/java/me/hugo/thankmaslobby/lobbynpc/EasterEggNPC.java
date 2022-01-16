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
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class EasterEggNPC {

    /*KWEEBEC_CORNER(0, "Kweebec Corner", PlayerSkin.fromUsername("KweebecCorner"), new Pos(-31.7, 79, 38.7, 46, 10), "Stay safe and keep free!"),
    EFS3(1, "EFS3", PlayerSkin.fromUsername("EFS3"), new Pos(11, 40, 13.5, 140, 0), "ඞ", "sus"),
    BUDDHACAT(2, "BuddhaCat", PlayerSkin.fromUsername("BuddhaCat"), new Pos(41.5, 65, -10.5, 70, 0), "Remember to ask me what phrase I want for my NPC!"),
    CANADIAN_FLASH(3, "CanadianFlash", PlayerSkin.fromUsername("CanadianFlash"), new Pos(10.5, 40, 16.5, 160, 0), "I'm CanadianFlash and I'm always late!"),
    PROPZIE(4, "Propzie", PlayerSkin.fromUsername("Propzie"), new Pos(-5.5, 40, 32.5, 160, 0), "Propzie!"),
    POWERBYTE7(5, "Powerbyte7", PlayerSkin.fromUsername("Powerbyte7"), new Pos(26.5, 68, 3.5, 50, 0),
            "I don't know why people say I hate Kweebecs. I just have a passion for pyrotechnics, that's completely different!"),
    BLITZ_STRIKE(5, "BlitzStrike_", PlayerSkin.fromUsername("BlitzStrike_"), new Pos(-5.5, 67, 53.5, 160, 20), "Hello!"),
    NOXY(6, "NoxyD", PlayerSkin.fromUsername("NoxyD"), new Pos(29.5, 78, 42.5, 120, 20), "Hello!"),
    ACORN(7, "TheCrispyAcorn", PlayerSkin.fromUsername("TheCrispyAcorn"), new Pos(-34.5, 70, 58.5, 90, 0),
            "If I wait any longer I might turn into a tree.");*/

    private int id;
    private String name;

    private String[] dialogue;

    private double x, y, z;
    private float yaw, pitch;

    private transient Pos npcPosition;
    private transient TextNPC npc;
    private transient PlayerSkin npcSkin;
    private transient ItemStack lockedState, unlockedState;

    public EasterEggNPC(int id, String name, Pos npcPosition, String... dialogue) {
        this.id = id;
        this.name = name;
        this.npcSkin =  PlayerSkin.fromUsername(name);
        this.dialogue = dialogue;
        this.npcPosition = npcPosition;

        this.x = npcPosition.x();
        this.y = npcPosition.y();
        this.z = npcPosition.z();

        this.yaw = npcPosition.yaw();
        this.pitch = npcPosition.pitch();
    }

    public EasterEggNPC(int id, String name, PlayerSkin npcSkin, Pos npcPosition, String... dialogue) {
        this.id = id;
        this.name = name;
        this.npcSkin = npcSkin;
        this.dialogue = dialogue;
        this.npcPosition = npcPosition;

        this.x = npcPosition.x();
        this.y = npcPosition.y();
        this.z = npcPosition.z();

        this.yaw = npcPosition.yaw();
        this.pitch = npcPosition.pitch();
    }

    public void spawnNPC() {
        Instance instance = MinecraftServer.getInstanceManager().getInstances().iterator().next();

        if (this.npcSkin == null || this.npcPosition == null) {
            this.npcSkin = PlayerSkin.fromUsername(this.name);
            this.npcPosition = new Pos(this.x, this.y, this.z, this.yaw, this.pitch);
        }

        this.npc = new TextNPC(instance, this.npcPosition, this.npcSkin, TriState.TRUE, getNPCInteraction(), Component.text(this.name, NamedTextColor.AQUA), Component.text("CLICK", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));

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
                gamePlayer.updateCategory(ThankmasLobby.getInstance().getSecretCategoryManager().NPC_CATEGORY);
            }

            // We pick a random dialogue line and send it.
            player.sendMessage(Component.text("[NPC] " + this.name, NamedTextColor.GOLD).append(Component.text(": " + this.dialogue[ThreadLocalRandom.current().nextInt(this.dialogue.length)], NamedTextColor.WHITE)));
            player.playSound(Sound.sound(Key.key("minecraft:entity.villager.yes"), Sound.Source.VOICE, 1.0f, 1.0f));
        };
    }

    private void sendUnlockMessage(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();

        int unlockedNumber = gamePlayer.getUnlockedNPCs().size();
        int maxNPCs = ThankmasLobby.getInstance().getEasterEggNPCManager().getMaxNPCs();

        String unlockedNPCs = unlockedNumber + "/" + maxNPCs;

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

        if (unlockedNumber == maxNPCs) {
            player.sendMessage(Component.text("You have unlocked all of the ", NamedTextColor.GREEN)
                    .append(Component.text(maxNPCs, NamedTextColor.AQUA))
                    .append(Component.text(" NPCs!", NamedTextColor.GREEN)));
        }
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
}
