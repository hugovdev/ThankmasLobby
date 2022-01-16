package me.hugo.thankmaslobby.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.lobbynpc.EasterEggNpc;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EasterEggNpcManager {

    private List<EasterEggNpc> easterEggNPCs;
    private int maxNPCs;

    @SneakyThrows
    public EasterEggNpcManager() {
        File dataFolder = new File("serverData");
        dataFolder.mkdirs();
        File npcsFile = new File(dataFolder, "easterEggNPCLocations.json");

        if (!npcsFile.exists())
            npcsFile.createNewFile();

        JsonElement jsonElement = JsonParser.parseReader(new FileReader(npcsFile));

        Type type = new TypeToken<List<EasterEggNpc>>() {
        }.getType();

        this.easterEggNPCs = ThankmasLobby.GSON.fromJson(jsonElement, type);

        if (this.easterEggNPCs != null) {
            this.maxNPCs = easterEggNPCs.size();

            System.out.println("Loaded " + this.maxNPCs + " easter egg npcs!");
            System.out.println("Spawning them...");

            for (EasterEggNpc easterEggNPC : this.easterEggNPCs) {
                easterEggNPC.spawnNpc();
                System.out.println("Spawned NPC for " + easterEggNPC.getName() + "!");
            }

            System.out.println("All the NPCs have been spawned!");
        } else {
            System.out.println("No easter egg NPCs were loaded! (File is empty!)");
            this.maxNPCs = 0;
        }
    }

    @SneakyThrows
    public List<EasterEggNpc> getEasterEggNPCsFromFile() {
        File dataFolder = new File("serverData");
        File easterEggNPCs = new File(dataFolder, "easterEggNPCLocations.json");

        JsonElement jsonElement = JsonParser.parseReader(new FileReader(easterEggNPCs));

        Type type = new TypeToken<List<EasterEggNpc>>() {
        }.getType();

        List<EasterEggNpc> list = ThankmasLobby.GSON.fromJson(jsonElement, type);

        if (list == null) list = new ArrayList<>();

        return list;
    }

    @SneakyThrows
    public void writeNewList(List<EasterEggNpc> newList) {
        File dataFolder = new File("serverData");
        File easterEggNPCs = new File(dataFolder, "easterEggNPCLocations.json");

        FileUtils.writeStringToFile(easterEggNPCs, ThankmasLobby.GSON.toJson(newList), Charset.defaultCharset());
    }

    public void setEasterEggNPCs(List<EasterEggNpc> easterEggNPCs) {
        this.easterEggNPCs = easterEggNPCs;
        this.maxNPCs = easterEggNPCs.size();
    }

    public int getMaxNPCs() {
        return maxNPCs;
    }

    public List<EasterEggNpc> getEasterEggNPCs() {
        return easterEggNPCs;
    }
}
