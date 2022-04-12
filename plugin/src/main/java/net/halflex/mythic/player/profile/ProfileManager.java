package net.halflex.mythic.player.profile;

import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import net.halflex.mythic.NotMythicScript;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileManager extends ReloadableModule<NotMythicScript> {

    private final ConcurrentHashMap<UUID,Profile> cachedPlayers = new ConcurrentHashMap<UUID,Profile>();

    public ProfileManager(NotMythicScript plugin) {
        super(plugin);
    }

    @Override
    public void load(NotMythicScript plugin) {
        Events.subscribe(PlayerQuitEvent.class).handler(event -> cachedPlayers.remove(event.getPlayer().getUniqueId())).bindWith(this);
    }

    @Override
    public void unload() {

    }

    public Profile getProfile(Player player){
        Profile profile;
        if (cachedPlayers.containsKey(player.getUniqueId())){
            profile = cachedPlayers.get(player.getUniqueId());
        } else {
            profile = new Profile(plugin.playerDataManager().getPlayerData(player));
            cachedPlayers.put(player.getUniqueId(), profile);
        }
        return profile;
    }

    public void runTimerSkills(long timer){
        cachedPlayers.values().forEach(profile -> profile.runTimerSkills(timer));
    }

}
