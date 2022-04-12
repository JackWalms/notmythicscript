package net.halflex.mythic.command.classes;

import io.lumine.mythic.bukkit.utils.commands.Command;
import net.halflex.mythic.NotMythicScript;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ResetClassCommand extends Command<NotMythicScript> {
    public ResetClassCommand(Command<NotMythicScript> parent) {
        super(parent);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        plugin.playerDataManager().getPlayerData(player).mythicClass(null);
        return true;
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
    public String[] getAliases(){
        return new String[] {"remove"};
    }

    @Override
    public String getName() {
        return "reset";
    }
}
