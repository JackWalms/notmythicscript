package net.halflex.mythic.skills;

import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.bukkit.utils.Events;
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

        Events.subscribe(MythicMechanicLoadEvent.class).handler(event -> {
            switch (event.getMechanicName().toLowerCase()){

            }
        }).bindWith(this);
    }

    @Override
    public void unload() {

    }
}
