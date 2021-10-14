package org.cubeville.cvloadouts.commands.commandparameters;

import org.cubeville.commons.commands.CommandParameterType;
import org.cubeville.cvloadouts.CVLoadouts;

public class CommandParameterLoadoutContainer implements CommandParameterType
{
    public boolean isValid(String value) {
        return CVLoadouts.getInstance().getLoadoutManager().contains(value);
    }

    public String getInvalidMessage(String value) {
        return value + " is no valid loadout!";
    }

    public Object getValue(String value) {
        return CVLoadouts.getInstance().getLoadoutManager().getLoadoutByName(value);
    }
}
