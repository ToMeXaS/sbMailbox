package lt.tomexas.skyblock.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MailboxInventory implements InventoryHolder {

    private Inventory inv;

    public MailboxInventory() {
        inv = Bukkit.createInventory(this, 18, "✉ Paštas");
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
