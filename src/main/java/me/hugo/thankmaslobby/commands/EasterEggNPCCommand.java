package me.hugo.thankmaslobby.commands;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.config.EasterEggNPCManager;
import me.hugo.thankmaslobby.lobbynpc.EasterEggNPC;
import me.hugo.thankmaslobby.player.GamePlayer;
import me.hugo.thankmaslobby.player.rank.Rank;
import me.hugo.thankmaslobby.util.PacketUtil;
import me.hugo.thankmaslobby.util.StringUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.goal.DoNothingGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EasterEggNPCCommand extends Command {

    public EasterEggNPCCommand(@NotNull String name) {
        super(name);

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You need to be a player to execute this command!");
                return;
            }

            Player player = (Player) sender;
            GamePlayer gamePlayer = ThankmasLobby.getInstance().getPlayerManager().getPlayerData(player);

            if(gamePlayer.getRank() == Rank.ADMIN) {
                player.sendMessage(Component.text("Incorrect usage! Use: ", NamedTextColor.RED).append(Component.text("/addeasterggnpc <id> <skinName> >phrase>")));
            } else {
                player.sendMessage(Component.text("You must be an admin to use this command!", NamedTextColor.RED));
            }
        });

        var npcId = ArgumentType.Integer("npcId");
        var npcSkin = ArgumentType.String("npcSkinName");
        var npcPhrase = ArgumentType.String("npcPhrase");

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You need to be a player to execute this command!");
                return;
            }

            Player player = (Player) sender;
            GamePlayer gamePlayer = ThankmasLobby.getInstance().getPlayerManager().getPlayerData(player);

            String skinPlayerName = context.get(npcSkin);
            String finalNpcPhrase = context.get(npcPhrase);
            Integer finalNpcId = context.get(npcId);

            if(gamePlayer.getRank() == Rank.ADMIN) {
                EasterEggNPC easterEggNPC = new EasterEggNPC(finalNpcId, skinPlayerName, PlayerSkin.fromUsername(skinPlayerName), player.getPosition(), finalNpcPhrase);
                EasterEggNPCManager npcManager = ThankmasLobby.getInstance().getEasterEggNPCManager();

                List<EasterEggNPC> newList = npcManager.getEasterEggNPCsFromFile();

                newList.add(easterEggNPC);
                npcManager.writeNewList(newList);
                player.sendMessage(Component.text("Added NPC of " + skinPlayerName + "!", NamedTextColor.YELLOW));

                easterEggNPC.spawnNPC();
            } else {
                player.sendMessage(Component.text("You must be an admin to use this command!", NamedTextColor.RED));
            }
        }, npcId, npcSkin, npcPhrase);

    }

}
