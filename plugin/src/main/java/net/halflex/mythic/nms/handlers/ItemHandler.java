package net.halflex.mythic.nms.handlers;

import io.lumine.mythic.core.utils.jnbt.CompoundTag;
import io.lumine.mythic.core.utils.jnbt.Tag;
import net.halflex.mythic.items.BukkitItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ItemHandler {

    void name(BukkitItemBuilder builder, String name);

    void lore(BukkitItemBuilder builder, List<String> lore);

    CompoundTag getNBTData(ItemStack itemStack);
    void setNBTData(BukkitItemBuilder builder, String key, Tag tag);

    void setNBTData(BukkitItemBuilder builder, CompoundTag compoundTag);
}
