package me.hugo.thankmaslobby.commands;

import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.config.EasterEggNpcManager;
import me.hugo.thankmaslobby.lobbynpc.EasterEggNpc;
import me.hugo.thankmaslobby.player.GamePlayer;
import me.hugo.thankmaslobby.player.rank.Rank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EasterEggNpcCommand extends Command {

    public EasterEggNpcCommand(@NotNull String name) {
        super(name);

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You need to be a player to execute this command!");
                return;
            }

            Player player = (Player) sender;
            GamePlayer gamePlayer = ThankmasLobby.getInstance().getPlayerManager().getPlayerData(player);

            if(gamePlayer.getRank() == Rank.ADMIN) {
                player.sendMessage(Component.text("Incorrect usage! Use: ", NamedTextColor.RED).append(Component.text("/addeasterggnpc <id> <skinName> \"<phrase>\"", NamedTextColor.AQUA)));
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
                EasterEggNpc easterEggNPC = new EasterEggNpc(finalNpcId, skinPlayerName, PlayerSkin.fromUsername(skinPlayerName), player.getPosition(), finalNpcPhrase);
                EasterEggNpcManager npcManager = ThankmasLobby.getInstance().getEasterEggNPCManager();

                List<EasterEggNpc> newList = npcManager.getEasterEggNPCsFromFile();

                newList.add(easterEggNPC);
                npcManager.writeNewList(newList);
                npcManager.setEasterEggNPCs(newList);
                player.sendMessage(Component.text("Added NPC of " + skinPlayerName + "!", NamedTextColor.YELLOW));

                easterEggNPC.spawnNpc();
            } else {
                player.sendMessage(Component.text("You must be an admin to use this command!", NamedTextColor.RED));
            }
        }, npcId, npcSkin, npcPhrase);

    }

}
