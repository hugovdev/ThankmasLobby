package me.hugo.thankmaslobby.lobbynpc;

import me.hugo.thankmaslobby.entities.TextNPC;
import me.hugo.thankmaslobby.games.Game;
import me.hugo.thankmaslobby.entities.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.TriState;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public enum ServerJoinNPC {

    KWEEBEC_PARTY("kweebec_party",
            new Pos(-0.5, 40, 4.5, 180, 0),
            new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTYzMzk3NTk2ODExMCwKICAicHJvZmlsZUlkIiA6ICIyY2ZlNGVkYmU5MjQ0NTdjYWQyMWZiNGRlNDdjY2E4MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJIdWdvR2FtZXJTdHlsZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mYTY2NDc5OTk3Yjk4ODc0YzY1ZjZhMDhkYmZiNmMzMjM4MGZlZDBhYTI3YWY1OGNiMWM2NmNhYmVkNWRhMjgxIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=", "NCxWo5drSlsTCTMMZFEhmoTUO0BHn+48XHezFn2ZkZ+J89+1heI9kR3tHf9SR5MpK1kryXX9pEmP90OOWsCEZZGuUIdcSZlkwLsiXWXGp3dlZe0uDwIFVnkAAhsHkinf4meJOUIbUTieSD/Qxiv5cyXVvIgNwXnpz3wGszSXiU1mgjAHd6GwXWqDD/kjkNlwGGEC2qDIKM/ZLNYyNZyDCwJnDX4MVpFvTHIvtEehujg2hW8iQD2kqzbB/0podNx9FDVBz69SoZ5W0pyUtFNeWYED0zZjAVk/Dg7JnYk3YNOgzHhFxv0yvzRBA7y0+k4Tpbfe9w7wV8XrPNfmhwDLEmB6HSosWsZcYZUSr8PClH5HyN5/DlKbSUZQ0CMCSPO9kpVQAnQgdRLwtPKdFZ9c5Hi2bU1DlLbTFs+vJL/NfKnL8OcwyjxjceWeShI26CQqqUyan/pM7ti4Ih6jsh+z+2vsqfxJ1RvdLQgi7KhX13GCjPAyZaVW+OBifuseRQa0eVsyyL1Vu42/oC+5K/zhxheR/1pk2+sj3zni8AOo6yS5Ds5CJxqqeNLFEe54U6rFrw5QhCXB/p2dyYjRIEjk0eIulqn09kWWZMvblXw4xPMEOA4DtPP5+7M1xdDujiU0oiikl2SWRpZa9lF6IO0cl40SFJBnX9t4PZW3DKpn6/8="),
            Game.KWEEBEC_PARTY,
            null, null, null, null, ItemStack.of(Material.CYAN_WOOL), null),
    CREATIVE("creative",
            new Pos(4.5, 40, 4.5, 180, 0),
            new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTU5MjgxNTExMTYyMywKICAicHJvZmlsZUlkIiA6ICI3MzgyZGRmYmU0ODU0NTVjODI1ZjkwMGY4OGZkMzJmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJ4cWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWFjZTdkYjZhOGJhNDNkZDg3M2Y1OTI0NmE2ZjZkNmVkMDMwMjlhMzVlM2VmNmEwZTZkN2M3OWM3MmE5MjEzOCIKICAgIH0KICB9Cn0=", "bjPpc80nbPwGWc9YRieBuNg77T9fJMix9iOXJyBfSH6vIygy4XWPXzj0vNwYEH2+rngMubk0R6BUmEBuCsh8tbvT67SM/qKyAFckCzw8avbdZ+w08AmRIuH4U91yYpqAM5BsShQaHAQIE87PtfiHlRyhwIBXrWse4M2AB3FiWgwUUW2s1BxdvixSltQKj7XsBLILAKkvCWsP/vkEtmNsV+YqnEkkkMM/01TUsVMytFQTyvDqt9Ev3YosM0yvjBAPJMe4PX5gP57WhCbYgV7BedyDnw9txPM8P2GiAshwc9LL3Ijhyx4hnKHQddC32J5gzZa3o6u77yksL9dbDa1t+QYL0uYDYzlrLKM5iCcooagIRQI/LA2T7aaCvpIHo+3E+LLryRCRUGR1eRCy6BaLM9UaOjVn16IRPVN065xlZpz8WhnSzeYdZUfMzd+xUrgFRTkT8ajZ4DoIs165lG4ZylF0dRydfEj1/uSWZ5mPa6fiBAvoXycEGUj9fvgjTDuues/M60HXMPDXEpd1tYAX2QDFl4p+i35U6B9+zQ4SELfUZxhudfspiGnR83h0+2Ir82YVb+YIIzHg+TCgq69xvim1/Ry+XvFB+BQy4KH1sL0NrUfillsQRDMaFiy381/D1FvzwOYSte7dRxhhjUgzdvSkiAL6SbF2MvapzsyfDSo="),
            Game.CREATIVE,
            null, null, null, null, ItemStack.of(Material.POLISHED_ANDESITE), null),
    SAVE_THE_KWEEBECS("save_the_kweebecs",
            new Pos(-5.5, 40, 4.5, 180, 0),
            new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTYzNTA4NjQ0NDI5OCwKICAicHJvZmlsZUlkIiA6ICJhYzYxZjQwZGJhNDE0YzkwOWU0NWJkMTgwMmY5MTYxYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbmlmYW5pIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2VhNGY3NzhjZDhjOGI4YmYzOTFlMzk0MzQwOWY4OGFmYjQxYWUzZDcwYTkzZWE3NGFjZDJmMTAxMjNiN2RlNDYiCiAgICB9CiAgfQp9", "RqzeC3kSFcCR6D4FtYvg0cL+xbRuuxc7edBACdeYbEfyUdRzBJ0AzoelnHwX9O2zkHTe/QduNI7o2HZLbo5VuAsaZItpLpcx9+GrxP64rEwwjcNVUXqXTKnBagZABsXYYyxEDTl8tKBYtciMmQJf4FcOjlhaZR8xjph/RAI26YWYnNQJE5FATfC6YDJH0mkCY80zk2t+aEJOAmYhWJPb5HkQFzM8JbsaeteFKy4ivyw4uFBjqJQrC8Eu0whowi/2m2pVbUxW4rlrvTKZ9RiW3qVUkxkiixKU610qRfj0rMf9IJfeeSu9O4hHTV6XSfTxSpUSOADUX+6UwS6tF7YxvSJ8FjuPLkuOdjQxpqr1ClzF6b0CtkKxYBlOxnzyw098gnH+jhCTkCzZ1z7g0JRG9V07gz5vDvSBXQV3i4DIuHMHAMdajPbi4AXL6bJSt99CA3FASMeYc3Mn/emyoD0ygx7qEQFeOQyU0O0CR735yHvTKyttB65C6ToMuj4Nb5Boey1+rOQ9nlr4Sc/ok74AJi4mkUDUpCJ6jFRkFiH1PH+XhdOFaqVnyMo9zWaFSABiowzpbNc6ZkU1ah70tumMTunT8SQxJoFKYgP9FUVAKaDpBEQC58BlCaWRxAwvG/qqQz+MWnKLKxoqPWzgZ+LT9zSr6LqgMSiCgHEopv4/qPw="),
            Game.SAVE_THE_KWEEBECS,
            null, null, null, ItemStack.of(Material.IRON_BOOTS), ItemStack.of(Material.STONE_SWORD), ItemStack.of(Material.COMPASS)),
    ;

    private final String serverName;
    private final Pos npcPosition;
    private final PlayerSkin npcSkin;
    private final Game gameToBeSent;
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;
    private final ItemStack mainHand;
    private final ItemStack offHand;

    private final TextNPC npc;

    ServerJoinNPC(String serverName,
                  Pos npcPosition,
                  PlayerSkin npcSkin,
                  Game gameToBeSent,

                  ItemStack helmet,
                  ItemStack chestplate,
                  ItemStack leggings,
                  ItemStack boots,
                  ItemStack mainHand,
                  ItemStack offHand) {
        this.serverName = serverName;
        this.npcPosition = npcPosition;
        this.npcSkin = npcSkin;

        this.gameToBeSent = gameToBeSent;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.mainHand = mainHand;
        this.offHand = offHand;

        Instance instance = MinecraftServer.getInstanceManager().getInstances().iterator().next();

        this.npc = new TextNPC(instance, this.npcPosition, this.npcSkin, TriState.FALSE, npcInteraction -> this.gameToBeSent.send(npcInteraction.player()),
                Component.text(gameToBeSent.getGameName(), NamedTextColor.AQUA),
                Component.text("0 playing", NamedTextColor.YELLOW), //TODO: Get from Velocity
                Component.text("CLICK TO PLAY", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));
        equipNPC();
    }

    public PlayerSkin getNpcSkin() {
        return npcSkin;
    }

    private void equipNPC() {
        final ItemStack air = ItemStack.of(Material.AIR);

        this.npc.setHelmet(this.helmet == null ? air : this.helmet);
        this.npc.setChestplate(this.chestplate == null ? air : this.chestplate);
        this.npc.setLeggings(this.leggings == null ? air : this.leggings);
        this.npc.setBoots(this.boots == null ? air : this.boots);
        this.npc.setItemInMainHand(this.mainHand == null ? air : this.mainHand);
        this.npc.setItemInOffHand(this.offHand == null ? air : this.offHand);
    }

    public String getServerName() {
        return serverName;
    }
}
