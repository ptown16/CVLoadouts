package org.cubeville.cvloadouts.commands;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterOnlinePlayer;
import org.cubeville.commons.commands.CommandParameterListString;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvloadouts.CVLoadouts;
import org.cubeville.cvloadouts.loadout.LoadoutContainer;
import org.cubeville.cvloadouts.loadout.LoadoutHandler;

public class LoadoutApply extends BaseCommand {

	public LoadoutApply() {
		super("apply");
		setPermission("cvloadouts.commands");
		addBaseParameter(new CommandParameterString());
		addParameter("team", true, new CommandParameterListString());
		addParameter("player", true, new CommandParameterOnlinePlayer());
	}

	@Override
	public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
			throws CommandExecutionException {

            Player playerInv = null;
            if(sender instanceof Player) playerInv = (Player)sender;
            
            LoadoutContainer loadout = CVLoadouts.getInstance().getLoadoutManager().getLoadoutByName((String) baseParameters.get(0));

            if (loadout == null)
                throw new CommandExecutionException("&cLoadout &6" + baseParameters.get(0) + "&c does not exist!");

            List<String> subLoadouts;
            if (parameters.containsKey("team"))
                subLoadouts = (List<String>) parameters.get("team");
            else
                subLoadouts = new ArrayList<>();

            if (parameters.containsKey("player"))
                playerInv = (Player) parameters.get("player");

            if(playerInv == null)
                throw new CommandExecutionException("&cNeed player argument if run from console.");
            
            if (!LoadoutHandler.applyLoadoutToPlayer(playerInv, loadout, subLoadouts))
                throw new CommandExecutionException("&cSub Loadout &6" + parameters.get("team") + "&c does not exist for &6" + baseParameters.get(0));
            
            return new CommandResponse("&aLoadout applied.");
            //            return new CommandResponse("&aLoadout &6" + loadout.getInventory(team).getName() + "&a applied to &6" + playerInv.getName()); // TODO
	}
}
