package lt.tomexas.skyblock.Command;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import lt.tomexas.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendMailCommand implements CommandExecutor {

    public Skyblock plugin;

    public SendMailCommand(Skyblock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();

            if (label.equalsIgnoreCase("sendmail")) {
                if (args.length < 1) { player.sendMessage(this.plugin.messages.get("mailboxUsage")); return true; }
                if (item.getType().equals(Material.AIR)) {
                    player.sendMessage(this.plugin.messages.get("noItemInHand"));
                    return true;
                }
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                Island island = SuperiorSkyblockAPI.getPlayer(targetPlayer).getIsland();

                if (this.plugin.mailboxInv.get(island.getOwner().getUniqueId()) != null) {
                    if (this.plugin.mailboxInv.get(island.getOwner().getUniqueId()).length > 8) {
                        player.sendMessage(this.plugin.messages.get("fullMailboxInv"));
                        return true;
                    }
                }

                this.sendItem(island, item);

                player.sendMessage(this.plugin.messages.get("sentItemToPlayer"));
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
        }
        return false;
    }

    private void sendItem(Island island, ItemStack item) {

        List<ItemStack> items = new ArrayList<>();
        if (this.plugin.mailboxInv.containsKey(island))
            items.addAll(Arrays.asList(this.plugin.mailboxInv.get(island)));
        items.add(item);

        this.plugin.mailboxInv.put(island, items.toArray(new ItemStack[0]));
        for (SuperiorPlayer superiorPlayer : island.getIslandMembers(true)) {
            Player player = Bukkit.getPlayer(superiorPlayer.getUniqueId());
            if (player == null) return;
            if (player.isOnline())
                player.sendMessage(this.plugin.messages.get("receivedItemInMailbox"));
        }
    }
}
