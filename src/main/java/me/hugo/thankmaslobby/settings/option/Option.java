package me.hugo.thankmaslobby.settings.option;

import me.hugo.thankmaslobby.player.GamePlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

public interface Option {

    String getId();

    String getName();

    List<Component> getDescription(OptionState state, boolean isGlassPane);

    int getSlot();

    OptionState getDefaultState();

    List<OptionState> getStates();

    boolean run(GamePlayer playerData, OptionState state, boolean inventoryClick);

    OptionState getState(boolean transform);

    default OptionState getState(int id) {
        List<OptionState> states = getStates();
        for (OptionState state : states)
            if (state.getStateId() == id) {
                return state;
            }
        return getDefaultState();
    }

    default OptionState getNextState(OptionState state) {
        int nextIndex = getStates().indexOf(state) + 1;
        return getStates().size() > nextIndex ? getStates().get(nextIndex) : getStates().get(0);
    }

}
