package net.halflex.mythic.player.clazz;

import io.lumine.mythic.api.config.MythicConfig;
import io.lumine.mythic.api.skills.SkillManager;
import io.lumine.mythic.api.skills.SkillTrigger;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.config.MythicLineConfigImpl;
import io.lumine.mythic.core.logging.MythicLogger;
import io.lumine.mythic.core.logging.MythicLogger.DebugLevel;
import io.lumine.mythic.core.skills.SkillMechanic;
import io.lumine.mythic.core.skills.SkillTriggers;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Accessors(fluent = true)
public class MythicClass {

    @Getter private final String internalName;
    private final MythicConfig config;
    private Map<SkillTrigger, Queue<SkillMechanic>> mechanics = new HashMap<>();
    private Queue<SkillMechanic> timerMechanics = null;

    public MythicClass(ClassManager manager, String file, String internalName, MythicConfig config){
        this.internalName = internalName;
        this.config = config;

        final SkillManager skillManager = MythicBukkit.inst().getSkillManager();

        List<String> skills = config.getStringList("Skills");


        for(String s : skills)   {
            MythicLogger.debug(DebugLevel.SKILL_CHECK, "Loading mechanic line: {0}", s);

            s = MythicLineConfigImpl.unparseBlock(s);
            SkillMechanic ms = skillManager.getMechanic(s);

            Pattern Rpattern;
            Matcher Rmatcher;
            int interval = 1;

            if(ms != null)    {
                MythicLogger.debug(DebugLevel.SKILL_CHECK, "Base skill found.");
                if(s.contains("~onTimer"))  {
                    MythicLogger.debug(DebugLevel.SKILL_CHECK, "SkillTrigger is Timer. Assigning skill to Timer...");
                    Rpattern = Pattern.compile("~onTimer:([0-9]+)");
                    Rmatcher = Rpattern.matcher(s);
                    Rmatcher.find();
                    try {
                        interval = Integer.parseInt((Rmatcher.group(1)));
                    } catch(Exception e)    {
                        MythicLogger.error("Error parsing Timer skill, invalid interval specified (must be an integer). AbstractSkill=" + s);
                        continue;
                    }
                    MythicLogger.debug(DebugLevel.SKILL_CHECK, "AbstractSkill set on timer with interval " + interval);
                    ms.setTimerInterval(interval);

                    if(timerMechanics == null)	{
                        timerMechanics = new LinkedList<>();
                    }
                    timerMechanics.add(ms);
                } else if(s.contains("~onSignal:"))   {
                    MythicLogger.debug(DebugLevel.SKILL_CHECK, "SkillTrigger is Signal.");
                    Rpattern = Pattern.compile("~onSignal:([a-zA-Z0-9_-]*)");
                    Rmatcher = Rpattern.matcher(s);
                    Rmatcher.find();
                    final String signal;
                    try {
                        signal = Rmatcher.group(1);
                    } catch(Exception e)    {
                        MythicLogger.error("Error parsing Signal skill, invalid signal specified (contains invalid characters). AbstractSkill=" + s);
                        continue;
                    }
                    MythicLogger.debug(DebugLevel.SKILL_CHECK, "AbstractSkill set on Signal with index " + signal);
                    saveSkill(SkillTriggers.SIGNAL, ms);
                } else    {
                    MythicLogger.debug(DebugLevel.SKILL_CHECK, "SkillTrigger is NOT Timer. Assigning skill to regular skill tree...");
                    saveSkill(ms.getTrigger(), ms);
                }
            } else    {
                MythicLogger.debug(DebugLevel.SKILL_CHECK, "Base skill was not found.");
            }
        }

    }

    private void saveSkill(SkillTrigger trigger, SkillMechanic skill)	{
        if(!this.mechanics.containsKey(trigger))	{
            mechanics.put(trigger, new LinkedList<>());
        }
        mechanics.get(trigger).add(skill);
    }

    public Queue<SkillMechanic> skillMechanics(SkillTrigger cause){
        return this.mechanics.get(cause);
    }

    public Queue<SkillMechanic> timerMechanics(){
        return this.timerMechanics;
    }

    public boolean hasSkillMechanics(SkillTrigger cause){
        return this.mechanics.containsKey(cause);
    }

    public boolean hasTimerMechanics(){
        return this.timerMechanics != null;
    }





}
