package net.halflex.mythic.command;

import io.lumine.mythic.bukkit.utils.commands.Command;
import io.lumine.mythic.bukkit.utils.text.Text;
import net.halflex.mythic.NotMythicScript;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TestCommand extends Command<NotMythicScript> {
    public TestCommand(Command<NotMythicScript> parent) {
        super(parent);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] strings) {
        Player player = (Player) commandSender;

        Text.sendMessage(player, plugin.playerDataManager().getPlayerData(player).uuid().toString());
        Text.sendMessage(player, plugin.playerDataManager().getPlayerData(player).mythicClass().internalName());
        return true;
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
        return false;
    }

    @Override
    public String getName() {
        return "test";
    }
}
