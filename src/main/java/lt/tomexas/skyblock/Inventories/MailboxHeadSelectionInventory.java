package lt.tomexas.skyblock.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MailboxHeadSelectionInventory implements InventoryHolder {

    private final Inventory inv;

    public MailboxHeadSelectionInventory () {
        inv = Bukkit.createInventory(this, 36, "✉ Pasirinkite viršūtinę dalį");
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
