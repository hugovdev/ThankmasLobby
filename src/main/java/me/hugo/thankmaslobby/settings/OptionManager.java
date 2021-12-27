package me.hugo.thankmaslobby.settings;

import me.hugo.thankmaslobby.settings.option.Option;
import me.hugo.thankmaslobby.settings.option.types.ChatOption;
import me.hugo.thankmaslobby.settings.option.types.FlyOption;
import me.hugo.thankmaslobby.settings.option.types.ParticlesOption;
import me.hugo.thankmaslobby.settings.option.types.VisibilityOption;

import java.util.Arrays;
import java.util.List;

public class OptionManager {

    private List<Option> list;

    public VisibilityOption VISIBILITY;
    public FlyOption FLY;
    public ParticlesOption PARTICLES;
    public ChatOption CHAT;

    public OptionManager() {
        this.FLY = new FlyOption();
        this.VISIBILITY = new VisibilityOption();
        this.PARTICLES = new ParticlesOption();
        this.CHAT = new ChatOption();

        this.list = Arrays.asList(FLY, VISIBILITY, PARTICLES, CHAT);
    }


    public List<Option> getList() {
        return list;
    }

    public Option valueOf(String id) {
        for (Option option : getList())
            if (option.getId().equalsIgnoreCase(id)) {
                return option;
            }
        return null;
    }


}
