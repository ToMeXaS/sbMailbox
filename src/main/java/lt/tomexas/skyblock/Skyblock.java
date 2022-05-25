package lt.tomexas.skyblock;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import lt.tomexas.skyblock.Command.SendMailCommand;
import lt.tomexas.skyblock.Listeners.MailboxPlayerListeners;
import lt.tomexas.skyblock.Utils.Creator;
import lt.tomexas.skyblock.Utils.SaveRestore;
import lt.tomexas.skyblock.Utils.mbFileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class Skyblock extends JavaPlugin {

    public HashMap<String, String> messages = new HashMap<>();
    public SaveRestore saveRestore;
    public Creator creator;

    public mbFileManager mailboxData;

    public Map<Island, ItemStack[]> mailboxInv = new HashMap<>();
    public Map<Island, Location> mailboxLoc = new HashMap<>();
    public Map<Island, String> mailboxHead = new HashMap<>();

    @Override
    public void onEnable() {
        setupMessages();
        this.mailboxData = new mbFileManager(this);
        this.saveRestore = new SaveRestore(this);
        this.creator = new Creator(this);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MailboxPlayerListeners(this), this);

        this.getCommand("sendmail").setExecutor(new SendMailCommand(this));

        for (Island island : SuperiorSkyblockAPI.getGrid().getIslands()) {
            this.saveRestore.restoreMailboxLoc(island);
            this.saveRestore.restoreMailboxInvs(island);
            this.saveRestore.restoreMailboxHeads(island);
        }

        for (Island island : SuperiorSkyblockAPI.getGrid().getIslands()) {
            int i = 0;
            for (SuperiorPlayer superiorPlayer : island.getIslandMembers(true)) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(superiorPlayer.getUniqueId());
                if (offlinePlayer.isOnline()) i++;
            }

            if (i > 0) {
                this.creator.createMailbox(island);
            }
        }
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
            if (island == null) continue;
            this.creator.removeMailbox(island);
        }

        if (!mailboxInv.isEmpty()) {
            for (Island island : SuperiorSkyblockAPI.getGrid().getIslands())
                this.saveRestore.saveMailboxInvs(island);
        }

        if (!mailboxLoc.isEmpty()) {
            for (Island island : SuperiorSkyblockAPI.getGrid().getIslands())
                this.saveRestore.saveMailboxLoc(island);
        }

        if (!mailboxHead.isEmpty()) {
            for (Island island : SuperiorSkyblockAPI.getGrid().getIslands())
                this.saveRestore.saveMailboxHeads(island);
        }
    }

    private void setupMessages() {
        messages.put("mailboxUsage", "§cNaudojimas: /sendmail <player_name> - išsiųsite daiktą, kurį laikote rankoje į kito žaidėjo salos pašto dežutę");
        messages.put("sentItemToPlayer", "§aDaiktas, kurį laikėte savo rankoje buvo išsiųstas sėkmingai!");
        messages.put("sentItemToAllPlayers", "§aSuccessfully sent an item to all player islands!");
        messages.put("receivedItemInMailbox", "§eJonas Havaras §8» §fPssst... ej, kątik gavai laišką į savąją pašto dežutę!");
        messages.put("noItemInHand", "§cJūs nelaikote jokio daikto savo rankoje!");
        messages.put("fullMailboxInv", "§cŠios salos pašto dežutės inventorius yra pilnas!");
    }
}
