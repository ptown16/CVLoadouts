package org.cubeville.cvloadouts.loadout;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.cubeville.commons.utils.PlayerUtils;
import org.cubeville.cvloadouts.CVLoadouts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoadoutHandler {

    public static ItemStack getItem(LoadoutContainer lc, String name, int index) {
        if(lc.getInventory(name) == null) throw new RuntimeException("Loadout not found");
        Inventory inventory = lc.getInventory(name);
        ItemStack item = inventory.getItem(index);
        if(item == null) throw new RuntimeException("Item slot is empty");
        return item;
    }

    public static boolean giveLoadoutItemToPlayer(Player player, LoadoutContainer lc, String name, int index, boolean exclusive) {
        if(lc.getInventory(name) == null) return false;

        Inventory inventory = lc.getInventory(name);

        Inventory playerInventory = player.getInventory();
        int freeSlot = playerInventory.firstEmpty();
        if(freeSlot == -1) throw new RuntimeException("No free slot in player's inventory");

        ItemStack item = inventory.getItem(index);
        if(item == null) return true;
        
        if(exclusive) {
            if(playerInventory.containsAtLeast(item, 1)) return true;
        }
        
        playerInventory.setItem(freeSlot, item);

        return true;
    }

    public static boolean takeLoadoutItemFromPlayer(Player player, LoadoutContainer lc, String name, int index) {
        if(lc.getInventory(name) == null) return false;

        Inventory inventory = lc.getInventory(name);
        Inventory playerInventory = player.getInventory();

        ItemStack item = inventory.getItem(index);
        if(item == null) return true;
        String itemName = item.getItemMeta().getDisplayName();

        int size = playerInventory.getSize();
        for(int i = 0; i < size; i++) {
            if(playerInventory.getItem(i) != null) {
                if(playerInventory.getItem(i).getItemMeta() != null) {
                    if(playerInventory.getItem(i).getItemMeta().getDisplayName() != null) {
                        if(playerInventory.getItem(i).getItemMeta().getDisplayName().equals(itemName)) {
                            playerInventory.setItem(i, null);
                        }
                    }
                }
            }
        }

        return true;
    }

    public static int getLoadoutSize(LoadoutContainer lc, String name) {
        Inventory inventory = lc.getInventory(name);
        int count = 0;
        for(int i = 0; i < 54; i++) {
            if(inventory.getItem(i) != null) {
                count++;
            }
        }
        return count;
    }

    public static boolean applyLoadoutToPlayer(Player player, LoadoutContainer lc, String subloadoutName) {
        List<String> subLoadoutNames = new ArrayList<>();
        subLoadoutNames.add(subloadoutName);
        return applyLoadoutToPlayer(player, lc, subLoadoutNames);
    }

    private static ItemStack getLoadoutItemAtIndex(LoadoutContainer lc, Inventory baseInventory, List<Inventory> subInventories, int Index) {
        ItemStack item = null;
        for(Inventory i: subInventories) {
            if(i.getItem(Index) != null) {
                item = i.getItem(Index);
                break;
            }
        }
        if(item == null) item = baseInventory.getItem(Index);
        return item;
    }

    public static boolean applyLoadoutToPlayer(Player player, String loadoutName, List<String> subloadoutNames) {
        LoadoutContainer lc = CVLoadouts.getInstance().getLoadoutManager().getLoadoutByName(loadoutName);
        if(lc == null) return false;
        return applyLoadoutToPlayer(player, lc, subloadoutNames);
    }
    
    public static boolean applyLoadoutToPlayer(Player player, LoadoutContainer lc, List<String> subloadoutNames) {
        List<Inventory> subInventories = new ArrayList<>();
        for(String subloadoutName: subloadoutNames) {
            if(lc.getInventory(subloadoutName) != null)
                subInventories.add(lc.getInventory(subloadoutName));
            else
                return false;
        }
        Inventory baseInventory = lc.getMainInventory();

        //Offhand
        {
            ItemStack item = getLoadoutItemAtIndex(lc, baseInventory, subInventories, 49);
            if(item == null || item.getType() != Material.BARRIER)
                player.getInventory().setItemInOffHand(item);
        }

        // Hotbar and inventory contents
        for (int i = 0; i < 36; i++) {
            ItemStack item = getLoadoutItemAtIndex(lc, baseInventory, subInventories, i);
            if(item == null || item.getType() != Material.BARRIER)
                player.getInventory().setItem(i, item);
        }

        //Armor
        int x = 45;
        for (int i = 39; i >= 36; i--) {
            ItemStack item = getLoadoutItemAtIndex(lc, baseInventory, subInventories, x);
            if(item == null || item.getType() != Material.BARRIER)
                player.getInventory().setItem(i, item);
            x++;
        }

        //Update Inventory
        player.updateInventory();

        return true;
    }
	
    public static boolean applyLoadoutFromSign(Player player, Sign sign, String regexFilter) {
        LoadoutContainer lc = CVLoadouts.getInstance().getLoadoutManager().getLoadoutByName(sign.getLine(2).replaceAll(regexFilter, ""));
		
        if (lc == null)
            return false;
		
        applyLoadoutToPlayer(player, lc, sign.getLine(3).replaceAll(regexFilter, ""));
        return true;
    }
    
    public static void cloneInventoryToLoadout(Player player, LoadoutContainer lc, String team) {
        Inventory inventory;
        if (team.equalsIgnoreCase("main")) {
            inventory = lc.getMainInventory();
        } else {
            return;
        }
        inventory.clear();
        inventory.setItem(49, player.getInventory().getItemInOffHand());
        for (int i = 0; i < 36; i++) inventory.setItem(i, player.getInventory().getItem(i));
        inventory.setItem(45, player.getInventory().getHelmet());
        inventory.setItem(46, player.getInventory().getChestplate());
        inventory.setItem(47, player.getInventory().getLeggings());
        inventory.setItem(48, player.getInventory().getBoots());
        CVLoadouts.getInstance().getConfig().set("LoadoutManager", CVLoadouts.getInstance().getLoadoutManager());
        CVLoadouts.getInstance().saveConfig();
    }
    
    // public static double getHealthOfArmorContents(Player player) {
    //     double health = player.getMaxHealth();
    //     if (player.getInventory().getHelmet() != null) {
    //     	NBTItem helmet = new NBTItem(player.getEquipment().getHelmet());
    //     	health += helmet.getAttributeAmountByName(AttributeType.GENERIC_MAX_HEALTH, EquipmentSlot.HEAD);
    //     }
    //     if (player.getInventory().getChestplate() != null) {
    //     	NBTItem chestplate = new NBTItem(player.getEquipment().getChestplate());
    //     	health += chestplate.getAttributeAmountByName(AttributeType.GENERIC_MAX_HEALTH, EquipmentSlot.CHEST);
    //     }
    //     if (player.getInventory().getLeggings() != null) {
    //     	NBTItem leggings = new NBTItem(player.getEquipment().getLeggings());
    //     	health += leggings.getAttributeAmountByName(AttributeType.GENERIC_MAX_HEALTH, EquipmentSlot.LEGS);
    //     }
    //     if (player.getInventory().getBoots() != null) {
    //     	NBTItem boots = new NBTItem( player.getEquipment().getBoots());
    //     	health += boots.getAttributeAmountByName(AttributeType.GENERIC_MAX_HEALTH, EquipmentSlot.FEET);
    //     }
    //     return health;
    // }
	
}
