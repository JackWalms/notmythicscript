package net.halflex.mythic.items;

import io.lumine.mythic.api.config.MythicConfig;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.halflex.mythic.utils.ConsoleColors;
import net.halflex.mythic.utils.Log;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Accessors(fluent = true)
public class CustomItem {
    @Getter final String internalName;
    @Getter private final MythicConfig config;
    private String file;
    private ItemManager manager;
    private Material material;


    public CustomItem(ItemManager manager, String internalName, String file, MythicConfig mc){
        this.internalName = internalName;
        this.config = mc;

        String materialStr = mc.getString("Material", "DIRT".toUpperCase());
        try {
            this.material = Material.valueOf(materialStr);
        } catch (IllegalArgumentException e){
            Log.info(ConsoleColors.RED_BOLD_BRIGHT + "'Material: "+ materialStr + "' is not a valid material. Check out "  + ConsoleColors.WHITE_UNDERLINED
                    + "https://papermc.io/javadocs/paper/1.18/org/bukkit/Material.html" + ConsoleColors.RESET + ConsoleColors.RED_BOLD_BRIGHT + " for a list of valid materials!");
            this.material = Material.DIRT;
        }

    }

    public ItemStack build(){
        BukkitItemBuilder builder = BukkitItemBuilder.of(this.material);
        return builder.build();
    }
}
