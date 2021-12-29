package me.hugo.thankmaslobby.entities;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.instance.Instance;

import java.util.function.Consumer;

@Getter
public class TextNPC extends NPC {

    private final Hologram[] holograms;

    public TextNPC(Instance instance, Pos position, PlayerSkin playerSkin, TriState faceNearestPlayer, Consumer<NPCInteraction> interaction, Component... hologramLines) {
        super(instance, position, playerSkin, faceNearestPlayer, interaction);

        holograms = new Hologram[hologramLines.length];

        int count = 0;

        for (int i = hologramLines.length - 1; i >= 0; i--) {
            Component component = hologramLines[i];
            Hologram hologram = new Hologram(instance, new Pos(position).add(0, 1.7 + (0.3 * count), 0), component);
            holograms[count] = hologram;

            count++;
        }
    }
}
