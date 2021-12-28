package me.hugo.thankmaslobby.games;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.*;
import net.minestom.server.item.metadata.PlayerHeadMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Game {

    KWEEBEC_PARTY(ItemStack.builder(Material.GOLDEN_AXE).meta(itemMetaBuilder -> itemMetaBuilder.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES, ItemHideFlag.HIDE_UNBREAKABLE, ItemHideFlag.HIDE_ENCHANTS)), "Kweebec Party", "kweebec_party",
            Arrays.asList(Component.text("Play 5 rounds of different").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("hytale-themed minigames! Try to").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("get as many points as possible").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("in each minigame to win!").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)), 11),

    SAVE_THE_KWEEBECS(ItemStack.builder(Material.PLAYER_HEAD).meta(new PlayerHeadMeta.Builder().playerSkin(
            new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTYzMzk3NTk2ODExMCwKICAicHJvZmlsZUlkIiA6ICIyY2ZlNGVkYmU5MjQ0NTdjYWQyMWZiNGRlNDdjY2E4MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJIdWdvR2FtZXJTdHlsZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mYTY2NDc5OTk3Yjk4ODc0YzY1ZjZhMDhkYmZiNmMzMjM4MGZlZDBhYTI3YWY1OGNiMWM2NmNhYmVkNWRhMjgxIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                    "NCxWo5drSlsTCTMMZFEhmoTUO0BHn+48XHezFn2ZkZ+J89+1heI9kR3tHf9SR5MpK1kryXX9pEmP90OOWsCEZZGuUIdcSZlkwLsiXWXGp3dlZe0uDwIFVnkAAhsHkinf4meJOUIbUTieSD/Qxiv5cyXVvIgNwXnpz3wGszSXiU1mgjAHd6GwXWqDD/kjkNlwGGEC2qDIKM/ZLNYyNZyDCwJnDX4MVpFvTHIvtEehujg2hW8iQD2kqzbB/0podNx9FDVBz69SoZ5W0pyUtFNeWYED0zZjAVk/Dg7JnYk3YNOgzHhFxv0yvzRBA7y0+k4Tpbfe9w7wV8XrPNfmhwDLEmB6HSosWsZcYZUSr8PClH5HyN5/DlKbSUZQ0CMCSPO9kpVQAnQgdRLwtPKdFZ9c5Hi2bU1DlLbTFs+vJL/NfKnL8OcwyjxjceWeShI26CQqqUyan/pM7ti4Ih6jsh+z+2vsqfxJ1RvdLQgi7KhX13GCjPAyZaVW+OBifuseRQa0eVsyyL1Vu42/oC+5K/zhxheR/1pk2+sj3zni8AOo6yS5Ds5CJxqqeNLFEe54U6rFrw5QhCXB/p2dyYjRIEjk0eIulqn09kWWZMvblXw4xPMEOA4DtPP5+7M1xdDujiU0oiikl2SWRpZa9lF6IO0cl40SFJBnX9t4PZW3DKpn6/8=")).build()),
            "Save The Kweebecs", "save_the_kweebecs",
            Arrays.asList(Component.text("Pick a team and save or defend").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("those kweebecs! Use abilities and kill").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("your enemies to get more gold!").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)), 13),

    CREATIVE(ItemStack.builder(Material.ARMOR_STAND), "Creative", "creative",
            Arrays.asList(Component.text("Build amazing creations and buildings").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("on the new creative!").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)), 15);


    private final ItemStackBuilder icon;
    private final String gameName;
    private final String serverName;
    private final List<Component> minigameDescription;
    private int slot;

    Game(ItemStackBuilder icon, String gameName, String serverName, List<Component> minigameDescription, int slot) {
        this.icon = icon;
        this.gameName = gameName;
        this.serverName = serverName;
        this.minigameDescription = minigameDescription;
        this.slot = slot;
    }

    public static Game fromSlot(int slot) {
        for (Game game : Game.values()) {
            if (game.slot == slot) {
                return game;
            }
        }
        return null;
    }

    public void send(Player player) {
        TextComponent hoverMessage = Component.text(this.gameName + "\n").color(NamedTextColor.GREEN)
                .append(Component.text("Server Stats\n\n").color(NamedTextColor.GRAY))
                .append(Component.text("Players in queue: ")
                        .color(NamedTextColor.YELLOW).append(Component.text("0\n").color(NamedTextColor.GRAY)))
                .append(Component.text("Players: ")
                        .color(NamedTextColor.YELLOW).append(Component.text("0/100").color(NamedTextColor.GRAY)));

        player.sendMessage(Component.text("Sending you to " + this.gameName + "... ").color(NamedTextColor.GREEN)
                .append(Component.text("(Hover for More)").color(NamedTextColor.YELLOW).hoverEvent(hoverMessage)));

        player.playSound(Sound.sound(Key.key("minecraft:block.bamboo.fall"), Sound.Source.AMBIENT, 1.0f, 1.0f));

        /* TODO: Actually send the player lol. */
    }

    public int getSlot() {
        return slot;
    }

    public String getServerName() {
        return serverName;
    }

    public String getGameName() {
        return gameName;
    }

    public ItemStack getIcon() {
        List<Component> newList = new ArrayList<>(minigameDescription);
        newList.add(Component.text(""));
        newList.add(Component.text("Click to join!").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        newList.add(Component.text("0").color(NamedTextColor.AQUA).append(Component.text(" players currently playing!").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false));
        ItemStack newIcon = icon.displayName(Component.text(gameName).decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false)).lore(newList).build();

        return newIcon;
    }
}
