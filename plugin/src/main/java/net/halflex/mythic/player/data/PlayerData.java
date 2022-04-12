package net.halflex.mythic.player.data;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.halflex.mythic.player.clazz.MythicClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Accessors(fluent = true)
public class PlayerData{

    private MythicClass mythicClass;
    @Getter private UUID uuid;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid());
    }

    public void mythicClass(MythicClass mythicClass) {
        this.mythicClass = mythicClass;
    }

    public MythicClass mythicClass(){
        return this.mythicClass;
    }
}
