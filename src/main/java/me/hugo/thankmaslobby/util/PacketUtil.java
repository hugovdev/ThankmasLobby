package me.hugo.thankmaslobby.util;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;

import java.util.Collections;
import java.util.UUID;

public class PacketUtil {

    public static PlayerInfoPacket addPlayerInfoPacket(UUID uuid, String username, PlayerSkin skin) {
        var textureProperty = new PlayerInfoPacket.AddPlayer.Property("textures", skin.textures(), skin.signature());
        var playerEntry = new PlayerInfoPacket.AddPlayer(uuid, username, Collections.singletonList(textureProperty), GameMode.CREATIVE, 0, null);

        return new PlayerInfoPacket(PlayerInfoPacket.Action.ADD_PLAYER, Collections.singletonList(playerEntry));
    }

    public static PlayerInfoPacket removePlayerInfoPacket(UUID uuid) {
        var playerEntry = new PlayerInfoPacket.RemovePlayer(uuid);

        return new PlayerInfoPacket(PlayerInfoPacket.Action.REMOVE_PLAYER, Collections.singletonList(playerEntry));
    }

}
