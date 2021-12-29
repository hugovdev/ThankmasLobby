package me.hugo.thankmaslobby.util;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.condition.InventoryCondition;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class PaginatedGUI {

    private List<Inventory> pages = new ArrayList<>();
    private String title;
    private Integer index;
    private ItemStack indicator;
    private Inventory previousMenu;
    private InventoryType inventoryType;
    private PageFormat pageFormat;
    private List<InventoryCondition> inventoryConditions = new ArrayList<>();

    public PaginatedGUI(InventoryType inventoryType, ItemStack indicator, String title, PageFormat pageFormat, Inventory previousMenu) {
        this.previousMenu = previousMenu;
        this.title = title;
        this.inventoryType = inventoryType;
        this.indicator = indicator;
        this.pageFormat = pageFormat;

        index = 0;
    }

    public boolean replaceItem(ItemStack replacing, ItemStack icon) {
        boolean found = false;
        for (Inventory menuHandler : new ArrayList<>(pages)) {
            Inventory inventory = menuHandler;
            int slot = InventoryUtil.getFirstOfItem(replacing, inventory);

            if (slot != -1) {
                menuHandler.setItemStack(slot, icon);
                found = true;
                break;
            }
        }
        return found;
    }

    public void addInventoryCondition(InventoryCondition inventoryCondition) {
        inventoryConditions.add(inventoryCondition);

        for(Inventory inventory : pages) {
            inventory.addInventoryCondition(inventoryCondition);
        }
    }

    public void replaceAll(ItemStack replacing, ItemStack icon) {
        for (Inventory menuHandler : new ArrayList<>(pages)) {
            Inventory inventory = menuHandler;
            int slot = InventoryUtil.getFirstOfItem(replacing, inventory);

            if (slot != -1) {
                menuHandler.setItemStack(slot, icon);
            }
        }
    }

    public void changeIndicator(ItemStack newItem) {
        this.indicator = newItem;
        for (Inventory page : pages) {
            page.setItemStack(page.getSize() - 5, newItem);
        }
    }

    public void setItem(ItemStack icon, int page, int slot) {
        if (pages.size() == 0)
            createNewPage();
        pages.get(page).setItemStack(slot, icon);
    }

    public int addItem(Inventory inventory, ItemStack icon) {
        int index = 0;

        List<Integer> slots = pageFormat.getFormatSlots();
        int slot = slots.get(index);

        if (inventory.getItemStack(slot).getMaterial() != Material.AIR) {
            while (inventory.getItemStack(slot).getMaterial() != Material.AIR) {
                index++;
                slot = slots.get(index);
            }
        }

        inventory.setItemStack(slot, icon);

        return slot;
    }

    public int[] addItem(ItemStack icon) {
        if (pages.size() == 0)
            createNewPage();

        int i = -1;
        int index = 0;

        for (Inventory menuHandler : new ArrayList<>(pages)) {
            List<Integer> slotList = pageFormat.getFormatSlots();
            int lastSlot = slotList.get(slotList.size() - 1);

            if (menuHandler.getItemStack(lastSlot).getMaterial() == Material.AIR) {
                i = addItem(menuHandler, icon);
                break;
            } else if (pages.size() - 1 == index) {
                Inventory newPage = createNewPage();
                i = addItem(newPage, icon);
                index++;
                break;
            }
            index++;
        }

        return new int[]{index, i};
    }

    public void open(Player player) {
        player.openInventory(pages.get(0));
    }

    public void open(Player player, int index) {
        player.openInventory(pages.get(index));
    }

    public Inventory createNewPage() {
        final int page = index + 1;
        final int previousIndex = index - 1;
        final Integer thatIndex = Integer.valueOf(index);

        Inventory newPage = new Inventory(inventoryType, title);

        newPage.setItemStack(newPage.getSize() - 5, indicator);

        newPage.setItemStack(newPage.getSize() - 6,
                ItemStack.of(page == 1 && previousMenu == null ? Material.ACACIA_DOOR : Material.ARROW)
                        .withDisplayName(Component.text((page == 1 ? (previousMenu == null ? "Close" : "Go Back") : "Page " + index), NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));

        for(InventoryCondition inventoryCondition : inventoryConditions) newPage.addInventoryCondition(inventoryCondition);

        if (index >= 1) {
            Inventory lastMenuHandler = pages.get(previousIndex);
            lastMenuHandler.setItemStack(lastMenuHandler.getSize() - 4, ItemStack.of(Material.ARROW)
                    .withDisplayName(Component.text("Page " + page, NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));

            lastMenuHandler.addInventoryCondition((player, i, clickType, inventoryConditionResult) -> {
                if (i == lastMenuHandler.getSize() - 4) {
                    player.openInventory(pages.get(thatIndex));
                }
            });
        } else {
            newPage.addInventoryCondition((player, i, clickType, inventoryConditionResult) -> {
                int backButtonSlot = newPage.getSize() - 6;

                if (i == backButtonSlot) {
                    if (page == 1) {
                        if (previousMenu == null) {
                            player.closeInventory();
                        } else {
                            player.openInventory(previousMenu);
                        }
                    } else {
                        player.openInventory(pages.get(previousIndex));
                    }
                    player.playSound(Sound.sound(Key.key("minecraft:block.wooden_button.click_on"), Sound.Source.AMBIENT, 1.0f, 1.0f));
                }

                inventoryConditionResult.setCancel(true);
            });
        }

        pages.add(newPage);

        index++;
        return newPage;
    }

    public void reset() {
        pages.clear();

        index = 0;
    }

    public enum PageFormat {

        ONE_ROW("---------"
                + "-XXXXXXX-"),

        ONE_ROW_WITHOUT_SIDES("---------"
                + "--XXXXX--"),
        TWO_ROWS("---------"
                + "-XXXXXXX-"
                + "-XXXXXXX-"),

        TWO_ROWS_TO_THE_LEFT("---------"
                + "-XXXXXX--"
                + "-XXXXXX--"),

        THREE_ROWS("---------"
                + "-XXXXXXX-"
                + "-XXXXXXX-"
                + "-XXXXXXX-"),
        THREE_ROWS_WITHOUT_SIDES("---------"
                + "--XXXXX--"
                + "--XXXXX--"
                + "--XXXXX--"),

        ONE_UPGRADE("---------"
                + "-X--X----"),

        THREE_UPGRADES("---------"
                + "-X-XXX---"),

        FOUR_UPGRADES("---------"
                + "-X-XXXX--"),

        FIVE_UPGRADES("---------" +
                "-X-XXXXX-"),

        SIX_UPGRADES("---------" +
                "X-XXXXXX-");

        private String format;

        PageFormat(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }

        public List<Integer> getFormatSlots() {
            char[] charArray = format.toCharArray();
            List<Integer> slots = new ArrayList<>();

            for (int i = 0; i < charArray.length - 1; i++) {
                if (charArray[i] == 'X') {
                    slots.add(i);
                }
            }
            return slots;
        }
    }
}

