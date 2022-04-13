package net.halflex.mythic.command.items;

import io.lumine.mythic.bukkit.utils.commands.Command;
import net.halflex.mythic.NotMythicScript;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ItemCommand extends Command<NotMythicScript> {

    public ItemCommand(Command<NotMythicScript> parent) {
        super(parent);
        addSubCommands(
                new GetCommand(this)
        );
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] strings) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public boolean isConsoleFriendly() {
        return true;
    }

    @Override
    public String[] getAliases(){
        return new String[] {"item", "i"};
    }

    @Override
    public String getName() {
        return "items";
    }
}
