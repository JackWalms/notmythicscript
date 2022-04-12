package net.halflex.mythic.nms.disabled;

import io.lumine.mythic.core.utils.jnbt.CompoundTag;
import io.lumine.mythic.core.utils.jnbt.Tag;
import net.halflex.mythic.nms.handlers.ItemHandler;
import net.halflex.mythic.nms.handlers.NMSHandler;

import java.util.Map;

public class NMSDisabled implements NMSHandler {
    public NMSDisabled(){

    }
    @Override
    public ItemHandler itemHandler() {
        return null;
    }

    @Override
    public CompoundTag createCompoundTag(Map<String, Tag> value) {
        return null;
    }
}
