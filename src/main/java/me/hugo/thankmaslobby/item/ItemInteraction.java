package me.hugo.thankmaslobby.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.HashMap;

public class ItemInteraction {
    
    private HashMap<ItemStack, ClickAction> itemInteractions;

    public ItemInteraction() {
        /*
        player.getInventory().setItemStack(0, ItemStack.of(Material.COMPASS).withDisplayName(Component.text("Game Selector").color(NamedTextColor.GREEN)
                .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
        player.getInventory().setItemStack(1, ItemStack.of(Material.CHEST).withDisplayName(Component.text("Cosmetics").color(NamedTextColor.GREEN)
                .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
        player.getInventory().setItemStack(2, ItemStack.of(Material.WRITTEN_BOOK).withDisplayName(Component.text("Information").color(NamedTextColor.GREEN)
                .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
        player.getInventory().setItemStack(4, ItemStack.of(Material.ENDER_PEARL).withDisplayName(Component.text("Ride Pearl").color(NamedTextColor.GREEN)
                .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
        player.getInventory().setItemStack(7, ItemStack.of(Material.COMPARATOR).withDisplayName(Component.text("Lobby Settings").color(NamedTextColor.GREEN)
                .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
        player.getInventory().setItemStack(8, ItemStack.of(Material.NETHER_STAR).withDisplayName(Component.text("Lobby Selector").color(NamedTextColor.GREEN)
                .append(Component.text(" (Right Click)").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)));
        */
    }

}
