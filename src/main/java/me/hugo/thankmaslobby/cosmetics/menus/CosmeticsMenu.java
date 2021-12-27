package me.hugo.thankmaslobby.cosmetics.menus;

import me.hugo.thankmaslobby.games.Game;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class CosmeticsMenu {

    private Inventory cosmeticsMenu;

    public CosmeticsMenu() {
        cosmeticsMenu = new Inventory(InventoryType.CHEST_4_ROW, "Cosmetics");

        for (CosmeticType cosmeticType : CosmeticType.values())
            cosmeticsMenu.setItemStack(cosmeticType.getSlot(), cosmeticType.getIcon());

        cosmeticsMenu.setItemStack(30, ItemStack.of(Material.ACACIA_DOOR)
                .withDisplayName(Component.text("Close Menu", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));

        cosmeticsMenu.setItemStack(31, ItemStack.of(Material.CHEST)
                .withDisplayName(Component.text("Cosmetics", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));


        cosmeticsMenu.addInventoryCondition((player, i, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);

            if (i == 30) {
                player.closeInventory();
                return;
            }

            CosmeticType selectedCosmeticType = CosmeticType.fromIcon(inventoryConditionResult.getClickedItem());

            if (selectedCosmeticType != null) {
                if (selectedCosmeticType.getNextMenu() != null) {
                    player.openInventory(selectedCosmeticType.getNextMenu());
                } else {
                    player.sendMessage(Component.text("Sorry! This category is under construction!", NamedTextColor.RED));
                }
            }
        });
    }

    public void openMenu(Player player) {
        player.playSound(Sound.sound(Key.key("minecraft:block.beehive.exit"), Sound.Source.AMBIENT, 1.0f, 1.0f));
        player.openInventory(cosmeticsMenu);
    }
}
