package net.halflex.mythic.command.items;

import io.lumine.mythic.bukkit.utils.commands.Command;
import net.halflex.mythic.NotMythicScript;
import net.halflex.mythic.items.Artifact;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetCommand extends Command<NotMythicScript> {

    public GetCommand(Command<NotMythicScript> parent) {
        super(parent);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Optional<Artifact> maybeArtifact = plugin.itemManager().getArtifact(args[0]);
        maybeArtifact.ifPresent(artifact -> ((Player) sender).getInventory().addItem(artifact.build()));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args) {
        return StringUtil.copyPartialMatches(args[0], plugin.itemManager().getArtifactNames(), new ArrayList<>());
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
        return "get";
    }
}
