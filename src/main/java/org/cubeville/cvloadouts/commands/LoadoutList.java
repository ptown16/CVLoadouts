package org.cubeville.cvloadouts.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvloadouts.CVLoadouts;

public class LoadoutList extends Command{

    private CommandResponse cr;
	
    public LoadoutList(){
        super("list");
        setPermission("cvloadouts.commands");
        //addParameter("sub", true, new CommandParameterLoadout());
    }

    @SuppressWarnings("unchecked")
    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {
        List<String> loadouts = CVLoadouts.getInstance().getLoadoutManager().getLoadoutNames();
        List<TextComponent> out = new ArrayList<>();
        out.add(new TextComponent("§6========================§aLoadouts§6========================"));
        TextComponent load = new TextComponent("");
        int i = loadouts.size();
        for (String mloadout: loadouts) {
            TextComponent m = new TextComponent(mloadout);
            m.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/loadout edit " + mloadout));
            m.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Edit " + mloadout).create()));
            i--;
            if(i >= 1) m.addExtra("§c||");
            load.addExtra(m);
        }
        out.add(load);
        for (TextComponent o : out) {
            player.spigot().sendMessage(o);
        }
        return new CommandResponse("");
    }
	
    public void createMessage(List<String> loadouts) {
        List<String> loadoutList = new ArrayList<>();
        int i = 0;
		
        for (String loadoutString: loadouts) {
            String newLoadout;
            if (loadoutList.size() < i+1) {
                newLoadout = "";
                loadoutList.add(i, newLoadout);
            } else {
                newLoadout = loadoutList.get(i);
            }
            System.out.println((newLoadout + loadoutString + "||").length());
            if ((newLoadout + loadoutString + "||").length() > 100) {
                newLoadout = newLoadout.substring(0, newLoadout.length() - 2);
                i++;
            } else {
                newLoadout = newLoadout + "&f" + loadoutString + "&c||";
            }
            loadoutList.set(i, newLoadout);
        }
		
        loadoutList.set(i, loadoutList.get(i).substring(0, loadoutList.get(i).length() - 2));
		
        for (String message: loadoutList) {
            cr.addMessage(message);
        }
    }

}

