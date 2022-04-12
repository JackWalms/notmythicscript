package net.halflex.mythic.nms.version.v1_18_R1;

import io.lumine.mythic.core.utils.jnbt.CompoundTag;
import io.lumine.mythic.core.utils.jnbt.Tag;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.halflex.mythic.nms.handlers.ItemHandler;
import net.halflex.mythic.nms.handlers.NMSHandler;

import java.util.Map;

@Accessors(fluent = true)
public class NMSHandler_v1_18_R1 implements NMSHandler {

    @Getter private final ItemHandler itemHandler = new ItemHandlerImpl(this);

    public NMSHandler_v1_18_R1(){

    }

    @Override
    public CompoundTag createCompoundTag(Map<String, Tag> value) {
        return new CompoundTagImpl(value);
    }
}
