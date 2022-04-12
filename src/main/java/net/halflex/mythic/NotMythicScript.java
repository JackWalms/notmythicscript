package net.halflex.mythic;


import io.lumine.mythic.bukkit.utils.Schedulers;
import io.lumine.mythic.bukkit.utils.plugin.LuminePlugin;
import io.lumine.mythic.core.config.ConfigExecutor;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.halflex.mythic.clock.PPClock;
import net.halflex.mythic.command.BaseCommand;
import net.halflex.mythic.config.ConfigManager;
import net.halflex.mythic.player.clazz.ClassManager;
import net.halflex.mythic.player.data.PlayerDataManager;
import net.halflex.mythic.player.profile.ProfileManager;
import net.halflex.mythic.skills.PlayerTriggers;
import net.halflex.mythic.skills.SkillManager;

@Accessors(fluent = true)
public class NotMythicScript extends LuminePlugin {

    @Getter private static NotMythicScript inst;
    @Getter private ProfileManager profileManager;
    @Getter private PlayerDataManager playerDataManager;
    @Getter private SkillManager skillManager;
    @Getter private ClassManager classManager;
    @Getter private ConfigManager configManager;

    @Override
    public void load(){
        inst = this;
        PlayerTriggers.register();
    }

    @Override
    public void enable(){
        this.playerDataManager = new PlayerDataManager(this);
        this.profileManager = new ProfileManager(this);
        this.skillManager = new SkillManager(this);
        this.classManager = new ClassManager(this);
        this.configManager = new ConfigManager(this);
        this.registerCommand("pphil", new BaseCommand(this));

        // load modules
        configManager.load(this);


        this.bind(Schedulers.async().runRepeating(new PPClock(), 0, ConfigExecutor.ClockInterval));
    }

    @Override
    public void disable(){

    }

}
