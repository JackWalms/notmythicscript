package net.halflex.mythic.skills;

import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import net.halflex.mythic.NotMythicScript;

public class SkillManager extends ReloadableModule<NotMythicScript> {

    private PlayerListeners playerListeners;

    public SkillManager(NotMythicScript plugin) {
        super(plugin, false);

        load(plugin);
    }

    @Override
    public void load(NotMythicScript plugin) {
        this.playerListeners = new PlayerListeners(plugin);
    }

    @Override
    public void unload() {

    }
}
