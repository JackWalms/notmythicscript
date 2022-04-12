package net.halflex.mythic.utils;

import io.lumine.mythic.bukkit.utils.adventure.text.Component;
import io.lumine.mythic.bukkit.utils.adventure.text.format.NamedTextColor;
import io.lumine.mythic.bukkit.utils.adventure.text.format.TextDecoration;
import io.lumine.mythic.bukkit.utils.adventure.text.minimessage.MiniMessage;
import io.lumine.mythic.bukkit.utils.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.NotNull;

public final class Msg {
    public static final char SECTION_CHAR = '\u00A7'; // §
    public static final char AMPERSAND_CHAR = '&';
    private static final MiniMessage miniMessage = MiniMessage.builder().strict(false).tags(StandardTags.defaults()).build();

    @NotNull
    public static Component parse(String text) {
        if (text.contains("&") || text.contains("§"))
                text = replaceLegacy(text);
        Component component = miniMessage.deserialize(text);
        if (component.decorations().get(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET)
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        return component.colorIfAbsent(NamedTextColor.WHITE);
    }

    @NotNull
    public static String replaceLegacy(@NotNull String text) {
        return text.replaceAll("[&§]0", "<black>")
                .replaceAll("[&§]1", "<dark_blue>")
                .replaceAll("[&§]2", "<dark_green>")
                .replaceAll("[&§]3", "<dark_aqua>")
                .replaceAll("[&§]4", "<dark_red>")
                .replaceAll("[&§]5", "<dark_purple>")
                .replaceAll("[&§]6", "<gold>")
                .replaceAll("[&§]7", "<gray>")
                .replaceAll("[&§]8", "<dark_gray>")
                .replaceAll("[&§]9", "<blue>")
                .replaceAll("[&§][Aa]", "<green>")
                .replaceAll("[&§][Bb]", "<aqua>")
                .replaceAll("[&§][Cc]", "<red>")
                .replaceAll("[&§][Dd]", "<light_purple>")
                .replaceAll("[&§][Ee]", "<yellow>")
                .replaceAll("[&§][Ff]", "<white>")
                .replaceAll("[&§][Kk]", "<obf>")
                .replaceAll("[&§][Ll]", "<b>")
                .replaceAll("[&§][Mm]", "<st>")
                .replaceAll("[&§][Nn]", "<u>")
                .replaceAll("[&§][Oo]", "<i>")
                .replaceAll("[&§][Rr]", "<reset>");
    }
}
