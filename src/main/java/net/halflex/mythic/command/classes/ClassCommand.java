package net.halflex.mythic.command.classes;

import io.lumine.mythic.bukkit.utils.commands.Command;
import net.halflex.mythic.NotMythicScript;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ClassCommand extends Command<NotMythicScript> {

    public ClassCommand(Command<NotMythicScript> parent) {
        super(parent);

        addSubCommands(
                new SetClassCommand(this),
                new ResetClassCommand(this)
        );
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public boolean isConsoleFriendly() {
        return false;
    }

    @Override
    public String getName() {
        return "class";
    }
}
