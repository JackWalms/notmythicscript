package net.halflex.mythic.config;

import io.lumine.mythic.api.config.MythicConfig;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import io.lumine.mythic.core.config.IOLoader;
import io.lumine.mythic.core.config.MythicConfigImpl;
import io.lumine.mythic.core.logging.MythicLogger;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.halflex.mythic.NotMythicScript;
import net.halflex.mythic.utils.ConsoleColors;
import net.halflex.mythic.utils.Log;

@Accessors(fluent = true)
public class ConfigManager extends ReloadableModule<NotMythicScript> {

    private IOLoader<NotMythicScript> loader;

    @Getter private static int ClockInterval = 1;

    public ConfigManager(NotMythicScript plugin) {
        super(plugin, false);
    }

    @Override
    public void load(NotMythicScript plugin) {

        this.loader = new IOLoader<>(plugin, "config.yml");
        loadSettings();

        plugin.classManager().loadClasses();
        plugin.itemManager().loadItems();

        Log.info(ConsoleColors.CYAN + "----- Mythic RPG ------");
        Log.info("Loaded " + plugin.classManager().getClassNames().size() + " class" + ((plugin.classManager().getClassNames().size() == 1) ? "": "es!"));
        Log.info("Loaded " + plugin.itemManager().getItemNames().size() + " item" + ((plugin.itemManager().getItemNames().size() == 1) ? "": "s!"));
    }

    @Override
    public void unload() {

    }

    private void loadSettings(){
        //Log.info("Loading Mythic RPG Settings...");
        MythicConfig mc = new MythicConfigImpl("Settings", loader.getCustomConfig());

        ClockInterval = mc.getInt("Clock.ClockInterval", 1);


    }
}
