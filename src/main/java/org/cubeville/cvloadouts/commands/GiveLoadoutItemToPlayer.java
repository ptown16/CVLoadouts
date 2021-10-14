package org.cubeville.cvloadouts.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterInteger;
import org.cubeville.commons.commands.CommandParameterOnlinePlayer;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvloadouts.CVLoadouts;
import org.cubeville.cvloadouts.loadout.LoadoutContainer;
import org.cubeville.cvloadouts.loadout.LoadoutHandler;

public class GiveLoadoutItemToPlayer extends BaseCommand {

    public GiveLoadoutItemToPlayer() {
        super("giveitem");
        setPermission("cvloadouts.commands");
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterInteger());
        addParameter("team", true, new CommandParameterString());
        addParameter("player", true, new CommandParameterOnlinePlayer());
        addFlag("exclusive");
        addFlag("sound");
        addFlag("take");
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {
        
        Player playerInv = null;
        if(sender instanceof Player) playerInv = (Player)sender;
        if(parameters.containsKey("player")) playerInv = (Player) parameters.get("player");
        
        LoadoutContainer loadout = CVLoadouts.getInstance().getLoadoutManager().getLoadoutByName((String) baseParameters.get(0));
        if(loadout == null) throw new CommandExecutionException("&cLoadout &6" + baseParameters.get(0) + "&c does not exist!");
        int loadoutIndex = (Integer) baseParameters.get(1);
        
        String team = "main";
        if(parameters.containsKey("team")) team = (String) parameters.get("team");
        
        if(playerInv == null) throw new CommandExecutionException("&cNeed player argument if run from console.");
        
        if(flags.contains("exclusive") && flags.contains("take")) throw new CommandExecutionException("&cFlags exclusive and take are exclusive. Haha. Exclusive. Get it?");
        
        if(flags.contains("take")) {
            if(!LoadoutHandler.takeLoadoutItemFromPlayer(playerInv, loadout, team, loadoutIndex))
                throw new CommandExecutionException("&cSub Loadout &6" + parameters.get("team") + "&c does not exist for &6" + baseParameters.get(0));
            
        }
        else {
            if(!LoadoutHandler.giveLoadoutItemToPlayer(playerInv, loadout, team, loadoutIndex, flags.contains("exclusive")))
                throw new CommandExecutionException("&cSub Loadout &6" + parameters.get("team") + "&c does not exist for &6" + baseParameters.get(0));
        }
        
        if(flags.contains("sound")) playerInv.playSound(playerInv.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 1.5f);
        return null;
    }
    
}
