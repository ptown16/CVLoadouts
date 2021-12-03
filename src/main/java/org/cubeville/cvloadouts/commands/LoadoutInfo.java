package org.cubeville.cvloadouts.commands;

import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvloadouts.CVLoadouts;
import org.cubeville.cvloadouts.loadout.LoadoutContainer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoadoutInfo extends Command {

    public LoadoutInfo() {
        super("info");
        setPermission("cvloadouts.commands");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
            throws CommandExecutionException {
        if (CVLoadouts.getInstance().getLoadoutManager().contains((String) baseParameters.get(0))) {
            LoadoutContainer lc = CVLoadouts.getInstance().getLoadoutManager().getLoadoutByName((String) baseParameters.get(0));
            CommandResponse cr = new CommandResponse("&6&l------" + baseParameters.get(0).toString().toLowerCase() + "------");
            cr.addMessage("&6---Sub-Loadouts");

            for (String loadoutName: lc.getInventoriesByName()) {
                cr.addMessage("&a-" + loadoutName);
            }

            return cr;
        } else {
            throw new CommandExecutionException("&cLoadout &6" + baseParameters.get(0).toString().toLowerCase() + " &cdoes not exist!");
        }
    }
}
