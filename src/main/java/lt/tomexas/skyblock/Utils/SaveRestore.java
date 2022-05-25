package lt.tomexas.skyblock.Utils;

import com.bgsoftware.superiorskyblock.api.island.Island;
import lt.tomexas.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SaveRestore {

    private final Skyblock plugin;

    public SaveRestore(Skyblock plugin)  {
        this.plugin = plugin;
    }


    public void saveMailboxInvs(Island island) {
        if (!this.plugin.mailboxInv.containsKey(island)) return;
        List<ItemStack> items = new ArrayList<>(Arrays.asList(this.plugin.mailboxInv.get(island)));
        this.plugin.mailboxData.getConfig().set("mailbox." + island.getOwner().getName() + ".inventory", items);
        this.plugin.mailboxData.saveConfig();
    }

    public void restoreMailboxInvs(Island island) {
        if (this.plugin.mailboxData.getConfig().get("mailbox." + island.getOwner().getName() + ".inventory") == null) return;
        ItemStack[] item = ((List<ItemStack>) this.plugin.mailboxData.getConfig().get("mailbox." + island.getOwner().getName() + ".inventory")).toArray(new ItemStack[0]);
        this.plugin.mailboxInv.put(island, item);
        this.plugin.mailboxData.getConfig().set("mailbox." + island.getOwner().getName() + ".inventory", null);
        this.plugin.mailboxData.saveConfig();
    }

    public void saveMailboxLoc(Island island) {
        if (!this.plugin.mailboxLoc.containsKey(island)) return;
        Location location = this.plugin.mailboxLoc.get(island);
        this.plugin.mailboxData.getConfig().set("mailbox." + island.getOwner().getName() + ".location", location);
        this.plugin.mailboxData.saveConfig();
    }

    public void restoreMailboxLoc(Island island) {
        if (this.plugin.mailboxData.getConfig().get("mailbox." + island.getOwner().getName() + ".location") == null) return;
        Location loc = this.plugin.mailboxData.getConfig().getLocation("mailbox." + island.getOwner().getName() + ".location");
        this.plugin.mailboxLoc.put(island, loc);
        this.plugin.mailboxData.saveConfig();
    }

    public void saveMailboxHeads(Island island) {
        if (!this.plugin.mailboxHead.containsKey(island)) return;
        String base64String = this.plugin.mailboxHead.get(island);
        this.plugin.mailboxData.getConfig().set("mailbox." + island.getOwner().getName() + ".head", base64String);
        this.plugin.mailboxData.saveConfig();
    }

    public void restoreMailboxHeads(Island island) {
        if (this.plugin.mailboxData.getConfig().get("mailbox." + island.getOwner().getName() + ".head") == null) return;
        String base64String = this.plugin.mailboxData.getConfig().getString("mailbox." + island.getOwner().getName() + ".head");
        this.plugin.mailboxHead.put(island, base64String);
        this.plugin.mailboxData.saveConfig();
    }
}
