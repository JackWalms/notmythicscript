package net.halflex.mythic.items;

import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import io.lumine.mythic.core.config.IOLoader;
import io.lumine.mythic.core.config.MythicConfigImpl;
import io.lumine.mythic.core.logging.MythicLogger;
import net.halflex.mythic.NotMythicScript;
import net.halflex.mythic.config.ConfigurationLoader;
import net.halflex.mythic.player.clazz.MythicClass;
import net.halflex.mythic.utils.ConsoleColors;
import net.halflex.mythic.utils.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemManager extends ReloadableModule<NotMythicScript> {

    private final Map<String, CustomItem> customItems = new HashMap<>();

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
        Log.info(ConsoleColors.GREEN + "Loading Custom Items...");
        IOLoader<NotMythicScript> loader = new IOLoader<>(plugin,"Items.yml", "Items");
        this.customItems.clear();

        for (String internalName : loader.getCustomConfig().getKeys(false)){
            final MythicConfigImpl mc = new MythicConfigImpl(internalName, loader.getFile(), loader.getCustomConfig());
            final String file = loader.getFile().getPath();
            final CustomItem mythicClass = new CustomItem(this, internalName, file, mc);

            register(internalName, mythicClass);
        }

    }

    private void register(String internalName, CustomItem customItem){
        if (customItems.containsKey(internalName)) return;
        customItems.put(internalName, customItem);
    }

    public Optional<CustomItem> getCustomItem(String internalName){
        return Optional.ofNullable(customItems.getOrDefault(internalName, null));
    }


    public Collection<String> getItemNames(){
        return customItems.keySet();
    }
}
