package net.halflex.mythic.nms.version.v1_18_R2;

import io.lumine.mythic.bukkit.utils.adventure.text.Component;
import io.lumine.mythic.bukkit.utils.adventure.text.serializer.gson.GsonComponentSerializer;
import io.lumine.mythic.bukkit.utils.text.Text;
import io.lumine.mythic.core.utils.jnbt.CompoundTag;
import io.lumine.mythic.core.utils.jnbt.Tag;
import net.halflex.mythic.items.BukkitItemBuilder;
import net.halflex.mythic.nms.handlers.ItemHandler;
import net.halflex.mythic.nms.handlers.NMSHandler;
import net.minecraft.nbt.ListTag;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemHandlerImpl implements ItemHandler {

    public ItemHandlerImpl(NMSHandler handler){

    }

    @Override
    public void name(BukkitItemBuilder builder, String name) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(builder.build());
        net.minecraft.nbt.CompoundTag tag = nmsItemStack.getOrCreateTag();
        net.minecraft.nbt.CompoundTag display = tag.getCompound("display");
        if (!tag.contains("display")) {
            tag.put("display", display);
        }
        if (name == null || name.isEmpty()) {
            display.put("Name", null);
            return;
        }
        Component components = Text.parse(name);
        display.put("Name", net.minecraft.nbt.StringTag.valueOf(GsonComponentSerializer.gson().serialize(components)));
        builder.setItemStack(CraftItemStack.asBukkitCopy(nmsItemStack));
    }

    @Override
    public void lore(BukkitItemBuilder builder, List<String> lore) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(builder.build());
        net.minecraft.nbt.CompoundTag tag = nmsItemStack.getOrCreateTag();
        net.minecraft.nbt.CompoundTag display = tag.getCompound("display");
        if (!tag.contains("display")) {
            tag.put("display", display);
        }
        if (lore == null || lore.isEmpty()) {
            display.put("Lore", null);
        }
        else {
            ListTag tagList = new ListTag();
            for (String line : lore) {
                tagList.add(net.minecraft.nbt.StringTag.valueOf(GsonComponentSerializer.gson().serialize(Text.parse(line))));
            }
            display.put("Lore", tagList);
        }
        builder.setItemStack(CraftItemStack.asBukkitCopy(nmsItemStack));
    }

    @Override
    public void setNBTData(BukkitItemBuilder builder, CompoundTag compoundTag) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(builder.build());
        nmsItemStack.setTag(((CompoundTagImpl)compoundTag).toNMSTag());
        builder.setItemStack(CraftItemStack.asBukkitCopy(nmsItemStack));
    }

    @Override
    public void setNBTData(BukkitItemBuilder builder, String key, Tag tag) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(builder.build());
        net.minecraft.nbt.CompoundTag nmsTag = nmsItemStack.getOrCreateTag();
        CompoundTag compoundTag = CompoundTagImpl.fromNMSTag(nmsTag).createBuilder().put(key, tag).build();
        nmsItemStack.setTag(((CompoundTagImpl)compoundTag).toNMSTag());
        builder.setItemStack(CraftItemStack.asBukkitCopy(nmsItemStack));
    }

    @Override
    public CompoundTag getNBTData(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItemStack == null){
            return null;
        }
        return CompoundTagImpl.fromNMSTag(nmsItemStack.getOrCreateTag());
    }
}
