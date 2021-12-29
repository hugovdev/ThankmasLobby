package me.hugo.thankmaslobby.commands;

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

public class TestCommand extends Command {

    public TestCommand() {
        super("spawnnpc");

        setDefaultExecutor(((commandSender, commandContext) -> {
            // If the command executor is not a player we tell them and return.
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("You need to be a player to execute this command!");
                return;
            }

            Player player = (Player) commandSender;

            EntityCreature npc = new EntityCreature(EntityType.PLAYER);
            npc.setInstance(player.getInstance(), player.getPosition());

            player.getPlayerConnection().sendPacket(PacketUtil.addPlayerInfoPacket(npc.getUuid(), StringUtil.randomString(8), PlayerSkin.fromUuid(player.getUuid().toString())));
            npc.updateNewViewer(player);

            npc.addAIGroup(new EntityAIGroupBuilder().addGoalSelector(new RandomStrollGoal(npc, 5)).addGoalSelector(new DoNothingGoal(npc, 8 * 20, 100)).build());
            player.sendMessage(Component.text("NPC spawned!", NamedTextColor.YELLOW));
        }));

        var npcSkin = ArgumentType.String("npcSkinName");
        var executionNumber = ArgumentType.Integer("executionNumber");

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You need to be a player to execute this command!");
                return;
            }

            Player player = (Player) sender;
            final String npcSkinName = context.get(npcSkin);

            EntityCreature npc = new EntityCreature(EntityType.PLAYER);
            npc.setInstance(player.getInstance(), player.getPosition());
            npc.addAIGroup(new EntityAIGroupBuilder().addGoalSelector(new RandomStrollGoal(npc, 5)).addGoalSelector(new DoNothingGoal(npc, 8 * 20, 100)).build());

            for (Player players : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                players.getPlayerConnection().sendPacket(PacketUtil.addPlayerInfoPacket(npc.getUuid(), StringUtil.randomString(8), PlayerSkin.fromUsername(npcSkinName)));
                npc.updateNewViewer(players);

                players.sendMessage(Component.text("NPC spawned!", NamedTextColor.YELLOW));
            }
        }, npcSkin);

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You need to be a player to execute this command!");
                return;
            }

            Player player = (Player) sender;
            final String npcSkinName = context.get(npcSkin);

            for (int i = 0; i <= context.get(executionNumber); i++) {
                EntityCreature npc = new EntityCreature(EntityType.PLAYER);
                npc.setInstance(player.getInstance(), player.getPosition());
                npc.addAIGroup(new EntityAIGroupBuilder().addGoalSelector(new RandomStrollGoal(npc, 5)).addGoalSelector(new DoNothingGoal(npc, 8 * 20, 100)).build());

                for (Player players : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    players.getPlayerConnection().sendPacket(PacketUtil.addPlayerInfoPacket(npc.getUuid(), StringUtil.randomString(8), PlayerSkin.fromUsername(npcSkinName)));
                    npc.updateNewViewer(players);

                    players.sendMessage(Component.text("NPC spawned!", NamedTextColor.YELLOW));
                }
            }
        }, npcSkin, executionNumber);

    }

}
