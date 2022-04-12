package net.halflex.mythic.nms.handlers;

import io.lumine.mythic.core.utils.jnbt.CompoundTag;
import io.lumine.mythic.core.utils.jnbt.Tag;

import java.util.Map;

public interface NMSHandler {

    ItemHandler itemHandler();

    CompoundTag createCompoundTag(Map<String, Tag> value);
}
