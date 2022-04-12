package net.halflex.mythic.command;

import io.lumine.mythic.bukkit.utils.commands.Command;
import net.halflex.mythic.NotMythicScript;
import net.halflex.mythic.command.classes.ClassCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BaseCommand extends Command<NotMythicScript> {

    public BaseCommand(NotMythicScript plugin) {
        super(plugin);
        addSubCommands(
                new ClassCommand(this),
                new TestCommand(this)
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
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
