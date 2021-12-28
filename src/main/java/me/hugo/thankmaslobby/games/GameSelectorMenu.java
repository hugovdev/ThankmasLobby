package me.hugo.thankmaslobby.games;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

public class GameSelectorMenu {

    private Inventory gameSelectorMenu;

    public GameSelectorMenu() {
        gameSelectorMenu = new Inventory(InventoryType.CHEST_3_ROW, "Game Selector");

        for (Game game : Game.values())
            gameSelectorMenu.setItemStack(game.getSlot(), game.getIcon());

        gameSelectorMenu.addInventoryCondition((player, i, clickType, inventoryConditionResult) -> {
            Game selectedGame = Game.fromSlot(i);

            if (selectedGame != null) {
                player.closeInventory();
                selectedGame.send(player);
            }
            inventoryConditionResult.setCancel(true);
        });
    }

    public void openMenu(Player player) {
        player.playSound(Sound.sound(Key.key("minecraft:block.amethyst_block.hit"), Sound.Source.AMBIENT, 1.0f, 1.0f));
        player.openInventory(gameSelectorMenu);
    }

    public Inventory getGameSelectorMenu() {
        return gameSelectorMenu;
    }
}
