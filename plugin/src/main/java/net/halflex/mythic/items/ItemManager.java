package net.halflex.mythic.items;

import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import io.lumine.mythic.core.config.IOLoader;
import io.lumine.mythic.core.config.MythicConfigImpl;
import net.halflex.mythic.NotMythicScript;
import net.halflex.mythic.utils.ConsoleColors;
import net.halflex.mythic.utils.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemManager extends ReloadableModule<NotMythicScript> {

    private final Map<String, Artifact> artifacts = new HashMap<>();

    public ItemManager(NotMythicScript plugin) {
        super(plugin);
    }

    @Override
    public void load(NotMythicScript plugin) {

    }

    @Override
    public void unload() {

    }

    public void loadItems(){
        Log.info(ConsoleColors.GREEN + "Loading Artifacts...");
        IOLoader<NotMythicScript> loader = new IOLoader<>(plugin,"Items.yml", "Items");
        this.artifacts.clear();

        for (String internalName : loader.getCustomConfig().getKeys(false)){
            final MythicConfigImpl mc = new MythicConfigImpl(internalName, loader.getFile(), loader.getCustomConfig());
            final String file = loader.getFile().getPath();
            final Artifact mythicClass = new Artifact(this, internalName, file, mc);

            register(internalName, mythicClass);
        }

    }

    private void register(String internalName, Artifact artifact){
        if (artifacts.containsKey(internalName)) return;
        artifacts.put(internalName, artifact);
    }

    public Optional<Artifact> getArtifact(String internalName){
        return Optional.ofNullable(artifacts.getOrDefault(internalName, null));
    }



    public Collection<String> getArtifactNames(){
        return artifacts.keySet();
    }
}
