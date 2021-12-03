package org.cubeville.cvloadouts.commands;

import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvloadouts.CVLoadouts;
import org.cubeville.cvloadouts.loadout.LoadoutManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoadoutRemove extends Command {

    public LoadoutRemove() {
        super("remove");
        setPermission("cvloadouts.commands");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("team", true, new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
            throws CommandExecutionException {
        CVLoadouts instance = CVLoadouts.getInstance();
        LoadoutManager manager = instance.getLoadoutManager();
        if (!parameters.containsKey("team")) {
            if (!manager.removeLoadout((String) baseParameters.get(0))) {
                throw new CommandExecutionException("&cLoadout doesn't exist!");
            } else {
                instance.saveLoadoutManager();
                return new CommandResponse("&aLoadout &6" + baseParameters.get(0).toString().toLowerCase() + "&a removed!");
            }
        } else if (manager.getLoadoutByName((String) baseParameters.get(0)) != null) {
            manager.getLoadoutByName((String) baseParameters.get(0)).removeInventory((String) parameters.get("team"));
            instance.saveLoadoutManager();
            return new CommandResponse("&aSub Loadout &6" + parameters.get("team") + "&a for loadout &6" + baseParameters.get(0) + "&a removed!");
        } else {
            throw new CommandExecutionException("&cLoadout &6" + baseParameters.get(0) + " &cdoes not exist! Could not create sub-loadout &6" + parameters.get("team"));
        }
    }
}
