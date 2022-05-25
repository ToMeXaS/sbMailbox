package lt.tomexas.skyblock.Listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lt.tomexas.skyblock.Inventories.MailboxHeadSelectionInventory;
import lt.tomexas.skyblock.Inventories.MailboxInventory;
import lt.tomexas.skyblock.Packets.MailboxEntity;
import lt.tomexas.skyblock.Packets.MailboxHolo;
import lt.tomexas.skyblock.Skyblock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;

public class MailboxPlayerListeners implements Listener {

    private final Skyblock plugin;
    private final Inventory inv;
    private final Inventory headSelectionInv;

    List<String> nameList1 = Arrays.asList(
            "&eŽalia", "&eRuda", "&eElektrinė", "&eŠviesiai purpurinė", "&eŽydra", "&eTamsiai pilka", "&eŠviesiai pilka"
    );

    List<String> base64List1 = Arrays.asList(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTVmYmJjNjI1ZmE0ZWI2NDk2YmU4ZGJiZjBhYTJiMjhmMTAyOTdjZmZiY2Y1ZTBhYWY2Y2IxMWU4ZjI2MTZlZCJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTg1ZDA4OTAwNmE4Yzc1NjViZWJkOWQ0NGRhYjY1ODkyMjM1NDk3NTg0MTQ1Y2Y0OTc0ZGY5ZGNhMDRkYjc0NSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc5ZjAyNDAyZWI1MjMwMDZhZmJjNDNiNjY2Mjg3OGExYmNlZDlhNDI4MjlmZTIyODMwMDBmMmExOGM4OWM4OSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODg1MzAwZjYzZTUyYWI2Y2FiMTc5NTUwNTBiNzU3NTU2ZDU2YzRlM2Q4MzIyMGIzMjk1ZTMwNWQwMTA2ZWZhIn19fQ==",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRiMzQ5ZTc0Y2Y5YTU1YWYwMzFlMzkyYWE4MDhjNWMxNjFlMTU3YTRlYmI3ODQxNDQ4MWNhMGNiYmEyZmFlMyJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjk2YWYzNmVhYWVkOGE3ZGJkMjcyN2ZkNGNkN2FmYmM2YzBhZmI4Yzc2MDYyOTRjOWNiNDAxMTQxYWFjMzc5In19fQ==",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzBhZTNkNTA2NDIzNzllOThhNjRkNGEwNmJiNGVmOTRhMzRiNDc4NmRhMzc4NGE5MzBkOTM0NmVjNjExM2QyIn19fQ=="
    );

    List<String> nameList2 = Arrays.asList("&eRaudona", "&eOrandžinė", "&eGeltona", "&eMėlyna", "&eVioletinė", "&eŠviesiai žalia", "&eBalta");

    List<String> base64List2 = Arrays.asList(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGVhYjQxZTNjYjg3NjllYmFkMmE4MjFkNTVjY2I4NWE0YTk0MGRkNWM2ZThiZjczOGIwYjEyMmY4NzkxZSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk2MDEyNjI5ODgzOTI5MjdmOWU1ZWUxMmJlZGQ1YmE5ZjRjMDE3OTc2ODFmMTUzNTI5Y2Q0M2UyMzQ4OGU0In19fQ==",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI4MWMwMTUzOWM3YzgzYjc0MjdmYzBiMTgwYjNkYzg4NGExNDM4ZDZiZWU0OGNlY2NmYmVmNzkxZmNhYyJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTVkOWI2ZDQ3ZTk3MzYyZjljNGJiY2Y5ZTJhN2FiMzEyMzgzZGU2ZTYzNTQ5Y2YwNWUyY2UzYjEzYmMxMTMyMCJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU4NWU2ODg4YzE5OTE4ZWE3ZGUxYmY2Y2E4ZGRlYTVlYjgzM2E4M2U4NWM0NmQ0ZTdlZjc3MzllMmY2In19fQ==",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjcxMzI3NDNjMDRlNTRmMmI0MTBkZDIwMGMwMGE3M2RmZDE4ODEyZGI3OTZmMzI4MTBhYWMwZDZlNzc4ZWY3MSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFmNTBmMTcyYTUxMGZhNjBiMmFmMGNiYjg0ZjM3MTUzNmM5NjVlYTU1MDg4MTI1M2Y3ZWNkMjY0MDc5NjllYiJ9fX0="
    );

    public MailboxPlayerListeners(Skyblock plugin) {
        this.plugin = plugin;
        inv = new MailboxInventory().getInventory();
        headSelectionInv = new MailboxHeadSelectionInventory().getInventory();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof MailboxInventory) {
            if (event.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)
                    || event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)
                    || event.getAction().equals(InventoryAction.PICKUP_ALL)
                    || event.getAction().equals(InventoryAction.PICKUP_HALF)
                    || event.getAction().equals(InventoryAction.PICKUP_SOME)
                    || event.getAction().equals(InventoryAction.PICKUP_ONE)
                    || event.getAction().equals(InventoryAction.PLACE_ALL)
                    || event.getAction().equals(InventoryAction.PLACE_SOME)
                    || event.getAction().equals(InventoryAction.PLACE_ONE)
                    || event.getAction().equals(InventoryAction.HOTBAR_MOVE_AND_READD)
                    || event.getAction().equals(InventoryAction.HOTBAR_SWAP)
                    || event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)
                    || event.getAction().equals(InventoryAction.DROP_ALL_CURSOR)
                    || event.getAction().equals(InventoryAction.DROP_ALL_SLOT)
                    || event.getAction().equals(InventoryAction.DROP_ONE_CURSOR)
                    || event.getAction().equals(InventoryAction.DROP_ONE_SLOT))
                event.setCancelled(true);
        }
        if (event.getView().getTopInventory().getHolder() instanceof MailboxHeadSelectionInventory) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getClickedInventory().getHolder() instanceof MailboxInventory) {
                ItemStack item = event.getClickedInventory().getItem(event.getRawSlot());
                if (item == null) return;

                if (event.getRawSlot() < 9) {
                    Island island = SuperiorSkyblockAPI.getPlayer((Player) event.getWhoClicked()).getIsland();

                    List<ItemStack> items = new ArrayList<>();
                    if (this.plugin.mailboxInv.containsKey(island))
                        items.addAll(Arrays.asList(this.plugin.mailboxInv.get(island)));
                    items.remove(item);
                    this.plugin.mailboxInv.put(island, items.toArray(new ItemStack[0]));

                    event.getWhoClicked().getInventory().addItem(item);
                    item.setType(Material.AIR);
                    event.getClickedInventory().setItem(event.getRawSlot(), item);
                }

                Player player = (Player) event.getWhoClicked();
                switch (event.getRawSlot()) {
                    case 17: {
                        player.closeInventory();

                        init(player, "headSelection");
                        player.openInventory(headSelectionInv);
                        break;
                    }

                    case 16: {
                        player.closeInventory();
                    }
                }
            }

            if (event.getClickedInventory().getHolder() instanceof MailboxHeadSelectionInventory) {
                Player player = (Player) event.getWhoClicked();
                Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
                ItemStack item = event.getClickedInventory().getItem(event.getRawSlot());
                NBTTagCompound tag = CraftItemStack.asNMSCopy(item).getTag();
                if (item != null && tag != null && item.getType().equals(Material.PLAYER_HEAD)) {
                    String base64String = tag.getCompound("SkullOwner").getCompound("Properties").get("textures").asString();
                    base64String = base64String.replace("[{Value:\"", "");
                    base64String = base64String.replace("\"}]", "");
                    this.plugin.creator.removeMailbox(island);
                    this.plugin.mailboxHead.put(island, base64String);
                    this.plugin.creator.createMailbox(island);
                    player.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onMBoxRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if(!event.hasBlock())
            return;
        if (event.getClickedBlock() == null)
            return;
        if(!event.getClickedBlock().getType().equals(Material.END_ROD))
            return;
        if(island == null)
            return;
        if(!this.plugin.mailboxLoc.containsKey(island))
            return;
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        event.setCancelled(true);

        if (this.plugin.mailboxInv.containsKey(island))
            inv.setContents(this.plugin.mailboxInv.get(island));

        init(player, "main");
        player.openInventory(inv);

    }

    @EventHandler
    private void onMBoxPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        Block block = event.getBlock();
        if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(MailboxHolo.getHoloCustomName())) {
            event.setCancelled(true);
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            Location loc = new Location(
                    block.getWorld(),
                    block.getLocation().getX()+0.5,
                    block.getLocation().getY(),
                    block.getLocation().getZ()+0.5,
                    event.getPlayer().getLocation().getYaw()+180.f,
                    0
            );
            this.plugin.creator.removeMailbox(island);
            this.plugin.mailboxLoc.put(island, loc);
            this.plugin.creator.createMailbox(island);
        }
    }

    @EventHandler
    public void MailboxBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        Location loc = event.getBlock().getLocation();

        if (event.getBlock().getType().equals(Material.END_ROD)) {
            if (loc.distanceSquared(this.plugin.mailboxLoc.get(island)) == 0.5) {
                event.setCancelled(true);
                this.plugin.creator.removeMailbox(island);
                player.getInventory().addItem(getSkull(this.plugin.mailboxHead.get(island),
                        MailboxHolo.getHoloCustomName(), null));
            }
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        for (Block block : event.blockList()) {
            for (Location loc : this.plugin.mailboxLoc.values()) {
                if (block.getLocation().distanceSquared(loc) == 0.5)
                    event.blockList().remove(block);
            }
        }
    }

    @EventHandler
    public void onEntExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            for (Location loc : this.plugin.mailboxLoc.values()) {
                if (block.getLocation().distanceSquared(loc) == 0.5)
                    event.blockList().remove(block);
            }
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (island != null ) {
            this.plugin.creator.createMailbox(island);
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        if (island != null) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                for (SuperiorPlayer superiorPlayer : island.getIslandMembers(true)) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(superiorPlayer.getUniqueId());
                    if (offlinePlayer.isOnline()) return;
                }

                this.plugin.creator.removeMailbox(island);

            }, 1);
        }
    }

    @EventHandler
    public void onIslandCreate(IslandCreateEvent event) {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            this.plugin.creator.createMailbox(event.getIsland());

        }, 20);
    }

    @EventHandler
    public void onIslandDisband(IslandDisbandEvent event) {
        this.plugin.creator.removeMailbox(event.getIsland());
        this.plugin.mailboxLoc.remove(event.getIsland());
        this.plugin.mailboxInv.remove(event.getIsland());
        this.plugin.mailboxHead.remove(event.getIsland());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (event.getFrom().getName().equalsIgnoreCase("superiorworld"))
            return;

        for (Player p : Bukkit.getOnlinePlayers()) {
            for (EntityArmorStand as : MailboxEntity.getAsList().values()) {
                MailboxEntity.addEntityPacket(p, as);
            }
            for (EntityArmorStand as : MailboxHolo.getholoList().values()) {
                MailboxHolo.addHoloPacket(p, as);
            }
            for (EntityArmorStand as : MailboxHolo.getholoList2().values()) {
                MailboxHolo.addHoloPacket(p, as);
            }
        }
    }



    private void init(Player player, String type) {
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        PacketPlayOutAnimation animation = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), 0);
        ((CraftPlayer) player).getHandle().b.sendPacket(animation);

        if (type.equals("main")) {
            for (int i = 10; i < inv.getSize(); i++)
                inv.setItem(i - 1, createInvItem(Material.BLACK_STAINED_GLASS_PANE, "&c", null));

            inv.setItem(16, createInvItem(Material.LIME_DYE, "&eHologramos įjungimas/išjungimas", Arrays.asList(
                    "&fČia galite paspausti",
                    "&fnorėdami įjungti arba išjungti",
                    "&fhologramą esančia virš dežutės!"
            )));
            inv.setItem(17, getSkull(this.plugin.mailboxHead.get(island), "&eSpalvos keitimas", Arrays.asList(
                    "&fČia galite pakeisti savosios",
                    "&fpašto dežutės spalvą!"
            )));
        }

        if (type.equals("headSelection")) {
            for (int i = 0; i < headSelectionInv.getSize(); i++)
                headSelectionInv.setItem(i, createInvItem(Material.BLACK_STAINED_GLASS_PANE, "", null));

            headSelectionInv.setItem(4, getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjRiZDlkZDEyOGM5NGMxMGM5NDVlYWRhYTM0MmZjNmQ5NzY1ZjM3YjNkZjJlMzhmN2IwNTZkYzdjOTI3ZWQifX19", "&eĮprasta pašto dežutė", null));

            for (int i = 0; i < base64List1.size(); i++) {
                    headSelectionInv.setItem(i + 10, getSkull(base64List1.get(i), nameList1.get(i), null));
            }

            for (int i = 0; i < base64List2.size(); i++) {
                headSelectionInv.setItem(i + 19, getSkull(base64List2.get(i), nameList2.get(i), null));
            }
        }
    }

    private ItemStack createInvItem(Material material, String displayName, @Nullable List<String> lore) {
        displayName = ChatColor.translateAlternateColorCodes('&', displayName);

        if (lore != null)
            for (int i = 0; i < lore.size(); i++)
                lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));

        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private static org.bukkit.inventory.ItemStack getSkull(String base64Str, String displayName, List<String> lore) {
        org.bukkit.inventory.ItemStack head = new org.bukkit.inventory.ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (displayName != null)
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++)
                lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
            meta.setLore(lore);
        }
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", base64Str));
        Field profileField;
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
}
