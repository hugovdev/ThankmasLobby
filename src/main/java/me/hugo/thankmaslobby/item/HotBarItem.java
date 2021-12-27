package me.hugo.thankmaslobby.item;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.entities.CustomPearl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.Arrays;

public enum HotBarItem {

    GAME_SELECTOR(ItemStack.of(Material.COMPASS).withDisplayName(Component.text("Game Selector").color(NamedTextColor.GREEN)
            .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)).withLore(
            Arrays.asList(Component.text("Choose a game from the").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("game selector!").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text(""),
                    Component.text("Click to open!").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
    ), player -> {
        ThankmasLobby.getInstance().getGameSelectorMenu().openMenu(player);
    }, 0),
    COSMETICS(ItemStack.of(Material.CHEST).withDisplayName(Component.text("Cosmetics").color(NamedTextColor.GREEN)
            .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)).withLore(
            Arrays.asList(Component.text("Choose fun gadgets or outfits").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("on this cosmetic selector!").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text(""),
                    Component.text("Click to open!").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
    ), player -> {
        player.sendMessage("§cThis feature is §bin development§c!");
    }, 1),
    INFORMATION(ItemStack.of(Material.WRITTEN_BOOK).withDisplayName(Component.text("Information").color(NamedTextColor.GREEN)
            .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)).withLore(
            Arrays.asList(Component.text("Learn more about Hytale Thankmas").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("and the server you are on!").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text(""),
                    Component.text("Click to open!").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
    ), player -> {
        player.openBook(ThankmasLobby.getInstance().getWelcomeBook());
    }, 2),
    RIDE_PEARL(ItemStack.of(Material.ENDER_PEARL).withDisplayName(Component.text("Ride Pearl").color(NamedTextColor.GREEN)
            .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)).withLore(
            Arrays.asList(Component.text("Move quickly through the lobby").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("riding this ender pearl!").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text(""),
                    Component.text("Click to shoot!").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
    ), player -> {
        Entity vehicle = player.getVehicle();

        if (vehicle != null && vehicle instanceof CustomPearl) {
            vehicle.removePassenger(player);
            vehicle.remove();
        }

        Entity enderPearl = new CustomPearl(player, EntityType.ENDER_PEARL);
        enderPearl.setInstance(player.getInstance(), player.getPosition().add(0, 1, 0));
        enderPearl.setVelocity(player.getPosition().direction().mul(40));
        enderPearl.setGlowing(true);
        enderPearl.addPassenger(player);
    }, 4);

    private ItemStack item;
    private ClickAction clickAction;
    private int slot;

    HotBarItem(ItemStack item, ClickAction clickAction, int slot) {
        this.item = item;
        this.clickAction = clickAction;
        this.slot = slot;
    }

    public static HotBarItem fromItem(ItemStack itemStack) {
        for (HotBarItem item : HotBarItem.values()) {
            if (item.getItem() == itemStack) {
                return item;
            }
        }
        return null;
    }

    public ClickAction getClickAction() {
        return clickAction;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }
}

