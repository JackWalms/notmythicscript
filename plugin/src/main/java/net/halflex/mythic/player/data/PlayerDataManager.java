package net.halflex.mythic.player.data;

import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.config.file.FileConfiguration;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import io.lumine.mythic.core.config.IOLoader;
import net.halflex.mythic.NotMythicScript;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager extends ReloadableModule<NotMythicScript> {

    private static final Map<UUID, PlayerData> cachedData = Collections.synchronizedMap(new HashMap<>());

    public PlayerDataManager(NotMythicScript plugin){
        super(plugin);
    }

    @Override
    public void load(NotMythicScript plugin) {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(event -> loadData(getPlayerData(event.getPlayer().getUniqueId()))).bindWith(this);
        Events.subscribe(PlayerQuitEvent.class)
                .handler(event -> {
                    saveData(getPlayerData(event.getPlayer().getUniqueId()));
                    cachedData.remove(event.getPlayer().getUniqueId());
                }).bindWith(this);
    }

    @Override
    public void unload() {

    }

    public PlayerData getPlayerData(UUID uuid){
        if (cachedData.containsKey(uuid)){
            return cachedData.get(uuid);
        }
        PlayerData pd = new PlayerData(uuid);
        cachedData.put(uuid, pd);
        return pd;
    }

    public PlayerData getPlayerData(Player player){
        return getPlayerData(player.getUniqueId());
    }

    private void loadData(PlayerData data){
        IOLoader<NotMythicScript> loader = new IOLoader<>(plugin, data.uuid().toString() + ".yml", "userdata");
        FileConfiguration config = loader.getCustomConfig();

        if (config.contains("class")){
            data.mythicClass(plugin.classManager().getClass(config.getString("class")));
        }

    }

    private void saveData(PlayerData data){
        IOLoader<NotMythicScript> loader = new IOLoader<>(plugin, data.uuid().toString() + ".yml", "userdata");
        FileConfiguration config = loader.getCustomConfig();

        config.set("class", (data.mythicClass() == null) ? null : data.mythicClass().internalName());

        loader.saveCustomConfig();

    }


}
