package net.halflex.mythic.clock;

import net.halflex.mythic.NotMythicScript;

import java.util.ConcurrentModificationException;

public class PPClock implements Runnable {

    private int timer = 0;

    @Override
    public void run() {
        try	{
            NotMythicScript.inst().profileManager().runTimerSkills(timer++);
        } catch(ConcurrentModificationException ex) {}
    }
}

