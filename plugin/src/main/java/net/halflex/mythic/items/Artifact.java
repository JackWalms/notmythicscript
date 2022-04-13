package net.halflex.mythic.items;

import com.google.common.collect.Maps;
import io.lumine.mythic.api.config.MythicConfig;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.bukkit.utils.config.ConfigurationSection;
import io.lumine.mythic.core.utils.jnbt.CompoundTagBuilder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.halflex.mythic.Constants;
import net.halflex.mythic.utils.ConsoleColors;
import net.halflex.mythic.utils.Log;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Accessors(fluent = true)
public class Artifact {
    @Getter final String internalName;
    @Getter private final MythicConfig config;
    private String file;
    private ItemManager manager;
    private ArtifactType artifactType;
    private Material material;
    private String displayedName;
    private List<String> lore;
    private Map<Attribute, AttributeModifier> attributeMap = new HashMap<>();
    private String color;
    private boolean hideAllFlags;
    private final List<ItemFlag> hideFlags = new ArrayList<>();
    private final Map<DyeColor, PatternType> dyeColorPatternTypeMap = Maps.newConcurrentMap();
    private final List<PotionEffect> potionEffects = new ArrayList<>();


    public Artifact(ItemManager manager, String internalName, String file, MythicConfig mc){
        this.internalName = internalName;
        this.config = mc;

        this.artifactType = ArtifactType.valueOf(mc.getString("Type", "ITEM").toUpperCase());

        String materialStr = mc.getString(new String[] {"Material", "Id"}, "DIRT".toUpperCase());
        try {
            this.material = Material.valueOf(materialStr);
        } catch (IllegalArgumentException e){
            Log.info(ConsoleColors.RED_BRIGHT + "'Material: "+ materialStr + "' is not a valid material. Check out "  + ConsoleColors.WHITE_UNDERLINED
                    + "https://papermc.io/javadocs/paper/1.18/org/bukkit/Material.html" + ConsoleColors.RESET + ConsoleColors.RED_BRIGHT + " for a list of valid materials!");
            this.material = Material.DIRT;
        }

        this.displayedName = mc.getString("Display");
        this.lore = mc.getStringList("Lore");

        try {
            if (mc.isConfigurationSection("Attributes")){
                for (String attributeKey : mc.getKeys("Attributes")){
                    double value = mc.getDouble("Attributes." + attributeKey + ".Value");
                    String operationStr = mc.getString("Attributes." + attributeKey + ".Operation", "ADD_SCALAR");
                    String equipSlot = mc.getString("Attributes." + attributeKey + ".Slot", null);

                    Attribute attribute = Attribute.valueOf("GENERIC_" + attributeKey.toUpperCase());
                    AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(operationStr.toUpperCase());
                    EquipmentSlot slot = (equipSlot == null) ? null : EquipmentSlot.valueOf(equipSlot.toUpperCase());
                    attributeMap.put(attribute, new AttributeModifier(UUID.randomUUID(), attributeKey, value, operation, slot));
                }
            }
        }catch (IllegalArgumentException exception){
            Log.warn("Attribute was incorrectly configured!");
            exception.printStackTrace();
        }

        List<String> flagsToHide = config.getStringList("Options.Hide");
        this.hideAllFlags = config.getBoolean("Options.HideAll", false);

        if (!flagsToHide.isEmpty()){
            for (String flag : flagsToHide){
                final String flagStr = "HIDE_" + flag.toUpperCase();
                this.hideFlags.add(ItemFlag.valueOf(flagStr));
            }
        }

        this.color = config.getString("Options.Color");

        List<String> bannerLayers = config.getStringList("BannerLayers");
        if (bannerLayers != null && !bannerLayers.isEmpty()){
            for (String layer : bannerLayers){
                String[] split = layer.split(" ");
                if (split.length < 2) continue;

                final DyeColor color = DyeColor.valueOf(split[0].toUpperCase());
                final PatternType patternType = PatternType.valueOf(split[1].toUpperCase());

                dyeColorPatternTypeMap.put(color, patternType);
            }
        }

        List<String> potionsList = config.getStringList("PotionEffects");
        if (potionsList != null && !potionsList.isEmpty()){
            for (String potion : potionsList){
                String[] split = potion.split(" ");
                PotionEffect effect = buildPotion(split);
                if(effect == null) continue;
                potionEffects.add(effect);
            }
        }

    }

    private PotionEffect buildPotion(String[] split){
        PotionEffect effect = null;
        PotionEffectType type = PotionEffectType.getByName(split[0]);
        if (type != null){
            int duration = Integer.parseInt(split[1]);
            int amplifier = Integer.parseInt(split[2]);
            boolean ambient = split.length < 4 || Boolean.parseBoolean(split[3]);
            boolean particles = split.length < 5 || Boolean.parseBoolean(split[4]);
            boolean icon = split.length < 6 || Boolean.parseBoolean(split[5]);
            effect = new PotionEffect(type, duration, amplifier, ambient, particles, icon);
        }
        return effect;
    }

    public ItemStack build(){
        BukkitItemBuilder builder = BukkitItemBuilder.of(this.material);
        builder.name(this.displayedName);
        builder.lore(this.lore);

        if (this.color != null){
            builder.color(this.color);
        }

        if (!dyeColorPatternTypeMap.isEmpty() && this.material == Material.SHIELD){
            builder.shield(this.color, dyeColorPatternTypeMap);
        }

        if (!dyeColorPatternTypeMap.isEmpty() && this.material.toString().contains("BANNER")){
            builder.banner(dyeColorPatternTypeMap);
        }

        if (hideAllFlags){
            builder.hideFlags();
        } else {
            builder.flag(hideFlags);
        }

        if (!attributeMap.isEmpty()){
            attributeMap.forEach(builder::attribute);
        }

        CompoundTagBuilder baseTagBuilder = CompoundTagBuilder.create();
        CompoundTagBuilder internalTags = CompoundTagBuilder.create();
        internalTags.putString("ArtifactType", this.artifactType.toString());
        internalTags.putString("InternalName", this.internalName);

        baseTagBuilder.put("internal", internalTags.build());
        builder.setNBTData(Constants.NBT_BASE, baseTagBuilder.build());

        return builder.build();
    }
}
