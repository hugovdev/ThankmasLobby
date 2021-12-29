package me.hugo.thankmaslobby.util;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;

public class InventoryUtil {

    public static int getFirstOfItem(ItemStack itemStack, Inventory inventory) {
        int slot = 0;

        for(ItemStack items : inventory.getItemStacks()) {
            if(items == itemStack) {
                return slot;
            }
            slot ++;
        }

        return -1;
    }

}
