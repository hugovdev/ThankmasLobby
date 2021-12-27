package me.hugo.thankmaslobby.settings;

import me.hugo.thankmaslobby.settings.option.Option;
import me.hugo.thankmaslobby.settings.option.types.FlyOption;

import java.util.Arrays;
import java.util.List;

public class OptionManager {

    private List<Option> list;
    public FlyOption FLY;

    public OptionManager() {
        this.FLY = new FlyOption();
        this.list = Arrays.asList(FLY);
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
