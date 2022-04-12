package net.halflex.mythic.items;

import io.lumine.mythic.core.utils.jnbt.CompoundTag;
import io.lumine.mythic.core.utils.jnbt.Tag;
import net.halflex.mythic.NotMythicScript;
import net.halflex.mythic.nms.handlers.ItemHandler;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class BukkitItemBuilder {
    private final ItemFlag[] ALL_FLAGS = new ItemFlag[]{
            ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_DESTROYS,
            ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE,
            ItemFlag.HIDE_POTION_EFFECTS
    };

    private ItemStack itemStack;

    protected BukkitItemBuilder() {
        this.itemStack = new ItemStack(Material.STONE);
    }

    protected BukkitItemBuilder(ItemStack itemStack) {
        this.itemStack = Objects.requireNonNull(itemStack, "itemStack");
    }

    public static BukkitItemBuilder of(Material material) {
        return new BukkitItemBuilder(new ItemStack(material));
    }

    public static BukkitItemBuilder of(ItemStack itemStack) {
        return new BukkitItemBuilder(itemStack);
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void transform(Consumer<ItemStack> item) {
        item.accept(this.itemStack);
    }

    public void transformMeta(Consumer<ItemMeta> meta) {
        ItemMeta m = this.itemStack.getItemMeta();
        if (m != null) {
            meta.accept(m);
            this.itemStack.setItemMeta(m);
        }
    }

    public void name(String name) {
        this.apply(builder -> NotMythicScript.inst().getNMSModule().itemHandler().name(this, name));
    }

    public void type(Material material) {
        transform(item -> item.setType(material));
    }

    public void lore(List<String> lines) {
        this.apply(builder -> getItemHandler().lore(this, lines));
    }

    public void clearLore() {
        transformMeta(meta -> {
            meta.lore(new ArrayList<>());
        });
    }

    public void durability(int durability) {
        transformMeta(meta -> {
            if (meta instanceof Damageable damageable) {
                damageable.setDamage(durability);
            }
        });
    }

    public void amount(int amount) {
        transform(item -> item.setAmount(amount));
    }

    public void enchant(Enchantment enchantment, int level) {
        transform(item -> item.addUnsafeEnchantment(enchantment, level));
    }

    public void enchant(Enchantment enchantment) {
        transform(item -> item.addUnsafeEnchantment(enchantment, 1));
    }

    public void clearEnchantments() {
        transform(item -> item.getEnchantments().keySet().forEach(item::removeEnchantment));
    }

    public void flag(ItemFlag... flags) {
        transformMeta(meta -> meta.addItemFlags(flags));
    }

    public void flag(List<ItemFlag> flags) {
        transformMeta(meta -> {
            for (ItemFlag flag : flags) {
                meta.addItemFlags(flag);
            }
        });
    }

    public void unflag(ItemFlag... flags) {
        transformMeta(meta -> meta.removeItemFlags(flags));
    }

    public void unflag(List<ItemFlag> flags) {
        transformMeta(meta -> {
            for (ItemFlag flag : flags) {
                meta.removeItemFlags(flag);
            }
        });
    }

    public void hideFlags() {
        flag(ALL_FLAGS);
    }

    public void showFlags() {
        unflag(ALL_FLAGS);
    }

    public void color(String colorCode) {
        transformMeta(meta -> {
            int r, g, b;
            if (colorCode.startsWith("#")) {
                java.awt.Color color = java.awt.Color.decode(colorCode);
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
            } else if (colorCode.contains(",")) {
                String[] rgb = colorCode.split(",");
                r = Integer.parseInt(rgb[0]);
                g = Integer.parseInt(rgb[1]);
                b = Integer.parseInt(rgb[2]);
            } else {
                DyeColor dyeColor = DyeColor.valueOf(colorCode.toUpperCase());
                Color color = dyeColor.getFireworkColor();
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
            }
            if (meta instanceof PotionMeta potion) {
                potion.setColor(Color.fromRGB(r, g, b));
            } else if (meta instanceof LeatherArmorMeta leather) {
                leather.setColor(Color.fromRGB(r, g, b));
            }
        });
    }

    public void breakable(boolean flag) {
        transformMeta(meta -> meta.setUnbreakable(!flag));
    }

    public void potionEffect(PotionEffectType type, int duration, int amplifier) {
        transformMeta(meta -> {
            if (meta instanceof PotionMeta potion) {
                PotionEffect effect = new PotionEffect(type, duration, amplifier);
                potion.addCustomEffect(effect, true);
            }
        });
    }

    public void potionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient) {
        transformMeta(meta -> {
            if (meta instanceof PotionMeta potion) {
                PotionEffect effect = new PotionEffect(type, duration, amplifier, ambient);
                potion.addCustomEffect(effect, true);
            }
        });

    }

    public void potionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
        transformMeta(meta -> {
            if (meta instanceof PotionMeta potion) {
                PotionEffect effect = new PotionEffect(type, duration, amplifier, ambient, particles);
                potion.addCustomEffect(effect, true);
            }
        });
    }

    public void potionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles, boolean icon) {
        transformMeta(meta -> {
            if (meta instanceof PotionMeta potion) {
                PotionEffect effect = new PotionEffect(type, duration, amplifier, ambient, particles, icon);
                potion.addCustomEffect(effect, true);
            }
        });
    }

    public void potionEffect(PotionEffect effect) {
        transformMeta(meta -> {
            if (meta instanceof PotionMeta potion) {
                potion.addCustomEffect(effect, true);
            }
        });
    }

    public void clearPotionEffects() {
        transformMeta(meta -> {
            if (meta instanceof PotionMeta potion) {
                potion.clearCustomEffects();
            }
        });
    }

    public void shield(String color, Map<DyeColor, PatternType> colorPatternMap) {
        transformMeta(meta -> {
            if (meta instanceof BlockStateMeta blockStateMeta) {
                final Banner banner = (Banner) blockStateMeta.getBlockState();

                if (color != null) {
                    banner.setBaseColor(DyeColor.valueOf(color.toUpperCase()));
                }
                if (!colorPatternMap.isEmpty()) {
                    colorPatternMap.forEach((dyeColor, patternType) -> banner.addPattern(new Pattern(dyeColor, patternType)));

                }
                banner.update();
                ((BlockStateMeta) meta).setBlockState(banner);
            }
        });
    }

    public void banner(Map<DyeColor, PatternType> colorPatternTypeMap) {
        transformMeta(meta -> {
            if (meta instanceof BannerMeta banner) {
                if (!colorPatternTypeMap.isEmpty()) {
                    colorPatternTypeMap.forEach(((dyeColor, patternType) -> banner.addPattern(new Pattern(dyeColor, patternType))));
                }
            }
        });
    }

    public void model(int model) {
        transformMeta(meta -> meta.setCustomModelData(model));
    }

    public void attribute(Attribute attribute, AttributeModifier attributeModifier) {
        transformMeta(meta -> {
            meta.addAttributeModifier(attribute, attributeModifier);
        });

    }

    public void setNBTData(String key, Tag tag) {
        getItemHandler().setNBTData(this, key, tag);

    }

    public void setNBTData(CompoundTag compoundTag) {
        getItemHandler().setNBTData(this, compoundTag);
    }

    public CompoundTag getNBTData() {
        return getItemHandler().getNBTData(build());
    }

    public void apply(Consumer<BukkitItemBuilder> consumer) {
        consumer.accept(this);
    }

    public ItemStack build() {
        return this.itemStack.ensureServerConversions();
    }

    private ItemHandler getItemHandler() {
        return NotMythicScript.inst().getNMSModule().itemHandler();
    }
}
