package me.hugo.thankmaslobby.games;

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
                TextComponent hoverMessage = Component.text(selectedGame.getGameName() + "\n").color(NamedTextColor.GREEN)
                        .append(Component.text("Server Stats\n\n").color(NamedTextColor.GRAY))
                        .append(Component.text("Players in queue: ")
                        .color(NamedTextColor.YELLOW).append(Component.text("0\n").color(NamedTextColor.GRAY)))
                        .append(Component.text("Players: ")
                                .color(NamedTextColor.YELLOW).append(Component.text("0/100").color(NamedTextColor.GRAY)));

                player.sendMessage(Component.text("Sending you to " + selectedGame.getGameName() + "... ").color(NamedTextColor.GREEN)
                        .append(Component.text("(Hover for More)").color(NamedTextColor.YELLOW).hoverEvent(hoverMessage)));
                /*
                TODO: Actually send the player lol.
                 */
            }
            inventoryConditionResult.setCancel(true);
        });
    }

    public void openMenu(Player player) {
        player.openInventory(gameSelectorMenu);
    }

    public Inventory getGameSelectorMenu() {
        return gameSelectorMenu;
    }
}
