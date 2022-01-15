package me.hugo.thankmaslobby.labelholograms;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.entities.CloudHologram;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.instance.Instance;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;

public class LabelHologram {

    private final String buildingName;
    private final String author;
    private final List<Component> description;
    private Point position;

    private List<CloudHologram> hologramLines;

    public LabelHologram(String buildingName, String author, List<Component> description, Point position) {
        this.buildingName = buildingName;
        this.author = author;
        this.description = description;
        this.position = position;

        this.hologramLines = new ArrayList<>();
        initHologramLines();
    }

    private void initHologramLines() {
        Instance lobbyInstance = ThankmasLobby.getInstance().getMainInstance();
        int count = 0;

        List<Component> finalText = new ArrayList<>();

        finalText.add(Component.text(this.author, NamedTextColor.AQUA)
                .append(Component.text(" built " + this.buildingName, NamedTextColor.GREEN)));

        if (!this.description.isEmpty()) {
            finalText.add(null);
            finalText.addAll(description);
        }

        for (int i = finalText.size() - 1; i >= 0; i--) {
            Component component = finalText.get(i);
            if (component != null) {
                CloudHologram hologram = new CloudHologram(lobbyInstance, new Pos(position).add(0, (0.3 * count), 0), component);
                hologramLines.add(hologram);
            }

            count++;
        }
    }


}
