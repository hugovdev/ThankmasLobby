package me.hugo.thankmaslobby.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.lobbynpc.EasterEggNPC;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EasterEggNPCManager {

    private List<EasterEggNPC> easterEggNPCs;
    private int maxNPCs;

    @SneakyThrows
    public EasterEggNPCManager() {
        File dataFolder = new File("serverData");
        dataFolder.mkdirs();
        File npcsFile = new File(dataFolder, "easterEggNPCLocations.json");

        if (!npcsFile.exists())
            npcsFile.createNewFile();

        JsonElement jsonElement = JsonParser.parseReader(new FileReader(npcsFile));

        Type type = new TypeToken<List<EasterEggNPC>>() {
        }.getType();

        this.easterEggNPCs = ThankmasLobby.GSON.fromJson(jsonElement, type);

        if (this.easterEggNPCs != null) {
            System.out.println("Loaded " + this.easterEggNPCs.size() + " easter egg npcs!");

            for (EasterEggNPC easterEggNPC : this.easterEggNPCs) {
                easterEggNPC.spawnNPC();
            }
            this.maxNPCs = easterEggNPCs.size();
        } else {
            System.out.println("No easter egg NPCs were loaded! (File is empty!)");
            this.maxNPCs = 0;
        }
    }

    @SneakyThrows
    public List<EasterEggNPC> getEasterEggNPCsFromFile() {
        File dataFolder = new File("serverData");
        File easterEggNPCs = new File(dataFolder, "easterEggNPCLocations.json");

        JsonElement jsonElement = JsonParser.parseReader(new FileReader(easterEggNPCs));

        Type type = new TypeToken<List<EasterEggNPC>>() {
        }.getType();

        List<EasterEggNPC> list = ThankmasLobby.GSON.fromJson(jsonElement, type);

        if (list == null) list = new ArrayList<>();

        return list;
    }

    @SneakyThrows
    public void writeNewList(List<EasterEggNPC> newList) {
        File dataFolder = new File("serverData");
        File easterEggNPCs = new File(dataFolder, "easterEggNPCLocations.json");

        FileUtils.writeStringToFile(easterEggNPCs, ThankmasLobby.GSON.toJson(newList), Charset.defaultCharset());
    }

    public void setEasterEggNPCs(List<EasterEggNPC> easterEggNPCs) {
        this.easterEggNPCs = easterEggNPCs;
        this.maxNPCs = easterEggNPCs.size();
    }

    public int getMaxNPCs() {
        return maxNPCs;
    }

    public List<EasterEggNPC> getEasterEggNPCs() {
        return easterEggNPCs;
    }
}
