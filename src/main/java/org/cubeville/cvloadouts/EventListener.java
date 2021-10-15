package org.cubeville.cvloadouts;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.cubeville.commons.utils.ColorUtils;

import org.cubeville.cvloadouts.loadout.LoadoutContainer;
import org.cubeville.cvloadouts.loadout.LoadoutHandler;
import org.cubeville.cvloadouts.loadout.LoadoutManager;

public class EventListener implements Listener
{
     @EventHandler
     public void onInventoryClose(InventoryCloseEvent event) {
         CVLoadouts pvp = CVLoadouts.getInstance();
         if (pvp.getLoadoutManager().containsInventory(event.getView().getTitle())) {
             pvp.saveLoadoutManager();
             String[] split = event.getView().getTitle().split(":");
             event.getPlayer().sendMessage(ColorUtils.addColor("&aLoadout &6" + split[0] + "&a:&6" + split[1] + " &asaved successfully!"));
         }
     }

    private final Set<Material> signMaterials = new HashSet<>(Arrays.asList(
            Material.OAK_WALL_SIGN,
            Material.SPRUCE_WALL_SIGN,
            Material.BIRCH_WALL_SIGN,
            Material.JUNGLE_WALL_SIGN,
            Material.ACACIA_WALL_SIGN,
            Material.DARK_OAK_WALL_SIGN,
            Material.CRIMSON_WALL_SIGN,
            Material.WARPED_WALL_SIGN,
            Material.OAK_SIGN,
            Material.SPRUCE_SIGN,
            Material.BIRCH_SIGN,
            Material.JUNGLE_SIGN,
            Material.ACACIA_SIGN,
            Material.DARK_OAK_SIGN,
            Material.CRIMSON_SIGN,
            Material.WARPED_SIGN
    ));

     private final String colorCodeRegex = "§[a-fk-o0-9r]";

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (!signMaterials.contains(event.getClickedBlock().getType())) return;
        Sign sign = (Sign) event.getClickedBlock().getState();
        String kitSignLine = sign.getLine(1).replaceAll(colorCodeRegex, "");
        if(kitSignLine.length() == 0 || kitSignLine.charAt(0) != '[') return;

        for (String lString: loadoutAliases) {
            if (kitSignLine.equalsIgnoreCase(lString)) {
                LoadoutHandler.applyLoadoutFromSign(event.getPlayer(), sign, colorCodeRegex);
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onSignChange(SignChangeEvent event) {
        String[] lines = event.getLines().clone();
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll(colorCodeRegex, "");
        }

        if(lines[1].length() == 0 || lines[1].charAt(0) != '[') return;

        {
            boolean found = false;
            for (String alias: loadoutAliases) {
                if (lines[1].equalsIgnoreCase(alias)) {
                    found = true;
                }
            }
            if(!found) return;
        }

        Player player = event.getPlayer();

        if (!(player.hasPermission("cvpvp.admin") || player.hasPermission("snbt.loadout.commands"))) {
            player.sendMessage(ColorUtils.addColor("&cYou do not have permission to make loadout signs!"));
            event.setCancelled(true);
            return;
        }

        LoadoutManager loadoutManager = CVLoadouts.getInstance().getLoadoutManager();
        LoadoutContainer lc = loadoutManager.getLoadoutByName(lines[2]);
        if(lc == null) {
            player.sendMessage(ColorUtils.addColor("&cLoadout &6" + event.getLine(2) + " &cdoes not exist!"));
            event.setCancelled(true);
            return;
        }

        if(!lc.containsInventory(lines[3])) {
            player.sendMessage(ColorUtils.addColor("&cTag &6" + lines[2] + ":" + lines[3] + " &cdoes not exist!"));
            event.setCancelled(true);
            return;
        }

        player.sendMessage(ColorUtils.addColor("&aLoadout sign created successfully!"));

    }

    public static List<String> loadoutAliases = new ArrayList<>(Arrays.asList("[load-out]","[kit]","[class]"));
}
