package net.halflex.mythic.player.profile;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillTrigger;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillMetadataImpl;
import io.lumine.mythic.core.skills.SkillTriggers;
import io.lumine.mythic.core.skills.TriggeredSkill;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.halflex.mythic.config.ConfigManager;
import net.halflex.mythic.player.data.PlayerData;

import java.util.function.Consumer;

@Accessors(fluent = true)
public class Profile implements SkillCaster {

    @Getter private PlayerData playerData;

    private boolean usingDamageSkill = false;

    public Profile(PlayerData data){
        this.playerData = data;
    }

    @Override
    public AbstractEntity getEntity() {
        return BukkitAdapter.adapt(playerData.getPlayer());
    }

    public AbstractPlayer abstractPlayer(){
        return BukkitAdapter.adapt(playerData.getPlayer());
    }


    @Override
    public void setUsingDamageSkill(boolean b) {
        this.usingDamageSkill = b;
    }

    @Override
    public boolean isUsingDamageSkill() {
        return this.usingDamageSkill;
    }

    public PlayerData getPlayerData(){
        return this.playerData;
    }

    public boolean runSkills(SkillTrigger cause, AbstractEntity trigger){
        return runSkills(cause, null, trigger);
    }

    public boolean runSkills(SkillTrigger cause, AbstractLocation origin){
        return runSkills(cause, origin, null);
    }

    public boolean runSkills(SkillTrigger cause, AbstractLocation origin, AbstractEntity trigger){
        return runSkills(cause, origin, trigger, true, null);
    }

    public boolean runSkills(SkillTrigger cause, AbstractLocation origin, AbstractEntity trigger, Consumer<SkillMetadata> transformer){
        return runSkills(cause, origin, trigger, true, transformer);
    }

    public boolean runSkills(SkillTrigger cause, AbstractLocation origin, AbstractEntity trigger, boolean sync, Consumer<SkillMetadata> transformer){
        if (getPlayerData().mythicClass() == null) return false;
        if (!getPlayerData().mythicClass().hasSkillMechanics(cause)) return false;

        final TriggeredSkill ts = new TriggeredSkill(cause, this, origin, trigger, getPlayerData().mythicClass().skillMechanics(cause), sync, meta -> {
           if (transformer != null){
               transformer.accept(meta);
           }
        });
        return ts.getCancelled();
    }

    public void runTimerSkills(long timer){
        if (!abstractPlayer().isOnline()) return;
        if (getPlayerData().mythicClass() == null) return;
        if (!getPlayerData().mythicClass().hasTimerMechanics()) return;

        final SkillMetadata metadata = new SkillMetadataImpl(SkillTriggers.TIMER, this, null);

        getPlayerData().mythicClass().timerMechanics().forEach(skill -> {
            if (timer % (skill.getTimerInterval() / ConfigManager.ClockInterval()) == 0){
                if (skill.isUsableFromSkill(metadata)){
                    skill.executeSkills(metadata);
                }
            }
        });

    }
}
