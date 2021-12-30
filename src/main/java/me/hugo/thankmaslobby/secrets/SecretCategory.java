package me.hugo.thankmaslobby.secrets;

import me.hugo.thankmaslobby.player.GamePlayer;
import me.hugo.thankmaslobby.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SecretCategory {

    private ItemStack icon;
    private String name;
    private String description;
    private SecretCategoryChecker secretChecker;
    private int maximum;

    public SecretCategory(ItemStack icon, String name, String description, SecretCategoryChecker secretChecker, int maximum) {
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.secretChecker = secretChecker;
        this.maximum = maximum;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public int getMaximum() {
        return maximum;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public SecretCategoryChecker getSecretChecker() {
        return secretChecker;
    }

    public ItemStack getMenuIcon(GamePlayer gamePlayer) {
        List<Component> lore = new ArrayList<>();

        lore.add(Component.text("Secret Category", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());

        for (String loreLine : StringUtils.wrapLine(getDescription(), 30))
            lore.add(Component.text(loreLine, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));

        lore.add(Component.empty());

        lore.add(Component.text("Progress: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(getSecretChecker().checkUnlocked(gamePlayer) + "/" + getMaximum(), NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)));

        lore.add(Component.empty());
        lore.add(Component.text("Click to open!", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));

        ItemStack icon = ItemStack.of(getIcon().getMaterial()).withDisplayName(Component.text(getName(), NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)).withLore(lore);

        return icon;
    }

    public interface SecretCategoryChecker {
        int checkUnlocked(GamePlayer gamePlayer);
        void openMenu(GamePlayer gamePlayer);
    }
}
