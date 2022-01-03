package me.hugo.thankmaslobby.cosmetics.menus;

import me.hugo.thankmaslobby.util.StringUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.ItemStackBuilder;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public enum CosmeticType {

    WARDROBE(ItemStack.builder(Material.LEATHER_CHESTPLATE).meta(meta -> meta.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)), "Wardrobe",
            "Choose betweem different Outfits and dress yourself as your favorite character!", 11, null),
    GADGETS(ItemStack.builder(Material.TRIDENT).meta(meta -> meta.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES)), "Gadgets",
            "Use different games and gadgets to have fun while waiting in the lobby!", 13, null),
    LASER(ItemStack.builder(Material.DANDELION), "Click Laser",
            "Select a particle or effect that will show whenever you click on the lobby!", 15, null),
    ;

    private ItemStack icon;
    private String name;
    private String description;
    private final int slot;
    private final Inventory nextMenu;

    CosmeticType(ItemStackBuilder icon, String name, String description, int slot, Inventory nextMenu) {
        List<Component> loreLines = new ArrayList<>();

        for (String line : StringUtil.wrapLine(description, 30)) {
            loreLines.add(Component.text(line, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        }

        loreLines.add(Component.text(""));
        loreLines.add(Component.text("Click to open!", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));

        this.icon = icon.displayName(Component.text(name, NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)).
                lore(loreLines).build();
        this.name = name;
        this.description = description;
        this.slot = slot;
        this.nextMenu = nextMenu;
    }

    public static CosmeticType fromIcon(ItemStack icon) {
        for (CosmeticType cosmeticType : CosmeticType.values()) {
            if (cosmeticType.getIcon() == icon) {
                return cosmeticType;
            }
        }
        return null;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSlot() {
        return slot;
    }

    public Inventory getNextMenu() {
        return nextMenu;
    }
}
