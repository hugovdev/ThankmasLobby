package me.hugo.thankmaslobby.settings.option.types;

import me.hugo.thankmaslobby.player.GamePlayer;
import me.hugo.thankmaslobby.settings.option.Option;
import me.hugo.thankmaslobby.settings.option.OptionState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisibilityOption implements Option {

    private List<OptionState> states;

    public VisibilityOption() {
        this.states = new ArrayList<>();
        this.states.add(new OptionState(0, Material.ENDER_EYE, NamedTextColor.GREEN, Material.LIME_STAINED_GLASS_PANE, "ON", this));
        this.states.add(new OptionState(1, Material.ENDER_PEARL, NamedTextColor.RED, Material.RED_STAINED_GLASS_PANE, "OFF", this));
    }

    @Override
    public boolean run(GamePlayer playerData, OptionState state, boolean inventoryClick) {
        boolean isHiding = state.getStateId() == 1;

        playerData.getPlayer().sendMessage(Component.text("You have ", NamedTextColor.YELLOW)
                .append(Component.text((isHiding ? "hidden" : "shown"), isHiding ? NamedTextColor.RED : NamedTextColor.GREEN))
                .append(Component.text(" players!", NamedTextColor.YELLOW)));

        /*
        TODO: Manage player visibility.
         */

        return true;
    }

    @Override
    public String getId() {
        return "visibility";
    }

    @Override
    public OptionState getDefaultState() {
        return states.get(0);
    }

    @Override
    public String getName() {
        return "Player Visibility";
    }

    @Override
    public List<Component> getDescription(OptionState state, boolean isGlassPane) {
        return Arrays.asList(Component.text("Enable or disable player", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("visibility on the lobby!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text(""),
                Component.text("Click to change!", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public int getSlot() {
        return 10;
    }

    @Override
    public List<OptionState> getStates() {
        return states;
    }

    @Override
    public OptionState getState(boolean transform) {
        return getStates().get(transform ? 1 : 0);
    }

}
