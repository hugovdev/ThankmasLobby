package me.hugo.thankmaslobby.labelholograms;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;

import java.util.Arrays;

public enum LabelHolograms {

    BLITZ_STRIKE("Kweebec Statue", "BlitzStrike_", new Pos(-6.5, 66.5, 48.5),
            Component.text("BlitzStrike_ built this kweebecs statue,", NamedTextColor.GREEN),
            Component.text("a pigeon statue and a turbo chicken statue.", NamedTextColor.GREEN)),
    BLITZ_STRIKE2("BuddhaCat Pixel Art", "BlitzStrike_", new Pos(41.5, 66.5, -2.5),
            Component.text("This BuddhaCat pixel art represents buddha's", NamedTextColor.GREEN),
            Component.text("struggle to stay connected to the internet.", NamedTextColor.GREEN)),
    SCOOTER("Scooter's Head", "Scooter", new Pos(-21.5, 66.5, -5.5),
            Component.text("Scooter made his own head and we can see", NamedTextColor.GREEN),
            Component.text("he did a damn good job.", NamedTextColor.GREEN)),
    ACORN_AND_ETHAN("Kweebec Head", "TheCrispyAcron and Ethan", new Pos(-27.5, 66.5, 11.5),
            Component.text("Acorn and Ethan rebuilt this kweebec skin's", NamedTextColor.GREEN),
            Component.text("head with blocks. Skin by Powerbyte7.", NamedTextColor.GREEN)),
    _TOOTFACE("Burning Kweebec", "@_TooTface", new Pos(26.5, 66.5, 49.5),
            Component.text("@_TooTface built this burning kweebec statue,", NamedTextColor.GREEN),
            Component.text("which we adore. #BurnTheKweebecs", NamedTextColor.GREEN)),
    POWERBYTE7("Hype Train", "Powerbyte7", new Pos(26.5, 68.5, 7.5),
            Component.text("Everyone get on board, the hype train", NamedTextColor.GREEN),
            Component.text("is leaving soon.", NamedTextColor.GREEN),
            null,
            Component.text("Next Stop: ", NamedTextColor.GREEN).append(Component.text("Hytale Release", NamedTextColor.AQUA))),;

    private final String buildName;
    private final String builder;
    private final Pos position;
    private final Component[] description;

    private final LabelHologram labelHologram;

    LabelHolograms(String buildName, String builder, Pos position, Component... description) {
        this.buildName = buildName;
        this.builder = builder;
        this.position = position;
        this.description = description;

        this.labelHologram = new LabelHologram(buildName, builder, Arrays.asList(description), position);
    }

    public String getBuildName() {
        return buildName;
    }

    public String getBuilder() {
        return builder;
    }

    public Pos getPosition() {
        return position;
    }

    public Component[] getDescription() {
        return description;
    }

    public LabelHologram getLabelHologram() {
        return labelHologram;
    }
}
