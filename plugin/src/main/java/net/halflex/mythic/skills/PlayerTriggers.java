package net.halflex.mythic.skills;

import io.lumine.mythic.api.skills.SkillTrigger;

public final class PlayerTriggers {

    public static final SkillTrigger
            PICKUP = SkillTrigger.trigger("PICKUP"),
            DYE_SHEEP = SkillTrigger.trigger("DYESHEEP");


    public static void register(){
        PICKUP.register();
        DYE_SHEEP.register();
    }
}
