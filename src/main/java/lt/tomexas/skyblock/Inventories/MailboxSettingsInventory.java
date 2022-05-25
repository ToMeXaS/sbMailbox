package lt.tomexas.skyblock.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MailboxSettingsInventory implements InventoryHolder {
    private final Inventory inv;

    public MailboxSettingsInventory() {
        inv = Bukkit.createInventory(this, 36, "✉ Nustatymai");
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
