package net.halflex.mythic;


import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.Schedulers;
import io.lumine.mythic.bukkit.utils.plugin.LuminePlugin;
import io.lumine.mythic.core.config.ConfigExecutor;
import io.lumine.mythic.core.logging.MythicLogger;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.halflex.mythic.clock.PPClock;
import net.halflex.mythic.command.BaseCommand;
import net.halflex.mythic.config.ConfigManager;
import net.halflex.mythic.items.ItemManager;
import net.halflex.mythic.nms.disabled.NMSDisabled;
import net.halflex.mythic.nms.handlers.NMSHandler;
import net.halflex.mythic.player.clazz.ClassManager;
import net.halflex.mythic.player.data.PlayerDataManager;
import net.halflex.mythic.player.profile.ProfileManager;
import net.halflex.mythic.skills.PlayerTriggers;
import net.halflex.mythic.skills.SkillManager;
import org.bukkit.Bukkit;

@Accessors(fluent = true)
public class NotMythicScript extends LuminePlugin {

    @Getter private static NotMythicScript inst;
    @Getter private ProfileManager profileManager;
    @Getter private PlayerDataManager playerDataManager;
    @Getter private SkillManager skillManager;
    @Getter private ItemManager itemManager;
    @Getter private ClassManager classManager;
    @Getter private ConfigManager configManager;
    private NMSHandler NMSModule;

    @Override
    public void load(){
        inst = this;
        PlayerTriggers.register();
    }

    @Override
    public void enable(){
        this.NMSModule = getNMSModule();
        this.playerDataManager = new PlayerDataManager(this);
        this.profileManager = new ProfileManager(this);
        this.itemManager = new ItemManager(this);
        this.skillManager = new SkillManager(this);
        this.classManager = new ClassManager(this);
        this.configManager = new ConfigManager(this);
        this.registerCommand("notmythicscript", new BaseCommand(this));

        configManager.load(this);

        this.bind(Schedulers.async().runRepeating(new PPClock(), 0, ConfigExecutor.ClockInterval));
        this.bind(Events.subscribe(MythicReloadedEvent.class).handler(event -> {
           configManager.load(this);
        }));
    }

    @Override
    public void disable(){

    }

    public NMSHandler getNMSModule() {
        if (this.NMSModule != null) return this.NMSModule;

        this.NMSModule = new NMSDisabled();

        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            final Class<?> clazz = Class.forName("net.halflex.mythic.nms.version." + version + ".NMSHandler_" + version);
            if (NMSHandler.class.isAssignableFrom(clazz)) {
                this.NMSModule = (NMSHandler) clazz.getConstructor().newInstance();
                MythicLogger.log("Loaded NMS Module for " + packageName);
            }
        } catch (final Exception e) {
            //e.printStackTrace();
            MythicLogger.error("NotMythicScript does not support this version of CraftBukkit - " + packageName
                    + " - Please update your server!");
        }

        return this.NMSModule;
    }

}
