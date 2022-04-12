package net.halflex.mythic.player.clazz;

import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import io.lumine.mythic.core.config.MythicConfigImpl;
import io.lumine.mythic.core.logging.MythicLogger;
import net.halflex.mythic.NotMythicScript;
import net.halflex.mythic.config.ConfigurationLoader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClassManager extends ReloadableModule<NotMythicScript> {

    private final Map<String, MythicClass> classes = new HashMap<>();

    public ClassManager(NotMythicScript plugin) {
        super(plugin);
    }

    @Override
    public void load(NotMythicScript notMythicScript) {
        Events.subscribe(MythicReloadedEvent.class).handler(event -> {
            loadClasses();
        }).bindWith(this);

    }

    @Override
    public void unload() {

    }

    public void loadClasses(){
        MythicLogger.log("Loading Classes...");
        ConfigurationLoader<NotMythicScript> loader = new ConfigurationLoader<>("Classes.yml", "Players");
        this.classes.clear();

        for (String internalName : loader.getConfig().getKeys(false)){
            final MythicConfigImpl mc = new MythicConfigImpl(internalName, loader.getFile(), loader.getConfig());
            final String file = loader.getFile().getPath();
            final MythicClass mythicClass = new MythicClass(this, file, internalName, mc);

            register(internalName, mythicClass);
        }

    }

    private void register(String internalName, MythicClass mythicClass){
        if (classes.containsKey(internalName)) return;
        classes.put(internalName, mythicClass);
    }

    public Optional<MythicClass> getPlayerClass(String internalName){
        return Optional.ofNullable(classes.getOrDefault(internalName, null));
    }

    public MythicClass getClass(String internalName){
        return classes.getOrDefault(internalName, null);
    }

    public Collection<String> getClassNames(){
        return classes.keySet();
    }


}
