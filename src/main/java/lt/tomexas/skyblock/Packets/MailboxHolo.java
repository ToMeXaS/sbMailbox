package lt.tomexas.skyblock.Packets;

import lt.tomexas.skyblock.Skyblock;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MailboxHolo {

    private static final Map<Player, EntityArmorStand> holoList = new HashMap<>();
    private static final Map<Player, EntityArmorStand> holoList2 = new HashMap<>();
    private static final Map<Player, EntityArmorStand> holoList3 = new HashMap<>();
    private static String HoloCustomName = "&e✉ &fPašto dežutė &e✉";
    private static String Holo2ndLine = "&e&lSPAUSKITE ČIA";
    private static String HolohasMail = "&cJūs turite laiškų!";

    private static Skyblock plugin = Skyblock.getPlugin(Skyblock.class);

    public static void createHolo(Player player, Block block) {

        if (holoList.containsKey(player))
            return;

        Location loc = block.getLocation().add(0.5,0.3,0.5);
        WorldServer world = ((CraftWorld) player.getWorld()).getHandle();
        EntityArmorStand as = new EntityArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
        HoloCustomName = ChatColor.translateAlternateColorCodes('&', HoloCustomName);

        as.setSmall(true);
        as.setInvisible(true);
        as.setCustomNameVisible(true);
        as.setCustomName(new ChatComponentText(HoloCustomName));

        addHoloPacket(player, as);
        holoList.put(player, as);

        EntityArmorStand as2 = new EntityArmorStand(world, loc.getX(), loc.getY()-0.28, loc.getZ());
        Holo2ndLine = ChatColor.translateAlternateColorCodes('&', Holo2ndLine);

        as2.setSmall(true);
        as2.setInvisible(true);
        as2.setCustomNameVisible(true);
        as2.setCustomName(new ChatComponentText(Holo2ndLine));

        addHoloPacket(player, as2);
        holoList2.put(player, as2);
    }

    public static void addHoloPacket(Player player, EntityArmorStand as) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        connection.sendPacket(new PacketPlayOutSpawnEntity(as));
        connection.sendPacket(new PacketPlayOutEntityMetadata(as.getId(), as.getDataWatcher(), true));
    }

    public static void removeHoloPacket(Player p) {
        for (Player player : holoList.keySet()) {
            if (p == player) {
                PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                connection.sendPacket(new PacketPlayOutEntityDestroy(holoList.get(player).getId()));
                holoList.remove(p);
            }
        }

        for (Player player : holoList2.keySet()) {
            if (p == player) {
                PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
                connection.sendPacket(new PacketPlayOutEntityDestroy(holoList2.get(player).getId()));
                holoList2.remove(p);
            }
        }

    }

    public static Map<Player, EntityArmorStand> getholoList() {
        return holoList;
    }
    public static Map<Player, EntityArmorStand> getholoList2() {
        return holoList2;
    }

    public static String getHoloCustomName() {
        return HoloCustomName;
    }
}
