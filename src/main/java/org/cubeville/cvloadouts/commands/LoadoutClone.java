package org.cubeville.cvloadouts.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvloadouts.commands.commandparameters.CommandParameterLoadoutContainer;
import org.cubeville.cvloadouts.loadout.LoadoutContainer;
import org.cubeville.cvloadouts.loadout.LoadoutHandler;

public class LoadoutClone extends Command {

    public LoadoutClone() {
        super("clone");
        addBaseParameter(new CommandParameterLoadoutContainer());
        addParameter("team", true, new CommandParameterString());
        addFlag("clear");
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters,
            List<Object> baseParameters) throws CommandExecutionException {
        String team = "main";
        LoadoutContainer lc = (LoadoutContainer) baseParameters.get(0);
        
        if (parameters.containsKey("team")) {
            team = (String) parameters.get("team");
        }
        
        if (!lc.containsInventory(team)) throw new CommandExecutionException("&6" + team + " &cdoes not exist for &6" + lc.getName());

        LoadoutHandler.cloneInventoryToLoadout(player, lc, team);
        
        if (flags.contains("clear")) player.getInventory().clear();
        
        return new CommandResponse("&aSuccessfully cloned inventory to &6" + lc.getName() + "&a:&6" + team);
    }

}
