package me.hugo.thankmaslobby.secrets;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.hugo.thankmaslobby.ThankmasLobby;
import me.hugo.thankmaslobby.player.GamePlayer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class SecretCategoryManager {

    private BiMap<Integer, SecretCategory> secretCategorySlots = HashBiMap.create();
    public SecretCategory NPC_CATEGORY;

    public SecretCategoryManager() {
        /*
        Register every category with their menu slot.
         */
        NPC_CATEGORY = new SecretCategory(ItemStack.of(Material.PLAYER_HEAD), "NPCs",
                "Find all the secret NPCs around the lobby. They might be sitting somewhere, fishing or just walking around!",
                new SecretCategory.SecretCategoryChecker() {
                    @Override
                    public int checkUnlocked(GamePlayer gamePlayer) {
                        return gamePlayer.getUnlockedNPCs().size();
                    }

                    @Override
                    public void openMenu(GamePlayer gamePlayer) {
                        gamePlayer.getUnlockedNPCMenu().open(gamePlayer.getPlayer());
                    }
                }, ThankmasLobby.getInstance().getEasterEggNPCManager().getMaxNPCs());

        secretCategorySlots.put(11, NPC_CATEGORY);

        secretCategorySlots.put(13, new SecretCategory(ItemStack.of(Material.TURTLE_EGG), "Turtle Eggs",
                "Magical Turtle Eggs have been seen around the lobby! Find them to get magical powers!",
                new SecretCategory.SecretCategoryChecker() {
                    @Override
                    public int checkUnlocked(GamePlayer gamePlayer) {
                        return gamePlayer.getUnlockedNPCs().size();
                    }

                    @Override
                    public void openMenu(GamePlayer gamePlayer) {
                        gamePlayer.getUnlockedNPCMenu().open(gamePlayer.getPlayer());
                    }
                }, 1));
    }

    public BiMap<Integer, SecretCategory> getSecretCategorySlots() {
        return secretCategorySlots;
    }
}
