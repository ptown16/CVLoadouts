package org.cubeville.cvloadouts.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvloadouts.CVLoadouts;
import org.cubeville.cvloadouts.loadout.LoadoutContainer;
import org.cubeville.cvloadouts.loadout.LoadoutHandler;

import java.util.ArrayList;
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
            List<TextComponent> out = new ArrayList<>();
            LoadoutContainer lc = CVLoadouts.getInstance().getLoadoutManager().getLoadoutByName((String) baseParameters.get(0));
            out.add(new TextComponent("§6§l------" + baseParameters.get(0).toString().toLowerCase() + "------"));
            out.add(new TextComponent("§6---Sub-Loadouts"));

            for (String loadoutName: lc.getInventoriesByName()) {
                TextComponent l = new TextComponent(loadoutName);
                l.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/loadout edit " + baseParameters.get(0) + " team:" + loadoutName));
                l.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Edit team:" + loadoutName).create()));
                l.setColor(ChatColor.AQUA);
                l.addExtra(" §6(§r" + LoadoutHandler.getLoadoutSize(lc, loadoutName) + "§6)");
                out.add(l);
            }
            for (TextComponent o : out) {
                player.spigot().sendMessage(o);
            }
            return new CommandResponse("");
        } else {
            throw new CommandExecutionException("&cLoadout &6" + baseParameters.get(0).toString().toLowerCase() + " &cdoes not exist!");
        }
    }
}
