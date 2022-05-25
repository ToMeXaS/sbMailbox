package lt.tomexas.skyblock.Packets;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import lt.tomexas.skyblock.Skyblock;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.IMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class MailboxEntity {

    private static final Map<Player, EntityArmorStand> asList = new HashMap<>();
    private static final List<Pair<EnumItemSlot, ItemStack>> equipmentList = new ArrayList<>();
    private static final Skyblock plugin = Skyblock.getPlugin(Skyblock.class);

    public static void createEntity(Player player, Location loc) {

        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (asList.containsKey(player))
            return;
        if (!plugin.mailboxHead.containsKey(island))
            plugin.mailboxHead.put(island, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjRiZDlkZDEyOGM5NGMxMGM5NDVlYWRhYTM0MmZjNmQ5NzY1ZjM3YjNkZjJlMzhmN2IwNTZkYzdjOTI3ZWQifX19");

        WorldServer world = ((CraftWorld) player.getWorld()).getHandle();
        EntityArmorStand as = new EntityArmorStand(world, loc.getX(), loc.getY(), loc.getZ());

        as.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        as.setSmall(true);
        as.setInvisible(true);
        MailboxHolo.createHolo(player, loc.getBlock());

        equipmentList.add(new Pair<>(EnumItemSlot.f, CraftItemStack.asNMSCopy(getSkull(plugin.mailboxHead.get(island)))));

        addEntityPacket(player, as);
        asList.put(player, as);
    }

    public static void addEntityPacket(Player player, EntityArmorStand as) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        connection.sendPacket(new PacketPlayOutSpawnEntity(as));
        connection.sendPacket(new PacketPlayOutEntityMetadata(as.getId(), as.getDataWatcher(), true));
        connection.sendPacket(new PacketPlayOutEntityEquipment(as.getId(), equipmentList));
    }

    public static void removeEntityPacket(Player p) {
        for (Player player : asList.keySet()) {
            if (p == player) {
                PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                connection.sendPacket(new PacketPlayOutEntityDestroy(asList.get(player).getId()));
                asList.remove(p);
            }
        }
    }

    private static org.bukkit.inventory.ItemStack getSkull(String base64Str) {
        org.bukkit.inventory.ItemStack head = new org.bukkit.inventory.ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", base64Str));
        Field profileField = null;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

    public static Map<Player, EntityArmorStand> getAsList() {
        return asList;
    }
}
