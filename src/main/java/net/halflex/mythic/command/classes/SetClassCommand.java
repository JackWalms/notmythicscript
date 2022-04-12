package net.halflex.mythic.command.classes;

import io.lumine.mythic.bukkit.utils.commands.Command;
import net.halflex.mythic.NotMythicScript;
import net.halflex.mythic.player.clazz.MythicClass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SetClassCommand extends Command<NotMythicScript> {

    public SetClassCommand(Command<NotMythicScript> parent) {
        super(parent);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        Optional<MythicClass> maybeMythic = plugin.classManager().getPlayerClass(args[0]);
        maybeMythic.ifPresent(mythicClass -> {
            plugin.playerDataManager().getPlayerData(player).mythicClass(mythicClass);
            //Text.sendMessage(player, "");
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args) {
        return StringUtil.copyPartialMatches(args[0], plugin.classManager().getClassNames(), new ArrayList<>());
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
    public String[] getAliases() {
        return new String[] {"class"};
    }

    @Override
    public String getName() {
        return "setclass";
    }
}
