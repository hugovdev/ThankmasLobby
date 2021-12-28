package me.hugo.thankmaslobby.player;

import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private HashMap<UUID, GamePlayer> playerStorage;

    public PlayerManager() {
        playerStorage = new HashMap<>();
    }

    public GamePlayer getPlayerData(Player player) {
        if(playerStorage.containsKey(player.getUuid())) {
            return playerStorage.get(player.getUuid());
        } else {
            GamePlayer playerData = new GamePlayer(player);
            playerStorage.put(player.getUuid(), playerData);

            return playerData;
        }
    }

    public HashMap<UUID, GamePlayer> getPlayerStorage() {
        return playerStorage;
    }

    public void removePlayerData(Player player) {
        playerStorage.remove(player.getUuid());
    }

}
