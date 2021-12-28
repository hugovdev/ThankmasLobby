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

public class FlyOption implements Option {
    private List<OptionState> states;

    public FlyOption() {
        this.states = new ArrayList<>();
        this.states.add(new OptionState(0, Material.FEATHER, NamedTextColor.RED, Material.RED_STAINED_GLASS_PANE, "OFF", this));
        this.states.add(new OptionState(1, Material.FEATHER, NamedTextColor.GREEN, Material.LIME_STAINED_GLASS_PANE, "ON", this));
    }

    @Override
    public boolean run(GamePlayer playerData, OptionState state, boolean inventoryClick) {
        if (playerData.isDonator("Flight Mode")) {
            boolean isEnabled = state.getStateId() == 1;
            playerData.getPlayer().sendMessage(Component.text("You ", NamedTextColor.YELLOW)
                    .append(Component.text((isEnabled ? "enabled" : "disabled"), isEnabled ? NamedTextColor.GREEN : NamedTextColor.RED))
                    .append(Component.text(" your flight!", NamedTextColor.YELLOW)));

            playerData.getPlayer().setAllowFlying(isEnabled);
            playerData.getPlayer().setFlying(isEnabled);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getId() {
        return "fly";
    }

    @Override
    public OptionState getDefaultState() {
        return states.get(0);
    }

    @Override
    public String getName() {
        return "Flight Mode";
    }

    @Override
    public List<Component> getDescription(OptionState state, boolean isGlassPane) {
        return Arrays.asList(Component.text("Enable or disable your", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("ability to fly!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text(""),
                isGlassPane ? Component.text(state.getStateName(), state.getStateColor()).decoration(TextDecoration.ITALIC, false) : Component.text("Click to change!", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public int getSlot() {
        return 14;
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
