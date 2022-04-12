package net.halflex.mythic.utils;

import net.halflex.mythic.NotMythicScript;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public final class Log {
    public static void info(@Nonnull String s) {
        NotMythicScript.inst().getLogger().info(s);
    }

    public static void warn(@Nonnull String s) {
        NotMythicScript.inst().getLogger().warning(s);
    }

    public static void severe(@Nonnull String s) {
        NotMythicScript.inst().getLogger().severe(s);
    }

    public static void warn(@Nonnull String s, Throwable t) {
        NotMythicScript.inst().getLogger().log(Level.WARNING, s, t);
    }

    public static void severe(@Nonnull String s, Throwable t) {
        NotMythicScript.inst().getLogger().log(Level.SEVERE, s, t);
    }

    private Log() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}
