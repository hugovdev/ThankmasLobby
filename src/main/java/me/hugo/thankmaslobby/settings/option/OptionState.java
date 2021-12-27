package me.hugo.thankmaslobby.settings.option;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class OptionState {

    private int stateId;
    private Material stateIcon;
    private TextColor stateColor;
    private Material stateToggleIcon;
    private String stateName;
    private Option option;

    private ItemStack menuIcon, toggleMenuIcon;

    public OptionState(int stateId, Material stateIcon, TextColor stateColor, Material stateToggleIcon, String stateName, Option option) {
        this.stateId = stateId;
        this.stateIcon = stateIcon;
        this.stateColor = stateColor;
        this.stateToggleIcon = stateToggleIcon;
        this.stateName = stateName;
        this.option = option;

        this.menuIcon = ItemStack.of(stateIcon).withDisplayName(Component.text(stateName, stateColor)).withLore(option.getDescription(this, false));
        this.toggleMenuIcon = ItemStack.of(stateToggleIcon).withDisplayName(Component.text(stateName, stateColor)).withLore(option.getDescription(this, true));
    }

    public ItemStack getMenuIcon() {
        return menuIcon;
    }

    public ItemStack getToggleMenuIcon() {
        return toggleMenuIcon;
    }

    public int getStateId() {
        return stateId;
    }

    public TextColor getStateColor() {
        return stateColor;
    }

    public String getStateName() {
        return stateName;
    }
}
