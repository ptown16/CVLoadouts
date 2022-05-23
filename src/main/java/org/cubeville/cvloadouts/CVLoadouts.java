package org.cubeville.cvloadouts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.cubeville.commons.commands.CommandParser;

import org.cubeville.cvloadouts.commands.*;
import org.cubeville.cvloadouts.loadout.LoadoutContainer;
import org.cubeville.cvloadouts.loadout.LoadoutManager;
import org.cubeville.cvloadouts.loadout.LoadoutHandler;

import java.util.List;
import java.util.Set;

public class CVLoadouts extends JavaPlugin {

    private LoadoutManager loadoutManager;
    private CommandParser commandParser;
    
    private static CVLoadouts instance;

    public static CVLoadouts getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        ConfigurationSerialization.registerClass(LoadoutContainer.class, "LoadoutContainer");
        ConfigurationSerialization.registerClass(LoadoutManager.class, "LoadoutManager");

        loadoutManager = (LoadoutManager) getConfig().get("LoadoutManager");
        if(loadoutManager == null) loadoutManager = new LoadoutManager();
        loadoutManager.setManager(this);

        commandParser = new CommandParser();
        commandParser.addCommand(new GiveLoadoutItemToPlayer());
        commandParser.addCommand(new LoadoutApply());
        commandParser.addCommand(new LoadoutCreate());
        commandParser.addCommand(new LoadoutClone());
        commandParser.addCommand(new LoadoutEdit());
        commandParser.addCommand(new LoadoutInfo());
        commandParser.addCommand(new LoadoutList());
        commandParser.addCommand(new LoadoutRemove());
        //commandParser.addCommand(new SpawnItem());
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EventListener(), this);
    }

    public void onDisable() {
       instance = null;
    }

    public LoadoutManager getLoadoutManager() {
        return loadoutManager;
    }

    public void saveLoadoutManager() {
        getConfig().set("LoadoutManager", loadoutManager);
        saveConfig();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("loadout")) {
            return commandParser.execute(sender, args);
        }
        return false;
    }

    // Here follow some API functions (well, one, so far ;) )
    public ItemStack getLoadoutItem(String loadoutName, int itemIndex) {
	LoadoutContainer container = loadoutManager.getLoadoutByName(loadoutName);
	if(container == null) return null;
	Inventory inventory = container.getMainInventory();
	return inventory.getItem(itemIndex);
    }

    public ItemStack getLoadoutItem(String loadoutName, String subloadoutName, int itemIndex) {
	LoadoutContainer container = loadoutManager.getLoadoutByName(loadoutName);
	if(container == null) return null;
	Inventory inventory = container.getInventory(subloadoutName);
	return inventory.getItem(itemIndex);
    }

    public void applyLoadoutToPlayer(Player player, String loadoutName, List<String> subLoadoutNames) {
        LoadoutHandler.applyLoadoutToPlayer(player, loadoutName, subLoadoutNames);
    }
}
