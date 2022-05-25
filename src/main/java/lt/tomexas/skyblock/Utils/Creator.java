package lt.tomexas.skyblock.Utils;

import com.bgsoftware.superiorskyblock.api.island.Island;
import lt.tomexas.skyblock.Packets.MailboxEntity;
import lt.tomexas.skyblock.Packets.MailboxHolo;
import lt.tomexas.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Creator {

    private final Skyblock plugin;

    public Creator(Skyblock plugin) {
        this.plugin = plugin;
    }

    public void createMailbox(Island island) {
        Location loc;
        if (this.plugin.mailboxLoc.containsKey(island)) {
            loc = this.plugin.mailboxLoc.get(island);
        } else {
            loc = island.getCenter(World.Environment.NORMAL).add(0,0,3);
            loc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw()+180.0f, loc.getPitch());
        }

        MailboxEntity.createEntity(island.getOwner().asPlayer(), loc);
        this.plugin.mailboxLoc.put(island, loc);

        Location finalLoc = loc;
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
            Block block = finalLoc.getWorld().getBlockAt(finalLoc);
            block.setType(Material.END_ROD);
            block.getState().update(true);
        }, 1);
    }

    public void removeMailbox(Island island) {
        MailboxHolo.removeHoloPacket(island.getOwner().asPlayer());
        MailboxEntity.removeEntityPacket(island.getOwner().asPlayer());
        Location loc = this.plugin.mailboxLoc.get(island);
        loc.getWorld().getBlockAt(loc).setType(Material.AIR);
    }
}
